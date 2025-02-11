package com.sigmundgranaas.floppy.controller;

import com.sigmundgranaas.floppy.data.JobResponse;
import com.sigmundgranaas.floppy.data.TeamPokemonDTO;
//import com.sigmundgranaas.floppy.error.InvalidTeamSizeException;
import com.sigmundgranaas.floppy.service.PdfGenerationService;
import jakarta.validation.Valid;
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
            @Valid @RequestBody TeamPokemonDTO teamData) {
        if (teamData.team().size() != 6) {
            // Testing skipping this
            // throw new InvalidTeamSizeException("Pokemon team must contain exactly 6 Pokemon");
        }
        return queuePdfGeneration(teamData);
    }
}
