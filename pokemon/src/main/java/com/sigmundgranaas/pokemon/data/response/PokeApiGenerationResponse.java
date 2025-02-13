package com.sigmundgranaas.pokemon.data.response;

import java.util.List;

public class PokeApiGenerationResponse {
    public List<PokemonSpecies> pokemonSpecies;

   public record PokemonSpecies(String name, String url ) {
    }
}