package nl.remcokuipers.pokemoncardcollection.dto;

import java.util.List;

public record PokemonCardResponseDTO(
        Long id,
        String externalApiId,
        String name,
        String supertype,
        List<String> types,
        List<String> subtypes,
        String number,
        String rarity,
        String setName
) {

}