package com.bio1systems.tccc_Extractor.extractor;

/**
 * Generalized superclass for building an extractor.
 * Every extractor has an extract method that pulls
 * out the required information and returns it in list
 * form.
 */
public interface Extractor {

  /**
   * @param source text document
   */
  void extract(String source);
}
