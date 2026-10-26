package nl.remcokuipers.pokemoncardcollection.controller;

import nl.remcokuipers.pokemoncardcollection.dto.PokemonCardPriceResponseDTO;
import nl.remcokuipers.pokemoncardcollection.dto.PokemonCardResponseDTO;
import nl.remcokuipers.pokemoncardcollection.dto.TcgdexCardSummaryResponseDTO;
import nl.remcokuipers.pokemoncardcollection.service.CurrentUserService;
import nl.remcokuipers.pokemoncardcollection.service.PokemonCardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pokemoncards")
public class PokemonCardController {
    private final PokemonCardService pokemonCardService;
    private final CurrentUserService currentUserService;

    public PokemonCardController(PokemonCardService pokemonCardService, CurrentUserService currentUserService) {
        this.pokemonCardService = pokemonCardService;
        this.currentUserService = currentUserService;
    }

    @GetMapping
    public ResponseEntity<List<PokemonCardResponseDTO>> getAllPokemonCards() {
        currentUserService.getCurrentUser();
        List<PokemonCardResponseDTO> pokemonCards = pokemonCardService.getAllPokemonCards();
        return ResponseEntity.ok(pokemonCards);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PokemonCardResponseDTO> getPokemonCardById(@PathVariable Long id) {
        currentUserService.getCurrentUser();
        PokemonCardResponseDTO pokemonCard = pokemonCardService.getPokemonCardById(id);
        return ResponseEntity.ok(pokemonCard);
    }

    @GetMapping("/external/{externalApiId}")
    public ResponseEntity<PokemonCardResponseDTO> getPokemonCardByExternalApiId(@PathVariable String externalApiId) {
        currentUserService.getCurrentUser();
        PokemonCardResponseDTO pokemonCard = pokemonCardService.getPokemonCardByExternalApiId(externalApiId);
        return ResponseEntity.ok(pokemonCard);
    }

    @GetMapping("/search")
    public ResponseEntity<List<TcgdexCardSummaryResponseDTO>> searchByName(@RequestParam String name) {
        currentUserService.getCurrentUser();
        List<TcgdexCardSummaryResponseDTO> cards = pokemonCardService.searchByName(name);
        return ResponseEntity.ok(cards);
    }

    @GetMapping("/external/{externalApiId}/prices")
    public ResponseEntity<PokemonCardPriceResponseDTO> getPokemonCardPriceByExternalApiId(@PathVariable String externalApiId) {
        currentUserService.getCurrentUser();
        PokemonCardPriceResponseDTO cards = pokemonCardService.getPokemonCardPriceByExternalApiId(externalApiId);
        return ResponseEntity.ok(cards);
    }
}
