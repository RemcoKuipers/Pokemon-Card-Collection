package nl.remcokuipers.pokemoncardcollection.controller;

import nl.remcokuipers.pokemoncardcollection.dto.FileUploadResponseDTO;
import nl.remcokuipers.pokemoncardcollection.service.FileUploadService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/collectionentries/{collectionEntryId}/files")
public class FileUploadController {
    private final FileUploadService fileUploadService;

    public FileUploadController(FileUploadService fileUploadService) {
        this.fileUploadService = fileUploadService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FileUploadResponseDTO> uploadFile (
            @PathVariable Long collectionEntryId,
            @RequestParam ("file")MultipartFile file) {
                FileUploadResponseDTO createdFile = fileUploadService.uploadFile(collectionEntryId, file);
                return ResponseEntity.status(HttpStatus.CREATED).body(createdFile);
    }
}
