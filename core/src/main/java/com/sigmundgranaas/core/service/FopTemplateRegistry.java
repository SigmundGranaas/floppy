package com.sigmundgranaas.core.service;

import com.sigmundgranaas.core.config.ResourceUtils;
import com.sigmundgranaas.core.error.TemplateLoadException;
import com.sigmundgranaas.core.error.TemplateNotFoundException;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.xml.transform.Templates;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class FopTemplateRegistry {
    private final Map<String, Templates> templateCache = new ConcurrentHashMap<>();
    private final ResourceUtils resourceUtils;
    private final TransformerFactory transformerFactory;

    public FopTemplateRegistry(ResourceUtils resourceUtils) {
        this.resourceUtils = resourceUtils;
        this.transformerFactory = TransformerFactory.newInstance();
        initializeTemplates();
    }

    private void initializeTemplates() {
        loadTemplate("single-pokemon", "single-pokemon-template.xsl");
        loadTemplate("team-pokemon", "team-pokemon-template.xsl");
        loadTemplate("all-pokemon", "all-pokemon-template.xsl");
    }

    private void loadTemplate(String name, String path) {
        try {
            Resource resource = resourceUtils.getTemplateResource(path);
            Templates template = transformerFactory.newTemplates(new StreamSource(resource.getInputStream()));
            templateCache.put(name, template);
        } catch (Exception e) {
            throw new TemplateLoadException("Failed to load template: " + name);
        }
    }

    public Templates getTemplate(String templateName) {
        return Optional.ofNullable(templateCache.get(templateName))
                .orElseThrow(() -> new TemplateNotFoundException("Template not found: " + templateName));
    }
}
