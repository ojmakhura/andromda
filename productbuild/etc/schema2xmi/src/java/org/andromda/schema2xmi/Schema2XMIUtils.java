package org.andromda.schema2xmi;


/**
 * Contains utilities for the Schema2XMI tool.
 *
 * @author Chad Brandon
 */
class Schema2XMIUtils
{
    /**
     * Constructs the entire type name from the
     * given name and length.
     *
     * @param name the name of the type
     * @param length the length of the type.
     * @return the type name with the length.
     */
    static String constructTypeName(
        final String name,
        final String length)
    {
        final StringBuffer buffer = new StringBuffer();
        if (name != null)
        {
            buffer.append(name);
        }
        if (name != null && length != null && !name.matches(".+\\(.+\\)"))
        {
            buffer.append("(" + length + ")");
        }
        return buffer.toString();
    }
}