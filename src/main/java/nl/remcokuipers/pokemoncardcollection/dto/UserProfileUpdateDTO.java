package nl.remcokuipers.pokemoncardcollection.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import nl.remcokuipers.pokemoncardcollection.enums.Gender;

public record UserProfileUpdateDTO(
        @NotNull
        Gender gender,
        @NotBlank
        String displayName,
        String country
) {
}
