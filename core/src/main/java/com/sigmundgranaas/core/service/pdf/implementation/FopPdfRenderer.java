package com.sigmundgranaas.core.service.pdf.implementation;

import com.sigmundgranaas.core.service.pdf.api.PdfRenderer;
import com.sigmundgranaas.core.service.pdf.api.PdfRenderingException;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.springframework.stereotype.Component;

import javax.xml.transform.*;
import javax.xml.transform.sax.SAXResult;
import java.io.ByteArrayOutputStream;

@Component
public class FopPdfRenderer implements PdfRenderer {
    private final FopFactory fopFactory;

    public FopPdfRenderer(FopConfigurationService fopConfig) {
        this.fopFactory = fopConfig.getFopFactory();
    }

    @Override
    public byte[] render(Templates template, Source xmlSource) throws PdfRenderingException {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, out);

            Transformer transformer = template.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            Result result = new SAXResult(fop.getDefaultHandler());
            transformer.transform(xmlSource, result);

            return out.toByteArray();
        } catch (Exception e) {
            throw new PdfRenderingException("Failed to render PDF", e);
        }
    }
}
