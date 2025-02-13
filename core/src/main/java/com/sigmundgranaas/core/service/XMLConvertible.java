package com.sigmundgranaas.core.service;

import javax.xml.transform.Source;

@FunctionalInterface
public interface XMLConvertible {
    Source asXMLSource();
}
