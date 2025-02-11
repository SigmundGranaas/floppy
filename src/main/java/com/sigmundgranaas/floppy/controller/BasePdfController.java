package com.sigmundgranaas.floppy.controller;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.sigmundgranaas.floppy.data.JobResponse;
import com.sigmundgranaas.floppy.data.JobStatus;
import com.sigmundgranaas.floppy.data.PdfRequestDTO;
import com.sigmundgranaas.floppy.service.PdfGenerationService;
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

    protected BasePdfController(PdfGenerationService pdfService) {
        this.pdfService = pdfService;
    }

    @GetMapping("/status/{jobId}")
    public ResponseEntity<JobStatus> getJobStatus(@PathVariable String jobId) {
        return ResponseEntity.ok(pdfService.getJobStatus(jobId));
    }

    @GetMapping("/download/{jobId}")
    public ResponseEntity<Resource> downloadPdf(@PathVariable String jobId) {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"pokemon.pdf\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfService.getPdfResource(jobId));
    }

    protected ResponseEntity<JobResponse> queuePdfGeneration(PdfRequestDTO data) {
        return ResponseEntity.ok(new JobResponse(pdfService.queuePdfGeneration(data)));
    }

    protected ResponseEntity<JobResponse> queuePdfGeneration(Source request, String template) {
        return ResponseEntity.ok(new JobResponse(pdfService.queuePdfGeneration(request, template)));
    }
}