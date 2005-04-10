package org.andromda.translation.ocl.validation;

import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.Collection;

/**
 * Contains a single operation {@link #isPredicateFeature(String) that determines if a passed in <code>feature</code>
 * matches the name of a feature that should use a predicate when being translated.
 *
 * @author Chad Brandon
 */
class OCLPredicateFeatures
{
    /**
     * Contains the names of feature calls that are expected to use predicates while being translated.
     */
    private static final String[] PREDICATE_FEATURES = new String[]{"one", "forAll", "reject", "select", "any",
                                                                    "exists", };

    private static final Collection features = Arrays.asList(PREDICATE_FEATURES);

    /**
     * Indicates whether or not the passed in <code>feature</code> is the name of a boolean evaluating feature.
     *
     * @param feature
     * @return true/false
     */
    static final boolean isPredicateFeature(final String feature)
    {
        return features.contains(StringUtils.trimToEmpty(feature));
    }
}
