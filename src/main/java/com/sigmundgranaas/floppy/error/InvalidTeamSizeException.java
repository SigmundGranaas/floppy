package com.sigmundgranaas.floppy.error;

public class InvalidTeamSizeException extends PdfGenerationException {
    public InvalidTeamSizeException(String message) {
        super(message);
    }
}
