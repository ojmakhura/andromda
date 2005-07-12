package org.andromda.core.common;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;
import org.apache.commons.lang.WordUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * A utility object for doing string manipulation operations that are commonly needed by the code generation templates.
 *
 * @author Matthias Bohlen
 * @author Chris Shaw
 * @author Chad Brandon
 * @author Wouter Zoons
 */
public class StringUtilsHelper
        extends StringUtils
{
    /**
     * <p/>
     * Replaces a given suffix of the source string with a new one. If the suffix isn't present, the string is returned
     * unmodified. </p>
     *
     * @param src       the <code>String</code> for which the suffix should be replaced
     * @param suffixOld a <code>String</code> with the suffix that should be replaced
     * @param suffixNew a <code>String</code> with the new suffix
     * @return a <code>String</code> with the given suffix replaced or unmodified if the suffix isn't present
     */
    public static String replaceSuffix(final String src, final String suffixOld, final String suffixNew)
    {
        if (src.endsWith(suffixOld))
        {
            return src.substring(0, src.length() - suffixOld.length()) + suffixNew;
        }
        return src;
    }

    /**
     * <p/>
     * Returns the argument string as a camel cased name beginning with an uppercased letter. </p>
     * <p/>
     * Non word characters be removed and the letter following such a character will be uppercased. </p>
     *
     * @param string any string
     * @return the string converted to a camel cased name beginning with a lower cased letter.
     */
    public static String upperCamelCaseName(final String string)
    {
        if (StringUtils.isEmpty(string))
        {
            return string;
        }

        final String[] parts = splitAtNonWordCharacters(string);
        final StringBuffer conversionBuffer = new StringBuffer();
        for (int i = 0; i < parts.length; i++)
        {
            if (parts[i].length() < 2)
            {
                conversionBuffer.append(parts[i].toUpperCase());
            }
            else
            {
                conversionBuffer.append(parts[i].substring(0, 1).toUpperCase());
                conversionBuffer.append(parts[i].substring(1));
            }
        }
        return conversionBuffer.toString();
    }

    /**
     * Removes the last occurance of the oldValue found within the string.
     *
     * @param string the String to remove the <code>value</code> from.
     * @param value  the value to remove.
     * @return String the resulting string.
     */
    public static String removeLastOccurrence(String string, final String value)
    {
        if (string != null && value != null)
        {
            StringBuffer buf = new StringBuffer();
            int index = string.lastIndexOf(value);
            if (index != -1)
            {
                buf.append(string.substring(0, index));
                buf.append(string.substring(
                        index + value.length(),
                        string.length()));
                string = buf.toString();
            }
        }
        return string;
    }

    /**
     * <p/>
     * Returns the argument string as a camel cased name beginning with a lowercased letter. </p>
     * <p/>
     * Non word characters be removed and the letter following such a character will be uppercased. </p>
     *
     * @param string any string
     * @return the string converted to a camel cased name beginning with a lower cased letter.
     */
    public static String lowerCamelCaseName(final String string)
    {
        return uncapitalize(upperCamelCaseName(string));
    }

    /**
     * Converts the argument into a web file name, this means: all lowercase characters and words are separated with
     * dashes.
     *
     * @param string any string
     * @return the string converted to a value that would be well-suited for a web file name
     */
    public static String toWebFileName(final String string)
    {
        return separate(string, "-").toLowerCase();
    }

    /**
     * Converts the argument into a message key in a properties resource bundle, all lowercase characters, words are
     * separated by dots.
     *
     * @param string any string
     * @return the string converted to a value that would be well-suited for a message key
     */
    public static String toResourceMessageKey(final String string)
    {
        return separate(
                StringUtils.trimToEmpty(string),
                ".").toLowerCase();
    }

    /**
     * Converts into a string suitable as a human readable phrase, First character is uppercase (the rest is left
     * unchanged), words are separated by a space.
     *
     * @param string any string
     * @return the string converted to a value that would be well-suited for a human readable phrase
     */
    public static String toPhrase(final String string)
    {
        return capitalize(separate(string, " "));
    }

    /**
     * Converts the argument to lowercase, removes all non-word characters, and replaces each of those sequences by the
     * separator.
     */
    public static String separate(final String string, final String separator)
    {
        if (StringUtils.isBlank(string))
        {
            return string;
        }

        final String[] parts = splitAtNonWordCharacters(string);
        final StringBuffer buffer = new StringBuffer();

        for (int i = 0; i < parts.length - 1; i++)
        {
            if (parts[i].trim().length() > 0)
            {
                buffer.append(parts[i]).append(separator);
            }
        }
        return buffer.append(parts[parts.length - 1]).toString();
    }

    /**
     * Splits at each sequence of non-word characters. <p/>Sequences of capitals will be left untouched.
     */
    private static String[] splitAtNonWordCharacters(final String string)
    {
        Pattern capitalSequencePattern = Pattern.compile("[A-Z]+");
        Matcher matcher = capitalSequencePattern.matcher(StringUtils.trimToEmpty(string));
        StringBuffer sb = new StringBuffer();
        while (matcher.find())
        {
            matcher.appendReplacement(sb, ' ' + matcher.group());
        }
        matcher.appendTail(sb);

        // split on all non-word characters: make sure we send the good parts
        return sb.toString().split("[\\W+]");
    }

    /**
     * Suffixes each line with the argument suffix.
     *
     * @param multiLines A String, optionally containing many lines
     * @param suffix     The suffix to append to the end of each line
     * @return String The input String with the suffix appended at the end of each line
     */
    public static String suffixLines(final String multiLines, final String suffix)
    {
        final String[] lines = StringUtils.trimToEmpty(multiLines).split("\n");
        final StringBuffer linesBuffer = new StringBuffer();
        for (int i = 0; i < lines.length; i++)
        {
            String line = lines[i];
            linesBuffer.append(line);
            linesBuffer.append(suffix);
            linesBuffer.append("\n");
        }
        return linesBuffer.toString();
    }

    /**
     * Converts any multi-line String into a version that is suitable to be included as-is in properties resource
     * bundle.
     *
     * @param multiLines A String, optionally containing many lines
     * @return String The input String with a backslash appended at the end of each line, or <code>null</code> if the
     *         input String was blank.
     */
    public static String toResourceMessage(String multiLines)
    {
        String resourceMessage = null;

        if (StringUtils.isNotBlank(multiLines))
        {
            final String suffix = "\\";
            multiLines = suffixLines(multiLines, ' ' + suffix).trim();
            while (multiLines.endsWith(suffix))
            {
                multiLines = multiLines.substring(
                        0,
                        multiLines.lastIndexOf(suffix)).trim();
            }
            resourceMessage = multiLines;
        }

        return resourceMessage;
    }

    /**
     * Takes an english word as input and prefixes it with 'a ' or 'an ' depending on the first character of the
     * argument String.
     * <p/>
     * The characters 'a', 'e', 'i' and 'o' will yield the 'an' predicate while all the others will yield the 'a'
     * predicate. </p>
     *
     * @param word the word needing the predicate
     * @return the argument prefixed with the predicate
     * @todo: this method could be implemented with better logic, for example to support 'an r' and 'a rattlesnake'
     */
    public static String prefixWithAPredicate(final String word)
    {
        final StringBuffer formattedBuffer = new StringBuffer();

        formattedBuffer.append("a ");
        formattedBuffer.append(word);

        char firstChar = word.charAt(0);
        switch (firstChar)
        {
            case 'a': // fall-through
            case 'e': // fall-through
            case 'i': // fall-through
            case 'o':
                formattedBuffer.insert(1, 'n');
                break;
            default:
        }

        return formattedBuffer.toString();
    }

    /**
     * Converts multiline text into a single line, normalizing whitespace in the process. This means whitespace
     * characters will not follow each other directly.
     * <p/>
     * The resulting String will be trimmed. <p/>
     * <p/>
     * If the input String is null the return value will be an empty string. </p>
     *
     * @param string A String, may be null
     * @return The argument in a single line
     */
    public static String toSingleLine(String string)
    {
        // remove anything that is greater than 1 space.
        return (string == null) ? "" : string.replaceAll("[$\\s]+", " ").trim();
    }

    /**
     * Linguistically pluralizes a singular noun. <p/> <ul> <li><code>noun</code> becomes <code>nouns</code></li>
     * <li><code>key</code> becomes <code>keys</code></li> <li><code>word</code> becomes <code>words</code></li>
     * <li><code>property</code> becomes <code>properties</code></li> <li><code>bus</code> becomes
     * <code>busses</code></li> <li><code>boss</code> becomes <code>bosses</code></li> </ul>
     * <p/>
     * Whitespace as well as <code>null</code> arguments will return an empty String. </p>
     *
     * @param singularNoun A singular noun to pluralize
     * @return The plural of the argument singularNoun
     */
    public static String pluralize(final String singularNoun)
    {
        String pluralNoun = trimToEmpty(singularNoun);

        int nounLength = pluralNoun.length();

        if (nounLength == 1)
        {
            pluralNoun = pluralNoun + 's';
        }
        else if (nounLength > 1)
        {
            char secondToLastChar = pluralNoun.charAt(nounLength - 2);

            if (pluralNoun.endsWith("y"))
            {
                switch (secondToLastChar)
                {
                    case 'a': // fall-through
                    case 'e': // fall-through
                    case 'i': // fall-through
                    case 'o': // fall-through
                    case 'u':
                        pluralNoun = pluralNoun + 's';
                        break;
                    default:
                        pluralNoun = pluralNoun.substring(0, nounLength - 1) + "ies";
                }
            }
            else if (pluralNoun.endsWith("s"))
            {
                switch (secondToLastChar)
                {
                    case 's':
                        pluralNoun = pluralNoun + "es";
                        break;
                    default:
                        pluralNoun = pluralNoun + "ses";
                }
            }
            else
            {
                pluralNoun = pluralNoun + 's';
            }
        }
        return pluralNoun;
    }

    /**
     * Formats the argument string without any indentiation, the text will be wrapped at the default column.
     *
     * @see #format(String, String)
     */
    public static String format(String plainText)
    {
        return format(plainText, "");
    }

    /**
     * Formats the given argument with the specified indentiation, wrapping the text at a 64 column margin.
     *
     * @see #format(String, String, int)
     */
    public static String format(String plainText, String indentation)
    {
        return format(plainText, indentation, 64);
    }

    /**
     * Formats the given argument with the specified indentiation, wrapping the text at the desired column margin.
     * The returned String will not be suited for display in HTML environments.
     *
     * @see #format(String, String, int, boolean)
     */
    public static String format(String plainText, String indentation, int wrapAtColumn)
    {
        return format(plainText, indentation, wrapAtColumn, true);
    }

    /**
     * Formats the given argument with the specified indentation, wrapping the text at the desired column margin.
     * <p/>
     * The returned text will be suitable for display in HTML environments such as JavaDoc, all newlines will be
     * replaced by breaks.
     * <p/>
     * This method does <em>not</em> trim the input text.
     * <p/>
     * <strong>Please note:</strong> <em>the first line will not be indented</em>
     *
     * @param the          text to format, the empty string will be returned in case this argument is <code>null</code>
     * @param indentation  the empty string will be used if this argument would be <code>null</code>
     * @param wrapAtColumn does not take into account the length of the indentation
     * @param htmlStyle    whether or not to make sure the returned string is suited for display in HTML environments
     *                     such as JavaDoc
     * @return a String instance which represents the formatted input, if case indentiation was specified the first
     *         line of this returned value will not be affected by it
     */
    public static String format(String plainText, String indentation, int wrapAtColumn, boolean htmlStyle)
    {
        String format = null;

        // if the text is blank we do nothing
        if (StringUtils.isEmpty(plainText))
        {
            format = "";
        }
        else
        {
            if (indentation == null)
            {
                indentation = "";
            }

            final StringBuffer buffer = new StringBuffer();

            try
            {
                final BufferedReader reader =
                        new BufferedReader(new StringReader(WordUtils.wrap(plainText, wrapAtColumn, null, false)));
                String line = reader.readLine();
                while (line != null)
                {
                    buffer.append(indentation);
                    buffer.append(line);

                    line = reader.readLine();

                    // only do a newline when there's actually coming more stuff
                    if (line != null)
                    {
                        buffer.append(SystemUtils.LINE_SEPARATOR);
                        if (htmlStyle)
                        {
                            buffer.append("<p/>");
                            buffer.append(SystemUtils.LINE_SEPARATOR);
                        }
                    }
                }
                reader.close();
            }
            catch (IOException ioException)
            {
                // do nothing, we'll simply return the contents of the buffer
            }

            format = buffer.toString();
        }
        return format;
    }
}