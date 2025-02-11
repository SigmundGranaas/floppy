package com.sigmundgranaas.floppy.job;

import com.sigmundgranaas.floppy.data.JobStatus;
import com.sigmundgranaas.floppy.data.PdfJob;

import java.util.Optional;

public interface JobQueue {
    void submit(PdfJob job);
    Optional<JobStatus> getStatus(String jobId);
    Optional<PdfJob> poll();
    void markComplete(String jobId);
    void markFailed(String jobId);
}
