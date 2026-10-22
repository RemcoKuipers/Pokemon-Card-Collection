package nl.remcokuipers.pokemoncardcollection.controller;

import nl.remcokuipers.pokemoncardcollection.dto.FileUploadResponseDTO;
import nl.remcokuipers.pokemoncardcollection.entity.User;
import nl.remcokuipers.pokemoncardcollection.service.CurrentUserService;
import nl.remcokuipers.pokemoncardcollection.service.FileUploadService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public ResponseEntity<FileUploadResponseDTO> uploadFile (
            @PathVariable Long collectionEntryId,
            @RequestParam ("file")MultipartFile file) {
                FileUploadResponseDTO createdFile = fileUploadService.uploadFile(collectionEntryId, file);
                return ResponseEntity.status(HttpStatus.CREATED).body(createdFile);
    }

    @GetMapping
    public ResponseEntity<List<FileUploadResponseDTO>> getFilesByCollectionEntryId(
            @PathVariable Long collectionEntryId) {
        List<FileUploadResponseDTO> files = fileUploadService.getFilesByCollectionEntryId(collectionEntryId);
        return ResponseEntity.ok(files);
    }
}
