package nl.remcokuipers.pokemoncardcollection.repository;

import nl.remcokuipers.pokemoncardcollection.entity.CollectionEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CollectionEntryRepository extends JpaRepository<CollectionEntry, Long> {
}
