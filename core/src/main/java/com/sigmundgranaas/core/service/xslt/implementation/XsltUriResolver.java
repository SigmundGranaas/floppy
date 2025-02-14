package com.sigmundgranaas.core.service.xslt.implementation;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;

import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.stream.StreamSource;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;

public class XsltUriResolver implements URIResolver {
    private final ResourcePatternResolver resourceResolver;
    private final String basePath;

    public XsltUriResolver(ResourcePatternResolver resourceResolver, String basePath) {
        this.resourceResolver = resourceResolver;
        this.basePath = basePath;
    }

    @Override
    public Source resolve(String href, String base) throws TransformerException {
        try {
            // Remove any existing .xsl extension
            String cleanHref = href.replaceAll("\\.xsl$", "");

            // Handle relative paths from imported/included stylesheets
            String fullPath;
            if (base != null && base.startsWith("classpath:")) {
                Path basePath = Paths.get(new URI(base).getPath()).getParent();
                fullPath = "classpath:" + basePath.resolve(cleanHref).normalize();
            } else {
                fullPath = "classpath:" + this.basePath + "/" + cleanHref;
            }

            // Try multiple possible paths
            Resource resource = tryResolveResource(fullPath);
            if (resource != null && resource.exists()) {
                return new StreamSource(resource.getInputStream(), resource.getURI().toString());
            }

            throw new TransformerException("Could not resolve XSLT resource: " + href);
        } catch (Exception e) {
            throw new TransformerException("Failed to resolve XSLT resource: " + href, e);
        }
    }

    private Resource tryResolveResource(String basePath) {
        // Try different variations of the path
        String[] variations = {
                basePath + ".xsl",          // with .xsl extension
                basePath + "/index.xsl",    // index file in directory
                basePath                    // exact path
        };

        for (String path : variations) {
            Resource resource = resourceResolver.getResource(path);
            if (resource.exists()) {
                return resource;
            }
        }

        return null;
    }
}