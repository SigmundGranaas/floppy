package com.sigmundgranaas.core.service.pdf;

import com.sigmundgranaas.core.service.pdf.api.PdfRenderingException;
import com.sigmundgranaas.core.service.pdf.implementation.FopConfigurationService;
import com.sigmundgranaas.core.service.pdf.implementation.FopPdfRenderer;
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

class FopPdfRendererTest {

    private FopPdfRenderer pdfRenderer;
    private FopConfigurationService fopConfig;


    @BeforeEach
    void setUp() {
        fopConfig = new FopConfigurationService();
        pdfRenderer = new FopPdfRenderer(fopConfig);
    }

    @Test
    void shouldGenerateValidPdfFromSimpleXml() throws Exception {
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
                    <fo:block>Hello World</fo:block>
                </fo:flow>
            </fo:page-sequence>
        </fo:root>
        """;

        Source xmlSource = new StreamSource(new StringReader(xmlContent));
        Templates template = createTestTemplate();

        byte[] pdfContent = pdfRenderer.render(template, xmlSource);

        assertNotNull(pdfContent);
        assertTrue(pdfContent.length > 0);

        try (PDDocument document = Loader.loadPDF(pdfContent)) {
            assertNotNull(document);
            assertEquals(1, document.getNumberOfPages());
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);
            assertTrue(text.contains("Hello World"));
        }
    }
    @Test
    void shouldHandleLargeContent() throws Exception {
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

        var contentBuilder = new StringBuilder(header);

        // Add 1000 blocks of text to generate a large PDF
        for (int i = 0; i < 1000; i++) {
            contentBuilder.append("<fo:block>Content line ").append(i).append("</fo:block>\n");
        }

        contentBuilder.append("</fo:flow>\n")
                .append("</fo:page-sequence>\n")
                .append("</fo:root>");

        Source xmlSource = new StreamSource(new StringReader(contentBuilder.toString()));
        Templates template = createTestTemplate();


        byte[] pdfContent = pdfRenderer.render(template, xmlSource);


        assertNotNull(pdfContent);
        assertTrue(pdfContent.length > 0);

        try (PDDocument document = Loader.loadPDF(pdfContent)) {
            assertTrue(document.getNumberOfPages() > 1);
        }
    }
    @Test
    void shouldHandleSpecialCharacters() throws Exception {

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
            </fo:root>
            """;

        Source xmlSource = new StreamSource(new StringReader(xmlContent));
        Templates template = createTestTemplate();


        byte[] pdfContent = pdfRenderer.render(template, xmlSource);


        try (PDDocument document = Loader.loadPDF(pdfContent)) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);
            assertTrue(text.contains("áéíóú"));
            assertTrue(text.contains("ñ"));
            assertTrue(text.contains("€"));
        }
    }

    @Test
    void shouldThrowExceptionForInvalidXml() throws Exception {

        String invalidXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><invalid>";
        Source xmlSource = new StreamSource(new StringReader(invalidXml));
        Templates template = createTestTemplate();

        assertThrows(PdfRenderingException.class, () -> {
            pdfRenderer.render(template, xmlSource);
        });
    }

    private Templates createTestTemplate() throws Exception {
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
            </xsl:stylesheet>
            """;

        TransformerFactory factory = TransformerFactory.newInstance();
        return factory.newTemplates(new StreamSource(new StringReader(xslt)));
    }
}