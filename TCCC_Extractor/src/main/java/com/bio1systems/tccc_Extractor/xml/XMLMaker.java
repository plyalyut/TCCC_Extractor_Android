package com.bio1systems.tccc_Extractor.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.List;

/**
 * Creates XML based on XML specifications.
 */
public class XMLMaker {

    public Document doc;
    public Element root;

    public XMLMaker(){
        this.doc = createBlankDocument();
    }

    /**
     * Creates a blank document and returns it.
     * @return Blank document corresponding to the created document
     */
    public Document createBlankDocument(){
        try {
            // creates a new document
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = docFactory.newDocumentBuilder();
            return documentBuilder.newDocument();
        }
        catch(ParserConfigurationException e){
            System.out.println("Error in creating document, please report this bug");
            return null;
        }
    }

    /**
     * Creates a root and appends it to the document.
     * @param rootname root tag to be added to the document
     * @return root element
     */
    public Element createRoot(String rootname){
        Element root = this.doc.createElement(rootname);
        this.doc.appendChild(root);
        this.root = root;
        return root;
    }

    /**
     * Gets the root node and returns it.
     * @return Root Node
     */
    public Element getRootNode(){
        return this.root;
    }

    /**
     * Adds a node to the a parent node
     * @param parent parent node to the child to be added.
     * @param childname name of the child node.
     */
    public Element addNode(Element parent, String childname){
        Element child = this.doc.createElement(childname);
        parent.appendChild(child);
        return child;
    }

    /**
     * Sets the attribute of the node with a Mapping.
     * @param node Element in the document
     * @param attributes Map<String,String> corresponding to the value and the attributes
     * @return Element with the attributes already added.
     */
    /*
    public Element addAttributes(Element node, Map<String, String> attributes){
        attributes.forEach(node::setAttribute);
        return node;
    }
     */

    /**
     * Adds text nodes to a parent node with a specific property and value list.
     * @param parent_node Parent node to which to add the text fields
     * @param property Property name of the text nodes
     * @param values values for the text nodes
     * @return the parent node
     */
    public Element addTextNodes(Element parent_node, String property, List<String> values){
        for(String val: values){
            Element temp_node = this.addNode(parent_node, property);
            this.addTextField(temp_node,val);
        }
        return parent_node;
    }

    /**
     * Add a text field to a node.
     * @param node Node in the document.
     * @param text Text to add to the element node
     * @return Element node with the changes made to the document.
     */
    public Element addTextField(Element node, String text){
        Text textnode = this.doc.createTextNode(text);
        node.appendChild(textnode);
        return node;
    }

    /**
     * Converts the document to an XML file.
     */
    public void convertToXML(){
        TransformerFactory transformFactory = TransformerFactory.newInstance();
        try{
            Transformer transformer = transformFactory.newTransformer();
            DOMSource domsrc = new DOMSource(this.doc);
            //StreamResult consoleResult = new StreamResult(System.out);
            StreamResult result = new StreamResult(new File("output.xml"));
            transformer.transform(domsrc, result);
        } catch(TransformerException e){
            System.out.println("Error, could not create XML file.");
        }
    }


    /**
     * Returns the completed document
     * @return Document
     */
    public Document returnDocument(){
        return this.doc;
    }
}
