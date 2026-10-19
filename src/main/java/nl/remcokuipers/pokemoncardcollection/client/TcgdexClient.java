package nl.remcokuipers.pokemoncardcollection.client;

import nl.remcokuipers.pokemoncardcollection.dto.TcgdexCardResponseDTO;
import nl.remcokuipers.pokemoncardcollection.dto.TcgdexSetResponseDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class TcgdexClient {
    private final RestClient restClient;

    public TcgdexClient() {
        this.restClient = RestClient.builder()
                .baseUrl("https://api.tcgdex.net/v2/en")
                .build();
    }

    public TcgdexCardResponseDTO getCardById(String externalApiId) {
        return restClient.get()
                .uri("/cards/{id}", externalApiId)
                .retrieve()
                .body(TcgdexCardResponseDTO.class);
    }
}
