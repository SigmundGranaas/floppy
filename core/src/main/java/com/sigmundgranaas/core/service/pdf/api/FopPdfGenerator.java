package com.sigmundgranaas.core.service.pdf.api;

import com.sigmundgranaas.core.error.PdfGenerationException;

public interface FopPdfGenerator {
    PdfResult generate(PdfGenerationRequest request) throws PdfGenerationException;
}

