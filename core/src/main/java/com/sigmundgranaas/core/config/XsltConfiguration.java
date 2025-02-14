package com.sigmundgranaas.core.config;

import com.sigmundgranaas.core.service.xslt.implementation.XsltUriResolver;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import javax.xml.XMLConstants;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.URIResolver;
import java.io.IOException;

@Configuration
@Slf4j
public class XsltConfiguration {

    @Value("${xslt.base-path:templates}")
    private String xsltBasePath;

    private final ResourcePatternResolver resourcePatternResolver;

    public XsltConfiguration(ResourcePatternResolver resourcePatternResolver) {
        this.resourcePatternResolver = resourcePatternResolver;
    }

    @Bean
    public TransformerFactory transformerFactory(URIResolver uriResolver) {
        TransformerFactory factory = TransformerFactory.newInstance();
        factory.setURIResolver(uriResolver);
        return factory;
    }

    @Bean
    public URIResolver xsltUriResolver() {
        // Ensure base path doesn't start with slash
        String cleanBasePath = xsltBasePath.replaceFirst("^/", "");
        return new XsltUriResolver(resourcePatternResolver, cleanBasePath);
    }
}