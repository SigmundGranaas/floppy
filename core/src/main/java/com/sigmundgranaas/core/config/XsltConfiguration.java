package com.sigmundgranaas.core.config;

import com.sigmundgranaas.core.service.xslt.XsltUriResolver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import javax.xml.transform.TransformerFactory;

@Configuration
public class XsltConfiguration {

    @Value("${xslt.base-path:/xslt}")
    private String xsltBasePath;

    @Bean
    public ResourcePatternResolver resourcePatternResolver() {
        return new PathMatchingResourcePatternResolver();
    }

    @Bean
    public TransformerFactory transformerFactory() {
        TransformerFactory factory = TransformerFactory.newInstance();
        factory.setURIResolver(xsltUriResolver());
        return factory;
    }

    @Bean
    public XsltUriResolver xsltUriResolver() {
        return new XsltUriResolver(resourcePatternResolver(), xsltBasePath);
    }
}
