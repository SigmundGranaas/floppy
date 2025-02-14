package com.sigmundgranaas.pokemon.controller;

import com.sigmundgranaas.core.data.JobResponse;
import com.sigmundgranaas.core.service.job.api.PdfGenerationService;
import com.sigmundgranaas.core.service.xml.XMLConverter;
import com.sigmundgranaas.pokemon.data.request.AllPokemonDTO;
import com.sigmundgranaas.pokemon.data.request.PokemonRangeDTO;
import com.sigmundgranaas.pokemon.service.PokeApiService;
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
            XMLConverter converter, PokeApiService pokeApiService) {
        super(pdfService, converter);
        this.pokeApiService = pokeApiService;
    }

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