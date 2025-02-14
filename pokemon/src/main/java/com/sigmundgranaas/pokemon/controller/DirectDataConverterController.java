package com.sigmundgranaas.pokemon.controller;

import com.sigmundgranaas.core.data.JobResponse;
import com.sigmundgranaas.core.service.PdfGenerationService;
import com.sigmundgranaas.core.service.pdf.api.FopPdfGenerator;
import com.sigmundgranaas.core.service.pdf.api.PdfGenerationRequest;
import com.sigmundgranaas.core.service.pdf.api.PdfResult;
import com.sigmundgranaas.core.service.xml.XMLConverter;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/v1/general")
@Validated
public class DirectDataConverterController extends BasePdfController {
    private final XMLConverter converter;
    private final FopPdfGenerator generator;

    public DirectDataConverterController(PdfGenerationService pdfService, XMLConverter converter, FopPdfGenerator generator) {
        super(pdfService);
        this.converter = converter;
        this.generator = generator;
    }

    @PostMapping("/pdf/queue/{template}")
    public ResponseEntity<JobResponse> generatePdf(
            @Valid @RequestBody Object data, @PathVariable String template) {

        return queuePdfGeneration(converter.convert(data), template);
    }

    @PostMapping("/pdf/{template}")
    public ResponseEntity<byte[]> convertPdf(
            @Valid @RequestBody Object data,
            @PathVariable String template) {

        PdfResult result = generator.generate(new PdfGenerationRequest(
                converter.convert(data),
                template
        ));

        String filename = template + "-" + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE) + ".pdf";

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + filename + "\"")
                .body(result.content());
    }
}
