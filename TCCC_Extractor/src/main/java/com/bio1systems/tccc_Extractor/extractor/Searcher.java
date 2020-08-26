package com.bio1systems.tccc_Extractor.extractor;

import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 * Helper methods for searching through CoreNLP objects.
 */
public class Searcher {

  /**
   * Searches for the desired NER tag mentioned in the document.
   * @param doc CoreDocument processed by CoreNLP
   * @param tag tag to search for
   * @return List<CoreSentence> with the corresponding condition.
   */
  public static List<CoreSentence> searchNERTag(CoreDocument doc, String tag){
    List<CoreSentence> found_ner = new ArrayList<>();
    for(CoreSentence s: doc.sentences()){
      for(String ner: s.nerTags()){
        if(ner.equals(tag)){
          found_ner.add(s);
        }
      }
    }
    return found_ner;
  }

  /**
   * Searches for NER Relations with sentences having both the keys and the
   * values.
   * @param doc Annotated document with the NER tag
   * @param key Key corresponding to XML Schema
   * @param value Value corresponding to XML Schema
   * @return List<CoreSentence> Sentences with both the key and the value.
   */
  public static List<CoreSentence> searchNERRelations(CoreDocument doc, String key, String value){
    Set<CoreSentence> keySents = new HashSet<>(searchNERTag(doc, key));
    Set<CoreSentence> valueSents = new HashSet<>(searchNERTag(doc, value));

    keySents.retainAll(valueSents);
    return new ArrayList<>(keySents);
  }


  /**
   * Extracts all the values with a specific key.
   * @param doc Annotated document
   * @param key NER key for values to be found
   * @param value NER values to look for.
   * @return List<String> corresponding to all the entries for a given key.
   */
  public static List<String> extractObject(CoreDocument doc, String key, String value){
    // Assumption: Adjacent tokens with the same NER tag are in fact part of the
    // same entity.

    List<String> values_extracted = new ArrayList<>();
    List<CoreSentence> sents = searchNERRelations(doc, key, value);

    // Linking algorithm
    for(CoreSentence s: sents){
      int start = 0;
      int end = 0;
      for(int i = 0; i<s.tokens().size(); i++){
        if(s.nerTags().get(i).equals(value)){
          end++;
        } else if (start != end) {
          values_extracted.add(String.join(" ",s.tokensAsStrings().subList(start, end)));
          start = end+1;
          end = start;
        } else {
          start = end + 1;
          end = start;
        }
      }
      if(start!=end){
        values_extracted.add(String.join(" ",s.tokensAsStrings().subList(start, end)));
      }
    }

    return values_extracted;
  }

  /**
   * Creates the pipeline for use in searching RegexNER using CoreNLP
   * @return Created nlp pipeline
   */
  public static StanfordCoreNLP createNERPipeline(){
    Properties prop = new Properties();
    prop.put("annotators", "tokenize, ssplit, pos,lemma, ner, regexner");
    StanfordCoreNLP nlp = new StanfordCoreNLP(prop);
    return nlp;
  }

  /**
   * Updates the regexner mapping with the correct mapping.
   * @param nlp previously created pipeline
   * @param filepath new filepath of the mappings
   * @return new nlp pipeline
   */
  public static StanfordCoreNLP updateNERwithMapping(StanfordCoreNLP nlp, String filepath){
    Properties props = nlp.getProperties();
    props.setProperty("regexner.mapping", filepath);
    nlp = new StanfordCoreNLP(props);
    return nlp;
  }

  


}
