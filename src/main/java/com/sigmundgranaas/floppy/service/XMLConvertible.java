package com.sigmundgranaas.floppy.service;

import javax.xml.transform.Source;

@FunctionalInterface
public interface XMLConvertible {
    Source asXMLSource();
}
