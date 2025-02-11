package com.sigmundgranaas.floppy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FloppyApplication {
    public static void main(String[] args) {
        SpringApplication.run(FloppyApplication.class, args);
    }
}
