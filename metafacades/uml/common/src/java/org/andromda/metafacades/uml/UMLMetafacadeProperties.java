package org.andromda.metafacades.uml;

/**
 * Stores the metafacade namespace properties used throughout the UML
 * metafacades.
 * 
 * @author Chad Brandon
 */
public class UMLMetafacadeProperties
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
     * Allows the pluralization of association end names (when the multiplicity
     * is greater than one) to be turned on/off.
     */
    public static final String PLURALIZE_ASSOCIATION_END_NAMES = "pluralizeAssociationEndNames";

    /**
     * The default suffix to use for foreign keys.
     */
    public static final String FOREIGN_KEY_SUFFIX = "foreignKeySuffix";

    /**
     * The mask to apply to role names (lowercase, uppercase, camelcase,
     * nospace, none).
     */
    public static final String ROLE_NAME_MASK = "roleNameMask";

    /**
     * The maximum length a SQL name can be before its truncated.
     */
    public static final String MAX_SQL_NAME_LENGTH = "maxSqlNameLength";

    /**
     * Whether or not to allow default identifiers for modeled entities.
     */
    public static final String ALLOW_DEFAULT_IDENTITIFIERS = "allowDefaultIdentifiers";

    /**
     * The name given to default identifiers (if enabled).
     */
    public static final String DEFAULT_IDENTIFIER = "defaultIdentifier";

    /**
     * The type to given default identifiers.
     */
    public static final String DEFAULT_IDENTIFIER_TYPE = "defaultIdentifierType";

    /**
     * The visibility to apply to default identifiers.
     */
    public static final String DEFAULT_IDENTIFIER_VISIBILITY = "defaultIdentifierVisibility";
    
    /**
     * The seperator used for packages and element names when constructing
     * fully qualified names.
     */
    public static final String NAMESPACE_SEPERATOR = "namespaceSeperator";
    
    /**
     * Used to seperate the names when constructing the fully qualified validation name.
     */
    public static final String UNDEFINED_NAME = "undefinedName";
}