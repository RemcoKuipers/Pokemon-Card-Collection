package nl.remcokuipers.pokemoncardcollection.dto;

import jakarta.validation.constraints.NotNull;

public record UserEnabledUpdateDTO(
        @NotNull
        Boolean enabled
) {
}
