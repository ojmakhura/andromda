package org.andromda.translation.validation;

/**
 * Contains the patterns matching constructs within the OCL
 * language.
 * 
 * @author Chad Brandon
 */
public class OCLPatterns
{
    /**
     * Matches on the pattern of a scope path (i.e. java :: lang :: Integer).
     */
    public static final String SCOPE_PATH = "(\\w+|\\s*::\\s*)*";
    
    /**
     * Matches on the pattern of a navigational path (i.e. person.name.first)
     */
    public static final String NAVIGATIONAL_PATH = "(\\w+|\\s*\\.\\s*)*";
}
