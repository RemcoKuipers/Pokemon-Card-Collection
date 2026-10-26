package nl.remcokuipers.pokemoncardcollection.controller;

import nl.remcokuipers.pokemoncardcollection.service.CurrentUserService;
import nl.remcokuipers.pokemoncardcollection.service.FileUploadService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/files")
public class AdminFileUploadController {
    private final FileUploadService fileUploadService;
    private final CurrentUserService currentUserService;

    public AdminFileUploadController(FileUploadService fileUploadService, CurrentUserService currentUserService) {
        this.fileUploadService = fileUploadService;
        this.currentUserService = currentUserService;
    }

    @DeleteMapping("/{fileId}")
    public ResponseEntity<Void> deleteStoredFile(@PathVariable Long fileId) {
        currentUserService.getCurrentUser();
        fileUploadService.deleteFileAsAdmin(fileId);
        return ResponseEntity.noContent().build();
    }
}
