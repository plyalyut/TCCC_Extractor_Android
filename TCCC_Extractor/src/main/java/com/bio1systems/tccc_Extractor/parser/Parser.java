package com.bio1systems.tccc_Extractor.parser;


import com.bio1systems.tccc_Extractor.extractor.Preprocessing;
import com.bio1systems.tccc_Extractor.extractor.SymptomsExtractor;
import com.bio1systems.tccc_Extractor.xml.XMLMaker;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Parser {
    Document final_document;
    XMLMaker xmlMaker;
    Element rootNode;
    SymptomsExtractor extractor;
    public Parser(){
        this.xmlMaker = new XMLMaker();
        rootNode = this.xmlMaker.createRoot("ABC");
        this.final_document = xmlMaker.returnDocument();
        this.extractor = new SymptomsExtractor(this.xmlMaker);
    }

    // We want to extract from a wide variety of extractors
    // Every extractor should add to the documents

    /**
     * Runs the main extractor on a wide variety of extractors.
     * @param text String to be extracted
     */
    public void run_extractor(String text){
        text = Preprocessing.clean(text);
        this.extractor.extract(text);
        this.xmlMaker.convertToXML();
    }

    public static void main(String[] args){
        Parser parser = new Parser();
        String transcript = "Scene is safe. respiratory moi. airway is open. respiration rate is tachypneic. symmetrical rise and fall of the chest.  No bleeding.  pulse is tachycardic and strong.  skin is flushed, warm, and moist.  Evac priority is high. ";
        parser.run_extractor(transcript);
    }
}
