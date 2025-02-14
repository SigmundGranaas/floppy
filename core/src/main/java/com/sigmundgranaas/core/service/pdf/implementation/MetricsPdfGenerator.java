package com.sigmundgranaas.core.service.pdf.implementation;

import com.sigmundgranaas.core.error.PdfGenerationException;
import com.sigmundgranaas.core.service.pdf.api.FopPdfGenerator;
import com.sigmundgranaas.core.service.pdf.api.PdfGenerationRequest;
import com.sigmundgranaas.core.service.pdf.api.PdfResult;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.Timer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class MetricsPdfGenerator implements FopPdfGenerator {
    private final FopPdfGenerator delegate;
    private final MeterRegistry registry;

    public MetricsPdfGenerator(
            @Qualifier("loggingPdfGenerator") FopPdfGenerator delegate,
            MeterRegistry registry) {
        this.delegate = delegate;
        this.registry = registry;
    }

    @Override
    public PdfResult generate(PdfGenerationRequest request) throws PdfGenerationException {
        Timer.Sample sample = Timer.start(registry);
        AtomicInteger activeGenerations = registry.gauge("pdf.generations.active",
                new AtomicInteger(0));

        try {
            if (activeGenerations != null) {
                activeGenerations.incrementAndGet();
            }

            PdfResult result = delegate.generate(request);

            // Record generation time
            sample.stop(registry.timer("pdf.generation.duration",
                    Tags.of(
                            Tag.of("template", request.templateName()),
                            Tag.of("status", "success")
                    )));

            // Record page count
            registry.counter("pdf.pages",
                            "template", request.templateName())
                    .increment(result.metadata().pageCount());

            // Record PDF size
            registry.counter("pdf.size",
                            "template", request.templateName())
                    .increment(result.metadata().byteSize());

            return result;
        } catch (Exception e) {
            // Record failure metrics
            sample.stop(registry.timer("pdf.generation.duration",
                    Tags.of(
                            Tag.of("template", request.templateName()),
                            Tag.of("status", "error"),
                            Tag.of("error_type", e.getClass().getSimpleName())
                    )));

            registry.counter("pdf.generation.errors",
                            "template", request.templateName(),
                            "error_type", e.getClass().getSimpleName())
                    .increment();

            throw e;
        } finally {
            if (activeGenerations != null) {
                activeGenerations.decrementAndGet();
            }
        }
    }
}