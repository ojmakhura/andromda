package org.andromda.translation.validation;

import java.util.Arrays;
import java.util.Collection;

import org.apache.commons.lang.StringUtils;

/**
 * Contains a single operatoin {@link #isBooleanFeature(String)
 * that determines if a passed in <code>feature</code> matches
 * the name of a feature that evaulates to boolean.
 * 
 * @author Chad Brandon
 */
public class OCLBooleanEvaluatingFeatures
{
    /**
     * Contains the names of feature calls that are
     * expected to evaluate to boolean expressions.
     */
    private static final String[] BOOLEAN_EVALUATING_FEATURES = new String[]
    {
        "one",
        "forAll",
        "reject",
        "select",
        "any",
        "exists",
    };
    
    private static final Collection features = Arrays.asList(BOOLEAN_EVALUATING_FEATURES);
    
    /**
     * Indicates whether or not the passed in <code>feature</code>
     * is the name of a boolean evaluating feature.
     * 
     * @param feature
     * @return true/false
     */
    public static final boolean isBooleanFeature(final String feature)
    {
        return features.contains(StringUtils.trimToEmpty(feature));
    }
}
