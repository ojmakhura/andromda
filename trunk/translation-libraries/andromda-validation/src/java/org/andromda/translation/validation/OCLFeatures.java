package org.andromda.translation.validation;

/**
 * Contains the patterns matching reserved features of the OCL language.
 * 
 * @author Chad Brandon
 */
public class OCLFeatures
{
    /**
     * Matches on the <code>allInstances</code> feature.
     */
    public static final String ALL_INSTANCES = "(\\s*\\w*\\s*(\\w+|::)*)?\\s*allInstances\\s*\\(\\s*\\)";

    /**
     * Matches on the <code>oclIsKindOf</code> feature.
     */
    public static final String OCL_IS_KIND_OF = "oclIsKindOf\\s*\\(\\s*"
        + OCLPatterns.SCOPE_PATH + "\\s*\\)\\s*";

    /**
     * Matches on the <code>oclIsTypeOf</code> feature.
     */
    public static final String OCL_IS_TYPE_OF = "oclIsTypeOf\\s*\\(\\s*"
        + OCLPatterns.SCOPE_PATH + "\\s*\\)\\s*";

    /**
     * Matches on the <code>concat</code> feature.
     */
    public static final String CONCAT = "concat\\s*\\(\\s*"
        + OCLPatterns.NAVIGATIONAL_PATH + "\\s*\\)\\s*";
}