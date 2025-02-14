package com.sigmundgranaas.core.service.storage.implementation;

import com.sigmundgranaas.core.error.StorageException;
import com.sigmundgranaas.core.service.storage.api.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Primary
@Service
@Slf4j
public class LocalStorageService implements StorageService {
    private final Path storageLocation;

    public LocalStorageService(@Value("${pdf.storage.location:temp}") String location) {
        this.storageLocation = Paths.get(location);
        createStorageDirectory();
    }

    @Override
    public void storeDocument(String jobId, byte[] content) {
        try {
            Files.write(storageLocation.resolve(jobId + ".pdf"), content);
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
