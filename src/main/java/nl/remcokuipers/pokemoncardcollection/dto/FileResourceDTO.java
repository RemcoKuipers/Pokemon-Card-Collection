package nl.remcokuipers.pokemoncardcollection.dto;

import nl.remcokuipers.pokemoncardcollection.enums.FileType;
import org.springframework.core.io.Resource;

public record FileResourceDTO(
        String fileName,
        FileType fileType,
        Resource resource
) {
}
