package org.andromda.core.common;

/**
 * @deprecated use {@link org.andromda.utils.StringUtilsHelper} instead
 */
public class StringUtilsHelper
{
    /**
     * @deprecated use {@link org.andromda.utils.StringUtilsHelper#replaceSuffix(String, String, String)} instead
     */
    public static String replaceSuffix(
        final String src,
        final String suffixOld,
        final String suffixNew)
    {
        return org.andromda.utils.StringUtilsHelper.replaceSuffix(src, suffixOld, suffixNew);
    }

    /**
     * @deprecated use {@link org.andromda.utils.StringUtilsHelper#upperCamelCaseName(String)} instead
     */
    public static String upperCamelCaseName(final String string)
    {
        return org.andromda.utils.StringUtilsHelper.upperCamelCaseName(string);
    }

    /**
     * @deprecated use {@link org.andromda.utils.StringUtilsHelper#removeLastOccurrence(String, String)} instead
     */
    public static String removeLastOccurrence(
        final String string,
        final String value)
    {
        return org.andromda.utils.StringUtilsHelper.removeLastOccurrence(string, value);
    }

    /**
     * @deprecated use {@link org.andromda.utils.StringUtilsHelper#lowerCamelCaseName(String)} instead
     */
    public static String lowerCamelCaseName(final String string)
    {
        return org.andromda.utils.StringUtilsHelper.lowerCamelCaseName(string);
    }

    /**
     * @deprecated use {@link org.andromda.utils.StringUtilsHelper#toResourceMessageKey(String)} instead
     */
    public static String toResourceMessageKey(final String string)
    {
        return org.andromda.utils.StringUtilsHelper.toResourceMessageKey(string);
    }

    /**
     * @deprecated use {@link org.andromda.utils.StringUtilsHelper#toPhrase(String)} instead
     */
    public static String toPhrase(final String string)
    {
        return org.andromda.utils.StringUtilsHelper.toPhrase(string);
    }

    /**
     * @deprecated use {@link org.andromda.utils.StringUtilsHelper#separate(String, String)} instead
     */
    public static String separate(
        final String string,
        final String separator)
    {
        return org.andromda.utils.StringUtilsHelper.separate(string, separator);
    }

    /**
     * @deprecated use {@link org.andromda.utils.StringUtilsHelper#suffixLines(String, String)} instead
     */
    public static String suffixLines(
        final String multiLines,
        final String suffix)
    {
        return org.andromda.utils.StringUtilsHelper.suffixLines(multiLines, suffix);
    }

    /**
     * @deprecated use {@link org.andromda.utils.StringUtilsHelper#toResourceMessage(String)} instead
     */
    public static String toResourceMessage(String multiLines)
    {
        return org.andromda.utils.StringUtilsHelper.toResourceMessage(multiLines);
    }

    /**
     * @deprecated use {@link org.andromda.utils.StringUtilsHelper#prefixWithAPredicate(String)} instead
     */
    public static String prefixWithAPredicate(final String word)
    {
        return org.andromda.utils.StringUtilsHelper.prefixWithAPredicate(word);
    }

    /**
     * @deprecated use {@link org.andromda.utils.StringUtilsHelper#toSingleLine(String)} instead
     */
    public static String toSingleLine(String string)
    {
        return org.andromda.utils.StringUtilsHelper.toSingleLine(string);
    }

    /**
     * @deprecated use {@link org.andromda.utils.StringUtilsHelper#pluralize(String)} instead
     */
    public static String pluralize(final String singularNoun)
    {
        return org.andromda.utils.StringUtilsHelper.pluralize(singularNoun);
    }

    /**
     * @deprecated use {@link org.andromda.utils.StringUtilsHelper#format(String)} instead
     */
    public static String format(final String plainText)
    {
        return org.andromda.utils.StringUtilsHelper.format(plainText);
    }

    /**
     * @deprecated use {@link org.andromda.utils.StringUtilsHelper#format(String, String)} instead
     */
    public static String format(
        final String plainText,
        final String indentation)
    {
        return org.andromda.utils.StringUtilsHelper.format(plainText, indentation);
    }

    /**
     * @deprecated use {@link org.andromda.utils.StringUtilsHelper#format(String, String, int)} instead
     */
    public static String format(
        final String plainText,
        final String indentation,
        final int wrapAtColumn)
    {
        return org.andromda.utils.StringUtilsHelper.format(plainText, indentation, wrapAtColumn);
    }

    /**
     * @deprecated use {@link org.andromda.utils.StringUtilsHelper#format(String, String, int, boolean)} instead
     */
    public static String format(
        final String plainText,
        String indentation,
        final int wrapAtColumn,
        final boolean htmlStyle)
    {
        return org.andromda.utils.StringUtilsHelper.format(plainText, indentation, wrapAtColumn, htmlStyle);
    }

    /**
     * @deprecated use {@link org.andromda.utils.StringUtilsHelper} instead
     */
    public static String getLineSeparator()
    {
        return org.andromda.utils.StringUtilsHelper.getLineSeparator();
    }
}