package org.andromda.andromdapp;


/**
 * Some utlities for dealing with the AndroMDApp generator.
 *
 * @author Chad Brandon
 */
public class AndroMDAppUtils
{
    /**
     * The delimiter for seperating patterns.
     */
    private static final String COMMA = ",";

    /**
     * Converts a comma delimated string to an array of Strings.
     *
     * @param string to convert.
     * @return the resulting array or null if the string was null.
     */
    public static String[] stringToArray(final String string)
    {
        return string != null ? string.split(COMMA) : null;
    }
}