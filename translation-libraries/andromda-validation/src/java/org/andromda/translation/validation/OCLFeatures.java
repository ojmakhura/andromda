package org.andromda.translation.validation;

/**
 * Contains the patterns matching reserved features of the OCL language.
 * 
 * @author Chad Brandon
 */
public class OCLFeatures
{
    /**
     * Matches on the <code>allInstances()</code> feature.
     */
    public static final String ALL_INSTANCES = "(\\s*\\w*\\s*(\\w+|::)*)?\\s*allInstances\\s*\\(\\s*\\)";
    
    /**
     * Matches on the pattern of a path (i.e. java :: lang :: Integer).
     */
    private static final String PATH_PATTERN = "(\\w+|\\s*::\\s*)*";
    
    /**
     * Matches on the <code>oclIsKindOf</code> feature.
     */
    public static final String OCL_IS_KIND_OF = "oclIsKindOf\\s*\\(\\s*" + PATH_PATTERN + "\\s*\\)\\s*";
    
    /**
     * Matches on the <code>.oclIsTypeOf</code> feature.
     */
    public static final String OCL_IS_TYPE_OF = "oclIsTypeOf\\s*\\(\\s*" + PATH_PATTERN + "\\s*\\)\\s*";
    
    public static void main(String args[])
    {
        System.out.println("OCL_IS_KIND_OF: " + OCL_IS_KIND_OF);
        System.out.println("oclIsKindOf ( java :: lang :: Integer )".matches(OCL_IS_KIND_OF));
    }
}
