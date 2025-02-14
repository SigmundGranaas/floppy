package com.sigmundgranaas.pokemon.data.request;

import com.sigmundgranaas.core.data.PdfRequestDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.util.List;
import java.util.Map;

@Schema(description = "Data for generating a single Pokemon PDF")
public record SinglePokemonDTO(
        @NotBlank(message = "Pokemon name is required")
        @Schema(description = "Name of the Pokemon", example = "Charizard")
        String name,

        @Min(value = 1, message = "Level must be at least 1")
        @Max(value = 100, message = "Level cannot exceed 100")
        @Schema(description = "Pokemon's level (1-100)", example = "50")
        int level,

        @NotNull
        @Size(min = 1, max = 4, message = "Pokemon must have between 1 and 4 moves")
        @Schema(description = "List of Pokemon's moves (max 4)",
                example = "[\"Flamethrower\", \"Dragon Claw\", \"Air Slash\", \"Solar Beam\"]")
        List<String> moves,

        @NotNull
        @Schema(description = "Pokemon's stats",
                example = "{\"hp\": 78, \"attack\": 84, \"defense\": 78, \"special-attack\": 109, \"special-defense\": 85, \"speed\": 100}")
        Map<String, Integer> stats,

        @NotBlank(message = "Pokemon type is required")
        @Schema(description = "Pokemon's type(s)", example = "Fire/Flying")
        String type,

        @Schema(description = "URL to Pokemon's sprite image",
                example = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/6.png")
        String imageUrl
) implements PdfRequestDTO {
    @Override
    @Schema(hidden = true)
    public String getTemplateName() {
        return "single-pokemon-template.xsl";
    }
}

