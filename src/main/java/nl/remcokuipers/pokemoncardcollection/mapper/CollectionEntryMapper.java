package nl.remcokuipers.pokemoncardcollection.mapper;

import nl.remcokuipers.pokemoncardcollection.dto.CollectionEntryRequestDTO;
import nl.remcokuipers.pokemoncardcollection.dto.CollectionEntryResponseDTO;
import nl.remcokuipers.pokemoncardcollection.entity.CollectionEntry;
import nl.remcokuipers.pokemoncardcollection.entity.PokemonCard;
import nl.remcokuipers.pokemoncardcollection.entity.User;
import org.springframework.stereotype.Component;

@Component
public class CollectionEntryMapper {

    public CollectionEntryResponseDTO mapToResponseDTO(CollectionEntry collectionEntry) {
        return new CollectionEntryResponseDTO(
                collectionEntry.getId(),
                collectionEntry.getPokemonCard().getId(),
                collectionEntry.getQuantity(),
                collectionEntry.getCondition()

        );
    }

    public CollectionEntry mapToEntity(User user, PokemonCard pokemonCard, CollectionEntryRequestDTO dto) {

        return new CollectionEntry(
                dto.quantity(),
                dto.condition(),
                user,
                pokemonCard
        );
    }
}
