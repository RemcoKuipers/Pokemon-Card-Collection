package nl.remcokuipers.pokemoncardcollection.service;

import nl.remcokuipers.pokemoncardcollection.entity.CollectionEntry;
import nl.remcokuipers.pokemoncardcollection.entity.User;
import nl.remcokuipers.pokemoncardcollection.exception.InvalidFileException;
import nl.remcokuipers.pokemoncardcollection.exception.ResourceNotFoundException;
import nl.remcokuipers.pokemoncardcollection.mapper.FileUploadMapper;
import nl.remcokuipers.pokemoncardcollection.repository.CollectionEntryRepository;
import nl.remcokuipers.pokemoncardcollection.repository.FileUploadRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

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


    }

    private CollectionEntry getOwnedCollectionEntry(Long collectionEntryId) {
        User user = currentUserService.getCurrentUser();
        return collectionEntryRepository.findByIdAndUser(collectionEntryId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Collection entry with id " + collectionEntryId + " not found"));

    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new InvalidFileException("File is empty");
        }
        String contentType = file.getContentType();
        if (!"image/jpeg".equals(contentType) && !"image/png".equals(contentType)) {
            throw new InvalidFileException("Invalid file type");
        }
    }


}
