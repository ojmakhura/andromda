package org.andromda.cartridges.meta;

import org.apache.commons.lang.StringUtils;

/**
 * Contains utilities used by the meta cartridge.
 * 
 * @author Chad Brandon
 */
public class MetaUtils
{
    /**
     * Creates a single line from the given <code>string</code>. 
     * In other words, removes all whitespace besides spaces.
     * 
     * @param string the String from which to remove the excess whitespace.
     */
    public String toSingleLine(String string)
    {
        return StringUtils.trimToEmpty(string).replaceAll("[$\\s]+", " ");
    }
}
