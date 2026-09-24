package nl.remcokuipers.pokemoncardcollection.mapper;

import nl.remcokuipers.pokemoncardcollection.dto.PokemonCardResponseDTO;
import nl.remcokuipers.pokemoncardcollection.entity.PokemonCard;
import org.springframework.stereotype.Component;

@Component
public class PokemonCardMapper {

    public PokemonCardResponseDTO mapToResponseDTO(PokemonCard pokemonCard) {
        return new PokemonCardResponseDTO(
                pokemonCard.getId(),
                pokemonCard.getExternalApiId(),
                pokemonCard.getName(),
                pokemonCard.getSupertype(),
                pokemonCard.getTypes(),
                pokemonCard.getSubtypes(),
                pokemonCard.getNumber(),
                pokemonCard.getRarity(),
                pokemonCard.getSetName()
        );
    }
}
