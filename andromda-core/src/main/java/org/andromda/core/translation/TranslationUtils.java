package org.andromda.core.translation;

import java.util.Objects;

import org.andromda.core.common.ExceptionUtils;
import org.andromda.core.common.Introspector;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Contains translation utilities.
 *
 * @author Chad Brandon
 */
public class TranslationUtils
{
    /**
     * <p>
     * <code>TranslationUtils</code> instances should NOT be constructed in standard programming. Instead, the class
     * should be used as <code>TranslationUtils.replacePattern(" some pattern ");</code>. </p>
     * <p>
     * This constructor is public to permit tools that require a JavaBean instance to operate. </p>
     */
    public TranslationUtils()
    {
        // Here for documentation purposes
    }

    /**
     * Searches for and replaces the specified pattern with braces around it, like so: "{pattern}" every time it
     * occurs in the string.
     *
     * @param string      the string to to perform replacement on.
     * @param pattern     the pattern to find
     * @param replaceWith the pattern to place the existing one with.
     * @return String the string will all replacements
     */
    public static String replacePattern(
        String string,
        final String pattern,
        final String replaceWith)
    {
        if (string != null)
        {
            ExceptionUtils.checkNull("pattern", pattern);
            ExceptionUtils.checkNull("replaceWith", replaceWith);
            string = StringUtils.replace(string, '{' + pattern + '}', replaceWith);
        }
        return string;
    }

    /**
     * Searches for and replaces the specified pattern with braces around it, like so: "{pattern}" the first time it
     * occurs in the string
     *
     * @param string      the string to to perform replacement on.
     * @param pattern     the pattern to find
     * @param replaceWith the pattern to place the existing one with.
     * @return String the string will all replacements
     */
    public static String replaceFirstPattern(
        String string,
        final String pattern,
        final String replaceWith)
    {
        if (string != null)
        {
            ExceptionUtils.checkNull("pattern", pattern);
            ExceptionUtils.checkNull("replaceWith", replaceWith);
            string = StringUtils.replaceOnce(string, '{' + pattern + '}', replaceWith);
        }
        return string;
    }

    /**
     * Returns true if the specified pattern with braces around it, like so: "{pattern}" exists in the string.
     *
     * @param string  the string to to perform replacement on.
     * @param pattern the pattern to find
     * @return boolean true if the string contains the pattern, false otherwise
     */
    public static boolean containsPattern(
        final String string,
        final String pattern)
    {
        boolean containsPattern = string != null && pattern != null;
        if (containsPattern)
        {
            containsPattern = StringUtils.contains(string, '{' + pattern + '}');
        }
        return containsPattern;
    }

    /**
     * Calls the object's toString method and trims the value. Returns and empty string if null is given.
     *
     * @param object the object to use.
     * @return String
     */
    public static String trimToEmpty(final Object object)
    {
        return StringUtils.trimToEmpty(Objects.toString(object, ""));
    }

    /**
     * Calls the object's toString method and deletes any whitespace from the value. Returns and empty string if null is
     * given.
     *
     * @param object the object to deleteWhite space from.
     * @return String
     */
    public static String deleteWhitespace(final Object object)
    {
        return StringUtils.deleteWhitespace(Objects.toString(object, ""));
    }

    /**
     * Retrieves the "starting" property name from one that is nested, for example, will return ' <name1>' from the
     * string <name1>. <name2>. <name3>. If the property isn't nested, then just return the name that is passed in.
     *
     * @param property the property.
     * @return String
     */
    public static String getStartingProperty(String property)
    {
        StringUtils.trimToEmpty(property);
        int dotIndex = property.indexOf('.');
        if (dotIndex != -1)
        {
            property = property.substring(0, dotIndex);
        }
        return property;
    }

    /**
     * Removes any extra whitespace. Does not remove the spaces between the words. Only removes tabs and newline
     * characters. This is to allow everything to be on one line while keeping the spaces between words.
     *
     * @param string the string
     * @return String the string with the removed extra spaces.
     */
    public static String removeExtraWhitespace(final String string)
    {
        return string == null ? "" : string.replaceAll("[$\\s]+", " ").trim();
    }

    /**
     * Just retrieves properties from a bean, but gives a more informational error when the property can't be
     * retrieved, it also cleans the resulting property from any excess white space
     *
     * @param bean     the bean from which to retrieve the property
     * @param property the property name
     * @return Object the value of the property
     */
    public static Object getProperty(
        final Object bean,
        final String property)
    {
        final String methodName = "TranslationUtils.getProperty";
        try
        {
            return Introspector.instance().getProperty(bean, property);
        }
        catch (final Exception exception)
        {
            throw new TranslatorException(
                "Error performing " + methodName + " with bean '" + bean + "' and property '" + property + '\'',
                exception);
        }
    }

    /**
     * Just retrieves properties from a bean, but gives a more informational error when the property can't be
     * retrieved, it also cleans the resulting property from any excess white space
     *
     * @param bean     the bean from which to retrieve the property
     * @param property the property name
     * @return Object the value of the property
     */
    public static String getPropertyAsString(
        final Object bean,
        final String property)
    {
        return TranslationUtils.trimToEmpty(TranslationUtils.getProperty(bean, property));
    }
}
