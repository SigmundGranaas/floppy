package com.sigmundgranaas.floppy.error;

import java.util.Map;

public record ErrorResponse(String message, Map<String, String> errors) {
    public ErrorResponse(String message) {
        this(message, Map.of());
    }
}