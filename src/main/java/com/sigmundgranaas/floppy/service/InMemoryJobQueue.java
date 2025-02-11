package com.sigmundgranaas.floppy.service;

import com.sigmundgranaas.floppy.data.JobStatus;
import com.sigmundgranaas.floppy.data.PdfJob;
import com.sigmundgranaas.floppy.job.JobQueue;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
public class InMemoryJobQueue implements JobQueue {
    private final Map<String, PdfJob> jobs = new ConcurrentHashMap<>();
    private final Queue<PdfJob> pendingJobs = new ConcurrentLinkedQueue<>();

    @Override
    public void submit(PdfJob job) {
        jobs.put(job.jobId(), job);
        pendingJobs.offer(job);
    }

    @Override
    public Optional<JobStatus> getStatus(String jobId) {
        return Optional.ofNullable(jobs.get(jobId))
                .map(PdfJob::status);
    }

    @Override
    public Optional<PdfJob> poll() {
        return Optional.ofNullable(pendingJobs.poll());
    }

    @Override
    public void markComplete(String jobId) {
        updateJobStatus(jobId, JobStatus.COMPLETED);
    }

    @Override
    public void markFailed(String jobId) {
        updateJobStatus(jobId, JobStatus.FAILED);
    }

    private void updateJobStatus(String jobId, JobStatus status) {
        jobs.computeIfPresent(jobId, (id, job) ->
                new PdfJob(job.jobId(), job.templateName(), job.data(), status, job.createdAt()));
    }
}