package org.andromda.cartridges.java;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.lang.StringUtils;

/**
 * Contains utilities used within the WebService cartridge.
 *
 * @author Chad Brandon
 */
public class JavaUtils
{

    /**
     * The namespace delimiter (seperates namespaces).
     */
    public static final char NAMESPACE_DELIMITER = '.';

    /**
     * Reverses the <code>packageName</code>.
     *
     * @param packageName the package name to reverse.
     * @return the reversed package name.
     */
    public static String reversePackage(String packageName)
    {
        return StringUtils.reverseDelimited(packageName, NAMESPACE_DELIMITER);
    }
    
    private static SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ssZ");
    /**
     * Returns the current Date in the specified format. $conversionUtils does not seem to work in vsl.
     *
     * @param format The format for the output date
     * @return the current date in the specified format.
     */
    public static String getDate(String format)
    {
        if (df == null || !format.equals(df.toLocalizedPattern()))
        {
            df = new SimpleDateFormat(format);
        }
        return df.format(new Date());
    }
    
    /**
     * Returns the current Date in the specified format.
     *
     * @return the current date with the default format .
     */
    public static String getDate()
    {
        return df.format(new Date());
    }
}
