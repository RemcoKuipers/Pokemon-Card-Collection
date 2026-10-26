package nl.remcokuipers.pokemoncardcollection.controller;

import jakarta.validation.Valid;
import nl.remcokuipers.pokemoncardcollection.dto.PokemonCardResponseDTO;
import nl.remcokuipers.pokemoncardcollection.dto.PokemonCardUpdateDTO;
import nl.remcokuipers.pokemoncardcollection.service.CurrentUserService;
import nl.remcokuipers.pokemoncardcollection.service.PokemonCardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cards")
public class AdminPokemonCardController {
    private final PokemonCardService pokemonCardService;
    private final CurrentUserService currentUserService;

    public AdminPokemonCardController(PokemonCardService pokemonCardService, CurrentUserService currentUserService) {
        this.pokemonCardService = pokemonCardService;
        this.currentUserService = currentUserService;
    }

    @PutMapping("/{id}")
    public ResponseEntity<PokemonCardResponseDTO> updatePokemonCard(@PathVariable Long id,@Valid @RequestBody PokemonCardUpdateDTO dto) {
        currentUserService.getCurrentUser();
        return ResponseEntity.ok(pokemonCardService.updatePokemonCard(id, dto));
    }
}
