package com.sigmundgranaas.pokemon.controller;

import com.sigmundgranaas.core.data.JobResponse;
import com.sigmundgranaas.core.service.job.api.PdfGenerationService;
import com.sigmundgranaas.core.service.xml.XMLConverter;
import com.sigmundgranaas.pokemon.data.request.AllPokemonDTO;
import com.sigmundgranaas.pokemon.data.request.PokemonRangeDTO;
import com.sigmundgranaas.pokemon.service.PokeApiService;
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
@Validated
@RequestMapping("/api/v1/pokemon/pdf/range")
@Tag(name = "Pokemon Range PDF", description = "Generate PDFs for a range of Pokemon, fetched from the pokemon api")
public class PokemonRangeController extends BasePdfController {
    private final PokeApiService pokeApiService;

    public PokemonRangeController(
            PdfGenerationService pdfService,
            XMLConverter converter, PokeApiService pokeApiService) {
        super(pdfService, converter);
        this.pokeApiService = pokeApiService;
    }

    @Operation(
            summary = "Generate PDF for Pokemon range",
            description = "Creates a PDF document containing information about Pokemon within the specified ID range",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = PokemonRangeDTO.class),
                            examples = @ExampleObject(
                                    value = """
                    {
                        "fromId": 1,
                        "toId": 151,
                        "sortBy": "name",
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
                            description = "Invalid range parameters provided",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
                    )
            }
    )
    @PostMapping
    public ResponseEntity<JobResponse> generatePdf(
            @RequestBody PokemonRangeDTO rangeData) {
        var pokemonList = pokeApiService.getPokemonInRange(
                rangeData.fromId(),
                rangeData.toId()
        );

        var allPokemonDTO = new AllPokemonDTO(
                pokemonList,
                rangeData.sortBy(),
                rangeData.filterBy(),
                rangeData.pageSize()
        );

        return queuePdfGeneration(allPokemonDTO);
    }
}