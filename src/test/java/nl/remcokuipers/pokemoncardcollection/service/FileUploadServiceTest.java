package nl.remcokuipers.pokemoncardcollection.service;

import nl.remcokuipers.pokemoncardcollection.dto.FileUploadResponseDTO;
import nl.remcokuipers.pokemoncardcollection.entity.CollectionEntry;
import nl.remcokuipers.pokemoncardcollection.entity.FileUpload;
import nl.remcokuipers.pokemoncardcollection.entity.User;
import nl.remcokuipers.pokemoncardcollection.exception.FileStorageException;
import nl.remcokuipers.pokemoncardcollection.mapper.FileUploadMapper;
import nl.remcokuipers.pokemoncardcollection.repository.CollectionEntryRepository;
import nl.remcokuipers.pokemoncardcollection.repository.FileUploadRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FileUploadServiceTest {
    @Mock
    private FileUploadRepository fileUploadRepository;
    @Mock
    private FileUploadMapper fileUploadMapper;
    @Mock
    private CollectionEntryRepository collectionEntryRepository;
    @Mock
    private CurrentUserService currentUserService;
    @TempDir
    Path uploadDirectory;

    @Test
    void uploadFile_verwijdertBestandBijDatabasefout() throws IOException {
        // Arrange
        User user = new User();
        CollectionEntry collectionEntry = new CollectionEntry();

        when(currentUserService.getCurrentUser())
                .thenReturn(user);
        when(collectionEntryRepository.findByIdAndUser(1L, user))
                .thenReturn(Optional.of(collectionEntry));
        when(fileUploadRepository.save(any(FileUpload.class)))
                .thenThrow(new RuntimeException("DatabaseFout"));
        byte[] content = Files.readAllBytes(
                Path.of("docs/postman/testbestanden/starmie.png")
        );
        MockMultipartFile file = new MockMultipartFile("file", "starmie.png", "image/png", content);
        FileUploadService fileUploadService = new FileUploadService(fileUploadRepository, fileUploadMapper, collectionEntryRepository, currentUserService, uploadDirectory.toString());

        // Act & Assert
        assertThrows(FileStorageException.class,
                () -> fileUploadService.uploadFile(1L, file)
        );
        try (Stream<Path> stream = Files.list(uploadDirectory)) {
            assertEquals(0L, stream.count());
        }
    }
}