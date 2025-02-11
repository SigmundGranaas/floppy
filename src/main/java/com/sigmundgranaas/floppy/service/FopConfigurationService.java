package com.sigmundgranaas.floppy.service;

import com.sigmundgranaas.floppy.error.PdfGenerationException;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.FopFactoryBuilder;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class FopConfigurationService {
    private final FopFactory fopFactory;

    public FopConfigurationService() {
        try {
            FopFactoryBuilder builder = new FopFactoryBuilder(new File(".").toURI())
                    .setStrictFOValidation(true)
                    .setAccessibility(true);

            this.fopFactory = builder.build();
        } catch (Exception e) {
            throw new PdfGenerationException("Failed to initialize FOP factory", e);
        }
    }

    public FopFactory getFopFactory() {
        return fopFactory;
    }
}
