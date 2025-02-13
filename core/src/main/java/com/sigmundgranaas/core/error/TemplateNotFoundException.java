package com.sigmundgranaas.core.error;


public class TemplateNotFoundException extends PdfGenerationException {
    public TemplateNotFoundException(String message) {
        super(message);
    }
}
