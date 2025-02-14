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
            // Handle relative paths from imported/included stylesheets
            String fullPath;
            if (base != null && base.startsWith("classpath:")) {
                Path basePath = Paths.get(new URI(base).getPath()).getParent();
                fullPath = "classpath:" + basePath.resolve(href).normalize();
            } else {
                fullPath = "classpath:" + this.basePath + "/" + href;
            }

            Resource resource = resourceResolver.getResource(fullPath);
            return new StreamSource(resource.getInputStream(), resource.getURI().toString());
        } catch (Exception e) {
            throw new TransformerException("Failed to resolve XSLT resource: " + href, e);
        }
    }
}
