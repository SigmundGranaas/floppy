package com.sigmundgranaas.core.service.storage.implementation;

import com.sigmundgranaas.core.service.storage.api.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Profile("memory")
@Slf4j
public class InMemoryStorageService implements StorageService {
    private final Map<String, byte[]> storage = new ConcurrentHashMap<>();

    @Override
    public void storeDocument(String jobId, byte[] content) {
        storage.put(jobId + ".pdf", content);
    }

    @Override
    public Optional<Resource> getDocument(String jobId) {
        byte[] content = storage.get(jobId + ".pdf");
        if (content == null) {
            return Optional.empty();
        }

        return Optional.of(new ByteArrayResource(content) {
            @Override
            public String getFilename() {
                return jobId + ".pdf";
            }
        });
    }
}
