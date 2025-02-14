package com.sigmundgranaas.core.service.pdf.implementation;

import com.sigmundgranaas.core.error.PdfGenerationException;
import com.sigmundgranaas.core.service.pdf.api.*;
import com.sigmundgranaas.core.service.xslt.api.TemplateProvider;
import org.springframework.stereotype.Component;

import javax.xml.transform.Templates;

@Component
public class FopPdfGeneratorImpl implements FopPdfGenerator {
    private final TemplateProvider templateProvider;
    private final PdfRenderer renderer;
    private final PdfMetadataExtractor metadataExtractor;

    public FopPdfGeneratorImpl(
            TemplateProvider templateProvider,
            PdfRenderer renderer,
            PdfMetadataExtractor metadataExtractor) {
        this.templateProvider = templateProvider;
        this.renderer = renderer;
        this.metadataExtractor = metadataExtractor;
    }

    @Override
    public PdfResult generate(PdfGenerationRequest request) throws PdfGenerationException {
        try {
            Templates template = templateProvider.getTemplate(request.templateName());
            byte[] content = renderer.render(template, request.xmlSource());
            PdfMetadata metadata = metadataExtractor.extract(content);
            return new PdfResult(content, metadata);
        } catch (Exception e) {
            throw new PdfGenerationException("Failed to generate PDF", e);
        }
    }
}