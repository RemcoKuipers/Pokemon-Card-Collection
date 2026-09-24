package nl.remcokuipers.pokemoncardcollection.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserRequestDTO(
        @NotBlank
        String username,
        @NotBlank
        @Email
        String email,
        @NotBlank
        @Size(min = 8)
        @Pattern(
                regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[^A-Za-z0-9]).+$",
                message = "Password must contain at least one letter, one number and one special character"
        )
        String password
) {
}