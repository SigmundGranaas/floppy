package com.sigmundgranaas.core.service.xslt.implementation;

import com.sigmundgranaas.core.error.TemplateLoadException;
import com.sigmundgranaas.core.error.TemplateNotFoundException;
import com.sigmundgranaas.core.service.xslt.api.TemplateProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;

import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class XsltTemplateService implements TemplateProvider {
    private final TransformerFactory transformerFactory;
    private final ResourcePatternResolver resourceResolver;
    private final Map<String, Templates> templateCache = new ConcurrentHashMap<>();

    @Value("${xslt.base-path:/xslt}")
    private String xsltBasePath;

    public XsltTemplateService(TransformerFactory transformerFactory, @Qualifier("resourcePatternResolver") ResourcePatternResolver resourceResolver) {
        this.transformerFactory = transformerFactory;
        this.resourceResolver = resourceResolver;
    }

    public Templates getTemplate(String templatePath)  {
        return templateCache.computeIfAbsent(templatePath, this::loadTemplate);
    }

    private Templates loadTemplate(String templatePath) {
        try {
            // Clean the template path
            String cleanPath = templatePath.replaceAll("\\.xsl$", "");

            // Try to find the template
            Resource resource = findTemplateResource(cleanPath);
            if (resource == null) {
                throw new TemplateNotFoundException("Template not found: " + templatePath);
            }

            Source source = new StreamSource(resource.getInputStream(), resource.getURI().toString());
            return transformerFactory.newTemplates(source);
        } catch (Exception e) {
            throw new TemplateLoadException("Failed to load template: " + templatePath);
        }
    }

    private Resource findTemplateResource(String path)  {
        // Build possible paths
        List<String> possiblePaths = new ArrayList<>();
        possiblePaths.add(String.format("classpath:%s/%s.xsl", xsltBasePath, path));
        possiblePaths.add(String.format("classpath:%s/%s/index.xsl", xsltBasePath, path));
        possiblePaths.add(String.format("classpath:%s/%s", xsltBasePath, path));

        // Try each path
        for (String possiblePath : possiblePaths) {
            Resource resource = resourceResolver.getResource(possiblePath);
            if (resource.exists()) {
                return resource;
            }
        }

        return null;
    }
}