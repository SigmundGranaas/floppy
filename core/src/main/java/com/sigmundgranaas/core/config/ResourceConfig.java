package com.sigmundgranaas.core.config;

import com.sigmundgranaas.core.service.storage.LocalStorageService;
import com.sigmundgranaas.core.service.storage.StorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;

@Configuration
public class ResourceConfig {
    @Value("${pdf.storage.location:pdf-storage}")
    private String storageLocation;

    @Bean
    public ResourceLoader resourceLoader() {
        return new DefaultResourceLoader();
    }

    @Bean
    public StorageService storageService() {
        return new LocalStorageService(storageLocation);
    }
}