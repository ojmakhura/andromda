package org.andromda.cartridges.webservice;

/**
 * Stores global variables for the WebService cartridge metafacades.
 *
 * @author Chad Brandon
 */
public class WebServiceGlobals
{
    /**
     * The prefix for the XSD namespace.
     */
    public final static String XSD_NAMESPACE_PREFIX = "xsd:";

    /**
     * Defines the property that stores the location of the schema type mappings URI.
     */
    public static final String SCHEMA_TYPE_MAPPINGS_URI = "schemaTypeMappingsUri";

    /**
     * The prefix given to wrapped style WSDL array types.
     */
    public static final String ARRAY_NAME_PREFIX = "arrayNamePrefix";

    /**
     * The namespace delimiter (seperates namespaces).
     */
    public static final char NAMESPACE_DELIMITER = '.';
}
