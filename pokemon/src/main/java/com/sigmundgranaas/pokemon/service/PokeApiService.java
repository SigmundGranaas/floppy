package com.sigmundgranaas.pokemon.service;


import com.sigmundgranaas.pokemon.data.request.PokemonSummaryDTO;
import com.sigmundgranaas.pokemon.data.response.PokeApiPokemonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Service
public class PokeApiService {
    private final RestTemplate restTemplate;
    private static final String POKEAPI_BASE_URL = "https://pokeapi.co/api/v2";
    private static final int BATCH_SIZE = 10; // Number of concurrent requests

    public PokeApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<PokemonSummaryDTO> getPokemonInRange(int fromId, int toId) {
        if (fromId > toId) {
            throw new IllegalArgumentException("fromId must be less than or equal to toId");
        }

        List<CompletableFuture<PokemonSummaryDTO>> futures = new ArrayList<>();
        ExecutorService executor = Executors.newFixedThreadPool(BATCH_SIZE);

        try {
            // Create a list of futures for parallel processing
            for (int id = fromId; id <= toId; id++) {
                final int pokemonId = id;
                CompletableFuture<PokemonSummaryDTO> future = CompletableFuture.supplyAsync(() -> {
                    try {
                        return fetchPokemonById(pokemonId);
                    } catch (HttpClientErrorException e) {
                        if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                            log.warn("Pokemon with ID {} not found", pokemonId);
                            return null;
                        }
                        throw e;
                    } catch (Exception e) {
                        log.error("Error fetching Pokemon with ID {}: {}", pokemonId, e.getMessage());
                        throw e;
                    }
                }, executor);
                futures.add(future);
            }

            // Wait for all futures to complete and collect results
            return futures.stream()
                    .map(CompletableFuture::join)
                    .filter(pokemon -> pokemon != null)
                    .toList();

        } finally {
            executor.shutdown();
        }
    }

    private PokemonSummaryDTO fetchPokemonById(int id) {
        String pokemonUrl = UriComponentsBuilder.fromHttpUrl(POKEAPI_BASE_URL)
                .path("/pokemon/{id}")
                .buildAndExpand(id)
                .toUriString();

        PokeApiPokemonResponse response = restTemplate.getForObject(pokemonUrl, PokeApiPokemonResponse.class);

        if (response == null) {
            throw new RuntimeException("Failed to fetch Pokemon with ID: " + id);
        }

        return new PokemonSummaryDTO(
                response.name,
                1, // Default level
                !response.types.isEmpty()
                        ? response.types.get(0).type.name
                        : "unknown"
        );
    }
}