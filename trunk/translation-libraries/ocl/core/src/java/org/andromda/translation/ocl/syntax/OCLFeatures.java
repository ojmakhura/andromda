package org.andromda.translation.ocl.syntax;

import org.andromda.core.translation.TranslationUtils;

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
    private static final String ALL_INSTANCES = "(\\s*\\w*\\s*(\\w+|::)*)?\\s*allInstances\\s*\\(\\s*\\)";

    /**
     * Matches on the <code>oclIsKindOf</code> feature.
     */
    private static final String OCL_IS_KIND_OF = "oclIsKindOf\\s*\\(\\s*" + OCLPatterns.SCOPE_PATH
        + "\\s*\\)\\s*";

    /**
     * Matches on the <code>oclIsTypeOf</code> feature.
     */
    private static final String OCL_IS_TYPE_OF = "oclIsTypeOf\\s*\\(\\s*" + OCLPatterns.SCOPE_PATH
        + "\\s*\\)\\s*";

    /**
     * Matches on the <code>concat</code> feature.
     */
    private static final String CONCAT = "concat\\s*\\(\\s*" + OCLPatterns.NAVIGATIONAL_PATH
        + "\\s*\\)\\s*";

    /**
     * Matches on any of the features.
     */
    private static final String ALL_PATTERNS = ALL_INSTANCES + "|" + OCL_IS_KIND_OF + "|"
        + OCL_IS_TYPE_OF + "|" + CONCAT;

    /**
     * Indicates if the expression is an <em>allInstances</em>. OCL feature.
     * 
     * @param expression the expression to evaluate.
     * @return true/false
     */
    public static boolean isAllInstances(Object expression)
    {
        return TranslationUtils.deleteWhitespace(expression).matches(OCLFeatures.ALL_INSTANCES);
    }

    /**
     * Indicates if the expression is an <em>concat</em>. OCL feature.
     * 
     * @param expression the expression to evaluate.
     * @return true/false
     */
    public static boolean isConcat(Object expression)
    {
        return TranslationUtils.deleteWhitespace(expression).matches(OCLFeatures.CONCAT);
    }

    /**
     * Indicates if the expression is an <em>oclIsTypeOf</em>. OCL feature.
     * 
     * @param expression the expression to evaluate.
     * @return true/false
     */
    public static boolean isOclIsTypeOf(Object expression)
    {
        return TranslationUtils.deleteWhitespace(expression).matches(OCLFeatures.OCL_IS_TYPE_OF);
    }

    /**
     * Indicates if the expression is an <em>oclIsKindOf</em>. OCL feature.
     * 
     * @param expression the expression to evaluate.
     * @return true/false
     */
    public static boolean isOclIsKindOf(Object expression)
    {
        return TranslationUtils.deleteWhitespace(expression).matches(OCLFeatures.OCL_IS_KIND_OF);
    }

    /**
     * Indicates if this <code>expression</code> is an OCL feature (that is it
     * matches one of the features defined within this class).
     * 
     * @param expression the expression to match.
     * @return true/false
     */
    public static boolean isOclFeature(Object expression)
    {
        return TranslationUtils.deleteWhitespace(expression).matches(ALL_PATTERNS);
    }

    /**
     * Represents the <em>self</em> keyword in OCL.
     */
    private static final String SELF = "self";

    /**
     * Indicates if this <code>expression</code> is an instance of the
     * <em>self</em> key word.
     * 
     * @param expression the expression to check.
     * @return true/false
     */
    public static boolean isSelf(Object expression)
    {
        return TranslationUtils.deleteWhitespace(expression).matches(SELF);
    }
}