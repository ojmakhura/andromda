package org.andromda.translation.query;

/**
 * Contains the patterns matching for the OCL language.
 * 
 * @author Chad Brandon
 */
public class OCLPatterns
{
    /**
     * Matches on the <code>allInstances</code> feature.
     */
    public static final String ALL_INSTANCES = "allInstances\\s*\\(\\s*\\)";
    
    /**
     * Matches on a feature call
     */
    public static final String ARROW_FEATURE_CALL = ".*\\s*->\\s*\\w*\\(.*\\)";
    
    public static void main(String args[])
    {
        System.out.println("matches: " + "certificate.validityStart -> select()".matches(ARROW_FEATURE_CALL));
    }
}