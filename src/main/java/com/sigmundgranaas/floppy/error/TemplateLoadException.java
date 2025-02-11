package com.sigmundgranaas.floppy.error;

public class TemplateLoadException extends PdfGenerationException {
    public TemplateLoadException(String message) {
        super(message);
    }
}
