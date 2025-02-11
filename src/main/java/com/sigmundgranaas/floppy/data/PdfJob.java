package com.sigmundgranaas.floppy.data;

import com.sigmundgranaas.floppy.service.XMLConvertible;

import javax.xml.transform.Source;
import java.time.LocalDateTime;

public record PdfJob(
        String jobId,
        String templateName,
        Source data,
        JobStatus status,
        LocalDateTime createdAt
) {}