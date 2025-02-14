package com.sigmundgranaas.core.service.xslt.implementation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.StringUtils;

import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class XsltUriResolver implements URIResolver {
    private final ResourcePatternResolver resourceResolver;
    private final String basePath;

    public XsltUriResolver(ResourcePatternResolver resourceResolver, String basePath) {
        this.resourceResolver = resourceResolver;
        this.basePath = "templates/";
    }

    @Override
    public Source resolve(String href, String base) throws TransformerException {
        try {
            href = href.replaceAll("[\n\r]", "").trim();
            log.debug("Resolving - href: {}, base: {}", href, base);

            List<String> pathsToTry = generatePossiblePaths(href, base);

            for (String path : pathsToTry) {
                try {
                    Resource resource = resourceResolver.getResource(path);
                    if (resource.exists()) {
                        log.debug("Found resource at: {}", path);
                        return new StreamSource(resource.getInputStream(), resource.getURI().toString());
                    }
                } catch (IOException e) {
                    log.debug("Failed to check path: {}", path, e);
                }
            }

            log.error("Failed to resolve resource. Attempted paths: {}", String.join(", ", pathsToTry));
            throw new TransformerException("Could not find resource in any of these locations: " + pathsToTry);

        } catch (Exception e) {
            log.error("Failed to resolve XSLT resource: {} (base: {})", href, base, e);
            throw new TransformerException("Failed to resolve XSLT resource: " + href, e);
        }
    }

    private List<String> generatePossiblePaths(String href, String base) {
        List<String> pathsToTry = new ArrayList<>();

        if (base != null) {
            // Extract the template name from the base path
            String templateName = extractTemplateName(base);

            if (templateName != null) {
                if (href.startsWith("../")) {
                    // For relative paths starting with ../
                    String relativeHref = href.substring(3); // Remove "../"
                    // Add path at template root level
                    pathsToTry.add(String.format("classpath:%s%s/%s",
                            basePath, templateName, relativeHref));
                } else {
                    // For paths without ../
                    String currentDir = extractCurrentDirectory(base, templateName);
                    if (currentDir != null && !currentDir.isEmpty()) {
                        // Try relative to current directory first
                        pathsToTry.add(String.format("classpath:%s%s/%s/%s",
                                basePath, templateName, currentDir, href));
                    }
                    // Then try at template root
                    pathsToTry.add(String.format("classpath:%s%s/%s",
                            basePath, templateName, href));
                }
            }
        }

        // Always try in shared templates directory as fallback
        pathsToTry.add(String.format("classpath:%s%s", basePath, href));

        return pathsToTry.stream()
                .map(StringUtils::cleanPath)
                .map(path -> path.replace("//", "/"))
                .distinct()
                .toList();
    }

    private String extractTemplateName(String base) {
        int templatesIndex = base.indexOf("/templates/");
        if (templatesIndex >= 0) {
            String afterTemplates = base.substring(templatesIndex + "/templates/".length());
            int firstSlash = afterTemplates.indexOf('/');
            if (firstSlash > 0) {
                return afterTemplates.substring(0, firstSlash);
            }
        }
        return null;
    }

    private String extractCurrentDirectory(String base, String templateName) {
        int templatesIndex = base.indexOf("/templates/");
        if (templatesIndex >= 0) {
            String afterTemplates = base.substring(templatesIndex + "/templates/".length());
            int lastSlash = afterTemplates.lastIndexOf('/');
            if (lastSlash > templateName.length()) {
                // Get everything between template name and last slash
                return afterTemplates.substring(templateName.length() + 1, lastSlash);
            }
        }
        return null;
    }
}