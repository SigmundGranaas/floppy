package com.sigmundgranaas.floppy.data;

import java.util.List;

public record AllPokemonDTO(
        List<PokemonSummaryDTO> pokemon,
        String sortBy,
        String filterBy,
        int pageSize
) implements PdfRequestDTO {
    @Override
    public String getTemplateName() {
        return "all-pokemon";
    }
}