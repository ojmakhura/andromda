package org.andromda.translation.ocl.query;

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
    public static final String OPERATION_FEATURE_CALL = "(.*\\s*(->|\\.))?\\s*\\w*\\s*\\([[\\w+|\\.|:|,]|\\s*]*\\)";
}