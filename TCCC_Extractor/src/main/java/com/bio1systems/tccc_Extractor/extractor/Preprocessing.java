package com.bio1systems.tccc_Extractor.extractor;

/**
 * Runs data preprocessing on the strings
 */
public class Preprocessing {

    /**
     * Cleans the string so that it is easier to find the correct words.
     * @param text uncleaned text
     * @return cleaned String
     */
    public static String clean(String text) {
        return text.toLowerCase();
    }
}
