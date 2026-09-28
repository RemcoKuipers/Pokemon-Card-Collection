package nl.remcokuipers.pokemoncardcollection.repository;

import nl.remcokuipers.pokemoncardcollection.entity.Favorite;
import nl.remcokuipers.pokemoncardcollection.entity.PokemonCard;
import nl.remcokuipers.pokemoncardcollection.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    boolean existsByUserAndPokemonCard(User user, PokemonCard pokemonCard);
}
