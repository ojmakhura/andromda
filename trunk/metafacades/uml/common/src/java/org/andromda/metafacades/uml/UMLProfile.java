package org.andromda.metafacades.uml;

import java.util.ResourceBundle;

/**
 * Contains the common UML AndroMDA profile. That is, it contains elements
 * "common" to all AndroMDA components (tagged values, and stereotypes).
 * 
 * @author Chad Brandon
 */
public class UMLProfile
{
    // Get the resource
    static ResourceBundle umlProfile = ResourceBundle
        .getBundle(UMLProfile.class.getName());

    /* ----------------- Stereotypes -------------------- */

    /**
     * Represents a persistent entity.
     */
    public static final String STEREOTYPE_ENTITY = umlProfile
        .getString("STEREOTYPE_ENTITY");

    /**
     * Represents a finder method on an entity.
     */
    public static final String STEREOTYPE_FINDER_METHOD = umlProfile
        .getString("STEREOTYPE_FINDER_METHOD");

    /**
     * Represents the primary key of an entity.
     */
    public static final String STEREOTYPE_IDENTIFIER = umlProfile
        .getString("STEREOTYPE_IDENTIFIER");

    /**
     * Represents a service.
     */
    public static final String STEREOTYPE_SERVICE = umlProfile
        .getString("STEREOTYPE_SERVICE");

    /**
     * The base exception stereotype. If a model element is stereotyped with
     * this (or one of its sub stereotypes), then it is excepted that a
     * cartridge will generate an exception
     */
    public static final String STEREOTYPE_EXCEPTION = umlProfile
        .getString("STEREOTYPE_EXCEPTION");

    /**
     * Represents exceptions thrown during normal application processing (such
     * as business exceptions). It extends the base exception stereotype.
     */
    public static final String STEREOTYPE_APPLICATION_EXCEPTION = umlProfile
        .getString("STEREOTYPE_APPLICATION_EXCEPTION");

    /**
     * Represents unexpected exceptions that can occur during application
     * processing. This that a caller isn't expected to handle.
     */
    public static final String STEREOTYPE_UNEXPECTED_EXCEPTION = umlProfile
        .getString("STEREOTYPE_UNEXPECTED_EXCEPTION");

    /**
     * Represents a reference to an exception model element. Model dependencies
     * to unstereotyped exception model elements can be stereotyped with this.
     * This allows the user to create a custom exception class since the
     * exception itself will not be generated but the references to it will be
     * (i.e. the throws clause within an operation).
     */
    public static final String STEREOTYPE_EXCEPTION_REF = umlProfile
        .getString("STEREOTYPE_EXCEPTION_REF");

    /**
     * Represents a reference to an entity.
     */
    public static final String STEREOTYPE_ENTITY_REF = umlProfile
        .getString("STEREOTYPE_ENTITY_REF");

    /* ----------------- Tagged Values -------------------- */

    /**
     * Represents documentation stored as a tagged value
     */
    public static final String TAGGEDVALUE_DOCUMENTATION = umlProfile
        .getString("TAGGEDVALUE_DOCUMENTATION");

    /**
     * Represents a SQL table name for entity persistence.
     */
    public static final String TAGGEDVALUE_PERSISTENCE_TABLE = umlProfile
        .getString("TAGGEDVALUE_PERSISTENCE_TABLE");

    /**
     * Represents a SQL table column name for entity persistence.
     */
    public static final String TAGGEDVALUE_PERSISTENCE_COLUMN = umlProfile
        .getString("TAGGEDVALUE_PERSISTENCE_COLUMN");

    /**
     * Represents a SQL table column length
     */
    public static final String TAGGEDVALUE_PERSISTENCE_COLUMN_LENGTH = umlProfile
        .getString("TAGGEDVALUE_PERSISTENCE_COLUMN_LENGTH");
}