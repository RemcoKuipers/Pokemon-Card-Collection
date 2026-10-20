package nl.remcokuipers.pokemoncardcollection.dto;

import java.util.List;

public record TcgdexCardResponseDTO(
        String id,
        String localId,
        String name,
        String category,
        String rarity,
        TcgdexSetResponseDTO set,
        List<String> types,
        TcgdexPricingResponseDTO pricing

) {
}
