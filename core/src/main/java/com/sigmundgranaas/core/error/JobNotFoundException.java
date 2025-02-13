package com.sigmundgranaas.core.error;

// Specific Exceptions
public class JobNotFoundException extends PdfGenerationException {
    public JobNotFoundException(String message) {
        super(message);
    }
}
