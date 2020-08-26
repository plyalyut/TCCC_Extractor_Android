package com.bio1systems.tccc_Extractor.xml;

import javax.xml.bind.Element;

/**
 * Interface for creating XML Files.
 */
public interface XMLAble {
    // TODO: we want to be able to convert a class into an XML friendly object

    /**
     * Creates the XML node based on the data in the class.
     * @return Created Element
     */
    public Element createXML();
}
