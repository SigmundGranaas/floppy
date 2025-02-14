package com.sigmundgranaas.pokemon.data.request;

import com.sigmundgranaas.core.data.PdfRequestDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

@Schema(description = "Data for generating a Pokemon team PDF")
public record TeamPokemonDTO(
        @NotNull
        @Size(min = 6, max = 6, message = "Team must contain exactly 6 Pokemon")
        @Schema(description = "List of 6 Pokemon in the team")
        List<SinglePokemonDTO> team,

        @NotBlank(message = "Trainer name is required")
        @Schema(description = "Name of the Pokemon trainer", example = "Ash Ketchum")
        String trainerName,

        @NotBlank(message = "Team name is required")
        @Schema(description = "Name of the Pokemon team", example = "Kanto Champions")
        String teamName,

        @NotNull
        @PastOrPresent(message = "Formation date cannot be in the future")
        @Schema(description = "Date when the team was formed", example = "2024-02-14")
        LocalDate formationDate
) implements PdfRequestDTO {
    @Override
    @Schema(hidden = true)
    public String getTemplateName() {
        return "team-pokemon-template";
    }
}
