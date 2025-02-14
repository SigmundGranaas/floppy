package com.sigmundgranaas.core.service.storage.api;

import org.springframework.core.io.Resource;

import java.util.Optional;

public interface StorageService {
    void storeDocument(String jobId, byte[] content);
    Optional<Resource> getDocument(String jobId);
}
