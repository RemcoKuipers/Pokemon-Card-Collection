package nl.remcokuipers.pokemoncardcollection.dto;

import nl.remcokuipers.pokemoncardcollection.enums.Gender;

import java.time.LocalDate;

public record UserProfileResponseDTO(
        Long id,
        String displayName,
        String country,
        LocalDate dateOfBirth,
        Gender gender
) {
}
