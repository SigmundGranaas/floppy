package com.sigmundgranaas.core.service;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.sigmundgranaas.core.error.PdfGenerationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.time.Duration;
import java.time.Instant;

@Service
@Slf4j
public class XMLConverter {

    private final XmlMapper mapper;

    public XMLConverter(XmlMapper mapper) {
        this.mapper = mapper;
    }

    public Source convert(Object dto){
        // Convert DTO to XML
        Instant xmlStartTime = Instant.now();
        String xmlData = convertToXml(dto);
        log.info("Generated XML (took {} ms): {}",
                Duration.between(xmlStartTime, Instant.now()).toMillis(),
                xmlData);

        return new StreamSource(new StringReader(xmlData));
    }

    private String convertToXml(Object data) {
        Instant startTime = Instant.now();
        try {
            // Configure the mapper for better output
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            mapper.setDefaultUseWrapper(false);

            String xml = mapper.writeValueAsString(data);
            log.info("Converted DTO to XML (took {} ms): {}",
                    Duration.between(startTime, Instant.now()).toMillis(),
                    xml);
            return xml;
        } catch (Exception e) {
            log.error("Failed to convert data to XML after {} ms. Error: {}",
                    Duration.between(startTime, Instant.now()).toMillis(),
                    e.getMessage(), e);
            throw new PdfGenerationException("Failed to convert data to XML", e);
        }
    }
}
