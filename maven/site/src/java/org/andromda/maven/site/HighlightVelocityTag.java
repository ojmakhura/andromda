package org.andromda.maven.site;

import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.lang.StringUtils;
import org.xml.sax.SAXException;

import java.util.Set;
import java.util.HashSet;
import java.util.StringTokenizer;

public class HighlightVelocityTag extends AbstractHighlightTag
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
            else if (VTL_KEYWORDS.contains(token))
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

    private final static Set VTL_KEYWORDS = new HashSet(10,1);
    static
    {
        VTL_KEYWORDS.add("#foreach"); VTL_KEYWORDS.add("#end"); VTL_KEYWORDS.add("#macro");
        VTL_KEYWORDS.add("#if"); VTL_KEYWORDS.add("#elseif"); VTL_KEYWORDS.add("#else");
        VTL_KEYWORDS.add("#parse");

        // these are not Java language keywords, but we want them highlighted anyway
        VTL_KEYWORDS.add("false"); VTL_KEYWORDS.add("in"); VTL_KEYWORDS.add("true");
    }

}
