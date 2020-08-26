package com.bio1systems.tccc_Extractor.extractor;

import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

import java.util.List;

import com.bio1systems.tccc_Extractor.xml.XMLMaker;
import org.w3c.dom.Element;

public class SymptomsExtractor implements Extractor{
  private StanfordCoreNLP nlp;
  private XMLMaker xml_maker;
  private Element parent_node; // Used for appending signs and symptoms onto.

  private final String BP_TOKEN = "BLOOD_PRESSURE";
  private final String OXYGEN_LEVEL = "OXYGEN_LEVEL";

  /**
   * Sets up the maker_object
   * @param maker_object
   */
  public SymptomsExtractor(XMLMaker maker_object){
    nlp = Searcher.createNERPipeline();
    setUpXML(maker_object);
    //nlp_extractor
  }

  /**
   * Sets up the XML object for use in populating the XML
   * fields.
   * @param maker_object Initialized XML object.
   */
  private void setUpXML(XMLMaker maker_object){
    // Name of the parent node for all the signs and symptoms
    String signs_and_symptoms_name = "SIGNS_AND_SYMPTOMS_VITALS_LIST";
    this.xml_maker = maker_object;
    //xml_maker = maker_object;
    Element root_node = this.xml_maker.getRootNode();
    parent_node = this.xml_maker.addNode(root_node, signs_and_symptoms_name);
  }

  @Override
  public void extract(String text) {
    CoreDocument coredoc = new CoreDocument(text);
    getAirwayCondition(coredoc);
    getBleedingCondition(coredoc);
    getBreathingCondition(coredoc);
    getPulseCondition(coredoc);
    getSkinCondition(coredoc);
  }

  /**
   * Extracts out all the entities related to Airway Condition
   * @param document extracted coreNLP document
   * @return List<SymptomSign> corresponding to airway.
   */
  private void getAirwayCondition(CoreDocument document){
    String AIRWAY_XML = "AIRWAY";
    Element airway_parent_element = xml_maker.addNode(parent_node, AIRWAY_XML);

    // Reload CoreNLP with the correct path file
    // This is a janky solution but it works for now.
    String AIRWAY_MAPPING = "file:///android_asset/signsymptom/airway-ner.txt";
    nlp = Searcher.updateNERwithMapping(nlp, AIRWAY_MAPPING);
    nlp.annotate(document);

    // Finds the corresponding NER tagged sentences
    List<String> airway_values = Searcher.extractObject(document,"AIRWAY", "AIRWAY_VALUE");

    // TODO: we want this to generate a list of airway_values and then convert to node
    xml_maker.addTextNodes(airway_parent_element, "AIRWAY_VALUE", airway_values);
  }

  /**
   * Extracts out all the entities related to bleeding.
   * @param document document corresponding to the text
   * @return
   */
  private void getBleedingCondition(CoreDocument document){
    String BLEEDING_XML = "BLEEDING";
    Element bleeding_parent_element = xml_maker.addNode(parent_node, BLEEDING_XML);

    // Reload CoreNLP with the correct filepath
    String BLEEDING_MAPPING = "file:///android_asset/signsymptom/bleeding-ner.txt";
    nlp = Searcher.updateNERwithMapping(nlp, BLEEDING_MAPPING);
    nlp.annotate(document);

    // Finds the corresponding NER tagged sentences
    List<String> bleeding_values = Searcher.extractObject(document,"BLEEDING", "BLEEDING_VALUE");

    //TODO convert to XML schema
    xml_maker.addTextNodes(bleeding_parent_element, "BLEEDING_VALUE", bleeding_values);

  }

  private void getBreathingCondition(CoreDocument document){
    String BREATHING_XML = "BREATHING";
    Element breathing_parent_element = xml_maker.addNode(parent_node, BREATHING_XML);

    // Reload CoreNLP with the correct filepath
    String BREATHING_MAPPING = "file:///android_asset/signsymptom/breathing-ner.txt";
    nlp = Searcher.updateNERwithMapping(nlp, BREATHING_MAPPING);
    nlp.annotate(document);

    // Finds the corresponding NER tagged sentences
    List<String> breathing_values = Searcher.extractObject(document,"BREATHING", "BREATHING_VALUE");

    //TODO convert to XML schema
    xml_maker.addTextNodes(breathing_parent_element, "BREATHING_VALUE", breathing_values);
  }

