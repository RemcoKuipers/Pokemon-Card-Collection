package nl.remcokuipers.pokemoncardcollection.service;

import jakarta.transaction.Transactional;
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
import java.util.Optional;

@Service
public class CollectionEntryService {
    private final PokemonCardRepository pokemonCardRepository;
    private final CollectionEntryRepository collectionEntryRepository;
    private final CollectionEntryMapper collectionEntryMapper;
    private final FileUploadService fileUploadService;

    public CollectionEntryService(
            PokemonCardRepository pokemonCardRepository,
            CollectionEntryMapper collectionEntryMapper,
            CollectionEntryRepository collectionEntryRepository, FileUploadService fileUploadService
    ) {
        this.pokemonCardRepository = pokemonCardRepository;
        this.collectionEntryRepository = collectionEntryRepository;
        this.collectionEntryMapper = collectionEntryMapper;
        this.fileUploadService = fileUploadService;
    }

    public CollectionEntryResponseDTO addCollectionEntry(CollectionEntryRequestDTO dto, User user) {

        PokemonCard pokemonCard = pokemonCardRepository.findById(dto.pokemonCardId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Pokemon Card Not Found with ID: " + dto.pokemonCardId()
                        )
                );
        Optional<CollectionEntry> existingEntry = collectionEntryRepository.findByUserAndPokemonCardAndCondition(
                user,
                pokemonCard,
                dto.condition()
        );

        if (existingEntry.isPresent()) {
            CollectionEntry collectionEntry = existingEntry.get();
            collectionEntry.setQuantity(collectionEntry.getQuantity() + dto.quantity());
            CollectionEntry savedCollectionEntry = collectionEntryRepository.save(collectionEntry);
            return collectionEntryMapper.mapToResponseDTO(savedCollectionEntry);
        }

        CollectionEntry collectionEntry =
                collectionEntryMapper.mapToEntity(user, pokemonCard, dto);

        CollectionEntry savedCollectionEntry = collectionEntryRepository.save(collectionEntry);

        return collectionEntryMapper.mapToResponseDTO(savedCollectionEntry);
    }

    public List<CollectionEntryResponseDTO> getCollectionEntriesByUser(User user, String name, String setName, String rarity) {
        List<CollectionEntry> collectionEntries;
        if (name == null || name.isBlank()) {
            collectionEntries = collectionEntryRepository.findByUser(user);
        } else {
            collectionEntries = collectionEntryRepository.findByUserAndPokemonCardNameContainingIgnoreCase(user, name);
        }

        return collectionEntries
                .stream()
                .filter(entry -> setName == null || setName.isBlank()
                        || entry.getPokemonCard().getSetName().equalsIgnoreCase(setName))
                .filter(entry -> rarity == null || rarity.isBlank()
                        || entry.getPokemonCard().getRarity().equalsIgnoreCase(rarity))

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

    @Transactional
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
        Optional<CollectionEntry> existingEntry = collectionEntryRepository.findByUserAndPokemonCardAndCondition(
                user,
                collectionEntry.getPokemonCard(),
                dto.condition()

        );
        if (existingEntry.isPresent()
                && !existingEntry.get().getId().equals(collectionEntry.getId())) {
            CollectionEntry targetEntry = existingEntry.get();
            targetEntry.setQuantity(targetEntry.getQuantity() + dto.quantity());
            targetEntry = collectionEntryRepository.save(targetEntry);
            collectionEntryRepository.delete(collectionEntry);
            return collectionEntryMapper.mapToResponseDTO(targetEntry);
        }
        collectionEntry.setQuantity(dto.quantity());
        collectionEntry.setCondition(dto.condition());

        CollectionEntry savedCollectionEntry = collectionEntryRepository.save(collectionEntry);
        return collectionEntryMapper.mapToResponseDTO(savedCollectionEntry);
    }

    public void deleteCollectionEntry(Long id, User user) {
        CollectionEntry collectionEntry =
                collectionEntryRepository.findByIdAndUser(id, user)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Collection entry with ID " + id + " not found."
                                )
                        );
        fileUploadService.deleteFilesByCollectionEntry(collectionEntry);
        collectionEntryRepository.delete(collectionEntry);

    }
}
