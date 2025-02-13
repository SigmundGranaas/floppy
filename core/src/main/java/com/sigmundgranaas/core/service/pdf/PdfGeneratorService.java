package com.sigmundgranaas.core.service.pdf;

import com.sigmundgranaas.core.data.PdfJob;
import com.sigmundgranaas.core.service.FopConfigurationService;
import com.sigmundgranaas.core.service.FopTemplateRegistry;
import com.sigmundgranaas.core.error.PdfGenerationException;
import com.sigmundgranaas.core.service.xslt.XsltTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.MimeConstants;
import org.springframework.stereotype.Service;

import javax.xml.transform.*;
import javax.xml.transform.sax.SAXResult;
import java.io.ByteArrayOutputStream;
import java.time.Duration;
import java.time.Instant;

@Service
@Slf4j
public class PdfGeneratorService {
    private final FopConfigurationService fopConfig;
    private final FopTemplateRegistry templateRegistry;
    private final XsltTemplateService templateService;

    public PdfGeneratorService(
            FopConfigurationService fopConfig,
            FopTemplateRegistry templateRegistry, XsltTemplateService templateService) {
        this.fopConfig = fopConfig;
        this.templateRegistry = templateRegistry;
        this.templateService = templateService;
    }

    public byte[] generatePdf(PdfJob job) {
        Instant startTime = Instant.now();
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            // Get template
            Instant templateStartTime = Instant.now();
            log.info("Getting template for: {}", job.templateName());
            Templates template = templateService.getTemplate(job.templateName());
            log.info("Template loaded (took {} ms)",
                    Duration.between(templateStartTime, Instant.now()).toMillis());

            // Setup FOP
            Instant fopStartTime = Instant.now();
            log.info("Setting up FOP");
            Fop fop = fopConfig.getFopFactory().newFop(MimeConstants.MIME_PDF, out);
            log.info("FOP setup complete (took {} ms)",
                    Duration.between(fopStartTime, Instant.now()).toMillis());

            // Transform
            Instant transformStartTime = Instant.now();
            log.info("Starting transformation");
            Transformer transformer = template.newTransformer();
            Result res = new SAXResult(fop.getDefaultHandler());

            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            transformer.transform(job.data(), res);
            log.info("Transformation complete (took {} ms)",
                    Duration.between(transformStartTime, Instant.now()).toMillis());

            byte[] result = out.toByteArray();
            log.info("Generated PDF size: {} bytes", result.length);
            log.info("Total PDF generation time: {} ms",
                    Duration.between(startTime, Instant.now()).toMillis());

            return result;
        } catch (Exception e) {
            log.error("Failed to generate PDF after {} ms. Error: {}",
                    Duration.between(startTime, Instant.now()).toMillis(),
                    e.getMessage(), e);
            throw new PdfGenerationException("Failed to generate PDF", e);
        }
    }


}