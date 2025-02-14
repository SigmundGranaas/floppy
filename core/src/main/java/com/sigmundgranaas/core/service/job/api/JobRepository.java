package com.sigmundgranaas.core.service.job.api;

import com.sigmundgranaas.core.data.JobStatus;
import com.sigmundgranaas.core.data.PdfJob;

import java.util.Optional;

public interface JobRepository {
    PdfJob save(PdfJob job);
    Optional<PdfJob> findById(String jobId);
    void updateStatus(String jobId, JobStatus status);
}

