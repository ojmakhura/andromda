package org.andromda.maven.site;

import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.lang.StringUtils;
import org.xml.sax.SAXException;

import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

public class HighlightJavaTag extends AbstractHighlightTag
{
    private String keywordClass = null;
    private String literalClass = null;
    private String numericClass = null;

    public String getKeywordClass()
    {
        return keywordClass;
    }

    public void setKeywordClass(String keywordClass)
    {
        this.keywordClass = keywordClass;
    }

    public String getLiteralClass()
    {
        return literalClass;
    }

    public void setLiteralClass(String literalClass)
    {
        this.literalClass = literalClass;
    }

    public String getNumericClass()
    {
        return numericClass;
    }

    public void setNumericClass(String numericClass)
    {
        this.numericClass = numericClass;
    }

    protected void highlight(XMLOutput output, String text) throws SAXException
    {
        boolean inString = false;
        boolean inChar = false;
        StringTokenizer tokenizer = new StringTokenizer(text, " \n\r\t\f\"\'(){}[].,;:?!+-*/%^&|<>=~", true);
        while (tokenizer.hasMoreTokens())
        {
            String token = tokenizer.nextToken();

            if (inString)
            {
                startTokenHighlight(output, getLiteralClass());
                output.write(token);
                endTokenHighlight(output);
                if (token.equals("\""))
                {
                    inString = false;
                }
            }
            else if (inChar)
            {
                startTokenHighlight(output, getLiteralClass());
                output.write(token);
                endTokenHighlight(output);
                if (token.equals("\'"))
                {
                    inChar = false;
                }
            }
            else if (JAVA_KEYWORDS.contains(token))
            {
                startTokenHighlight(output, getKeywordClass());
                output.write(token);
                endTokenHighlight(output);
            }
            else if (StringUtils.isNumeric(token))
            {
                startTokenHighlight(output, getNumericClass());
                output.write(token);
                endTokenHighlight(output);
            }
            else
            {
                if (token.equals("\""))
                {
                    startTokenHighlight(output, getLiteralClass());
                    output.write(token);
                    endTokenHighlight(output);
                    inString = true;
                }
                else if (token.equals("\'"))
                {
                    startTokenHighlight(output, getLiteralClass());
                    output.write(token);
                    endTokenHighlight(output);
                    inChar = true;
                }
                else
                {
                    output.write(token);
                }
            }
        }
    }

    private final static Set JAVA_KEYWORDS = new HashSet(50,1);
    static
    {
        JAVA_KEYWORDS.add("abstract"); JAVA_KEYWORDS.add("boolean"); JAVA_KEYWORDS.add("break");
        JAVA_KEYWORDS.add("byte"); JAVA_KEYWORDS.add("case"); JAVA_KEYWORDS.add("catch");
        JAVA_KEYWORDS.add("char"); JAVA_KEYWORDS.add("class"); JAVA_KEYWORDS.add("const");
        JAVA_KEYWORDS.add("continue"); JAVA_KEYWORDS.add("default"); JAVA_KEYWORDS.add("do");
        JAVA_KEYWORDS.add("double"); JAVA_KEYWORDS.add("else"); JAVA_KEYWORDS.add("enum");
        JAVA_KEYWORDS.add("extends"); JAVA_KEYWORDS.add("final"); JAVA_KEYWORDS.add("finally");
        JAVA_KEYWORDS.add("float"); JAVA_KEYWORDS.add("for"); JAVA_KEYWORDS.add("goto");
        JAVA_KEYWORDS.add("if"); JAVA_KEYWORDS.add("implements"); JAVA_KEYWORDS.add("import");
        JAVA_KEYWORDS.add("instanceof"); JAVA_KEYWORDS.add("int"); JAVA_KEYWORDS.add("interface");
        JAVA_KEYWORDS.add("long"); JAVA_KEYWORDS.add("native"); JAVA_KEYWORDS.add("new");
        JAVA_KEYWORDS.add("package"); JAVA_KEYWORDS.add("private"); JAVA_KEYWORDS.add("protected");
        JAVA_KEYWORDS.add("public"); JAVA_KEYWORDS.add("return"); JAVA_KEYWORDS.add("short");
        JAVA_KEYWORDS.add("static"); JAVA_KEYWORDS.add("strictfp"); JAVA_KEYWORDS.add("super");
        JAVA_KEYWORDS.add("switch"); JAVA_KEYWORDS.add("synchronized"); JAVA_KEYWORDS.add("this");
        JAVA_KEYWORDS.add("throw"); JAVA_KEYWORDS.add("throws"); JAVA_KEYWORDS.add("transient");
        JAVA_KEYWORDS.add("try"); JAVA_KEYWORDS.add("void"); JAVA_KEYWORDS.add("volatile");
        JAVA_KEYWORDS.add("while");

        // these are not Java language keywords, but we want them highlighted anyway
        JAVA_KEYWORDS.add("null");
    }

}
