package org.andromda.maven.doxia.module.xdoc;

import java.util.StringTokenizer;

/**
 * Support XML syntax highlighting with the Andromdadoc Doxia Module
 *
 */
public class HighlightXmlSink
{
    private static final String CSS_ELEMENT_CLASS = "xe";
    private static final String CSS_ATTRIBUTE_CLASS = "xa";
    private static final String CSS_LITERAL_CLASS ="xl";
    private static final String CSS_COMMENT_CLASS = "xc";
    private static final String CSS_CDATA_CLASS = "xc";
    
    private String elementClass = CSS_ELEMENT_CLASS;
    private String attributeClass = CSS_ATTRIBUTE_CLASS;
    private String literalClass = CSS_LITERAL_CLASS;
    private String cdataClass = CSS_CDATA_CLASS;
    private String commentClass = CSS_COMMENT_CLASS;

    public HighlightXmlSink()
    {
        // empty implementation
    }
    
    public HighlightXmlSink(
            String elementClass,
            String attributeClass,
            String literalClass,
            String commentClass,
            String cdataClass)
    {
        this.setElementClass(elementClass);
        this.setAttributeClass(attributeClass);
        this.setLiteralClass(literalClass);
        this.setCommentClass(commentClass);
        this.setCdataClass(cdataClass);
    }
    
    protected void highlight(AndromdadocSink sink, String text) 
        throws Exception
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
                sink.markup(token);

                if (token.indexOf("]]") > -1)
                {
                    commentsAwaitClosure = true;
                    inCdata = false;
                }
            }
            else if (inComment)
            {
                sink.content(token);

                if (token.indexOf("--") > -1)
                {
                    commentsAwaitClosure = true;
                    inComment = false;
                }
            }
            else if (inString)
            {
                sink.content(token);

                if (token.equals("\"") || token.equals("\'"))
                {
                    sink.div_();
                    inString = false;
                    sink.div(getAttributeClass());
                }
            }
            else if (inAttribute)
            {
                if (token.equals("\"") || token.equals("\'"))
                {
                    sink.div_();
                    sink.div(getLiteralClass());
                    sink.markup(token);
                    inString = true;
                }
                else if (token.equals(">"))
                {

                    sink.div_();
                    sink.content(token);
                    inElement = false;
                    inAttribute = false;
                }
                else
                {
                    sink.markup(token);
                }
            }
            else if (inElement)
            {
                if (inAttribute == false && 
                        (token.equals(" ") || token.equals("\t") || token.equals("\f") ||
                        token.equals("\n") || token.equals("\r")))
                {
                    sink.div_();
                    sink.div(getAttributeClass());
                    sink.markup(token);
                    inAttribute = true;
                }
                else if (token.equals(">"))
                {
                    sink.div_();
                    sink.content(token);
                    inElement = false;
                }
                else if (token.startsWith("![CDATA["))
                {
                    inCdata = true;
                    inElement = false;
                    sink.div(getCdataClass());
                    sink.content("<");
                    sink.markup(token);
                }
                else if (token.startsWith("!--"))
                {
                    inComment = true;
                    inElement = false;
                    sink.div(getCommentClass());
                    sink.content("<");
                    sink.content(token);
                }
                else
                {
                    if (wroteOpeningCharacter == false)
                    {
                        sink.content("<");
                        wroteOpeningCharacter = true;
                    }
                    sink.div(getElementClass());
                    sink.markup(token);
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
                    sink.content(token);
                    if (commentsAwaitClosure)
                    {
                        commentsAwaitClosure = false;
                        sink.div_();
                    }
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
