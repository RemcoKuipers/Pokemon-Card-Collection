package nl.remcokuipers.pokemoncardcollection.controller;

import nl.remcokuipers.pokemoncardcollection.dto.PokemonCardResponseDTO;
import nl.remcokuipers.pokemoncardcollection.dto.TcgdexCardSummaryResponseDTO;
import nl.remcokuipers.pokemoncardcollection.entity.PokemonCard;
import nl.remcokuipers.pokemoncardcollection.service.PokemonCardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pokemoncards")
public class PokemonCardController {
    private final PokemonCardService pokemonCardService;

    public PokemonCardController(PokemonCardService pokemonCardService) {
        this.pokemonCardService = pokemonCardService;
    }

    @GetMapping
    public ResponseEntity<List<PokemonCardResponseDTO>> getAllPokemonCards() {
        List<PokemonCardResponseDTO> pokemonCards = pokemonCardService.getAllPokemonCards();
        return ResponseEntity.ok(pokemonCards);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PokemonCardResponseDTO> getPokemonCardById(@PathVariable Long id) {
        PokemonCardResponseDTO pokemonCard = pokemonCardService.getPokemonCardById(id);
        return ResponseEntity.ok(pokemonCard);
    }

    @GetMapping("/external/{externalApiId}")
    public ResponseEntity<PokemonCardResponseDTO> getPokemonCardByExternalApiId(@PathVariable String externalApiId) {
        PokemonCardResponseDTO pokemonCard = pokemonCardService.getPokemonCardByExternalApiId(externalApiId);
        return ResponseEntity.ok(pokemonCard);
    }

    @GetMapping("/search")
    public ResponseEntity<List<TcgdexCardSummaryResponseDTO>> searchByName(@RequestParam String name) {
        List<TcgdexCardSummaryResponseDTO> cards = pokemonCardService.searchByName(name);
        return ResponseEntity.ok(cards);
    }
}
