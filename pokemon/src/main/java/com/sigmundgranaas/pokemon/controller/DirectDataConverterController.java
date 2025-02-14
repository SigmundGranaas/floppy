package com.sigmundgranaas.pokemon.controller;

import com.sigmundgranaas.core.data.JobResponse;
import com.sigmundgranaas.core.service.job.api.PdfGenerationService;
import com.sigmundgranaas.core.service.pdf.api.FopPdfGenerator;
import com.sigmundgranaas.core.service.pdf.api.PdfGenerationRequest;
import com.sigmundgranaas.core.service.pdf.api.PdfResult;
import com.sigmundgranaas.core.service.xml.XMLConverter;
import com.sigmundgranaas.pokemon.data.request.SinglePokemonDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/v1/general")
@Validated
public class DirectDataConverterController extends BasePdfController {
    private final FopPdfGenerator generator;

    public DirectDataConverterController(PdfGenerationService pdfService, XMLConverter converter, FopPdfGenerator generator) {
        super(pdfService, converter);
        this.generator = generator;
    }


    @Operation(
            summary = "Queue unverified data for PDF generation",
            description = "Queue unverified data for asynchronous PDF generation",
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
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = JobResponse.class)
                            )
                    )
            }
    )
    @PostMapping("/pdf/queue/{template}")
    public ResponseEntity<JobResponse> generatePdf(
            @Valid @RequestBody Object data, @Parameter(description = "Template name for PDF generation", example = "single-pokemon-template") @PathVariable String template) {

        return queuePdfGeneration(xmlConverter.convert(data), template);
    }

    @Operation(
            summary = "Generate Pokemon PDF synchronously",
            description = "Convert unverified data to PDF using the specified template synchronously",
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
                            description = "PDF generated successfully",
                            content = @Content(mediaType = MediaType.APPLICATION_PDF_VALUE)
                    )
            }
    )
    @PostMapping("/pdf/{template}")
    public ResponseEntity<byte[]> convertPdf(
            @Valid @RequestBody Object data,
            @Parameter(description = "Template name for PDF generation", example = "single-pokemon-template") @PathVariable String template) {
        PdfResult result = generator.generate(new PdfGenerationRequest(
                xmlConverter.convert(data),
                template
        ));
        String filename = template + "-" + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE) + ".pdf";

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + filename + "\"")
                .body(result.content());
    }
}
