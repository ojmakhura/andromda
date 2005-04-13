package org.andromda.maven.site;

import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.TagSupport;
import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.lang.StringEscapeUtils;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.XMLWriter;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Collection;
import java.util.Iterator;

public abstract class AbstractHighlightTag
        extends TagSupport
{
    private String value = null;

    protected abstract void highlight(XMLOutput output, String text) throws SAXException;

    public void doTag(XMLOutput xmlOutput) throws MissingAttributeException, JellyTagException
    {
        Object content = context.getVariable(getValue());

        if (content instanceof Collection)
        {
            final Collection collection = (Collection)content;
            if (collection != null && !collection.isEmpty())
            {
                content = collection.iterator().next();
            }
        }

        if (content instanceof Element)
        {
            try
            {
                String bodyText = getSourceCode((Element)content);
                highlight(xmlOutput, StringEscapeUtils.unescapeXml(bodyText));
            }
            catch (SAXException e)
            {
                throw new JellyTagException("Unable to print out highlighted body text: " + e);
            }
        }
        else
        {
            throw new JellyTagException("Not an XML element: " + getValue());
        }
    }

    private String getSourceCode(Element element) throws JellyTagException
    {
        StringWriter stringWriter = new StringWriter();

        try
        {
            XMLWriter xmlWriter = new XMLWriter(stringWriter);

            for (Iterator iterator = element.nodeIterator(); iterator.hasNext();)
            {
                Node childElement = (Node)iterator.next();
                xmlWriter.write(childElement.getText());
            }

            xmlWriter.flush();
            xmlWriter.close();
        }
        catch (IOException ioe)
        {
            throw new JellyTagException("Unable parse source for syntax highlighting: ");
        }

        return stringWriter.getBuffer().toString();
    }

    protected void startTokenHighlight(XMLOutput output, String cssClass) throws SAXException
    {
        if (cssClass != null)
        {
            AttributesImpl attributes = new AttributesImpl();
            attributes.addAttribute("", "class", "class", "string", cssClass);
            output.startElement("div", attributes);
        }
    }

    protected void endTokenHighlight(XMLOutput output) throws SAXException
    {
        output.endElement("div");
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }
}
