package nl.remcokuipers.pokemoncardcollection.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record TcgdexCardmarketResponseDTO(
        String unit,
        String updated,
        BigDecimal avg,
        BigDecimal trend,
        BigDecimal avg1,
        BigDecimal avg7,
        BigDecimal avg30,
        @JsonProperty("avg-holo") BigDecimal avgHolo,
        @JsonProperty("trend-holo") BigDecimal trendHolo,
        @JsonProperty("avg1-holo") BigDecimal avg1Holo,
        @JsonProperty("avg7-holo") BigDecimal avg7Holo,
        @JsonProperty("avg30-holo") BigDecimal avg30Holo

) {
}
