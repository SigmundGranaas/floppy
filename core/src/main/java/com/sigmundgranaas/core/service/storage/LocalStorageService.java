package com.sigmundgranaas.core.service.storage;

import com.sigmundgranaas.core.error.StorageException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class LocalStorageService implements StorageService {
    private final Map<String, byte[]> storage = new ConcurrentHashMap<>();
    private final Path storageLocation;

    public LocalStorageService(@Value("${pdf.storage.location:temp}") String location) {
        this.storageLocation = Paths.get(location);
        createStorageDirectory();
    }

    @Override
    public void storeDocument(String jobId, byte[] pdfContent) {
        storage.put(jobId, pdfContent);
        try {
            Files.write(storageLocation.resolve(jobId + ".pdf"), pdfContent);
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new StorageException("Failed to store PDF", e);
        }
    }

    @Override
    public Optional<Resource> getDocument(String jobId) {
        Path file = storageLocation.resolve(jobId + ".pdf");
        if (Files.exists(file)) {
            try {
                return Optional.of(new UrlResource(file.toUri()));
            } catch (MalformedURLException e) {
                throw new StorageException("Failed to read stored PDF", e);
            }
        }
        return Optional.empty();
    }

    private void createStorageDirectory() {
        try {
            Files.createDirectories(storageLocation);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }
}
