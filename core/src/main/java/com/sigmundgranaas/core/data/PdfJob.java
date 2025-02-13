package com.sigmundgranaas.core.data;


import javax.xml.transform.Source;
import java.time.LocalDateTime;

public record PdfJob(
        String jobId,
        String templateName,
        Source data,
        JobStatus status,
        LocalDateTime createdAt
) {}