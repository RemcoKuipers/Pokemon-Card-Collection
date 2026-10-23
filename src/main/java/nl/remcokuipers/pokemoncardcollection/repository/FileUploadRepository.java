package nl.remcokuipers.pokemoncardcollection.repository;

import nl.remcokuipers.pokemoncardcollection.entity.CollectionEntry;
import nl.remcokuipers.pokemoncardcollection.entity.FileUpload;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FileUploadRepository extends JpaRepository<FileUpload, Long> {
    List<FileUpload> findByCollectionEntry(CollectionEntry collectionEntry);
    Optional<FileUpload> findByIdAndCollectionEntry(Long fileId, CollectionEntry collectionEntry);
}
