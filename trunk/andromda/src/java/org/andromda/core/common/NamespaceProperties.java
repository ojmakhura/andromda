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

    /**
     * <p>
     * The URI to the mappings file containing the AndroMDA profile.
     * This property provides the ability to override the default 
     * profile values (i.e. stereotype names).
     * </p>
     */
    public static final String PROFILE_MAPPINGS_URI = "profileMappingsUri";
    
    /**
     * <p>
     * The URI to the logging configuration file.  This is an optional namespace
     * property that may be specified to customize the logging of AndroMDA. You can
     * retrieve the default log4j.xml contained within the {@link org.andromda.core.common}
     * package, customize it, and then specify the location of this logging file
     * with this property (it only really makes sense to specify this property in the
     * default namespace as it can only be configured at application initialization).
     * </p>
     */
    public static final String LOGGING_CONFIGURATION_URI = "loggingConfigurationUri";
}
