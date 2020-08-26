package com.bio1systems.tccc_Extractor.entity;

/**
 * Used for storing the medicine information on a specific treatment.
 */
public class Medicine {
  private String med_name;
  private String med_quantity;
  private String med_route;

  /**
   * Stores the medicine information like name of medicine, quantity, route
   * administered.
   * @param name: String containing the name of the medication
   * @param quantity: String containing the quantity administered
   * @param route: String containing the route of the medicine administered
   */
  public Medicine(String name, String quantity, String route){
      med_name = name;
      med_quantity = quantity;
      med_route = route;
  }

  /**
   * Sets the name of the medication
   * @param name: String, name of the medication
   */
  public void setName(String name){
    med_name = name;
  }

  /**
   * Sets the quantity of the medication
   * @param quantity: String amount administered
   */
  public void setQuantity(String quantity){
    med_quantity = quantity;
  }

  /**
   * Sets the route of the medication
   * @param route: String, route administered
   */
  public void setRoute(String route){
    med_route = route;
  }


}
