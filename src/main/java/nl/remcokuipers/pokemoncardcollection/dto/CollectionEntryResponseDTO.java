package nl.remcokuipers.pokemoncardcollection.dto;

import nl.remcokuipers.pokemoncardcollection.enums.Condition;

public record CollectionEntryResponseDTO(
        Long id,
        Long pokemonCardId,
        Integer quantity,
        Condition condition
) {
}
