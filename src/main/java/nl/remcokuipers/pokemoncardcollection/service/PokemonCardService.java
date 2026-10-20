package nl.remcokuipers.pokemoncardcollection.service;

import nl.remcokuipers.pokemoncardcollection.client.TcgdexClient;
import nl.remcokuipers.pokemoncardcollection.dto.PokemonCardResponseDTO;
import nl.remcokuipers.pokemoncardcollection.dto.TcgdexCardResponseDTO;
import nl.remcokuipers.pokemoncardcollection.dto.TcgdexCardSummaryResponseDTO;
import nl.remcokuipers.pokemoncardcollection.entity.PokemonCard;
import nl.remcokuipers.pokemoncardcollection.exception.ResourceNotFoundException;
import nl.remcokuipers.pokemoncardcollection.mapper.PokemonCardMapper;
import nl.remcokuipers.pokemoncardcollection.repository.PokemonCardRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PokemonCardService {
    private final PokemonCardRepository pokemonCardRepository;
    private final PokemonCardMapper pokemonCardMapper;
    private final TcgdexClient tcgdexClient;

    public PokemonCardService(
            PokemonCardRepository pokemonCardRepository,
            PokemonCardMapper pokemonCardMapper,
            TcgdexClient tcgdexClient
    ) {
        this.pokemonCardRepository = pokemonCardRepository;
        this.pokemonCardMapper = pokemonCardMapper;
        this.tcgdexClient = tcgdexClient;
    }

    public List<PokemonCardResponseDTO> getAllPokemonCards() {
        return pokemonCardRepository.findAll()
                .stream()
                .map(pokemonCardMapper::mapToResponseDTO)
                .toList();
    }

    public PokemonCardResponseDTO getPokemonCardById(Long id) {
        PokemonCard pokemonCard = pokemonCardRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "PokemonCard with id " + id + " not found!"
                        )
                );
        return pokemonCardMapper.mapToResponseDTO(pokemonCard);
    }

    public PokemonCardResponseDTO getPokemonCardByExternalApiId(String externalApiId) {
        Optional<PokemonCard> existingCard = pokemonCardRepository.findByExternalApiId(externalApiId);
        if (existingCard.isPresent()) {
            return pokemonCardMapper.mapToResponseDTO(existingCard.get());
        }
        TcgdexCardResponseDTO dto = tcgdexClient.getCardById(externalApiId);
        PokemonCard pokemonCard = pokemonCardMapper.mapToEntity(dto);
        PokemonCard savedPokemonCard = pokemonCardRepository.save(pokemonCard);

        return pokemonCardMapper.mapToResponseDTO(savedPokemonCard);
    }

    public List<TcgdexCardSummaryResponseDTO> searchByName(String name) {
        List<TcgdexCardSummaryResponseDTO> cards = tcgdexClient.searchByName(name);
        return cards;
    }
}
