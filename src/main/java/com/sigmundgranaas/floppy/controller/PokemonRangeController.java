package com.sigmundgranaas.floppy.controller;

import com.sigmundgranaas.floppy.data.AllPokemonDTO;
import com.sigmundgranaas.floppy.data.JobResponse;
import com.sigmundgranaas.floppy.data.PokemonRangeDTO;
import com.sigmundgranaas.floppy.service.PdfGenerationService;
import com.sigmundgranaas.floppy.service.PokeApiService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping("/api/v1/pokemon/pdf/range")
public class PokemonRangeController extends BasePdfController {
    private final PokeApiService pokeApiService;

    public PokemonRangeController(
            PdfGenerationService pdfService,
            PokeApiService pokeApiService) {
        super(pdfService);
        this.pokeApiService = pokeApiService;
    }

    @PostMapping
    public ResponseEntity<JobResponse> generatePdf(
            @Valid @RequestBody PokemonRangeDTO rangeData) {
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