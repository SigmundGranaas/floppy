package com.sigmundgranaas.floppy.controller;

import com.sigmundgranaas.floppy.data.AllPokemonDTO;
import com.sigmundgranaas.floppy.data.JobResponse;
import com.sigmundgranaas.floppy.data.SinglePokemonDTO;
import com.sigmundgranaas.floppy.data.TeamPokemonDTO;
import com.sigmundgranaas.floppy.service.PdfGenerationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/pokemon/pdf/single")
@Validated
public class SinglePokemonController extends BasePdfController {

    public SinglePokemonController(PdfGenerationService pdfService) {
        super(pdfService);
    }

    @PostMapping
    public ResponseEntity<JobResponse> generatePdf(
            @Valid @RequestBody SinglePokemonDTO pokemonData) {
        return queuePdfGeneration(pokemonData);
    }
}


