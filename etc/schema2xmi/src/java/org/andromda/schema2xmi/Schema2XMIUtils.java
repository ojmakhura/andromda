package org.andromda.schema2xmi;

import org.apache.commons.lang.StringUtils;


/**
 * Contains utilities for the Schema2XMI tool.
 *
 * @author Chad Brandon
 * @author Joel Kozikowski
 */
class Schema2XMIUtils
{
    /**
     * Constructs the entire type name from the
     * given name and length.
     *
     * @param name the name of the type
     * @param length the length of the type.
     * @param decimalPlaces the number of decimal places specified for the type
     * @return the type name with the length.
     */
    static String constructTypeName(
        final String name,
        final String length,
        final String decimalPlaces)
    {
        final StringBuffer buffer = new StringBuffer();
        if (name != null)
        {
            buffer.append(name);

            if (!name.matches(".+\\(.+\\)"))
            {
                if (StringUtils.isNotEmpty(length))
                {
                    buffer.append('(').append(length);
                    if (StringUtils.isNotEmpty(decimalPlaces))
                    {
                        buffer.append(',').append(decimalPlaces);
                    }
                    buffer.append(')');
                }
            }

        }
        return buffer.toString();
    }
}