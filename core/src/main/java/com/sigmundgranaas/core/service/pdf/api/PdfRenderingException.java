package com.sigmundgranaas.core.service.pdf.api;

public class PdfRenderingException extends RuntimeException {
    public PdfRenderingException(String failedToRenderPdf, Exception e) {
        super(failedToRenderPdf, e);
    }
}
