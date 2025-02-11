package com.sigmundgranaas.floppy.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PokeApiPokemonResponse {
    public int id;
    public String name;
    public int height;
    public int weight;
    public List<PokemonType> types;
    public List<PokemonAbility> abilities;
    public PokemonSprites sprites;
    public List<PokemonStat> stats;
    public List<PokemonMove> moves;
    public Species species;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PokemonType {
        public int slot;
        public Type type;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Type {
        public String name;
        public String url;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PokemonAbility {
        public boolean is_hidden;
        public int slot;
        public Ability ability;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Ability {
        public String name;
        public String url;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PokemonSprites {
        public String front_default;
        public String front_shiny;
        public String front_female;
        public String front_shiny_female;
        public String back_default;
        public String back_shiny;
        public String back_female;
        public String back_shiny_female;
        public Other other;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Other {
        public DreamWorld dream_world;
        public Home home;
        public OfficialArtwork official_artwork;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DreamWorld {
        public String front_default;
        public String front_female;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Home {
        public String front_default;
        public String front_female;
        public String front_shiny;
        public String front_shiny_female;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class OfficialArtwork {
        public String front_default;
        public String front_shiny;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PokemonStat {
        public int base_stat;
        public int effort;
        public Stat stat;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Stat {
        public String name;
        public String url;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PokemonMove {
        public Move move;
        public List<VersionGroupDetail> version_group_details;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Move {
        public String name;
        public String url;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class VersionGroupDetail {
        public int level_learned_at;
        public MoveLearnMethod move_learn_method;
        public VersionGroup version_group;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MoveLearnMethod {
        public String name;
        public String url;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class VersionGroup {
        public String name;
        public String url;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Species {
        public String name;
        public String url;
    }
}
