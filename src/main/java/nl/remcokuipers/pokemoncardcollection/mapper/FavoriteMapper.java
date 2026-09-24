package nl.remcokuipers.pokemoncardcollection.mapper;

import nl.remcokuipers.pokemoncardcollection.dto.FavoriteResponseDTO;
import nl.remcokuipers.pokemoncardcollection.entity.Favorite;
import nl.remcokuipers.pokemoncardcollection.entity.PokemonCard;
import nl.remcokuipers.pokemoncardcollection.entity.User;
import org.springframework.stereotype.Component;

@Component
public class FavoriteMapper {
    public FavoriteResponseDTO mapToResponseDTO(Favorite favorite) {
        return new FavoriteResponseDTO(
                favorite.getId(),
                favorite.getPokemonCard().getId()
        );
    }

    public Favorite mapToEntity(User user, PokemonCard pokemonCard) {
        return new Favorite(
                user,
                pokemonCard
        );

    }
}
