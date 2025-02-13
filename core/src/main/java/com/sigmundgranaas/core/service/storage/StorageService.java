package com.sigmundgranaas.core.service.storage;

import org.springframework.core.io.Resource;

import java.util.Optional;

public interface StorageService {
    void storePdf(String jobId, byte[] pdfContent);
    Optional<Resource> getPdf(String jobId);
}
