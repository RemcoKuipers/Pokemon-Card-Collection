package nl.remcokuipers.pokemoncardcollection.mapper;

import nl.remcokuipers.pokemoncardcollection.dto.TcgdexCardResponseDTO;
import nl.remcokuipers.pokemoncardcollection.dto.TcgdexSetResponseDTO;
import nl.remcokuipers.pokemoncardcollection.entity.PokemonCard;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;



class PokemonCardMapperTest {
    private final PokemonCardMapper pokemonCardMapper = new PokemonCardMapper();

    @Test
    void mapToEntity_zetExterneKaartgegevensOm(){
        // Arrange
        TcgdexSetResponseDTO set = new TcgdexSetResponseDTO("swsh3", "Darkness Ablaze");
        TcgdexCardResponseDTO externalCard = new TcgdexCardResponseDTO("swsh3-136", "136", "Furret", "Pokemon", "Uncommon", set, List.of("Colorless"), null
        );

        // Act
        PokemonCard actualCard = pokemonCardMapper.mapToEntity(externalCard);

        // Assert
        assertEquals("swsh3-136", actualCard.getExternalApiId());
        assertEquals("Furret", actualCard.getName() );
        assertEquals("Pokemon", actualCard.getSupertype());
        assertEquals("136", actualCard.getNumber());
        assertEquals("Uncommon", actualCard.getRarity());
        assertEquals("Darkness Ablaze", actualCard.getSetName());
        assertEquals(List.of("Colorless"), actualCard.getTypes());
    }

    @Test
    void mapToEntity_behoudLegeLijstAlsTypesOntbreken(){
        // Arrange
        TcgdexSetResponseDTO set = new TcgdexSetResponseDTO("swsh3", "Darkness Ablaze");
        TcgdexCardResponseDTO externalCard = new TcgdexCardResponseDTO("swsh3-136", "136", "Furret", "Pokemon", "Uncommon", set, null, null
        );

        // Act
        PokemonCard actualCard = pokemonCardMapper.mapToEntity(externalCard);

        // Assert
        assertNotNull(actualCard.getTypes());
        assertTrue(actualCard.getTypes().isEmpty());
    }
}