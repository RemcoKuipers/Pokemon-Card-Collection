package nl.remcokuipers.pokemoncardcollection.service;

import nl.remcokuipers.pokemoncardcollection.dto.CollectionEntryRequestDTO;
import nl.remcokuipers.pokemoncardcollection.dto.CollectionEntryResponseDTO;
import nl.remcokuipers.pokemoncardcollection.entity.CollectionEntry;
import nl.remcokuipers.pokemoncardcollection.entity.PokemonCard;
import nl.remcokuipers.pokemoncardcollection.entity.User;
import nl.remcokuipers.pokemoncardcollection.enums.Condition;
import nl.remcokuipers.pokemoncardcollection.mapper.CollectionEntryMapper;
import nl.remcokuipers.pokemoncardcollection.repository.CollectionEntryRepository;
import nl.remcokuipers.pokemoncardcollection.repository.PokemonCardRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CollectionEntryServiceTest {
    @Mock
    private PokemonCardRepository pokemonCardRepository;
    @Mock
    private CollectionEntryRepository collectionEntryRepository;
    @Mock
    private CollectionEntryMapper collectionEntryMapper;
    @InjectMocks
    private CollectionEntryService collectionEntryService;

    @Test
    void addCollectionEntry_verhoogtAantalBijBestaandeCombinatie() {
        // Arrange
        User user = new User();
        PokemonCard pokemonCard = new PokemonCard();
        CollectionEntry collectionEntry = new CollectionEntry(2, Condition.MINT, user, pokemonCard);
        CollectionEntryRequestDTO collectionEntryRequestDTO = new CollectionEntryRequestDTO(1L, 3, Condition.MINT);

        when(pokemonCardRepository.findById(1L))
                .thenReturn(Optional.of(pokemonCard));
        when(collectionEntryRepository.findByUserAndPokemonCardAndCondition(user, pokemonCard, Condition.MINT))
                .thenReturn(Optional.of(collectionEntry));
        when(collectionEntryRepository.save(collectionEntry))
                .thenReturn(collectionEntry);

        CollectionEntryResponseDTO expectedResponse = new CollectionEntryResponseDTO(1L, 1L, 5, Condition.MINT);
        when(collectionEntryMapper.mapToResponseDTO(collectionEntry))
                .thenReturn(expectedResponse);

        // Act
        CollectionEntryResponseDTO actualResponse = collectionEntryService.addCollectionEntry(collectionEntryRequestDTO, user);

        // Assert
        assertEquals(5, collectionEntry.getQuantity());
        assertEquals(expectedResponse, actualResponse);
        verify(collectionEntryRepository).save(collectionEntry);
    }

    @Test
    void addCollectionEntry_maaktNieuweEntryBijNieuweCombinatie() {
        // Arrange
        User user = new User();
        PokemonCard pokemonCard = new PokemonCard();
        CollectionEntry collectionEntry = new CollectionEntry(3, Condition.MINT, user, pokemonCard);

        when(pokemonCardRepository.findById(1L))
                .thenReturn(Optional.of(pokemonCard));
        when(collectionEntryRepository.findByUserAndPokemonCardAndCondition(user, pokemonCard, Condition.MINT))
                .thenReturn(Optional.empty());

        CollectionEntryRequestDTO collectionEntryRequestDTO = new CollectionEntryRequestDTO(1L, 3, Condition.MINT);
        when(collectionEntryMapper.mapToEntity(user, pokemonCard, collectionEntryRequestDTO))
                .thenReturn(collectionEntry);
        when(collectionEntryRepository.save(collectionEntry))
                .thenReturn(collectionEntry);
        CollectionEntryResponseDTO expectedResponse = new CollectionEntryResponseDTO(1L, 1L, 3, Condition.MINT);
        when(collectionEntryMapper.mapToResponseDTO(collectionEntry))
                .thenReturn(expectedResponse);

        // Act
        CollectionEntryResponseDTO actualResponse = collectionEntryService.addCollectionEntry(collectionEntryRequestDTO, user);

        // Assert
        assertEquals(expectedResponse, actualResponse);
        verify(collectionEntryMapper).mapToEntity(user, pokemonCard, collectionEntryRequestDTO);
        verify(collectionEntryRepository).save(collectionEntry);
    }
}