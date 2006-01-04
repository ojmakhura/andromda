package org.andromda.mdr.xmi.uml20.load.utils;

import java.util.HashMap;

/**
 * Helper class to solve HTML spec. characters.
 */
public class HTMLConverter
{
    private static String[] HTML_STRINGS = new String[255];
    private static String[] XML_STRINGS = new String[255];
    private static HashMap CHARACTERS = new HashMap();

    private static void addChar(char c, String s)
    {
        HTML_STRINGS[c] = s;
        CHARACTERS.put(s, new Character(c));
    }

    static
    {
        addChar('<', "&lt"); // Less than sign
        addChar('>', "&gt"); // Less than sign
        addChar('&', "&amp"); // Less than sign
        addChar('"', "&quot"); // Less than sign
        addChar('\'', "&apos"); // Less than sign

        System.arraycopy(HTML_STRINGS, 0, XML_STRINGS, 0, HTML_STRINGS.length);
        XML_STRINGS[9] = "&#9";
        XML_STRINGS[10] = "&#10";
        XML_STRINGS[13] = "&#13";
    }

    private final static char SPEC_NOTATION = '&';
    private final static char DELIMETER = ';';

    /**
     * change all spec. characters in the string to HTML notation
     * 
     * @param body String with spec. chars
     * @return String converted to HTML
     */
    public String makeHTMLstring(String body)
    {
        return makeHTMLstring(body, HTML_STRINGS);
    }

    public String makeXMLstring(String body)
    {
        return makeHTMLstring(body, XML_STRINGS);
    }

    /**
     * change all spec. characters in the string to HTML notation
     * 
     * @param body String with spec. chars
     * @param htmlStrings
     * @return String converted to HTML
     */
    public String makeHTMLstring(String body, String[] htmlStrings)
    {
        StringBuffer tmp = null;
        String subst;
        int length = body.length();
        int lastPos = 0;
        for (int i = 0; i < length; i++)
        {
            char character = body.charAt(i);
            if (character < 255)
            {
                subst = htmlStrings[character];
                if (subst != null)
                {
                    if (tmp == null)
                    {
                        tmp = new StringBuffer(length + 2);
                    }
                    tmp.append(body.substring(lastPos, i));
                    lastPos = i + 1;
                    tmp.append(subst);
                    tmp.append(DELIMETER);
                }
            }
        }
        if (lastPos > 0)
        {
            tmp.append(body.substring(lastPos, body.length()));
        }
        return tmp == null ? body : tmp.toString();
    }

    /**
     * change all HTML notations to noraml characters
     * 
     * @param body String containting HTML text
     * @return String in normal notation
     */
    public static String convertHTMLstring(String body)
    {
        if (body == null)
        {
            return "";
        }

        StringBuffer newStr = null;
        char[] bodyAsChars = null;
        int lastPos = 0;

        int length = body.length();

        try
        {
            for (int i = 0; i < length; i++)
            {
                char currentChar = body.charAt(i);
                if (currentChar == SPEC_NOTATION)
                {
                    if (bodyAsChars == null)
                    {
                        newStr = new StringBuffer(length);
                        bodyAsChars = body.toCharArray();
                    }
                    int dlm = body.indexOf(DELIMETER, i);
                    if (dlm != -1)
                    {
                        String spChar = body.substring(i, dlm);
                        Object replaceTo = CHARACTERS.get(spChar);
                        if (replaceTo != null)
                        {
                            newStr.append(bodyAsChars, lastPos, i - lastPos).append(replaceTo);
                            i = dlm;
                            lastPos = i + 1;
                        }
                    }
                }
            }
            if (newStr != null)
            {
                newStr.append(bodyAsChars, lastPos, length - lastPos);
            }
        }
        catch (Throwable o)
        {
            System.err.println("ERROR in " + body);
        }
        if (newStr == null)
        {
            return body;
        }
        return newStr.toString();
    }
}