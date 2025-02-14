package com.sigmundgranaas.core.service.xslt.implementation;

import com.sigmundgranaas.core.error.TemplateLoadException;
import com.sigmundgranaas.core.error.TemplateNotFoundException;
import com.sigmundgranaas.core.service.xslt.api.TemplateProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.xml.transform.*;
import javax.xml.transform.stream.StreamSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.parsers.DocumentBuilderFactory;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

@Service
@Slf4j
public class XsltTemplateService implements TemplateProvider {
    private final TransformerFactory transformerFactory;
    private final ResourcePatternResolver resourceResolver;
    private final Map<String, Templates> templateCache = new ConcurrentHashMap<>();
    private final DocumentBuilderFactory documentBuilderFactory;

    private static final Pattern CLEANUP_PATTERN = Pattern.compile("\\s+");

    @Value("${xslt.base-path:/templates}")
    private String xsltBasePath;

    public XsltTemplateService(TransformerFactory transformerFactory, ResourcePatternResolver resourceResolver) {
        this.transformerFactory = transformerFactory;
        this.resourceResolver = resourceResolver;
        this.documentBuilderFactory = DocumentBuilderFactory.newInstance();
        this.documentBuilderFactory.setNamespaceAware(true);

        // Configure the transformer factory for better error handling
        this.transformerFactory.setErrorListener(new ErrorListener() {
            @Override
            public void warning(TransformerException e) {
                log.warn("XSLT Warning: {}", e.getMessage());
            }

            @Override
            public void error(TransformerException e) {
                log.error("XSLT Error: {}", e.getMessage());
            }

            @Override
            public void fatalError(TransformerException e) throws TransformerException {
                log.error("XSLT Fatal Error: {}", e.getMessage());
                throw e;
            }
        });
    }

    @Override
    public Templates getTemplate(String templatePath) {
        return templateCache.computeIfAbsent(templatePath, this::loadTemplate);
    }

    private Templates loadTemplate(String templatePath) {
        try {
            String cleanPath = StringUtils.cleanPath(templatePath);
            Resource resource = findTemplateResource(cleanPath);

            if (resource == null) {
                throw new TemplateNotFoundException("Template not found: " + templatePath);
            }

            log.debug("Loading template from resource: {}", resource.getURI());

            // Pre-process the template content
            String processedContent = preprocessTemplate(resource);

            // Create a Source that uses our custom URI resolver
            Source source = createTemplateSource(processedContent, resource);

            // Set up the URI resolver with the correct base path
            String templateBasePath = determineTemplateBasePath(resource, cleanPath);
            XsltUriResolver uriResolver = new XsltUriResolver(resourceResolver, templateBasePath);
            transformerFactory.setURIResolver(uriResolver);

            return transformerFactory.newTemplates(source);
        } catch (Exception e) {
            String message = String.format("Failed to load template: %s - %s", templatePath, e.getMessage());
            log.error(message, e);
            throw new TemplateLoadException(message);
        }
    }

    private String preprocessTemplate(Resource resource) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder content = new StringBuilder();
            String line;
            boolean inImport = false;

            while ((line = reader.readLine()) != null) {
                // Handle import statements specially
                if (line.contains("<xsl:import") || line.contains("<xsl:include")) {
                    inImport = true;
                    content.append(cleanupImportLine(line));
                } else if (inImport && line.contains("/>")) {
                    inImport = false;
                    content.append(cleanupImportLine(line));
                } else {
                    content.append(line).append("\n");
                }
            }

            return content.toString();
        }
    }

    private String cleanupImportLine(String line) {
        // Extract the href value
        int hrefStart = line.indexOf("href=\"") + 6;
        int hrefEnd = line.indexOf("\"", hrefStart);

        if (hrefStart > 5 && hrefEnd > hrefStart) {
            String before = line.substring(0, hrefStart);
            String href = line.substring(hrefStart, hrefEnd).trim();
            String after = line.substring(hrefEnd);

            // Clean up the href value
            href = CLEANUP_PATTERN.matcher(href).replaceAll("");

            return before + href + after + "\n";
        }

        return line + "\n";
    }

    private Source createTemplateSource(String content, Resource resource) throws IOException {
        byte[] bytes = content.getBytes(StandardCharsets.UTF_8);
        return new StreamSource(
                new ByteArrayInputStream(bytes),
                resource.getURI().toString()
        );
    }

    private String determineTemplateBasePath(Resource resource, String templatePath) throws IOException {
        String resourcePath = resource.getURI().getPath();
        int templatesIndex = resourcePath.indexOf("/templates/");

        if (templatesIndex >= 0) {
            if (templatePath.contains("/")) {
                // For folder-based templates, use the template's directory
                return resourcePath.substring(0, templatesIndex) + "/templates/" +
                       templatePath.substring(0, templatePath.indexOf("/"));
            }
            // For single-file templates, use the templates directory
            return resourcePath.substring(0, templatesIndex) + "/templates";
        }

        return xsltBasePath;
    }

    private Resource findTemplateResource(String path) throws IOException {
        List<String> possiblePaths = new ArrayList<>();
        String cleanPath = StringUtils.cleanPath(path);

        // For single file templates
        possiblePaths.add(String.format("classpath:%s/%s", xsltBasePath, cleanPath));
        if (!cleanPath.endsWith(".xsl")) {
            possiblePaths.add(String.format("classpath:%s/%s.xsl", xsltBasePath, cleanPath));
        }

        // For folder-based templates
        if (!cleanPath.endsWith(".xsl")) {
            possiblePaths.add(String.format("classpath:%s/%s/index.xsl", xsltBasePath, cleanPath));
        }

        // Try each path
        for (String possiblePath : possiblePaths) {
            try {
                Resource resource = resourceResolver.getResource(possiblePath);
                if (resource.exists() && resource.isReadable()) {
                    log.debug("Found template at: {}", resource.getURI());
                    return resource;
                }
            } catch (IOException e) {
                log.debug("Failed to check path: {}", possiblePath, e);
            }
        }

        return null;
    }
}