package com.sigmundgranaas.core.service.xslt;

import com.sigmundgranaas.core.error.TemplateLoadException;
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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class XsltTemplateService {
    private final TransformerFactory transformerFactory;
    private final ResourcePatternResolver resourceResolver;
    private final Map<String, Templates> templateCache = new ConcurrentHashMap<>();

    @Value("${xslt.base-path:/xslt}")
    private String xsltBasePath;

    public XsltTemplateService(TransformerFactory transformerFactory,
                               @Qualifier("resourcePatternResolver") ResourcePatternResolver resourceResolver) {
        this.transformerFactory = transformerFactory;
        this.resourceResolver = resourceResolver;
    }

    public Templates getTemplate(String templateName) throws TransformerConfigurationException {
        return templateCache.computeIfAbsent(templateName, this::loadTemplate);
    }

    private Templates loadTemplate(String templateName) {
        try {
            String resourcePath = String.format("classpath:%s/%s", xsltBasePath, templateName);
            Resource resource = resourceResolver.getResource(resourcePath);
            Source source = new StreamSource(resource.getInputStream(), resource.getURI().toString());
            return transformerFactory.newTemplates(source);
        } catch (Exception e) {
            throw new TemplateLoadException("Failed to load template: " + templateName);
        }
    }

    public void clearCache() {
        templateCache.clear();
    }
}
