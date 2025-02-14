package com.sigmundgranaas.core.service.pdf.implementation;

import com.sigmundgranaas.core.error.PdfGenerationException;
import com.sigmundgranaas.core.service.pdf.api.FopPdfGenerator;
import com.sigmundgranaas.core.service.pdf.api.PdfGenerationRequest;
import com.sigmundgranaas.core.service.pdf.api.PdfMetadata;
import com.sigmundgranaas.core.service.pdf.api.PdfResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;

@Primary
@Slf4j
@Component
public class LoggingPdfGenerator implements FopPdfGenerator {
    private final FopPdfGenerator delegate;
    private final Clock clock;

    public LoggingPdfGenerator(
            @Qualifier("fopPdfGeneratorImpl") FopPdfGenerator delegate,
            Clock clock) {
        this.delegate = delegate;
        this.clock = clock;
    }

    @Override
    public PdfResult generate(PdfGenerationRequest request) throws PdfGenerationException {
        Instant start = Instant.now(clock);
        try {
            PdfResult result = delegate.generate(request);
            logSuccess(start, result.metadata());
            return result;
        } catch (Exception e) {
            logError(start, e);
            throw e;
        }
    }

    private void logSuccess(Instant start, PdfMetadata metadata) {
        Duration duration = Duration.between(start, Instant.now(clock));
        log.info("Generated PDF: pages={}, size={} bytes, duration={} ms",
                metadata.pageCount(),
                metadata.byteSize(),
                duration.toMillis());
    }

    private void logError(Instant start, Exception e) {
        Duration duration = Duration.between(start, Instant.now(clock));
        log.error("PDF generation failed after {} ms: {}",
                duration.toMillis(), e.getMessage(), e);
    }
}

