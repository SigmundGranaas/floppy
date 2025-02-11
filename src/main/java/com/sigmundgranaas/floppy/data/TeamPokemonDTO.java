package com.sigmundgranaas.floppy.data;

import java.time.LocalDate;
import java.util.List;

public record TeamPokemonDTO(
        List<SinglePokemonDTO> team,
        String trainerName,
        String teamName,
        LocalDate formationDate
) implements PdfRequestDTO {
    @Override
    public String getTemplateName() {
        return "team-pokemon";
    }
}