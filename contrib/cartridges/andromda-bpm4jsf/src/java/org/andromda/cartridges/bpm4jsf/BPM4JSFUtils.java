package org.andromda.cartridges.bpm4jsf;

import org.andromda.utils.StringUtilsHelper;


/**
 * Utilties for use within the BPM4JSF cartridge.
 * 
 * @author Chad Brandon
 */
public class BPM4JSFUtils
{
    /**
     * Converts the argument into a web resource name, this means: all lowercase
     * characters and words are separated with dashes.
     * 
     * @param string any string
     * @return the string converted to a value that would be well-suited for a
     *         web file name
     */
    public static String toWebResourceName(final String string)
    {
        return StringUtilsHelper.separate(string, "-").toLowerCase();
    }
}
