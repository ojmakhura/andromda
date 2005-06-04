package org.andromda.core.metafacade;


/**
 * Stores the common metafacade namespace properties used throughout <strong>ANY </strong> set of metafacades (UML,
 * etc).
 *
 * @author Chad Brandon
 */
public class MetafacadeProperties
{
    /**
     * Are we making use of the caching of metafacade properties (true/false).
     */
    public static final String ENABLE_METAFACADE_PROPERTY_CACHING = "enableMetafacadePropertyCaching";

    /**
     * The pattern used for constructing the name of the metafacade class implementation name
     * from the metaclass interface name (assuming each metaclass has an interface).
     * <p>
     * This is used when dynamically searching for mappings that may apply to ancestors of a given
     * meta class since there is no mapping for the particular metaclass itself.  The value of this property
     * must be in a format like '<em>{0}Impl</em>' for example, where <em>{0}</em> represents the name
     * of the interface.  <strong>NOTE:</strong> unless this property is specified, meta object inheritance
     * based mapping searches can <strong>not</strong> be performed.
     * </p>
     */
    public static final String METACLASS_IMPLEMENTATION_NAME_PATTERN = "metaclassImplementationNamePattern";
}