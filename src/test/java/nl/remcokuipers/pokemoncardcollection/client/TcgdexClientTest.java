package nl.remcokuipers.pokemoncardcollection.client;

import nl.remcokuipers.pokemoncardcollection.dto.TcgdexCardResponseDTO;
import nl.remcokuipers.pokemoncardcollection.exception.ExternalApiException;
import nl.remcokuipers.pokemoncardcollection.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

class TcgdexClientTest {
    private TcgdexClient tcgdexClient;
    private MockRestServiceServer mockServer;

    @BeforeEach
    void setUp() {
        RestClient.Builder builder = RestClient.builder();
        mockServer = MockRestServiceServer.bindTo(builder).build();
        tcgdexClient = new TcgdexClient(builder, "https://api.tcgdex.test/v2/en");
    }

    @Test
    void getCardById_geeftKaartTerugBijGeldigeResponse() {
        // Arrange
        String json = """
                {
                    "id": "swsh3-136",
                    "localId": "136",
                    "name": "Furret",
                    "category": "Pokemon",
                    "rarity": "Uncommon",
                    "set": {
                        "id": "swsh3",
                        "name": "Darkness Ablaze"
                    },
                    "types": ["Colorless"]
                }
                """;

        mockServer.expect(requestTo("https://api.tcgdex.test/v2/en/cards/swsh3-136"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(json, MediaType.APPLICATION_JSON));
        // Act
        TcgdexCardResponseDTO actualResponse = tcgdexClient.getCardById("swsh3-136");
        // Assert
        assertNotNull(actualResponse);
        assertEquals("swsh3-136", actualResponse.id());
        assertEquals("Furret", actualResponse.name());
        mockServer.verify();

    }

    @Test
    void getCardById_gooitResourceNotFoundExceptionBij404() {
        // Arrange
        mockServer.expect(requestTo("https://api.tcgdex.test/v2/en/cards/bestaat-niet"))
                .andRespond(withStatus(HttpStatus.NOT_FOUND));

        // Act & Assert
        assertThrows(
                ResourceNotFoundException.class,
                () -> tcgdexClient.getCardById("bestaat-niet")
        );
        mockServer.verify();
    }

    @Test
    void getCardById_gooitExternalApiExceptionBij500() {
        // Arrange
        mockServer.expect(requestTo("https://api.tcgdex.test/v2/en/cards/swsh3-136"))
                .andRespond(withStatus(HttpStatus.INTERNAL_SERVER_ERROR));

        // Act & Assert
        assertThrows(
                ExternalApiException.class,
                () -> tcgdexClient.getCardById("swsh3-136")
        );
        mockServer.verify();
    }

    @Test
    void getCardById_gooitExternalApiExceptionBijLegeResponse() {
        // Arrange
        mockServer.expect(requestTo("https://api.tcgdex.test/v2/en/cards/swsh3-136"))
                .andRespond(withSuccess());

        // Act & Assert
        assertThrows(
                ExternalApiException.class,
                () -> tcgdexClient.getCardById("swsh3-136")
        );
        mockServer.verify();
    }

    @Test
    void getCardById_gooitExternalApiExceptionBijVerbindingsfout() {
        // Arrange
        mockServer.expect(requestTo("https://api.tcgdex.test/v2/en/cards/swsh3-136"))
                .andRespond(withException(new IOException("Connection refused")));

        // Act & Assert
        assertThrows(
                ExternalApiException.class,
                () -> tcgdexClient.getCardById("swsh3-136")
        );
        mockServer.verify();
    }
}