package org.andromda.translation.validation;

/**
 * Contains the patterns matching reserved features of the OCL language.
 * 
 * @author Chad Brandon
 */
public class OCLReservedFeatures
{
    /**
     * Matches on the <code>allInstances()</code> call.
     */
    public static final String ALL_INSTANCES = "(\\s*\\w*\\s*(\\w+|::)*)?\\s*allInstances\\s*\\(\\s*\\)";
}
