package nl.remcokuipers.pokemoncardcollection.controller;

import nl.remcokuipers.pokemoncardcollection.dto.FileResourceDTO;
import nl.remcokuipers.pokemoncardcollection.dto.FileUploadResponseDTO;
import nl.remcokuipers.pokemoncardcollection.entity.User;
import nl.remcokuipers.pokemoncardcollection.enums.FileType;
import nl.remcokuipers.pokemoncardcollection.service.CurrentUserService;
import nl.remcokuipers.pokemoncardcollection.service.FileUploadService;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/collectionentries/{collectionEntryId}/files")
public class FileUploadController {
    private final FileUploadService fileUploadService;
    private final CurrentUserService currentUserService;

    public FileUploadController(FileUploadService fileUploadService, CurrentUserService currentUserService) {
        this.fileUploadService = fileUploadService;
        this.currentUserService = currentUserService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FileUploadResponseDTO> uploadFile(
            @PathVariable Long collectionEntryId,
            @RequestParam("file") MultipartFile file) {
        FileUploadResponseDTO createdFile = fileUploadService.uploadFile(collectionEntryId, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdFile);
    }

    @GetMapping
    public ResponseEntity<List<FileUploadResponseDTO>> getFilesByCollectionEntryId(
            @PathVariable Long collectionEntryId) {
        List<FileUploadResponseDTO> files = fileUploadService.getFilesByCollectionEntryId(collectionEntryId);
        return ResponseEntity.ok(files);
    }

    private MediaType getMediaType(FileType fileType) {
        if (fileType == FileType.PNG) {
            return MediaType.IMAGE_PNG;
        }
        return MediaType.IMAGE_JPEG;
    }

    @GetMapping("/{fileId}")
    public ResponseEntity<Resource> viewFile(@PathVariable Long fileId, @PathVariable Long collectionEntryId) {
        FileResourceDTO file =  fileUploadService.getFileResource(collectionEntryId, fileId);
        return ResponseEntity.ok()
                .contentType(getMediaType(file.fileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.inline()
                                .filename(file.fileName(), StandardCharsets.UTF_8)
                                .build()
                                .toString())
                .body(file.resource());
    }

    @GetMapping("/{fileId}/download")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long fileId, @PathVariable Long collectionEntryId) {
        FileResourceDTO file =  fileUploadService.getFileResource(collectionEntryId, fileId);
        return ResponseEntity.ok()
                .contentType(getMediaType(file.fileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment()
                                .filename(file.fileName(), StandardCharsets.UTF_8)
                                .build()
                                .toString())
                .body(file.resource());
    }

    @DeleteMapping("/{fileId}")
    public ResponseEntity<Void> deleteFile(@PathVariable Long collectionEntryId, @PathVariable Long fileId) {
        fileUploadService.deleteFile(collectionEntryId, fileId);
        return ResponseEntity.noContent().build();
    }

}
