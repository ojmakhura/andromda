package org.andromda.maven.site;

import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.lang.StringUtils;
import org.xml.sax.SAXException;

import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

public class HighlightJavaTag
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
                    startTokenHighlight(output, getCommentClass());
                    output.write("//");
                    continue;
                }
                else if (token.equals("*"))
                {
                    inPreComment = false;
                    inBlockComment = true;
                    startTokenHighlight(output, getCommentClass());
                    output.write("/*");
                    continue;
                }
                else
                {
                    output.write("/");
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
                if (token.equals("/"))
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
                output.write(token);
                if (token.equals("\"") || token.equals("\'"))
                {
                    inString = false;
                    endTokenHighlight(output);
                }
            }
            else if (inBlockComment == false && inLineComment == false)
            {
                if (JAVA_KEYWORDS.contains(token))
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
                    if (token.equals("\"") || token.equals("\'"))
                    {
                        startTokenHighlight(output, getStringLiteralClass());
                        output.write(token);
                        inString = true;
                    }
                    else if (token.equals("/"))
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
    }

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

}
