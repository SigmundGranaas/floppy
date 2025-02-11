package com.sigmundgranaas.floppy.error;

// Specific Exceptions
public class JobNotFoundException extends PdfGenerationException {
    public JobNotFoundException(String message) {
        super(message);
    }
}
