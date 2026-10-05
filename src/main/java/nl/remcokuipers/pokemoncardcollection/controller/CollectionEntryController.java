package nl.remcokuipers.pokemoncardcollection.controller;

import jakarta.validation.Valid;
import nl.remcokuipers.pokemoncardcollection.dto.CollectionEntryRequestDTO;
import nl.remcokuipers.pokemoncardcollection.dto.CollectionEntryResponseDTO;
import nl.remcokuipers.pokemoncardcollection.entity.User;
import nl.remcokuipers.pokemoncardcollection.service.CollectionEntryService;
import nl.remcokuipers.pokemoncardcollection.service.CurrentUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/collectionentries")
public class CollectionEntryController {

    private final CollectionEntryService collectionEntryService;
    private final CurrentUserService currentUserService;

    public CollectionEntryController(CollectionEntryService collectionEntryService,  CurrentUserService currentUserService) {
        this.collectionEntryService = collectionEntryService;
        this.currentUserService = currentUserService;
    }

    @GetMapping
    public ResponseEntity<List<CollectionEntryResponseDTO>> getCollectionEntries() {
        User user = currentUserService.getCurrentUser();
        return  ResponseEntity.ok(collectionEntryService.getCollectionEntriesByUser(user));

    }

    @PostMapping
    public ResponseEntity<CollectionEntryResponseDTO> addCollectionEntry(@Valid @RequestBody CollectionEntryRequestDTO dto) {
        User user = currentUserService.getCurrentUser();
        CollectionEntryResponseDTO createdEntry = collectionEntryService.addCollectionEntry(dto, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEntry);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CollectionEntryResponseDTO> getCollectionEntryById(@PathVariable long id) {
        User user = currentUserService.getCurrentUser();
        CollectionEntryResponseDTO collectionEntry = collectionEntryService.getCollectionEntryById(id, user);
        return ResponseEntity.ok(collectionEntry);
    }
}
