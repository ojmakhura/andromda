package org.andromda.core.common;

/**
 * <p>
 * Contains namespace properties used witin the AndroMDA core.
 * </p>
 * 
 * @author Chad Brandon
 */
public class NamespaceProperties
{
    /**
     * <p>
     * The location of the <strong>optional </strong> location of a merge
     * mappings file (merge mappings are mappings that allow the replacement of
     * patterns in generated files during model processing time).
     * </p>
     */
    public static final String MERGE_MAPPINGS_URI = "mergeMappingsUri";

    /**
     * <p>
     * Defines the location of merge templates. Merge templates are currently
     * templates which will override existing templates found in cartridges (so
     * they must have the exact same name as the template which this wish to
     * override).
     * </p>
     */
    public static final String MERGE_LOCATION = "mergeLocation";
}
