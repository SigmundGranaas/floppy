package com.sigmundgranaas.core.service;

import com.sigmundgranaas.core.data.PdfJob;
import com.sigmundgranaas.core.service.pdf.api.FopPdfGenerator;
import com.sigmundgranaas.core.service.pdf.api.PdfGenerationRequest;
import com.sigmundgranaas.core.service.storage.StorageService;
import com.sigmundgranaas.core.service.queue.JobQueue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PdfJobProcessorService {
    private final JobQueue jobQueue;
    private final FopPdfGenerator pdfGenerator;
    private final StorageService storageService;

    public PdfJobProcessorService(
            JobQueue jobQueue,
            FopPdfGenerator pdfGenerator,
            StorageService storageService) {
        this.jobQueue = jobQueue;
        this.pdfGenerator = pdfGenerator;
        this.storageService = storageService;
    }

    @Scheduled(fixedDelay = 1000) // Runs every second
    public void processNextJob() {
        jobQueue.poll().ifPresent(this::processJob);
    }

    private void processJob(PdfJob job) {
        log.info("Processing job: {}", job.jobId());
        try {
            var pdfContent = pdfGenerator.generate(new PdfGenerationRequest(job.data(), job.templateName()));
            storageService.storeDocument(job.jobId(), pdfContent.content());
            jobQueue.markComplete(job.jobId());
            log.info("Successfully completed job: {}", job.jobId());
        } catch (Exception e) {
            log.error("Failed to process job: {}", job.jobId(), e);
            jobQueue.markFailed(job.jobId());
        }
    }
}
