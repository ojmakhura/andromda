package org.andromda.maven.site;

import org.apache.commons.jelly.XMLOutput;
import org.xml.sax.SAXException;

import java.util.StringTokenizer;

public class HighlightXmlTag
        extends AbstractHighlightTag
{
    private String elementClass = null;
    private String attributeClass = null;
    private String literalClass = null;
    private String cdataClass = null;
    private String commentClass = null;

    protected void highlight(XMLOutput output, String text) throws SAXException
    {
        boolean inElement = false;
        boolean inCdata = false;
        boolean inComment = false;
        boolean inString = false;
        boolean inAttribute = false;

        boolean wroteOpeningCharacter = false;
        boolean commentsAwaitClosure = false; // flagged when the '>' needs to be in comment style

        StringTokenizer tokenizer = new StringTokenizer(text, " \n\r\t\f<>\"\'=", true);
        while (tokenizer.hasMoreTokens())
        {
            String token = tokenizer.nextToken();

            if (inCdata)
            {
                output.write(token);

                if (token.indexOf("]]") > -1)
                {
                    commentsAwaitClosure = true;
                    inCdata = false;
                }
            }
            else if (inComment)
            {
                output.write(token);

                if (token.indexOf("--") > -1)
                {
                    commentsAwaitClosure = true;
                    inComment = false;
                }
            }
            else if (inString)
            {
                output.write(token);

                if (token.equals("\"") || token.equals("\'"))
                {
                    endTokenHighlight(output);
                    inString = false;
                    startTokenHighlight(output, getAttributeClass());
                }
            }
            else if (inAttribute)
            {
                if (token.equals("\"") || token.equals("\'"))
                {
                    endTokenHighlight(output);
                    startTokenHighlight(output, getLiteralClass());
                    output.write(token);
                    inString = true;
                }
                else if (token.equals(">"))
                {

                    endTokenHighlight(output);
                    output.write(token);
                    inElement = false;
                    inAttribute = false;
                }
                else
                {
                    output.write(token);
                }
            }
            else if (inElement)
            {
                if (inAttribute == false && (token.equals(" ") || token.equals("\t") || token.equals("\f") ||
                        token.equals("\n") || token.equals("\r")))
                {
                    endTokenHighlight(output);
                    startTokenHighlight(output, getAttributeClass());
                    output.write(token);
                    inAttribute = true;
                }
                else if (token.equals(">"))
                {
                    endTokenHighlight(output);
                    output.write(token);
                    inElement = false;
                }
                else if (token.startsWith("![CDATA["))
                {
                    inCdata = true;
                    inElement = false;
                    startTokenHighlight(output, getCdataClass());
                    output.write("<");
                    output.write(token);
                }
                else if (token.startsWith("!--"))
                {
                    inComment = true;
                    inElement = false;
                    startTokenHighlight(output, getCommentClass());
                    output.write("<");
                    output.write(token);
                }
                else
                {
                    if (wroteOpeningCharacter == false)
                    {
                        output.write("<");
                        wroteOpeningCharacter = true;
                    }
                    startTokenHighlight(output, getElementClass());
                    output.write(token);
                }
            }
            else
            {
                if (token.equals("<"))
                {
                    inElement = true;
                    wroteOpeningCharacter = false;
                }
                else if (token.equals(">"))
                {
                    output.write(token);
                    if (commentsAwaitClosure)
                    {
                        commentsAwaitClosure = false;
                        endTokenHighlight(output);
                    }
                }
                else
                {
                    output.write(token);
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

    public String getCdataClass()
    {
        return cdataClass;
    }

    public void setCdataClass(String cdataClass)
    {
        this.cdataClass = cdataClass;
    }

    public String getLiteralClass()
    {
        return literalClass;
    }

    public void setLiteralClass(String literalClass)
    {
        this.literalClass = literalClass;
    }

    public String getAttributeClass()
    {
        return attributeClass;
    }

    public void setAttributeClass(String attributeClass)
    {
        this.attributeClass = attributeClass;
    }

    public String getElementClass()
    {
        return elementClass;
    }

    public void setElementClass(String elementClass)
    {
        this.elementClass = elementClass;
    }

}
