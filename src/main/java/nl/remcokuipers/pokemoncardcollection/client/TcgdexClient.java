package nl.remcokuipers.pokemoncardcollection.client;

import nl.remcokuipers.pokemoncardcollection.dto.TcgdexCardResponseDTO;
import nl.remcokuipers.pokemoncardcollection.exception.ExternalApiException;
import nl.remcokuipers.pokemoncardcollection.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

@Component
public class TcgdexClient {
    private final RestClient restClient;

    public TcgdexClient(@Value("${tcgdex.base-url}") String baseUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public TcgdexCardResponseDTO getCardById(String externalApiId) {
        try {
            TcgdexCardResponseDTO card = restClient.get()
                    .uri("/cards/{id}", externalApiId)
                    .retrieve()
                    .body(TcgdexCardResponseDTO.class);
            if (card == null) {
                throw new ExternalApiException("TCGdex returned an empty response.");
            }
            return card;
        } catch (HttpClientErrorException.NotFound exception) {
            throw new ResourceNotFoundException(
                    "Pokemon card with external ID " + externalApiId + " not found."
            );
        } catch (ResourceAccessException exception) {
            throw new ExternalApiException(
                    "TCGdex is currently unavailable. Please try again later."
            );
        } catch (HttpServerErrorException exception) {
            throw new ExternalApiException(
                    "TCGdex returned a server error. Please try again later."
            );
        }
    }
}
