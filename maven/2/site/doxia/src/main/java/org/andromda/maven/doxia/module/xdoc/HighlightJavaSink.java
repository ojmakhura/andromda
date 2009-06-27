package org.andromda.maven.doxia.module.xdoc;

import org.codehaus.plexus.util.StringUtils;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * Support Java code syntax highlighting with the Andromdadoc Doxia Module
 *
 */
public class HighlightJavaSink
{
    private static final String CSS_KEYWORD_CLASS = "jk";
    private static final String CSS_STRING_LITERAL_CLASS = "jl";
    private static final String CSS_NUMERIC_LITERAL_CLASS ="jn";
    private static final String CSS_COMMENT_CLASS = "jc";
    
    private String keywordClass = CSS_KEYWORD_CLASS;
    private String stringLiteralClass = CSS_STRING_LITERAL_CLASS;
    private String numericLiteralClass = CSS_NUMERIC_LITERAL_CLASS;
    private String commentClass = CSS_COMMENT_CLASS;

    private final static Set JAVA_KEYWORDS = new HashSet(52, 1);

    static
    {
        JAVA_KEYWORDS.add("abstract");
        JAVA_KEYWORDS.add("boolean");
        JAVA_KEYWORDS.add("break");
        JAVA_KEYWORDS.add("byte");
        JAVA_KEYWORDS.add("case");
        JAVA_KEYWORDS.add("catch");
        JAVA_KEYWORDS.add("char");
        JAVA_KEYWORDS.add("class");
        JAVA_KEYWORDS.add("const");
        JAVA_KEYWORDS.add("continue");
        JAVA_KEYWORDS.add("default");
        JAVA_KEYWORDS.add("do");
        JAVA_KEYWORDS.add("double");
        JAVA_KEYWORDS.add("else");
        JAVA_KEYWORDS.add("enum");
        JAVA_KEYWORDS.add("extends");
        JAVA_KEYWORDS.add("final");
        JAVA_KEYWORDS.add("finally");
        JAVA_KEYWORDS.add("float");
        JAVA_KEYWORDS.add("for");
        JAVA_KEYWORDS.add("goto");
        JAVA_KEYWORDS.add("if");
        JAVA_KEYWORDS.add("implements");
        JAVA_KEYWORDS.add("import");
        JAVA_KEYWORDS.add("instanceof");
        JAVA_KEYWORDS.add("int");
        JAVA_KEYWORDS.add("interface");
        JAVA_KEYWORDS.add("long");
        JAVA_KEYWORDS.add("native");
        JAVA_KEYWORDS.add("new");
        JAVA_KEYWORDS.add("package");
        JAVA_KEYWORDS.add("private");
        JAVA_KEYWORDS.add("protected");
        JAVA_KEYWORDS.add("public");
        JAVA_KEYWORDS.add("return");
        JAVA_KEYWORDS.add("short");
        JAVA_KEYWORDS.add("static");
        JAVA_KEYWORDS.add("strictfp");
        JAVA_KEYWORDS.add("super");
        JAVA_KEYWORDS.add("switch");
        JAVA_KEYWORDS.add("synchronized");
        JAVA_KEYWORDS.add("this");
        JAVA_KEYWORDS.add("throw");
        JAVA_KEYWORDS.add("throws");
        JAVA_KEYWORDS.add("transient");
        JAVA_KEYWORDS.add("try");
        JAVA_KEYWORDS.add("void");
        JAVA_KEYWORDS.add("volatile");
        JAVA_KEYWORDS.add("while");

        // these are not Java language keywords, but we want them highlighted anyway
        JAVA_KEYWORDS.add("null");
        JAVA_KEYWORDS.add("false");
        JAVA_KEYWORDS.add("true");
    }
    
    public HighlightJavaSink()
    {
        // empty implementation
    }
    
    protected void highlight(AndromdadocSink sink, String text) 
        throws Exception
    {
        boolean inPreComment = false;   // first slash
        boolean inLineComment = false;
        boolean inBlockComment = false;
        boolean inPostComment = false;  // asterisk of closing */
        boolean inString = false;

        StringTokenizer tokenizer = new StringTokenizer(text, " \n\r\t\f\"\'(){}[].,;:?!+-*/%^&|<>=~", true);
        while (tokenizer.hasMoreTokens())
        {
            String token = tokenizer.nextToken();

            if (inPreComment)
            {
                if (token.equals("/"))
                {
                    inPreComment = false;
                    inLineComment = true;
                    sink.div(getCommentClass());
                    sink.content("//");
                    continue;
                }
                else if (token.equals("*"))
                {
                    inPreComment = false;
                    inBlockComment = true;
                    sink.div(getCommentClass());
                    sink.content("/*");
                    continue;
                }
                else
                {
                    sink.content("/");
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
                if (token.equals("/"))
                {
                    sink.div_();
                    inPostComment = false;
                }
                else if (!token.equals("*"))
                {
                    inPostComment = false;
                    inBlockComment = true;
                }
                continue;
            }

            if (inString)
            {
                sink.content(token);
                if (token.equals("\"") || token.equals("\'"))
                {
                    inString = false;
                    sink.div_();
                }
            }
            else
            {
                if (JAVA_KEYWORDS.contains(token))
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
                    if (token.equals("\"") || token.equals("\'"))
                    {
                        sink.div(getStringLiteralClass());
                        sink.content(token);
                        inString = true;
                    }
                    else if (token.equals("/"))
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
