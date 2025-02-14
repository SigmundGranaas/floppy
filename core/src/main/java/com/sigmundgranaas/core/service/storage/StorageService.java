package com.sigmundgranaas.core.service.storage;

import org.springframework.core.io.Resource;

import java.util.Optional;

public interface StorageService {
    void storeDocument(String jobId, byte[] pdfContent);
    Optional<Resource> getDocument(String jobId);
}
