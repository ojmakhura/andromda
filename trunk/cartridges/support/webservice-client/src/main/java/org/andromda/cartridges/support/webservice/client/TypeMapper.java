package org.andromda.cartridges.support.webservice.client;

/**
 * Used to for mapping types to and from objects.
 *
 * @author Chad Brandon
 */
public interface TypeMapper
{
    /**
     * Gets the object for the given type.
     *
     * @param type the type from which to construct the object.
     *
     * @return the object.
     */
    Object getObject(Class type);

    /**
     * Gets the object for the given type optionally using the value
     * on the returned object.
     *
     * @param type the type to construct
     * @param value the optional value used to construct or retrieve the object.
     * @return the object.
     */
    Object getObject(Class type, String value);

    /**
     * Gets the appropriate string value of the given <code>object</code>.
     *
     * @param object the object to convert to a string.
     * @return the string value.
     */
    String getStringValue(Object object);

    /**
     * Indicates whether or not the given type is considered "simple".
     *
     * @param type the type to consider.
     * @return true/false
     */
    boolean isSimpleType(Class type);
}
