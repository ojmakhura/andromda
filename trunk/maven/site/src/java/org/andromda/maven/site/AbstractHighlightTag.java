package org.andromda.maven.site;

import org.apache.commons.jelly.TagSupport;
import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.lang.StringEscapeUtils;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

public abstract class AbstractHighlightTag extends TagSupport
{
    protected abstract void highlight(XMLOutput output, String text) throws SAXException;

    public void doTag(XMLOutput xmlOutput) throws MissingAttributeException, JellyTagException
    {
        final String bodyText = getBodyText();

        try
        {
            highlight(xmlOutput, StringEscapeUtils.unescapeXml(bodyText));
        }
        catch (SAXException e)
        {
            throw new JellyTagException("Unable to print out highlighted body text: "+e);
        }
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
}
