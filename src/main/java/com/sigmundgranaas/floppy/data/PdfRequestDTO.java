package com.sigmundgranaas.floppy.data;

public sealed interface PdfRequestDTO permits AllPokemonDTO, PokemonRangeDTO, SinglePokemonDTO, TeamPokemonDTO {
    String getTemplateName();
}