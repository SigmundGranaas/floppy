package com.sigmundgranaas.core.service.xslt.api;

import javax.xml.transform.Templates;

public interface TemplateProvider {
     Templates getTemplate(String templateName);
}
