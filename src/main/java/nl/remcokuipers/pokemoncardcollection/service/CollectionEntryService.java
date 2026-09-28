package nl.remcokuipers.pokemoncardcollection.service;

import nl.remcokuipers.pokemoncardcollection.dto.CollectionEntryRequestDTO;
import nl.remcokuipers.pokemoncardcollection.dto.CollectionEntryResponseDTO;
import nl.remcokuipers.pokemoncardcollection.entity.CollectionEntry;
import nl.remcokuipers.pokemoncardcollection.entity.PokemonCard;
import nl.remcokuipers.pokemoncardcollection.entity.User;
import nl.remcokuipers.pokemoncardcollection.exception.ResourceNotFoundException;
import nl.remcokuipers.pokemoncardcollection.mapper.CollectionEntryMapper;
import nl.remcokuipers.pokemoncardcollection.repository.CollectionEntryRepository;
import nl.remcokuipers.pokemoncardcollection.repository.PokemonCardRepository;
import org.springframework.stereotype.Service;

@Service
public class CollectionEntryService {
    private final PokemonCardRepository pokemonCardRepository;
    private final CollectionEntryRepository collectionEntryRepository;
    private final CollectionEntryMapper collectionEntryMapper;

    public CollectionEntryService(
            PokemonCardRepository pokemonCardRepository,
            CollectionEntryMapper collectionEntryMapper,
            CollectionEntryRepository collectionEntryRepository
    ) {
        this.pokemonCardRepository = pokemonCardRepository;
        this.collectionEntryRepository = collectionEntryRepository;
        this.collectionEntryMapper = collectionEntryMapper;
    }

    public CollectionEntryResponseDTO addCollectionEntry(CollectionEntryRequestDTO dto, User user) {

        PokemonCard pokemonCard = pokemonCardRepository.findById(dto.pokemonCardId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Pokemon Card Not Found with ID: " + dto.pokemonCardId()
                        )
                );

        CollectionEntry collectionEntry =
                collectionEntryMapper.mapToEntity(user, pokemonCard, dto);

        CollectionEntry savedCollectionEntry = collectionEntryRepository.save(collectionEntry);

        return collectionEntryMapper.mapToResponseDTO(savedCollectionEntry);
    }
}
