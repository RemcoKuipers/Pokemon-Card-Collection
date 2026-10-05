package nl.remcokuipers.pokemoncardcollection.controller;

import jakarta.validation.Valid;
import nl.remcokuipers.pokemoncardcollection.dto.FavoriteRequestDTO;
import nl.remcokuipers.pokemoncardcollection.dto.FavoriteResponseDTO;
import nl.remcokuipers.pokemoncardcollection.entity.User;
import nl.remcokuipers.pokemoncardcollection.service.CurrentUserService;
import nl.remcokuipers.pokemoncardcollection.service.FavoriteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/favorites")

public class FavoriteController {

    private final FavoriteService favoriteService;
    private final CurrentUserService currentUserService;

    public FavoriteController(CurrentUserService currentUserService, FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
        this.currentUserService = currentUserService;
    }

    @GetMapping
    public ResponseEntity<List<FavoriteResponseDTO>> getFavorites() {
        User user = currentUserService.getCurrentUser();
        List<FavoriteResponseDTO> favorites = favoriteService.getFavoritesByUser(user);
        return ResponseEntity.ok(favorites);
    }

    @PostMapping
    public ResponseEntity<FavoriteResponseDTO> addFavoriteEntry(@Valid @RequestBody FavoriteRequestDTO favoriteRequestDTO) {
        User user = currentUserService.getCurrentUser();
        FavoriteResponseDTO createdEntry = favoriteService.addFavorite(favoriteRequestDTO, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEntry);
    }
}
