package com.sigmundgranaas.core.service.queue;


import com.sigmundgranaas.core.data.JobStatus;
import com.sigmundgranaas.core.data.PdfJob;

import java.util.Optional;

public interface JobQueue {
    void submit(PdfJob job);
    Optional<JobStatus> getStatus(String jobId);
    Optional<PdfJob> poll();
    void markComplete(String jobId);
    void markFailed(String jobId);
}
