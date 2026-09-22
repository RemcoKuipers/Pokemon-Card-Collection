package nl.remcokuipers.pokemoncardcollection.entity;

import jakarta.persistence.*;
import nl.remcokuipers.pokemoncardcollection.enums.FileType;

@Entity
@Table(name = "file_uploads")
public class FileUpload {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String fileName;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FileType fileType;
    @Column(nullable = false)
    private String filePath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "collection_entry_id", nullable = false)
    private CollectionEntry collectionEntry;

    public FileUpload() {

    }

    public FileUpload(String fileName, FileType fileType, String filePath, CollectionEntry collectionEntry) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.filePath = filePath;
        this.collectionEntry = collectionEntry;

    }

    public Long getId() {
        return id;
    }

    public CollectionEntry getCollectionEntry() {
        return collectionEntry;
    }

    public void setCollectionEntry(CollectionEntry collectionEntry) {
        this.collectionEntry = collectionEntry;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public FileType getFileType() {
        return fileType;
    }

    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }
}
