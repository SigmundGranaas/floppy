package com.sigmundgranaas.floppy.error;

public class PdfNotFoundException extends PdfGenerationException {
    public PdfNotFoundException(String message) {
        super(message);
    }
}
