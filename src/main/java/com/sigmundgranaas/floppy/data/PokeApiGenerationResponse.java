package com.sigmundgranaas.floppy.data;

import java.util.List;

public class PokeApiGenerationResponse {
    public List<PokemonSpecies> pokemonSpecies;

   public record PokemonSpecies(String name, String url ) {
    }
}