package nl.remcokuipers.pokemoncardcollection.repository;

import nl.remcokuipers.pokemoncardcollection.entity.CollectionEntry;
import nl.remcokuipers.pokemoncardcollection.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CollectionEntryRepository extends JpaRepository<CollectionEntry, Long> {
    List<CollectionEntry> findByUser(User user);
    Optional<CollectionEntry> findByIdAndUser(Long id, User user);
}
