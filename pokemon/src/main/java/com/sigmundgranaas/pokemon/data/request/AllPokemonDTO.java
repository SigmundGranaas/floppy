package com.sigmundgranaas.pokemon.data.request;

import com.sigmundgranaas.core.data.PdfRequestDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.util.List;

@Schema(description = "Data for generating a PDF containing multiple Pokemon")
public record AllPokemonDTO(
        @NotNull
        @Size(min = 1, message = "Pokemon list cannot be empty")
        @Schema(description = "List of Pokemon summaries")
        List<PokemonSummaryDTO> pokemon,

        @Pattern(regexp = "^(name|level|type)$", message = "Sort by must be either 'name', 'level', or 'type'")
        @Schema(description = "Field to sort Pokemon by", example = "level",
                allowableValues = {"name", "level", "type"})
        String sortBy,

        @Schema(description = "Filter criteria for Pokemon", example = "type=Fire")
        String filterBy,

        @Min(value = 1, message = "Page size must be at least 1")
        @Max(value = 100, message = "Page size cannot exceed 100")
        @Schema(description = "Number of Pokemon per page (1-100)", example = "20")
        int pageSize
) implements PdfRequestDTO {
    @Override
    @Schema(hidden = true)
    public String getTemplateName() {
        return "all-pokemon-template.xsl";
    }
}