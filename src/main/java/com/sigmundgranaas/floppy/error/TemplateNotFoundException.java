package com.sigmundgranaas.floppy.error;


public class TemplateNotFoundException extends PdfGenerationException {
    public TemplateNotFoundException(String message) {
        super(message);
    }
}
