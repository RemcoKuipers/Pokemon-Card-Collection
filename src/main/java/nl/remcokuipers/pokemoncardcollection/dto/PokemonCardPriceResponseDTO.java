package nl.remcokuipers.pokemoncardcollection.dto;

import java.math.BigDecimal;

public record PokemonCardPriceResponseDTO(
        String externalApiId,
        TcgdexCardmarketResponseDTO cardmarket,
        BigDecimal normalDifferenceFromAvg30,
        BigDecimal holoDifferenceFromAvg30
) {
}
