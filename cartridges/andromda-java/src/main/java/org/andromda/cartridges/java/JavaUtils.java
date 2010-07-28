package org.andromda.cartridges.java;

import java.util.Date;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.FastDateFormat;

/**
 * Contains utilities used within the WebService cartridge.
 *
 * @author Chad Brandon
 */
public class JavaUtils
{

    /**
     * The namespace delimiter (separates namespaces).
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
    
    private static FastDateFormat df = FastDateFormat.getInstance("MM/dd/yyyy HH:mm:ssZ");
    
    /**
     * Returns the current Date in the specified format.
     *
     * @param format The format for the output date
     * @return the current date in the specified format.
     */
    public static String getDate(String format)
    {
        if (df == null || !format.equals(df.getPattern()))
        {
            df = FastDateFormat.getInstance(format);
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
