package com.sigmundgranaas.floppy.controller;

import com.sigmundgranaas.floppy.data.AllPokemonDTO;
import com.sigmundgranaas.floppy.data.JobResponse;
import com.sigmundgranaas.floppy.service.PdfGenerationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/pokemon/pdf/all")
@Validated
public class AllPokemonController extends BasePdfController {

    public AllPokemonController(PdfGenerationService pdfService) {
        super(pdfService);
    }

    @PostMapping
    public ResponseEntity<JobResponse> generatePdf(
            @Valid @RequestBody AllPokemonDTO pokemonData) {
        return queuePdfGeneration(pokemonData);
    }
}
