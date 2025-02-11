package com.sigmundgranaas.floppy.config;

import com.sigmundgranaas.floppy.error.TemplateLoadException;
import com.sigmundgranaas.floppy.error.TemplateNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ResourceUtils {
    private final String templatesLocation;

    public ResourceUtils(@Value("${pdf.templates.location:templates}") String templatesLocation) {
        this.templatesLocation = templatesLocation;
    }

    public Resource getTemplateResource(String templatePath) {
        try {
            // First try to load from classpath
            Resource resource = new ClassPathResource(templatesLocation + "/" + templatePath);
            if (resource.exists()) {
                log.debug("Found template in classpath: {}", templatePath);
                return resource;
            }

            // Then try to load from file system
            resource = new FileSystemResource(templatesLocation + "/" + templatePath);
            if (resource.exists()) {
                log.debug("Found template in filesystem: {}", templatePath);
                return resource;
            }

            throw new TemplateNotFoundException("Template not found: " + templatePath);
        } catch (Exception e) {
            throw new TemplateLoadException("Failed to load template: " + templatePath);
        }
    }
}
