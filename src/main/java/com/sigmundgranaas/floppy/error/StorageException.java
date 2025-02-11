package com.sigmundgranaas.floppy.error;

public class StorageException extends PdfGenerationException {
    public StorageException(String message) {
        super(message);
    }

    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
