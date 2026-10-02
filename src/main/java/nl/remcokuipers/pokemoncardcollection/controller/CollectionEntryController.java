package nl.remcokuipers.pokemoncardcollection.controller;

import nl.remcokuipers.pokemoncardcollection.dto.CollectionEntryResponseDTO;
import nl.remcokuipers.pokemoncardcollection.entity.User;
import nl.remcokuipers.pokemoncardcollection.service.CollectionEntryService;
import nl.remcokuipers.pokemoncardcollection.service.CurrentUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
