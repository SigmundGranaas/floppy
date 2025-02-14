package com.sigmundgranaas.core.service.pdf.implementation;

import com.sigmundgranaas.core.error.PdfGenerationException;
import com.sigmundgranaas.core.service.pdf.api.PdfMetadata;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class PdfMetadataExtractor {
    public PdfMetadata extract(byte[] pdfContent) throws PdfGenerationException {
        try (PDDocument document =  Loader.loadPDF(pdfContent)) {
            return new PdfMetadata(
                    document.getNumberOfPages(),
                    pdfContent.length
            );
        } catch (IOException e) {
            throw new PdfGenerationException("Failed to extract PDF metadata", e);
        }
    }
}
