package nl.remcokuipers.pokemoncardcollection.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;



public record PokemonCardUpdateDTO(
        @NotBlank
        String name,
        @NotBlank
        String supertype,
        @NotBlank
        String number,
        @NotBlank
        String rarity,
        @NotBlank
        String setName,
        @NotNull
        List<String> types,
        @NotNull
        List<String> subtypes

) {
}
