package nl.remcokuipers.pokemoncardcollection.mapper;

import nl.remcokuipers.pokemoncardcollection.dto.PokemonCardResponseDTO;
import nl.remcokuipers.pokemoncardcollection.dto.TcgdexCardResponseDTO;
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

    public PokemonCard mapToEntity(TcgdexCardResponseDTO dto) {
        PokemonCard pokemonCard = new PokemonCard(
                dto.id(),
                dto.name(),
                dto.category(),
                dto.localId(),
                dto.rarity(),
                dto.set().name()
        );
        if( dto.types() != null){
        pokemonCard.setTypes(dto.types());
        }

        return pokemonCard;
    }
}
