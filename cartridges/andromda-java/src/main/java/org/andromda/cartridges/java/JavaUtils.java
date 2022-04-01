package org.andromda.cartridges.java;

import java.util.Date;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;

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

    /**
     * Returns the current JDK version.
     *
     * @return the current JDK version (1.4, 1.5, 1.6, 1.7 etc).
     */
    public static String getJDKVersion()
    {
        // Default JDK version = 1.6
        String version = "1.8";
        final Integer classVersion = (int)Double.parseDouble(System.getProperty("java.class.version"));
        if (classVersion == 62)
        {
            version = "18";
        }
        else if (classVersion == 61)
        {
            version = "17";
        }
        else if (classVersion == 60)
        {
            version = "16";
        }
        else if (classVersion == 59)
        {
            version = "15";
        }
        else if (classVersion == 58)
        {
            version = "14";
        }
        else if (classVersion == 57)
        {
            version = "13";
        }
        else if (classVersion == 56)
        {
            version = "12";
        }
        else if (classVersion == 55)
        {
            version = "11";
        }
        else if (classVersion == 54)
        {
            version = "10";
        }
        else if (classVersion == 53)
        {
            version = "9";
        }
        else if (classVersion == 53)
        {
            version = "1.8";
        }
        else if (classVersion == 51)
        {
            version = "1.7";
        }
        else if (classVersion == 49)
        {
            version = "1.5";
        }
        else if (classVersion == 48)
        {
            version = "1.4";
        }
        else if (classVersion == 47)
        {
            version = "1.3";
        }
        else if (classVersion == 46)
        {
            version = "1.2";
        }
        else if (classVersion == 45)
        {
            version = "1.2";
        }
        return version;
    }
}
