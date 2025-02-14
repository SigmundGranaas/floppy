package com.sigmundgranaas.core.service.pdf.api;

public record PdfMetadata(
        int pageCount,
        int byteSize
) {}
