package nl.remcokuipers.pokemoncardcollection.dto;

import jakarta.validation.constraints.NotNull;

public record FavoriteRequestDTO(
        @NotNull
        Long pokemonCardId
) {
}
