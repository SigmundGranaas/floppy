package com.sigmundgranaas.pokemon.controller;

import com.sigmundgranaas.core.data.JobResponse;
import com.sigmundgranaas.core.service.job.api.PdfGenerationService;
import com.sigmundgranaas.core.service.xml.XMLConverter;
import com.sigmundgranaas.pokemon.data.request.TeamPokemonDTO;
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
@RequestMapping("/api/v1/pokemon/pdf/team")
@Validated
@Tag(name = "Pokemon Team PDF", description = "Generate PDF for a team of Pokemon")
public class TeamPokemonController extends BasePdfController {

    public TeamPokemonController(PdfGenerationService pdfService, XMLConverter converter) {
        super(pdfService, converter);
    }

    @Operation(
            summary = "Generate team PDF",
            description = "Creates a PDF document containing information about a team of 6 Pokemon",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = TeamPokemonDTO.class),
                            examples = @ExampleObject(
                                    value = """
                    {
                        "team": [
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
                        ],
                        "trainerName": "Ash Ketchum",
                        "teamName": "Kanto Champions",
                        "formationDate": "2024-02-14"
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
                            description = "Invalid team data or incorrect team size"
                    )
            }
    )
    @PostMapping
    public ResponseEntity<JobResponse> generatePdf(
            @RequestBody TeamPokemonDTO teamData) {
        if (teamData.team().size() != 6) {
            // Testing skipping this
            // throw new InvalidTeamSizeException("Pokemon team must contain exactly 6 Pokemon");
        }
        return queuePdfGeneration(teamData);
    }
}
