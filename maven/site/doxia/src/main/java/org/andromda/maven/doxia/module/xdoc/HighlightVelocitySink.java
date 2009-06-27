package org.andromda.maven.doxia.module.xdoc;

import org.codehaus.plexus.util.StringUtils;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * Support Velocity template syntax highlighting with the Andromdadoc Doxia Module
 *
 */
public class HighlightVelocitySink
{
    private static final String CSS_KEYWORD_CLASS = "vk";
    private static final String CSS_STRING_LITERAL_CLASS = "vl";
    private static final String CSS_NUMERIC_LITERAL_CLASS ="vn";
    private static final String CSS_COMMENT_CLASS = "vc";
    
    private String keywordClass = CSS_KEYWORD_CLASS;
    private String stringLiteralClass = CSS_STRING_LITERAL_CLASS;
    private String numericLiteralClass = CSS_NUMERIC_LITERAL_CLASS;
    private String commentClass = CSS_COMMENT_CLASS;

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

    public HighlightVelocitySink()
    {
        // empty implementation
    }
    
    protected void highlight(AndromdadocSink sink, String text) 
        throws Exception
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
                    sink.div(getCommentClass());
                    sink.content("##");
                    continue;
                }
                else if (token.equals("*"))
                {
                    inPreComment = false;
                    inBlockComment = true;
                    sink.div(getCommentClass());
                    sink.content("#*");
                    continue;
                }
                else if (VTL_KEYWORDS.contains(token))
                {
                    sink.div(getKeywordClass());
                    sink.content("#");
                    sink.content(token);
                    sink.div_();
                    inPreComment = false;
                    continue;
                }
                else
                {
                    sink.content("#");
                    inPreComment = false;
                }
            }
            else if (inLineComment)
            {
                if (token.equals("\n"))
                {
                    sink.div_();
                    inLineComment = false;
                }
                else
                {
                    sink.content(token);
                    continue;
                }
            }
            else if (inBlockComment)
            {
                sink.content(token);
                if (token.equals("*"))
                {
                    inBlockComment = false;
                    inPostComment = true;
                }
                continue;
            }
            else if (inPostComment)
            {
                sink.content(token);
                if (token.equals("#"))
                {
                    sink.div_();
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
                sink.div(getStringLiteralClass());
                sink.content(token);
                sink.div_();
                if (token.equals("\""))
                {
                    inString = false;
                }
            }
            else if (inChar)
            {
                sink.div(getStringLiteralClass());
                sink.content(token);
                sink.div_();
                if (token.equals("\'"))
                {
                    inChar = false;
                }
            }
            else if (VTL_KEYWORDS.contains(token))
            {
                sink.div(getKeywordClass());
                sink.content(token);
                sink.div_();
            }
            else if (StringUtils.isNumeric(token))
            {
                sink.div(getNumericLiteralClass());
                sink.content(token);
                sink.div_();
            }
            else
            {
                if (token.equals("\""))
                {
                    sink.div(getStringLiteralClass());
                    sink.content(token);
                    sink.div_();
                    inString = true;
                }
                else if (token.equals("\'"))
                {
                    sink.div(getStringLiteralClass());
                    sink.content(token);
                    sink.div_();
                    inChar = true;
                }
                else if (token.equals("#"))
                {
                    inPreComment = true;
                }
                else
                {
                    sink.content(token);
                }
            }
        }
    }

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
}
