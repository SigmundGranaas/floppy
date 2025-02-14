package com.sigmundgranaas.core.service.pdf.api;

import javax.xml.transform.Source;
import javax.xml.transform.Templates;

public interface PdfRenderer {
    byte[] render(Templates template, Source xmlSource) throws PdfRenderingException;
}
