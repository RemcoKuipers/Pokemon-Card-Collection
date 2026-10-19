package nl.remcokuipers.pokemoncardcollection.repository;

import nl.remcokuipers.pokemoncardcollection.entity.PokemonCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PokemonCardRepository extends JpaRepository<PokemonCard, Long> {
    Optional<PokemonCard> findByExternalApiId(String externalApiId);
}
