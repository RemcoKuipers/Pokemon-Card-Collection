package nl.remcokuipers.pokemoncardcollection.dto;

import jakarta.validation.constraints.NotBlank;
import nl.remcokuipers.pokemoncardcollection.enums.Gender;
import java.time.LocalDate;


public record UserProfileRequestDTO(
        @NotBlank
        String displayName,
        String country,
        LocalDate dateOfBirth,
        Gender gender
) {
}
