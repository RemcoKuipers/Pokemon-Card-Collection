package nl.remcokuipers.pokemoncardcollection.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import nl.remcokuipers.pokemoncardcollection.enums.Condition;

public record CollectionEntryUpdateDTO(
        @NotNull
        @Positive
        Integer quantity,
        @NotNull
        Condition condition

) {
}
