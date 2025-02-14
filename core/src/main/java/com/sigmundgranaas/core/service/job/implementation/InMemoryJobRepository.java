package com.sigmundgranaas.core.service.job.implementation;

import com.sigmundgranaas.core.data.JobStatus;
import com.sigmundgranaas.core.data.PdfJob;
import com.sigmundgranaas.core.service.job.api.JobRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@Slf4j
public class InMemoryJobRepository implements JobRepository {
    private final Map<String, PdfJob> jobs = new ConcurrentHashMap<>();

    @Override
    public PdfJob save(PdfJob job) {
        jobs.put(job.jobId(), job);
        return job;
    }

    @Override
    public Optional<PdfJob> findById(String jobId) {
        return Optional.ofNullable(jobs.get(jobId));
    }

    @Override
    public void updateStatus(String jobId, JobStatus newStatus) {
        jobs.computeIfPresent(jobId, (id, existingJob) ->
                new PdfJob(
                        existingJob.jobId(),
                        existingJob.templateName(),
                        existingJob.data(),
                        newStatus,
                        existingJob.createdAt()
                )
        );
    }
}
