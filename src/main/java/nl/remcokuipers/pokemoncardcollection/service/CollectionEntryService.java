package nl.remcokuipers.pokemoncardcollection.service;

import nl.remcokuipers.pokemoncardcollection.dto.CollectionEntryRequestDTO;
import nl.remcokuipers.pokemoncardcollection.dto.CollectionEntryResponseDTO;
import nl.remcokuipers.pokemoncardcollection.dto.CollectionEntryUpdateDTO;
import nl.remcokuipers.pokemoncardcollection.entity.CollectionEntry;
import nl.remcokuipers.pokemoncardcollection.entity.PokemonCard;
import nl.remcokuipers.pokemoncardcollection.entity.User;
import nl.remcokuipers.pokemoncardcollection.exception.ResourceNotFoundException;
import nl.remcokuipers.pokemoncardcollection.mapper.CollectionEntryMapper;
import nl.remcokuipers.pokemoncardcollection.repository.CollectionEntryRepository;
import nl.remcokuipers.pokemoncardcollection.repository.PokemonCardRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<CollectionEntryResponseDTO> getCollectionEntriesByUser(User user) {
        List<CollectionEntry> collectionEntries = collectionEntryRepository.findByUser(user);

        return collectionEntries
                .stream()
                .map(collectionEntryMapper::mapToResponseDTO)
                .toList();

    }

    public CollectionEntryResponseDTO getCollectionEntryById(Long id, User user) {
        CollectionEntry collectionEntry =
                collectionEntryRepository.findByIdAndUser(id, user)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Collection entry with ID " + id + " not found."
                                )
                        );
        return collectionEntryMapper.mapToResponseDTO(collectionEntry);
    }

    public CollectionEntryResponseDTO updateCollectionEntry(
            Long id,
            CollectionEntryUpdateDTO dto,
            User user) {
        CollectionEntry collectionEntry =
                collectionEntryRepository.findByIdAndUser(id, user)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Collection entry with ID " + id + " not found."
                                )
                        );
        collectionEntry.setQuantity(dto.quantity());
        collectionEntry.setCondition(dto.condition());

        CollectionEntry savedCollectionEntry = collectionEntryRepository.save(collectionEntry);
        return collectionEntryMapper.mapToResponseDTO(savedCollectionEntry);
    }
}
