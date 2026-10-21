package nl.remcokuipers.pokemoncardcollection.dto;

import nl.remcokuipers.pokemoncardcollection.enums.FileType;

public record FileUploadResponseDTO(
        Long id,
        String fileName,
        FileType fileType,
        Long collectionEntryId
) {
}
