package nl.remcokuipers.pokemoncardcollection.dto;

public record UserResponseDTO(
        Long id,
        String username,
        String email,
        Boolean enabled
) {
}
