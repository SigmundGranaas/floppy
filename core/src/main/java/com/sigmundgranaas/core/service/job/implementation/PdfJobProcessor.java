package com.sigmundgranaas.core.service.job.implementation;

import com.sigmundgranaas.core.data.JobStatus;
import com.sigmundgranaas.core.data.PdfJob;
import com.sigmundgranaas.core.service.job.api.JobRepository;
import com.sigmundgranaas.core.service.pdf.api.FopPdfGenerator;
import com.sigmundgranaas.core.service.pdf.api.PdfGenerationRequest;
import com.sigmundgranaas.core.service.storage.api.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PdfJobProcessor {
    private final JobRepository jobRepository;
    private final StorageService pdfStorage;
    private final FopPdfGenerator pdfGenerator;

    public PdfJobProcessor(JobRepository jobRepository, StorageService pdfStorage, FopPdfGenerator pdfGenerator) {
        this.jobRepository = jobRepository;
        this.pdfStorage = pdfStorage;
        this.pdfGenerator = pdfGenerator;
    }

    public void processJob(PdfJob job) {
        try {
            log.info("Processing PDF job: {}", job.jobId());
            jobRepository.updateStatus(job.jobId(), JobStatus.PROCESSING);

            var pdfContent = pdfGenerator.generate(new PdfGenerationRequest(job.data(), job.templateName()));
            pdfStorage.storeDocument(job.jobId(), pdfContent.content());

            jobRepository.updateStatus(job.jobId(), JobStatus.COMPLETED);
            log.info("Successfully completed PDF job: {}", job.jobId());
        } catch (Exception e) {
            log.error("Failed to process PDF job: {}", job.jobId(), e);
            jobRepository.updateStatus(job.jobId(), JobStatus.FAILED);
        }
    }
}
