package com.sigmundgranaas.pokemon.controller;

import com.sigmundgranaas.core.data.JobResponse;
import com.sigmundgranaas.core.service.job.api.PdfGenerationService;
import com.sigmundgranaas.core.service.xml.XMLConverter;
import com.sigmundgranaas.pokemon.data.request.SinglePokemonDTO;
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
@RequestMapping("/api/v1/pokemon/pdf/single")
@Validated
@Tag(name = "Single Pokemon PDF", description = "Generate PDF for an individual Pokemon")
public class SinglePokemonController extends BasePdfController {

    public SinglePokemonController(PdfGenerationService pdfService, XMLConverter converter) {
        super(pdfService, converter);
    }

    @Operation(
            summary = "Generate single Pokemon PDF",
            description = "Creates a PDF document with detailed information about a single Pokemon",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = SinglePokemonDTO.class),
                            examples = @ExampleObject(
                                    value = """
                    {
                        "name": "Charizard",
                        "level": 50,
                        "moves": ["Flamethrower", "Dragon Claw", "Air Slash", "Solar Beam"],
                        "stats": {
                            "hp": 78,
                            "attack": 84,
                            "defense": 78,
                            "special-attack": 109,
                            "special-defense": 85,
                            "speed": 100
                        },
                        "type": "Fire/Flying",
                        "imageUrl": "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/6.png"
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
                            description = "Invalid Pokemon data provided"
                    )
            }
    )
    @PostMapping
    public ResponseEntity<JobResponse> generatePdf(
           @RequestBody SinglePokemonDTO pokemonData) {
        return queuePdfGeneration(pokemonData);
    }
}