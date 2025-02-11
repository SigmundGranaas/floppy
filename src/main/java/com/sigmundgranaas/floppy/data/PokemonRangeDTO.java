package com.sigmundgranaas.floppy.data;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record PokemonRangeDTO(
        @NotNull @Min(1) Integer fromId,
        @NotNull @Min(1) Integer toId,
        String sortBy,
        String filterBy,
        int pageSize
) implements PdfRequestDTO {
    @Override
    public String getTemplateName() {
        return "all-pokemon";
    }
}
