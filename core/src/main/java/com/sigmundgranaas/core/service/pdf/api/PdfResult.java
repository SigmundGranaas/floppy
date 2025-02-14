package com.sigmundgranaas.core.service.pdf.api;

public record PdfResult(
        byte[] content,
        PdfMetadata metadata
) {}
