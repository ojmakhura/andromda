package org.andromda.metafacades.uml;

/**
 * Stores the metafacade namespace properties used throughout the UML
 * metafacades.
 * 
 * @author Chad Brandon
 */
public class MetafacadeProperties
{
    /**
     * Stores the property containing the URI to the language mappings file.
     */
    public static final String LANGUAGE_MAPPINGS_URI = "languageMappingsUri";
    
    /**
     * Stores the property defining the prefix for entity table names.
     */
    public static final String TABLE_NAME_PREFIX = "tableNamePrefix";

    /**
     * Stores the property containing the URI to the SQL mappings file.
     */
    public static final String SQL_MAPPINGS_URI = "sqlMappingsUri";

    /**
     * Stores the property containing the URI to the JDBC mappings file.
     */
    public static final String JDBC_MAPPINGS_URI = "jdbcMappingsUri";

    /**
     * Stores the property containing the URI to the Wrapper mappings file.
     */
    public static final String WRAPPER_MAPPINGS_URI = "wrapperMappingsUri";
    
    /**
     * Allows the pluralization of association end names (when the multiplicity is
     * greater than one) to be turned on/off.
     */
    public static final String PLURALIZE_ASSOCIATION_END_NAMES = "pluralizeAssociationEndNames";
    
    /**
     * The default suffix to use for foreign keys.
     */
    public static final String FOREIGN_KEY_SUFFIX = "foreignKeySuffix";
}