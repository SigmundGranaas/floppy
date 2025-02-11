package com.sigmundgranaas.floppy.config;

import com.sigmundgranaas.floppy.service.LocalStorageService;
import com.sigmundgranaas.floppy.service.StorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ResourceConfig implements WebMvcConfigurer {
    @Value("${pdf.storage.location:pdf-storage}")
    private String storageLocation;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/pdf/**")
                .addResourceLocations("file:" + storageLocation + "/");
    }

    @Bean
    public StorageService storageService() {
        return new LocalStorageService(storageLocation);
    }
}