package nl.remcokuipers.pokemoncardcollection.service;

import nl.remcokuipers.pokemoncardcollection.dto.FileResourceDTO;
import nl.remcokuipers.pokemoncardcollection.dto.FileUploadResponseDTO;
import nl.remcokuipers.pokemoncardcollection.entity.CollectionEntry;
import nl.remcokuipers.pokemoncardcollection.entity.FileUpload;
import nl.remcokuipers.pokemoncardcollection.entity.User;
import nl.remcokuipers.pokemoncardcollection.enums.FileType;
import nl.remcokuipers.pokemoncardcollection.exception.FileStorageException;
import nl.remcokuipers.pokemoncardcollection.exception.InvalidFileException;
import nl.remcokuipers.pokemoncardcollection.exception.ResourceNotFoundException;
import nl.remcokuipers.pokemoncardcollection.mapper.FileUploadMapper;
import nl.remcokuipers.pokemoncardcollection.repository.CollectionEntryRepository;
import nl.remcokuipers.pokemoncardcollection.repository.FileUploadRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;


@Service
public class FileUploadService {
    private final FileUploadRepository fileUploadRepository;
    private final FileUploadMapper fileUploadMapper;
    private final CollectionEntryRepository collectionEntryRepository;
    private final CurrentUserService currentUserService;
    private final Path uploadDirectory;


    public FileUploadService(
            FileUploadRepository fileUploadRepository,
            FileUploadMapper fileUploadMapper,
            CollectionEntryRepository collectionEntryRepository,
            CurrentUserService currentUserService,
            @Value("${file.upload-dir}") String uploadDir) {
        this.fileUploadRepository = fileUploadRepository;
        this.fileUploadMapper = fileUploadMapper;
        this.collectionEntryRepository = collectionEntryRepository;
        this.currentUserService = currentUserService;
        this.uploadDirectory = Path.of(uploadDir).toAbsolutePath().normalize();

        try {
            Files.createDirectories(uploadDirectory);
        } catch (IOException exception) {
            throw new FileStorageException("Could not create upload directory", exception);
        }

    }

    private CollectionEntry getOwnedCollectionEntry(Long collectionEntryId) {
        User user = currentUserService.getCurrentUser();
        return collectionEntryRepository.findByIdAndUser(collectionEntryId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Collection entry with id " + collectionEntryId + " not found"));

    }

    private FileType validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new InvalidFileException("File is empty");
        }
        String contentType = file.getContentType();
        if (!"image/jpeg".equals(contentType) && !"image/png".equals(contentType)) {
            throw new InvalidFileException("Invalid file type");
        }
        return "image/jpeg".equals(contentType) ? FileType.JPEG : FileType.PNG;

    }

    private Path storeFile(MultipartFile file, String storedFileName) {
        String fileName = storedFileName;
        Path path = uploadDirectory.resolve(fileName);
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, path);
            return path;
        } catch (FileAlreadyExistsException exception) {
            throw new FileStorageException("Could not store uploaded file.", exception);
        } catch (IOException exception) {
            deleteStoredFile(path);
            throw new FileStorageException("Could not store uploaded file.", exception);
        }
    }

    private String getOriginalFileName(MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        if (fileName == null || fileName.isBlank()) {
            throw new InvalidFileException("File name is empty");
        }
        return fileName;
    }

    public FileUploadResponseDTO uploadFile(Long collectionEntryId, MultipartFile file) {
        CollectionEntry collectionEntry = getOwnedCollectionEntry(collectionEntryId);
        FileType fileType = validateFile(file);
        String originalFileName = getOriginalFileName(file);
        String storedFileName = UUID.randomUUID().toString() + "." + fileType.name().toLowerCase(Locale.ROOT);
        Path path = storeFile(file, storedFileName);
        FileUpload fileUpload = new FileUpload(originalFileName, fileType, path.toString(), collectionEntry);
        FileUpload savedFileUpload;
        try {
            savedFileUpload = fileUploadRepository.save(fileUpload);
        } catch (RuntimeException exception) {
            deleteStoredFile(path);
            throw new FileStorageException("Could not save file", exception);
        }
        return fileUploadMapper.mapToResponseDTO(savedFileUpload);
    }

    private void deleteStoredFile(Path path) {
        try {
            Files.deleteIfExists(path);
        } catch (IOException exception) {
            throw new FileStorageException("Could not delete file", exception);
        }
    }

    public List<FileUploadResponseDTO> getFilesByCollectionEntryId(Long collectionEntryId) {
        CollectionEntry collectionEntry = getOwnedCollectionEntry(collectionEntryId);
        List<FileUpload> uploads = fileUploadRepository.findByCollectionEntry(collectionEntry);
        return uploads.stream()
                .map(upload -> fileUploadMapper.mapToResponseDTO(upload))
                .toList();

    }

    private FileUpload getOwnedFileUpload(Long collectionEntryId, Long fileId) {
        CollectionEntry collectionEntry = getOwnedCollectionEntry(collectionEntryId);
        FileUpload upload = fileUploadRepository.findByIdAndCollectionEntry(fileId, collectionEntry)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "File with id " + fileId + " not found")
                );
        return upload;
    }

    private Resource loadFileResource(Path path) {

        try {
            Resource resource = new UrlResource(path.toUri());
            if (!resource.exists() || !resource.isReadable()) {
                throw new ResourceNotFoundException("File not found.");
            }
            return resource;
        } catch (MalformedURLException exception) {
            throw new FileStorageException("Could not load file", exception);
        }
    }

    public FileResourceDTO getFileResource(Long collectionEntryId, Long fileId) {
        FileUpload upload = getOwnedFileUpload(collectionEntryId, fileId);
        Path path = Path.of(upload.getFilePath());
        Resource resource = loadFileResource(path);
        return fileUploadMapper.mapToFileResourceDTO(upload, resource);
    }

    public void deleteFile(Long collectionEntryId, Long fileId) {
        FileUpload upload = getOwnedFileUpload(collectionEntryId, fileId);
        Path path = Path.of(upload.getFilePath());
        deleteStoredFile(path);
        fileUploadRepository.delete(upload);
    }

    public void deleteFilesByCollectionEntry(CollectionEntry collectionEntry) {
        List<FileUpload> uploads = fileUploadRepository.findByCollectionEntry(collectionEntry);
        for (FileUpload upload : uploads) {
            Path path = Path.of(upload.getFilePath());
            deleteStoredFile(path);
            fileUploadRepository.delete(upload);
        }
    }

    public void moveFilesToCollectionEntry(CollectionEntry sourceEntry, CollectionEntry targetEntry) {
        List<FileUpload> uploads = fileUploadRepository.findByCollectionEntry(sourceEntry);
        for (FileUpload upload : uploads) {
            upload.setCollectionEntry(targetEntry);
            fileUploadRepository.save(upload);
        }
    }

    public void deleteFileAsAdmin(Long fileId){
        Optional<FileUpload> upload = fileUploadRepository.findById(fileId);
        if (upload.isEmpty()) {
            throw new ResourceNotFoundException("File with id " + fileId + " not found");
        }
        FileUpload foundUpload = upload.get();
        deleteStoredFile(Path.of(foundUpload.getFilePath()));
        fileUploadRepository.delete(foundUpload);
    }

}
