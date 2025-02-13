package com.sigmundgranaas.pokemon.data.request;

import com.sigmundgranaas.core.data.PdfRequestDTO;

import java.util.List;
import java.util.Map;

public record SinglePokemonDTO(
        String name,
        int level,
        List<String> moves,
        Map<String, Integer> stats,
        String type,
        String imageUrl
) implements PdfRequestDTO {
    @Override
    public String getTemplateName() {
        return "single-pokemon-template.xsl";
    }
}

