package org.andromda.cartridges.interfaces;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Reads an XML-formatted cartridge description.
 * 
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen</a>
 *
 */
public class CartridgeXmlParser extends DefaultHandler
{
    private final SAXParserFactory _factory;
    private DefaultCartridgeDescriptor desc = null;

    public CartridgeXmlParser()
    {
        _factory = SAXParserFactory.newInstance();
        _factory.setValidating(false);
    }

    /**
     * Parses the XML descriptor file and returns an appropriate data structure.
     * @param in the input stream to read from
     * @return DefaultCartridgeDescriptor a description for this cartridge
     */
    public DefaultCartridgeDescriptor parse(InputStream in)
    {
        try
        {
            SAXParser parser = _factory.newSAXParser();
            parser.parse(in, this);
        }
        catch (IOException e)
        {
            desc = null;
            e.printStackTrace();
        }
        catch (IllegalArgumentException e)
        {
            desc = null;
            e.printStackTrace();
        }
        catch (ParserConfigurationException e)
        {
            desc = null;
            e.printStackTrace();
        }
        catch (SAXException e)
        {
            desc = null;
            e.printStackTrace();
        }
        return desc;
    }

    public void startDocument()
    {
        desc = new DefaultCartridgeDescriptor();
    }

    public void startElement(
        String namespaceURI,
        String localName,
        String qName,
        Attributes attributes)
    {

        if (qName.equals("cartridge"))
        {
            desc.setCartridgeName(attributes.getValue("name"));
        }
        else if (qName.equals("property"))
        {
            desc.addProperty(
                attributes.getValue("name"),
                attributes.getValue("value"));
        }
        else if (qName.equals("stereotype"))
        {
            desc.addSupportedStereotype(attributes.getValue("name"));
        }
        else if (qName.equals("outlet"))
        {
            desc.addOutlet(attributes.getValue("name"));
        }
        else if (qName.equals("class"))
        {
            desc.setCartridgeClassName(attributes.getValue("name"));
        }
        else if (qName.equals("template"))
        {
            String val = attributes.getValue("generateEmptyFiles");
            boolean generateEmptyFiles = 
               (val == null) || val.equalsIgnoreCase("true");
            TemplateConfiguration tc =
                new TemplateConfiguration(
                    desc,
                    attributes.getValue("stereotype"),
                    attributes.getValue("sheet"),
                    attributes.getValue("outputPattern"),
                    attributes.getValue("outlet"),
                    attributes.getValue("overWrite").equalsIgnoreCase(
                        "true"),                    
                    generateEmptyFiles);
            String tcn = attributes.getValue("transformClass");
            if (tcn != null)
            {
                try
                {
                    tc.setTransformClassname(tcn);
                }
                catch (ClassNotFoundException e)
                {
                    e.printStackTrace();
                    // @todo logging
                }
            }
            desc.addTemplateConfiguration(tc);
        }
    }
}
