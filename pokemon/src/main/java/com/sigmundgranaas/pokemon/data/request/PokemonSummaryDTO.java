package com.sigmundgranaas.pokemon.data.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Summary data for a Pokemon")
public record PokemonSummaryDTO(
        @NotBlank(message = "Pokemon name is required")
        @Schema(description = "Name of the Pokemon", example = "Pikachu")
        String name,

        @Min(value = 1, message = "Level must be at least 1")
        @Max(value = 100, message = "Level cannot exceed 100")
        @Schema(description = "Pokemon's level (1-100)", example = "25")
        int level,

        @NotBlank(message = "Pokemon type is required")
        @Schema(description = "Pokemon's type(s)", example = "Electric")
        String type
) {}
