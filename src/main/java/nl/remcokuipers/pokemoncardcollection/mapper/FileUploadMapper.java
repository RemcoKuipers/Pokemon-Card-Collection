package nl.remcokuipers.pokemoncardcollection.mapper;

import nl.remcokuipers.pokemoncardcollection.dto.FileResourceDTO;
import nl.remcokuipers.pokemoncardcollection.dto.FileUploadResponseDTO;
import nl.remcokuipers.pokemoncardcollection.entity.FileUpload;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class FileUploadMapper {

    public FileUploadResponseDTO mapToResponseDTO(FileUpload fileUpload) {
        return new FileUploadResponseDTO(
                fileUpload.getId(),
                fileUpload.getFileName(),
                fileUpload.getFileType(),
                fileUpload.getCollectionEntry().getId()
        );
    }
    public FileResourceDTO mapToFileResourceDTO(FileUpload fileUpload, Resource resource) {
        return new FileResourceDTO(
                fileUpload.getFileName(),
                fileUpload.getFileType(),
                resource
        );
    }
}
