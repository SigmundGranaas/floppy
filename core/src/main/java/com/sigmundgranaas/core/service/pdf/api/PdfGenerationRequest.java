package com.sigmundgranaas.core.service.pdf.api;


import javax.xml.transform.Source;

public record PdfGenerationRequest(
        Source xmlSource,
        String templateName
) {}
