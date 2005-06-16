package org.andromda.schema2xmi;

import org.apache.commons.lang.StringUtils;


/**
 * Provides formatting functions, when converting SQL names to model names.
 *
 * @author Chad Brandon
 */
public class SqlToModelNameFormatter
{
    /**
     * Converts a table name to an class name.
     *
     * @param name the name of the table.
     * @return the new class name.
     */
    public static String toClassName(String name)
    {
        return toCamelCase(name);
    }

    /**
     * Converts a column name to an attribute name.
     *
     * @param name the name of the column
     * @return the new attribute name.
     */
    public static String toAttributeName(String name)
    {
        return StringUtils.uncapitalize(toClassName(name));
    }

    /**
     * Turns a table name into a model element class name.
     *
     * @param name the table name.
     * @return the new class name.
     */
    public static String toCamelCase(String name)
    {
        StringBuffer buffer = new StringBuffer();
        String[] tokens = name.split("_");
        if (tokens != null && tokens.length > 0)
        {
            for (int ctr = 0; ctr < tokens.length; ctr++)
            {
                buffer.append(StringUtils.capitalize(tokens[ctr].toLowerCase()));
            }
        }
        else
        {
            buffer.append(StringUtils.capitalize(name.toLowerCase()));
        }
        return buffer.toString();
    }
}