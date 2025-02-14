package com.sigmundgranaas.core.service.job.api;

import com.sigmundgranaas.core.data.JobStatus;
import com.sigmundgranaas.core.data.PdfJob;
import com.sigmundgranaas.core.error.JobNotFoundException;
import com.sigmundgranaas.core.error.PdfNotFoundException;
import com.sigmundgranaas.core.service.storage.api.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.xml.transform.Source;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
public class PdfGenerationService {
    private final JobRepository jobRepository;
    private final JobSubmitter jobSubmitter;
    private final StorageService pdfStorage;

    public PdfGenerationService(JobRepository jobRepository, JobSubmitter jobSubmitter, StorageService pdfStorage) {
        this.jobRepository = jobRepository;
        this.jobSubmitter = jobSubmitter;
        this.pdfStorage = pdfStorage;
    }

    public String submitPdfGeneration(Source xmlSource, String templateName) {
        var jobId = UUID.randomUUID().toString();
        var job = new PdfJob(
                jobId,
                templateName,
                xmlSource,
                JobStatus.PENDING,
                LocalDateTime.now()
        );

        jobRepository.save(job);
        jobSubmitter.submit(job);
        return jobId;
    }

    public JobStatus getJobStatus(String jobId) {
        return jobRepository.findById(jobId)
                .map(PdfJob::status)
                .orElseThrow(() -> new JobNotFoundException("Job not found: " + jobId));
    }

    public Resource getPdfResource(String jobId) {
        return pdfStorage.getDocument(jobId)
                .orElseThrow(() -> new PdfNotFoundException("PDF not found for job: " + jobId));
    }
}
