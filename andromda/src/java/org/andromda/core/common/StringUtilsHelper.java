package org.andromda.core.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

/**
 * A utility object for doing string manipulation operations that are commonly
 * needed by the code generation templates.
 *
 * @author Matthias Bohlen
 * @author Chris Shaw
 * @author Chad Brandon
 */
public class StringUtilsHelper {

    /**
     * <p>Capitalizes a string.  That is, it returns "Hamburger" when
     * eating a "hamburger".</p>
     *
     * @deprecated - use upperCaseFirstLetter
     *
     * @param s the input string
     * @return String the output string
     */
    public static String capitalize(String s) {
        return StringUtils.capitalize(s);
    }

    /**
    * <p>Capitalizes a string. That is, it returns "HamburgerStall"
    * when receiving a "hamburgerStall".</p>
    *
    * @param s the input string
    * @return String the output string.
    */
    public static String upperCaseFirstLetter(String s) {
        if (s != null && s.length() > 0) {
            return s.substring(0, 1).toUpperCase() + s.substring(1);
        } else {
            return s;
        }
    }

    /**
    * <p>Removes the capitalization of a string. That is, it returns
    * "hamburgerStall" when receiving a "HamburgerStall".</p>
    *
    * @param s the input string
    * @return String the output string.
    */
    public static String lowerCaseFirstLetter(String s) {
    	return StringUtils.uncapitalize(s);
    }

    /**
     * <p>Replaces a given suffix of the source string with a new one.
     * If the suffix isn't present, the string is returned
     * unmodified.</p>
     *
     * @param src the <code>String</code> for which the suffix should be replaced
     * @param suffixOld a <code>String</code> with the suffix that should be replaced
     * @param suffixNew a <code>String</code> with the new suffix
     * @return a <code>String</code> with the given suffix replaced or
     *         unmodified if the suffix isn't present
     */
    public static String replaceSuffix(String src, String suffixOld, String suffixNew) {
        if (src.endsWith(suffixOld)) {
            return src.substring(0, src.length()-suffixOld.length())+suffixNew;
        }

        return src;
    }

    /**
     * <p>Returns the argument string as a Java class name according the Sun coding conventions.</p>
     * <p>Non word characters be removed and the letter following such a character will be uppercased.</p>
     *
     * @param string any string
     * @return the string converted to a value that would be well-suited for a Java class
     */
    public static String toJavaClassName(String string)
    {
        if ( (string == null) ||  (string.trim().length() == 0) )
            return string;

        final String[] parts = splitAtCapitalization(string);
        final StringBuffer conversionBuffer = new StringBuffer();
        for (int i = 0; i < parts.length; i++)
        {
            if (parts[i].length() < 2)
            {
                conversionBuffer.append(parts[i].toUpperCase());
            }
            else
            {
                conversionBuffer.append(parts[i].substring(0,1).toUpperCase());
                conversionBuffer.append(parts[i].substring(1).toLowerCase());
            }
        }
        return conversionBuffer.toString();
    }

    /**
     * <p>Returns the argument string as a Java method name according the Sun coding conventions.</p>
     * <p>Non word characters be removed and the letter following such a character will be uppercased.</p>
     *
     * @param string any string
     * @return the string converted to a value that would be well-suited for a Java method
     */
    public static String toJavaMethodName(String string)
    {
        return lowerCaseFirstLetter(toJavaClassName(string));
    }

    /**
     * Converts the argument into a web file name, this means: all lowercase
     * characters and words are separated with dashes.
     *
     * @param string any string
     * @return the string converted to a value that would be well-suited for a web file name
     */
    public static String toWebFileName(String string)
    {
        return separate(string, "-").toLowerCase();
    }

    /**
     * Converts the argument into a message key in a properties resource bundle,
     * all lowercase characters, words are separated by dots.
     *
     * @param string any string
     * @return the string converted to a value that would be well-suited for a message key
     */
    public static String toResourceMessageKey(String string)
    {
        return separate(string, ".").toLowerCase();
    }

    /**
     * Converts into a string suitable as a human readable phrase,
     * First character is uppercase (the rest is left unchanged), words are separated
     * by a space.
     *
     * @param string any string
     * @return the string converted to a value that would be well-suited for a human readable phrase
     */
    public static String toPhrase(String string)
    {
        return upperCaseFirstLetter(separate(string, " "));
    }

    /**
     * Converts the argument to lowercase, removes all non-word characters, and replaces each of those
     * sequences by a hyphen '-'.
     */
    public static String separate(String string, String separator)
    {
        if ( (string == null) ||  (string.trim().length() == 0) )
            return string;

        final String[] parts = splitAtCapitalization(string);
        final StringBuffer buffer = new StringBuffer();

        for (int i = 0; i < parts.length - 1; i++)
        {
            if (parts[i].trim().length() > 0)
                buffer.append(parts[i]).append(separator);
        }
        return buffer.append(parts[parts.length - 1]).toString();
    }

    /**
     * Splits at each sequence of non-word characters. Because this is used to convert to Java-style
     * conventions the final character in a capital sequence will be separated because it will be
     * interprested as the first letter of a new word.
     */
    private static String[] splitAtCapitalization(String string)
    {
        Pattern capitalSequencePattern = Pattern.compile("[A-Z]+");
        Matcher matcher = capitalSequencePattern.matcher(string);
        StringBuffer sb = new StringBuffer();
        while (matcher.find())
        {
            String group = matcher.group();
            if (group.length() > 1)
            {
                group = group.substring(0,group.length()-1) + ' ' + group.substring(group.length()-1);
            }
            matcher.appendReplacement(sb, ' ' + group);
        }
        matcher.appendTail(sb);

        // split on all non-word characters: make sure we send the good parts
        return sb.toString().split("[\\W+]");
    }
}
