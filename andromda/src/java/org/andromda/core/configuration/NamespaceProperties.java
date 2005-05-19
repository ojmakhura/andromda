package org.andromda.core.configuration;

/**
 * Contains namespace properties used witin the AndroMDA core. 
 *
 * @author Chad Brandon
 */
public class NamespaceProperties
{
    /**
     * The location of an <strong>optional </strong> merge mappings file (merge mappings are mappings that allow the
     * replacement of patterns in generated files during model processing time). 
     */
    public static final String MERGE_MAPPINGS_URI = "mergeMappingsUri";

    /**
     * Defines the location of merge templates. Merge templates are templates that can either be <em>merged</em> into
     * existing cartridge output, or replace cartridge templates all together.  This must be the directory where the
     * merge files can be found. 
     */
    public static final String MERGE_LOCATION = "mergeLocation";

    /**
     * The URI to the mappings file containing the AndroMDA profile. This property provides the ability to override the
     * default profile values (i.e. stereotype names, etc). 
     */
    public static final String PROFILE_MAPPINGS_URI = "profileMappingsUri";
    
    /**
     * If true, all resources are overwritten (even if ovewrite is set to <code>false</code>
     * within an andromda-cartridge.xml). If false, no resources are overwritten. 
     * It is useful to set to <code>true</code> when during pure modeling until you're ready 
     * to implement.
     */
    public static final String OVERWRITE = "overwrite";
}