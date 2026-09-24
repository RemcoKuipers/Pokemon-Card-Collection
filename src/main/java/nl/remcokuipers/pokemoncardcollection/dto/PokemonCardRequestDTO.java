package nl.remcokuipers.pokemoncardcollection.dto;

import jakarta.validation.constraints.NotBlank;

public record PokemonCardRequestDTO(
        @NotBlank
        String externalApiId) {
}
