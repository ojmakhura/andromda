package org.andromda.maven.site;

import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.lang.StringUtils;
import org.xml.sax.SAXException;

import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

public class HighlightVelocityTag
        extends AbstractHighlightTag
{
    private String keywordClass = null;
    private String stringLiteralClass = null;
    private String numericLiteralClass = null;
    private String commentClass = null;

    public String getCommentClass()
    {
        return commentClass;
    }

    public void setCommentClass(String commentClass)
    {
        this.commentClass = commentClass;
    }

    public String getKeywordClass()
    {
        return keywordClass;
    }

    public void setKeywordClass(String keywordClass)
    {
        this.keywordClass = keywordClass;
    }

    public String getStringLiteralClass()
    {
        return stringLiteralClass;
    }

    public void setStringLiteralClass(String stringLiteralClass)
    {
        this.stringLiteralClass = stringLiteralClass;
    }

    public String getNumericLiteralClass()
    {
        return numericLiteralClass;
    }

    public void setNumericLiteralClass(String numericLiteralClass)
    {
        this.numericLiteralClass = numericLiteralClass;
    }

    protected void highlight(XMLOutput output, String text) throws SAXException
    {
        boolean inPreComment = false;   // first #
        boolean inLineComment = false;
        boolean inBlockComment = false;
        boolean inPostComment = false;  // asterisk of closing */

        boolean inString = false;
        boolean inChar = false;
        StringTokenizer tokenizer = new StringTokenizer(text, " \n\r\t\f\"\'(){}[].,;:?!+-*/%^&|<>=~#", true);
        while (tokenizer.hasMoreTokens())
        {
            String token = tokenizer.nextToken();

            if (inPreComment)
            {
                if (token.equals("#"))
                {
                    inPreComment = false;
                    inLineComment = true;
                    startTokenHighlight(output, getCommentClass());
                    output.write("##");
                    continue;
                }
                else if (token.equals("*"))
                {
                    inPreComment = false;
                    inBlockComment = true;
                    startTokenHighlight(output, getCommentClass());
                    output.write("#*");
                    continue;
                }
                else if (VTL_KEYWORDS.contains(token))
                {
                    startTokenHighlight(output, getKeywordClass());
                    output.write("#");
                    output.write(token);
                    endTokenHighlight(output);
                    inPreComment = false;
                    continue;
                }
                else
                {
                    output.write("#");
                    inPreComment = false;
                }
            }
            else if (inLineComment)
            {
                if (token.equals("\n"))
                {
                    endTokenHighlight(output);
                    inLineComment = false;
                }
                else
                {
                    output.write(token);
                    continue;
                }
            }
            else if (inBlockComment)
            {
                output.write(token);
                if (token.equals("*"))
                {
                    inBlockComment = false;
                    inPostComment = true;
                }
                continue;
            }
            else if (inPostComment)
            {
                output.write(token);
                if (token.equals("#"))
                {
                    endTokenHighlight(output);
                    inPostComment = false;
                }
                else if (token.equals("*") == false)
                {
                    inPostComment = false;
                    inBlockComment = true;
                }
                continue;
            }

            if (inString)
            {
                startTokenHighlight(output, getStringLiteralClass());
                output.write(token);
                endTokenHighlight(output);
                if (token.equals("\""))
                {
                    inString = false;
                }
            }
            else if (inChar)
            {
                startTokenHighlight(output, getStringLiteralClass());
                output.write(token);
                endTokenHighlight(output);
                if (token.equals("\'"))
                {
                    inChar = false;
                }
            }
            else if (VTL_KEYWORDS.contains(token))
            {
                startTokenHighlight(output, getKeywordClass());
                output.write(token);
                endTokenHighlight(output);
            }
            else if (StringUtils.isNumeric(token))
            {
                startTokenHighlight(output, getNumericLiteralClass());
                output.write(token);
                endTokenHighlight(output);
            }
            else
            {
                if (token.equals("\""))
                {
                    startTokenHighlight(output, getStringLiteralClass());
                    output.write(token);
                    endTokenHighlight(output);
                    inString = true;
                }
                else if (token.equals("\'"))
                {
                    startTokenHighlight(output, getStringLiteralClass());
                    output.write(token);
                    endTokenHighlight(output);
                    inChar = true;
                }
                else if (token.equals("#"))
                {
                    inPreComment = true;
                }
                else
                {
                    output.write(token);
                }
            }
        }
    }

    private final static Set VTL_KEYWORDS = new HashSet(11, 1);

    static
    {
        VTL_KEYWORDS.add("set");
        VTL_KEYWORDS.add("foreach");
        VTL_KEYWORDS.add("end");
        VTL_KEYWORDS.add("if");
        VTL_KEYWORDS.add("elseif");
        VTL_KEYWORDS.add("else");
        VTL_KEYWORDS.add("parse");
        VTL_KEYWORDS.add("macro");

        // these are not Java language keywords, but we want them highlighted anyway
        VTL_KEYWORDS.add("false");
        VTL_KEYWORDS.add("in");
        VTL_KEYWORDS.add("true");
    }

}