  private void getPulseCondition(CoreDocument document){
    String PULSE_XML = "PULSE";
    Element pulse_element = xml_maker.addNode(parent_node, PULSE_XML);

    String PULSE_MAPPING = "file:///android_asset/signsymptom/pulse-ner.txt";
    nlp = Searcher.updateNERwithMapping(nlp, PULSE_MAPPING);
    nlp.annotate(document);

    // Finds the corresponding NER tagged sentences
    List<String> pulse_values = Searcher.extractObject(document,"PULSE", "PULSE_VALUE");

    // Creates a custom object compatible with XML extraction

    xml_maker.addTextNodes(pulse_element, "PULSE_VALUE", pulse_values);
  }

  private void getSkinCondition(CoreDocument document){
    String SKIN_XML = "SKIN";
    Element skin_element = xml_maker.addNode(parent_node, SKIN_XML);
    String SKIN_MAPPING = "file:///android_asset/signsymptom/skin-ner.txt";
    nlp = Searcher.updateNERwithMapping(nlp, SKIN_MAPPING);
    nlp.annotate(document);

    // Finds all the corresponding NER tagged sentences
    Element skin_temp_element = xml_maker.addNode(skin_element, "SKIN_TEMP");
    List<String> skin_temp = Searcher.extractObject(document, "SKIN", "SKIN_TEMP");
    xml_maker.addTextNodes(skin_temp_element, "SKIN_TEMP", skin_temp);

    Element skin_cond_element = xml_maker.addNode(skin_element, "SKIN_COND");
    List<String> skin_cond = Searcher.extractObject(document, "SKIN", "SKIN_COND");
    xml_maker.addTextNodes(skin_cond_element, "SKIN_CONDITION", skin_cond);

    Element skin_color_element = xml_maker.addNode(skin_element, "SKIN_COLOR");
    List<String> skin_color = Searcher.extractObject(document, "SKIN", "SKIN_COLOR");
    xml_maker.addTextNodes(skin_color_element, "SKIN_COLOR", skin_color);
  }

  /**
   * Extracts out the Blood pressure from a document
   * @param document: Annotated document with tokenization.
   * @return List<SymptomSign> comprising of list of blood pressures.
   */
  /*
  private List<SymptomSign> getBloodPressure(CoreDocument document){
    List<SymptomSign> bloodpressures = new ArrayList<>();

    // Sets up the rules for the pattern matcher.
    List<TokenSequencePattern> bpPatterns = Arrays.asList(
            TokenSequencePattern.compile("[{word>=40}] /over/ [{word>=40}]"),
            TokenSequencePattern.compile("[{word: /\\d{2,}[\\/]\\d{2,}/}]"));
    MultiPatternMatcher tokenmatcher =
            TokenSequencePattern.getMultiPatternMatcher(bpPatterns);
    List<SequenceMatchResult<CoreMap>> result = tokenmatcher.findNonOverlapping(document.tokens());

    // Adds it to the Symptom Sign result group
    for(int i =0; i<result.size(); i++){
      bloodpressures.add(new SymptomSign(BP_TOKEN,result.get(i).group()));
    }

    return bloodpressures;
  }

   */

  /**
   * Extracts the oxygen level from a document.
   * @param document: Annotated document with tokenization.
   * @return List<SymptomSign> comprised of oxygen levels.
   */
  /*
  private List<SymptomSign> getOxygen(CoreDocument document) {
    List<SymptomSign> oxygenLevels = new ArrayList<>();

    // Matcher for the oxygen levels.
    List<TokenSequencePattern> oxygenPatterns = Arrays.asList(
            TokenSequencePattern.compile("[{word::IS_NUM}] [{word: \"%\"}]"));
    MultiPatternMatcher tokenmatcher =
            TokenSequencePattern.getMultiPatternMatcher(oxygenPatterns);
    List<SequenceMatchResult<CoreMap>> result = tokenmatcher.findNonOverlapping(document.tokens());

    // Arranges it in the format we want name, followed by item.
    for(SequenceMatchResult<CoreMap> item: result){
      oxygenLevels.add(new SymptomSign(OXYGEN_LEVEL, item.group()));
    }


    return oxygenLevels;
  }
   */

  /*
  public static void main(String[] args){
    Properties props = new Properties();
    props.setProperty("annotators", "tokenize");
    StanfordCoreNLP nlp = new StanfordCoreNLP(props);
    SymptomsExtractor extractor = new SymptomsExtractor(nlp);
    extractor.extract("Oxygen was at 100%");
  }

   */


}
