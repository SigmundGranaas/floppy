package com.sigmundgranaas.floppy.service;

import com.sigmundgranaas.floppy.error.JobNotFoundException;
import com.sigmundgranaas.floppy.error.PdfNotFoundException;
import com.sigmundgranaas.floppy.data.JobStatus;
import com.sigmundgranaas.floppy.data.PdfJob;
import com.sigmundgranaas.floppy.data.PdfRequestDTO;
import com.sigmundgranaas.floppy.job.JobQueue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.xml.transform.Source;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
public class PdfGenerationService {
    private final JobQueue jobQueue;
    private final StorageService storageService;
    private final XMLConverter mapper;

    public PdfGenerationService(JobQueue jobQueue, StorageService storageService, XMLConverter mapper) {
        this.jobQueue = jobQueue;
        this.storageService = storageService;
        this.mapper = mapper;
    }

    public String queuePdfGeneration(PdfRequestDTO request) {
        var jobId = UUID.randomUUID().toString();
        var job = new PdfJob(
                jobId,
                request.getTemplateName(),
                mapper.convert(request),
                JobStatus.PENDING,
                LocalDateTime.now()
        );

        jobQueue.submit(job);
        return jobId;
    }

    public String queuePdfGeneration(Source request, String template) {
        var jobId = UUID.randomUUID().toString();
        var job = new PdfJob(
                jobId,
                template,
                request,
                JobStatus.PENDING,
                LocalDateTime.now()
        );

        jobQueue.submit(job);
        return jobId;
    }


    public JobStatus getJobStatus(String jobId) {
        return jobQueue.getStatus(jobId)
                .orElseThrow(() -> new JobNotFoundException("Job not found: " + jobId));
    }

    public Resource getPdfResource(String jobId) {
        return storageService.getPdf(jobId)
                .orElseThrow(() -> new PdfNotFoundException("PDF not found for job: " + jobId));
    }
}
