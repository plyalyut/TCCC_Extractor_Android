package com.bio1systems.tccc_Extractor.entity;

import java.util.List;

/**
 * Extractor for Symptoms and Signs
 */
public class SymptomSign {
  public String symptom_sign;
  public List attributes;

  public SymptomSign(String name, List val){
    symptom_sign = name;
    attributes = val;
  }

}
