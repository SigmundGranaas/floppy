package com.sigmundgranaas.pokemon.data.request;

import com.sigmundgranaas.core.data.PdfRequestDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Request parameters for generating a PDF for a range of Pokemon")
public record PokemonRangeDTO(
        @NotNull(message = "Starting Pokemon ID is required")
        @Min(value = 1, message = "Starting Pokemon ID must be at least 1")
        @Schema(description = "Starting Pokemon ID in the range", example = "1")
        Integer fromId,

        @NotNull(message = "Ending Pokemon ID is required")
        @Min(value = 1, message = "Ending Pokemon ID must be at least 1")
        @Schema(description = "Ending Pokemon ID in the range", example = "151")
        Integer toId,

        @Schema(description = "Field to sort Pokemon by (name, level, or type)", example = "name")
        String sortBy,

        @Schema(description = "Filter criteria for Pokemon (e.g., 'type=Fire')", example = "type=Fire")
        String filterBy,

        @Min(value = 1, message = "Page size must be at least 1")
        @Max(value = 100, message = "Page size cannot exceed 100")
        @Schema(description = "Number of Pokemon per page (1-100)", example = "20")
        int pageSize
) implements PdfRequestDTO {
    public PokemonRangeDTO {
        if (fromId != null && toId != null && fromId > toId) {
            throw new IllegalArgumentException("Starting ID must be less than or equal to ending ID");
        }
    }

    @Override
    @Schema(hidden = true)
    public String getTemplateName() {
        return "all-pokemon";
    }
}