package nl.remcokuipers.pokemoncardcollection.service;

import nl.remcokuipers.pokemoncardcollection.client.TcgdexClient;
import nl.remcokuipers.pokemoncardcollection.dto.PokemonCardResponseDTO;
import nl.remcokuipers.pokemoncardcollection.dto.TcgdexCardResponseDTO;
import nl.remcokuipers.pokemoncardcollection.dto.TcgdexSetResponseDTO;
import nl.remcokuipers.pokemoncardcollection.entity.PokemonCard;
import nl.remcokuipers.pokemoncardcollection.exception.ExternalApiException;
import nl.remcokuipers.pokemoncardcollection.exception.ResourceNotFoundException;
import nl.remcokuipers.pokemoncardcollection.mapper.PokemonCardMapper;
import nl.remcokuipers.pokemoncardcollection.repository.PokemonCardRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PokemonCardServiceTest {
    @Mock
    private PokemonCardRepository pokemonCardRepository;
    @Mock
    private PokemonCardMapper pokemonCardMapper;
    @Mock
    private TcgdexClient tcgdexClient;
    @InjectMocks
    private PokemonCardService pokemonCardService;

    @Test
    void getPokemonCardByExternalApiId_geeftLokaleKaartTerugZonderExterneAPI() {
        // Arrange
        PokemonCard pokemonCard = new PokemonCard("swsh3-136", "Furret", "Pokemon", "136", "Uncommon", "Darkness Ablaze");
        when(pokemonCardRepository.findByExternalApiId("swsh3-136")).thenReturn(Optional.of(pokemonCard));

        PokemonCardResponseDTO expectedResponse = new PokemonCardResponseDTO(1L, "swsh3-136", "Furret", "Pokemon",
                List.of("Colorless"), List.of(),
                "136", "Uncommon", "Darkness Ablaze");
        when(pokemonCardMapper.mapToResponseDTO(pokemonCard))
                .thenReturn(expectedResponse);

        // Act
        PokemonCardResponseDTO actualResponse = pokemonCardService.getPokemonCardByExternalApiId("swsh3-136");

        // Assert
        assertEquals(expectedResponse, actualResponse);
        verify(tcgdexClient, never()).getCardById(anyString());
        verify(pokemonCardRepository, never()).save(any(PokemonCard.class));

    }

    @Test
    void getPokemonCardByExternalApiId_haaltNieuweKaartOpEnSlaatDezeOp() {
        // Arrange
        PokemonCard pokemonCard = new PokemonCard("swsh3-136", "Furret", "Pokemon", "136", "Uncommon", "Darkness Ablaze");
        when(pokemonCardRepository.findByExternalApiId("swsh3-136")).thenReturn(Optional.empty());

        TcgdexSetResponseDTO set = new TcgdexSetResponseDTO("swsh3", "Darkness Ablaze");
        TcgdexCardResponseDTO externalCard = new TcgdexCardResponseDTO("swsh3-136", "136", "Furret", "Pokemon", "Uncommon", set, List.of("Colorless")
        );
        when(tcgdexClient.getCardById("swsh3-136")).thenReturn(externalCard);
        when(pokemonCardMapper.mapToEntity(externalCard)).thenReturn(pokemonCard);
        when(pokemonCardRepository.save(pokemonCard)).thenReturn(pokemonCard);

        PokemonCardResponseDTO expectedResponse = new PokemonCardResponseDTO(1L, "swsh3-136", "Furret", "Pokemon",
                List.of("Colorless"), List.of(),
                "136", "Uncommon", "Darkness Ablaze");
        when(pokemonCardMapper.mapToResponseDTO(pokemonCard)).thenReturn(expectedResponse);

        // Act
        PokemonCardResponseDTO actualResponse = pokemonCardService.getPokemonCardByExternalApiId("swsh3-136");

        // Assert
        assertEquals(expectedResponse, actualResponse);
        verify(pokemonCardRepository).save(pokemonCard);
        verify(tcgdexClient).getCardById("swsh3-136");

    }

    @Test
    void getPokemonCardByExternalApiId_slaatNiksOpAlsExterneKaartNietBestaat() {
        // Arrange
        when(pokemonCardRepository.findByExternalApiId("swsh3-136")).thenReturn(Optional.empty());
        when(tcgdexClient.getCardById("swsh3-136"))
                .thenThrow(new ResourceNotFoundException("Card not found."));

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> pokemonCardService.getPokemonCardByExternalApiId("swsh3-136"));
        verify(pokemonCardRepository, never()).save(any(PokemonCard.class));
    }

    @Test
    void getPokemonCardByExternalApiId_slaatNiksOpBijExterneApiStoring() {
        // Arrange
        when(pokemonCardRepository.findByExternalApiId("swsh3-136")).thenReturn(Optional.empty());
        when(tcgdexClient.getCardById("swsh3-136"))
                .thenThrow(new ExternalApiException("The external API is offline, please try again."));

        // Act & Assert
        assertThrows(ExternalApiException.class, () -> pokemonCardService.getPokemonCardByExternalApiId("swsh3-136"));
        verify(pokemonCardRepository, never()).save(any(PokemonCard.class));
    }
}