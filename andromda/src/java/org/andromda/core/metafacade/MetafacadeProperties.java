package org.andromda.core.metafacade;

/**
 * Stores the common metafacade namespace properties used throughout <strong>ANY
 * </strong> set of metafacades (UML, etc).
 * 
 * @author Chad Brandon
 */
public class MetafacadeProperties
{
    /**
     * Used to separate the namespaces between a validation name.
     */
    public static final String VALIDATION_NAME_SEPARATOR = "validationNameSeparator";

    /**
     * Are we making use of the caching of metafacade properties (true/false).
     */
    public static final String ENABLE_METAFACADE_PROPERTY_CACHING = "enableMetafacadePropertyCaching";
}