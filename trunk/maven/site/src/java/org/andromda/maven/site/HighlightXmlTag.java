package org.andromda.maven.site;

import org.apache.commons.jelly.XMLOutput;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.IOException;
import java.io.StringReader;

public class HighlightXmlTag extends AbstractHighlightTag
{
    // used in case the input xml has no root element
    private final static String DUMMY_ELEMENT_NAME = "ANDROMDADUMMY";

    private String elementClass = null;
    private String attributeClass = null;
    private String valueClass = null;
    private String cdataClass = null;

    public String getCdataClass()
    {
        return cdataClass;
    }

    public void setCdataClass(String cdataClass)
    {
        this.cdataClass = cdataClass;
    }

    public String getValueClass()
    {
        return valueClass;
    }

    public void setValueClass(String valueClass)
    {
        this.valueClass = valueClass;
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

    protected void highlight(XMLOutput output, String text) throws SAXException
    {
        if (!text.startsWith("<?xml"))
        {
            text = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<"+
                    DUMMY_ELEMENT_NAME+" test=\"value\">" + text + "</"+DUMMY_ELEMENT_NAME+">";
        }
        
//        text = text.replaceAll("&lt;", "<").replaceAll("&gt;", ">");
/*
        if (getCdataClass() == null)
        {
            text = text.replaceAll("<!\\[CDATA\\[", "&lt;![CDATA[");
            text = text.replaceAll("\\]\\]>", "]]&gt;");
        }
        else
        {
            text = text.replaceAll("<!\\[CDATA\\[", "<div class=\""+getCdataClass()+"\">&lt;![CDATA[");
            text = text.replaceAll("\\]\\]>", "]]&gt;</div>");
        }
*/

        XMLReader reader = XMLReaderFactory.createXMLReader();
        reader.setContentHandler(new SyntaxContentHandler(output,this));
        try
        {
            reader.parse(new InputSource(new StringReader(text)));
        }
        catch (IOException e)
        {
            throw new SAXException(e);
        }
    }

    public static class SyntaxContentHandler extends DefaultHandler
    {
        private XMLOutput output = null;
        private HighlightXmlTag highlighter = null;

        public SyntaxContentHandler(XMLOutput output, HighlightXmlTag highlighter)
        {
            this.output = output;
            this.highlighter = highlighter;
        }

        public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException
        {
            if (DUMMY_ELEMENT_NAME.equals(localName)) return;

            output.write("<");
            highlighter.startTokenHighlight(output, highlighter.getElementClass());
            output.write( (qName==null) ? localName : qName );

            highlighter.endTokenHighlight(output);
            highlighter.startTokenHighlight(output, highlighter.getAttributeClass());
            for (int i=0; i<atts.getLength(); i++)
            {
                String name = (atts.getQName(i) == null) ? atts.getLocalName(i) : atts.getQName(i);
                output.write(" ");
                highlighter.startTokenHighlight(output, highlighter.getAttributeClass());
                output.write(name);
                output.write("=");
                highlighter.endTokenHighlight(output);
                highlighter.startTokenHighlight(output, highlighter.getValueClass());
                output.write("\"");
                output.write(atts.getValue(i));
                output.write("\"");
                highlighter.endTokenHighlight(output);
            }

            highlighter.endTokenHighlight(output);
            output.write(">");
        }

        public void endElement(String namespaceURI, String localName, String qName) throws SAXException
        {
            if (DUMMY_ELEMENT_NAME.equals(localName)) return;

            output.write("</");
            highlighter.startTokenHighlight(output, highlighter.getElementClass());
            output.write( (qName==null) ? localName : qName );
            highlighter.endTokenHighlight(output);
            output.write(">");
        }

        public void characters(char ch[], int start, int length) throws SAXException
        {
            output.write(String.valueOf(ch, start, length));
        }

        public void ignorableWhitespace(char ch[], int start, int length) throws SAXException
        {
            output.write(String.valueOf(ch, start, length));
        }
    }
}
