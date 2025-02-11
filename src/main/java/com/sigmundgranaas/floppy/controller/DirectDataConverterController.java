package com.sigmundgranaas.floppy.controller;

import com.sigmundgranaas.floppy.data.JobResponse;
import com.sigmundgranaas.floppy.service.PdfGenerationService;
import com.sigmundgranaas.floppy.service.XMLConverter;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/general")
@Validated
public class DirectDataConverterController extends BasePdfController {

    private final XMLConverter converter;

    public DirectDataConverterController(PdfGenerationService pdfService, XMLConverter converter) {
        super(pdfService);
        this.converter = converter;
    }

    @PostMapping("/pdf/{template}")
    public ResponseEntity<JobResponse> generatePdf(
            @Valid @RequestBody Object data, @PathVariable String template) {

        return queuePdfGeneration(converter.convert(data), template);
    }
}
