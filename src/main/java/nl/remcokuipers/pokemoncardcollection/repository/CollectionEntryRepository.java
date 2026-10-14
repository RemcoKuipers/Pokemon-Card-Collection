package nl.remcokuipers.pokemoncardcollection.repository;

import nl.remcokuipers.pokemoncardcollection.entity.CollectionEntry;
import nl.remcokuipers.pokemoncardcollection.entity.PokemonCard;
import nl.remcokuipers.pokemoncardcollection.entity.User;
import nl.remcokuipers.pokemoncardcollection.enums.Condition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CollectionEntryRepository extends JpaRepository<CollectionEntry, Long> {
    List<CollectionEntry> findByUser(User user);
    Optional<CollectionEntry> findByIdAndUser(Long id, User user);
    Optional<CollectionEntry> findByUserAndPokemonCardAndCondition(User user, PokemonCard pokemonCard, Condition condition);

    Condition condition(Condition condition);
}
