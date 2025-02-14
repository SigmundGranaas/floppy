package com.sigmundgranaas.core.service.pdf;

import com.sigmundgranaas.core.error.PdfGenerationException;
import com.sigmundgranaas.core.error.TemplateNotFoundException;
import com.sigmundgranaas.core.service.pdf.api.*;
import com.sigmundgranaas.core.service.pdf.implementation.FopConfigurationService;
import com.sigmundgranaas.core.service.pdf.implementation.FopPdfGeneratorImpl;
import com.sigmundgranaas.core.service.pdf.implementation.FopPdfRenderer;
import com.sigmundgranaas.core.service.pdf.implementation.PdfMetadataExtractor;
import com.sigmundgranaas.core.service.xslt.api.TemplateProvider;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

class FopPdfGeneratorTest {
    private FopPdfGenerator pdfGenerator;
    private TemplateProvider templateProvider;
    private PdfRenderer renderer;
    private PdfMetadataExtractor metadataExtractor;

    @BeforeEach
    void setUp() throws Exception {
        FopConfigurationService fopConfig = new FopConfigurationService();
        renderer = new FopPdfRenderer(fopConfig);
        metadataExtractor = new PdfMetadataExtractor();
        templateProvider = new TestTemplateProvider();

        pdfGenerator = new FopPdfGeneratorImpl(templateProvider, renderer, metadataExtractor);
    }

    @Test
    void shouldGenerateValidSinglePagePdf() throws Exception {
        String xmlContent = """
            <?xml version="1.0" encoding="UTF-8"?>
            <fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
                <fo:layout-master-set>
                    <fo:simple-page-master master-name="A4" page-width="210mm" page-height="297mm">
                        <fo:region-body/>
                    </fo:simple-page-master>
                </fo:layout-master-set>
                <fo:page-sequence master-reference="A4">
                    <fo:flow flow-name="xsl-region-body">
                        <fo:block>Single Page Test Content</fo:block>
                    </fo:flow>
                </fo:page-sequence>
            </fo:root>
            """;

        Source xmlSource = new StreamSource(new StringReader(xmlContent));
        var request = new PdfGenerationRequest(xmlSource, "test-template");

        // When
        PdfResult result = pdfGenerator.generate(request);

        // Then
        assertNotNull(result);
        assertTrue(result.content().length > 0);
        assertEquals(1, result.metadata().pageCount());
        assertTrue(result.metadata().byteSize() > 0);

        try (PDDocument document = Loader.loadPDF(result.content())) {
            assertEquals(1, document.getNumberOfPages());
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);
            assertTrue(text.contains("Single Page Test Content"));
        }
    }

    @Test
    void shouldGenerateMultiPagePdf() throws Exception {
        // Given
        var header = """
            <?xml version="1.0" encoding="UTF-8"?>
            <fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
                <fo:layout-master-set>
                    <fo:simple-page-master master-name="A4" page-width="210mm" page-height="297mm">
                        <fo:region-body/>
                    </fo:simple-page-master>
                </fo:layout-master-set>
                <fo:page-sequence master-reference="A4">
                    <fo:flow flow-name="xsl-region-body">
            """;

        var footer = """
                    </fo:flow>
                </fo:page-sequence>
            </fo:root>
            """;

        var contentBuilder = new StringBuilder(header);

        // Add enough content to span multiple pages
        for (int i = 0; i < 100; i++) {
            contentBuilder.append("""
                        <fo:block margin-top="20pt" font-size="12pt">Content line %d</fo:block>
            """.formatted(i));
        }

        contentBuilder.append(footer);

        Source xmlSource = new StreamSource(new StringReader(contentBuilder.toString()));
        var request = new PdfGenerationRequest(xmlSource, "test-template");

        // When
        PdfResult result = pdfGenerator.generate(request);

        // Then
        assertNotNull(result);
        assertTrue(result.metadata().pageCount() > 1);
        assertTrue(result.metadata().byteSize() > 0);

        try (PDDocument document = Loader.loadPDF(result.content())) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);
            assertTrue(text.contains("Content line 0"));
            assertTrue(text.contains("Content line 99"));
        }
    }

    @Test
    void shouldHandleSpecialCharacters() throws Exception {
        // Given
        String xmlContent = """
            <?xml version="1.0" encoding="UTF-8"?>
            <fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
                <fo:layout-master-set>
                    <fo:simple-page-master master-name="A4" page-width="210mm" page-height="297mm">
                        <fo:region-body/>
                    </fo:simple-page-master>
                </fo:layout-master-set>
                <fo:page-sequence master-reference="A4">
                    <fo:flow flow-name="xsl-region-body">
                        <fo:block>Special characters: áéíóú ñ €</fo:block>
                    </fo:flow>
                </fo:page-sequence>
            </fo:root>""";

        Source xmlSource = new StreamSource(new StringReader(xmlContent));
        var request = new PdfGenerationRequest(xmlSource, "test-template");

        // When
        PdfResult result = pdfGenerator.generate(request);

        // Then
        try (PDDocument document = Loader.loadPDF(result.content())) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);
            assertTrue(text.contains("áéíóú"));
            assertTrue(text.contains("ñ"));
            assertTrue(text.contains("€"));
        }
    }

    @Test
    void shouldThrowExceptionForInvalidTemplate() {
        // Given
        Source xmlSource = new StreamSource(new StringReader("<invalid/>"));
        var request = new PdfGenerationRequest(xmlSource, "non-existent-template");

        // When/Then
        var exception = assertThrows(PdfGenerationException.class,
                () -> pdfGenerator.generate(request));
        assertTrue(exception.getCause() instanceof TemplateNotFoundException);
    }

    @Test
    void shouldThrowExceptionForInvalidXml() {
        // Given
        String invalidXml = "<?xml version='1.0'?><invalid>";
        Source xmlSource = new StreamSource(new StringReader(invalidXml));
        var request = new PdfGenerationRequest(xmlSource, "test-template");

        // When/Then
        assertThrows(PdfGenerationException.class, () -> pdfGenerator.generate(request));
    }

    /**
     * Test implementation of TemplateProvider that provides a simple identity transform
     */
    private static class TestTemplateProvider implements TemplateProvider {
        private final Map<String, Templates> templates = new HashMap<>();

        TestTemplateProvider() throws Exception {
            String xslt = """
                <?xml version="1.0" encoding="UTF-8"?>
                <xsl:stylesheet version="1.0"
                              xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                              xmlns:fo="http://www.w3.org/1999/XSL/Format">
                    <xsl:template match="/">
                        <xsl:apply-templates/>
                    </xsl:template>
                    <xsl:template match="@*|node()">
                        <xsl:copy>
                            <xsl:apply-templates select="@*|node()"/>
                        </xsl:copy>
                    </xsl:template>
                </xsl:stylesheet>""";

            TransformerFactory factory = TransformerFactory.newInstance();
            templates.put("test-template",
                    factory.newTemplates(new StreamSource(new StringReader(xslt))));
        }

        @Override
        public Templates getTemplate(String name) throws TemplateNotFoundException {
            Templates template = templates.get(name);
            if (template == null) {
                throw new TemplateNotFoundException("Template not found: " + name);
            }
            return template;
        }
    }
}