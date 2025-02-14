package com.sigmundgranaas.pokemon.controller;

import com.sigmundgranaas.core.data.JobResponse;
import com.sigmundgranaas.core.data.JobStatus;
import com.sigmundgranaas.core.data.PdfRequestDTO;
import com.sigmundgranaas.core.service.job.api.PdfGenerationService;
import com.sigmundgranaas.core.service.xml.XMLConverter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.xml.transform.Source;

@Slf4j
public abstract class BasePdfController {
    private final PdfGenerationService pdfService;
    protected final XMLConverter xmlConverter;

    protected BasePdfController(PdfGenerationService pdfService, XMLConverter xmlConverter) {
        this.pdfService = pdfService;
        this.xmlConverter = xmlConverter;
    }

    @GetMapping("/status/{jobId}")
    public ResponseEntity<JobStatus> getJobStatus(@Parameter(description = "ID of the PDF generation job", example = "550e8400-e29b-41d4-a716-446655440000") @PathVariable String jobId) {
        return ResponseEntity.ok(pdfService.getJobStatus(jobId));
    }

    @Operation(
            summary = "Download generated PDF",
            description = "Download a previously generated Pokemon PDF document",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "PDF downloaded successfully",
                            content = @Content(mediaType = MediaType.APPLICATION_PDF_VALUE)
                    )
            }
    )
    @GetMapping("/download/{jobId}")
    public ResponseEntity<Resource> downloadPdf(@PathVariable String jobId) {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"pokemon.pdf\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfService.getPdfResource(jobId));
    }

    protected ResponseEntity<JobResponse> queuePdfGeneration(PdfRequestDTO data) {
        return ResponseEntity.ok(new JobResponse(pdfService.submitPdfGeneration(xmlConverter.convert(data), data.getTemplateName())));
    }

    protected ResponseEntity<JobResponse> queuePdfGeneration(Source request, String template) {
        return ResponseEntity.ok(new JobResponse(pdfService.submitPdfGeneration(request, template)));
    }
}