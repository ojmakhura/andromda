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
     * Matches on the pattern of a scope path (i.e. java :: lang :: Integer).
     */
    private static final String SCOPE_PATH = "(\\w+|\\s*::\\s*)*";

    /**
     * Matches on the <code>oclIsKindOf</code> feature.
     */
    public static final String OCL_IS_KIND_OF = "oclIsKindOf\\s*\\(\\s*"
        + SCOPE_PATH + "\\s*\\)\\s*";

    /**
     * Matches on the <code>oclIsTypeOf</code> feature.
     */
    public static final String OCL_IS_TYPE_OF = "oclIsTypeOf\\s*\\(\\s*"
        + SCOPE_PATH + "\\s*\\)\\s*";

    /**
     * Matches on the pattern of a navigational path (i.e. person.name.first)
     */
    private static final String NAVIGATIONAL_PATH = "(\\w+|\\s*\\.\\s*)*";

    /**
     * Matches on the <code>concat</code> feature.
     */
    public static final String CONCAT = "concat\\s*\\(\\s*" + NAVIGATIONAL_PATH
        + "\\s*\\)\\s*";
}