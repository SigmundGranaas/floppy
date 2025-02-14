package com.sigmundgranaas.pokemon.controller;

import com.sigmundgranaas.core.data.JobResponse;
import com.sigmundgranaas.core.service.job.api.PdfGenerationService;
import com.sigmundgranaas.core.service.xml.XMLConverter;
import com.sigmundgranaas.pokemon.data.request.AllPokemonDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/pokemon/pdf/all")
@Validated
@Tag(name = "All Pokemon PDF", description = "Generate PDF containing multiple Pokemon")
public class AllPokemonController extends BasePdfController {

    public AllPokemonController(PdfGenerationService pdfService, XMLConverter converter) {
        super(pdfService, converter);
    }

    @Operation(
            summary = "Generate multi-Pokemon PDF",
            description = "Creates a PDF document containing information about multiple Pokemon with sorting and filtering options",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AllPokemonDTO.class),
                            examples = @ExampleObject(
                                    value = """
                    {
                        "pokemon": [
                            {
                                "name": "Charizard",
                                "level": 50,
                                "type": "Fire/Flying"
                            },
                            {
                                "name": "Pikachu",
                                "level": 25,
                                "type": "Electric"
                            }
                        ],
                        "sortBy": "level",
                        "filterBy": "type=Fire",
                        "pageSize": 20
                    }
                    """
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "PDF generation job queued successfully",
                            content = @Content(schema = @Schema(implementation = JobResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid request parameters"
                    )
            }
    )
    @PostMapping
    public ResponseEntity<JobResponse> generatePdf(
           @RequestBody AllPokemonDTO pokemonData) {
        return queuePdfGeneration(pokemonData);
    }
}
