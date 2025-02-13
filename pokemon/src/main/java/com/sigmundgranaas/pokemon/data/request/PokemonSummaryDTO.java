package com.sigmundgranaas.pokemon.data.request;

public record PokemonSummaryDTO(
        String name,
        int level,
        String type
) {}
