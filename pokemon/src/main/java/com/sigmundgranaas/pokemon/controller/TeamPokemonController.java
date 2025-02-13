package com.sigmundgranaas.pokemon.controller;

//import com.sigmundgranaas.floppy.error.InvalidTeamSizeException;
import com.sigmundgranaas.core.data.JobResponse;
import com.sigmundgranaas.core.service.PdfGenerationService;
import com.sigmundgranaas.pokemon.data.request.TeamPokemonDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/pokemon/pdf/team")
@Validated
public class TeamPokemonController extends BasePdfController {

    public TeamPokemonController(PdfGenerationService pdfService) {
        super(pdfService);
    }

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
