package com.sigmundgranaas.core.service.job.implementation;

import com.sigmundgranaas.core.data.PdfJob;
import com.sigmundgranaas.core.service.job.api.JobSubmitter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

@Component
@Slf4j
public class InMemoryJobSubmitter implements JobSubmitter {
    private final BlockingQueue<PdfJob> queue = new LinkedBlockingQueue<>();
    private final PdfJobProcessor processor;
    private final ExecutorService executorService;

    public InMemoryJobSubmitter(
            PdfJobProcessor processor,
            @Value("${queue.threads:4}") int threads) {
        this.processor = processor;
        this.executorService = Executors.newFixedThreadPool(threads);
        startProcessing();
    }

    @Override
    public void submit(PdfJob job) {
        queue.offer(job);
    }

    private void startProcessing() {
        executorService.submit(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    var job = queue.take();
                    processor.processJob(job);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
    }
}
