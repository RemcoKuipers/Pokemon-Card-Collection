package nl.remcokuipers.pokemoncardcollection.service;

import nl.remcokuipers.pokemoncardcollection.dto.PokemonCardResponseDTO;
import nl.remcokuipers.pokemoncardcollection.entity.PokemonCard;
import nl.remcokuipers.pokemoncardcollection.exception.ResourceNotFoundException;
import nl.remcokuipers.pokemoncardcollection.mapper.PokemonCardMapper;
import nl.remcokuipers.pokemoncardcollection.repository.PokemonCardRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PokemonCardService {
    private final PokemonCardRepository pokemonCardRepository;
    private final PokemonCardMapper pokemonCardMapper;

    public PokemonCardService(
            PokemonCardRepository pokemonCardRepository,
            PokemonCardMapper pokemonCardMapper
    ) {
        this.pokemonCardRepository = pokemonCardRepository;
        this.pokemonCardMapper = pokemonCardMapper;
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
}
