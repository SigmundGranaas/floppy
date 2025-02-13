package com.sigmundgranaas.pokemon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {"com.sigmundgranaas.core", "com.sigmundgranaas.pokemon"})
@EnableScheduling
public class PokemonApplication {
    public static void main(String[] args) {
        SpringApplication.run(PokemonApplication.class, args);
    }
}
