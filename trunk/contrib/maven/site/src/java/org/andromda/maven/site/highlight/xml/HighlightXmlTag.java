package org.andromda.maven.site.highlight.xml;

import org.andromda.maven.site.highlight.AbstractHighlightTag;

import java.io.StringReader;

public class HighlightXmlTag extends AbstractHighlightTag implements XmlHighlightStyles
{
    private String elementClass = null;
    private String attributeClass = null;
    private String literalClass = null;
    private String cdataClass = null;
    private String commentClass = null;

    protected void highlight(String text)
    {
        XmlHighlighter highlighter = new XmlHighlighter(this);
        highlighter.highlight(new StringReader(text), getWriter());
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
