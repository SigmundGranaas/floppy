package com.sigmundgranaas.pokemon.data.request;

import com.sigmundgranaas.core.data.PdfRequestDTO;

import java.util.List;

public record AllPokemonDTO(
        List<PokemonSummaryDTO> pokemon,
        String sortBy,
        String filterBy,
        int pageSize
) implements PdfRequestDTO {
    @Override
    public String getTemplateName() {
        return "all-pokemon-template.xsl";
    }
}