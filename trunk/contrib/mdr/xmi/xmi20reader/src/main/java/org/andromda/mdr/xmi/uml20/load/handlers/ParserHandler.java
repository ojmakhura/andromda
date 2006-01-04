package org.andromda.mdr.xmi.uml20.load.handlers;

import java.io.IOException;
import java.io.InputStream;

import java.util.ArrayList;

import javax.jmi.xmi.MalformedXMIException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.andromda.mdr.xmi.uml20.load.core.XMIConstants;
import org.andromda.mdr.xmi.uml20.load.reader.LoadInformationContainer;
import org.andromda.mdr.xmi.uml20.load.utils.HTMLConverter;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ParserHandler
    extends DefaultHandler
{
    private static final String SUPPORTED_XMI_VERSION = "2.1";
    private boolean wasXMIBlock = false;
    private boolean wasXMImetamodelBlock = false;
    private boolean needsValidation = true;
    private boolean elementStarted = false;
    private LoadInformationContainer mInfoContainer;
    final static int BYTES_PER_LINE = 60;

    /**
     * Buffering characters.
     */
    private StringBuffer mCharacters;
    private HandlerState mHandlerState;

    public ParserHandler(
        HandlerState handler,
        LoadInformationContainer infoContainer)
    {
        mCharacters = new StringBuffer();
        mHandlerState = handler;
        mInfoContainer = infoContainer;
    }

    /**
     * Parse xmi document
     * 
     * @param info info of the xmi file
     * @param URI
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     */
    public void parseXmi(InputStream info, String URI)
        throws IOException,
            ParserConfigurationException,
            SAXException
    {
        InputSource input = createInputSource(info, URI);

        elementStarted = false;
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setValidating(false);
        factory.setNamespaceAware(true);

        // factory.setFeature("http://apache.org/xml/features/allow-java-encodings",
        // true);
        SAXParser parser = factory.newSAXParser();

        // parser.setProperty("http://apache.org/xml/features/dom/defer-node-expansion",
        // "false");
        parser.parse(input, this);
    }

    public void startElement(String namespaceURI, String localName, String qName, Attributes atts)
        throws SAXException
    {
        if (needsValidation)
        {
            validateElement(qName, atts);
        }

        mCharacters.setLength(0);

        // qName = qName.intern();
        ArrayList names = new ArrayList(atts.getLength());
        ArrayList values = new ArrayList(atts.getLength());

        int count = atts.getLength();
        String name = null;
        String value = null;
        for (int i = 0; i < count; i++)
        {
            name = atts.getQName(i);

            // name = name.intern();
            value = atts.getValue(i);
            value = HTMLConverter.convertHTMLstring(value);
            value = value.intern();
            names.add(name);
            values.add(value);
        }

        try
        {
            mHandlerState.handleStartElement(localName, atts, namespaceURI);
        }
        catch (MalformedXMIException e)
        {
            throw new SAXException(e);
        }

        elementStarted = true;
    }

    public void endElement(String namespaceURI, String localName, String qName) throws SAXException
    {
        String value = null;
        if (mCharacters.length() > 0)
        {
            // we cannot do StringBuffer.toString() because it does some kind of
            // memory leak problems
            // with jdk1.4.1. In some cases OutOfMemory exception occurred while
            // parsing file.
            char[] chars = new char[mCharacters.length()];
            mCharacters.getChars(0, chars.length, chars, 0);
            value = new String(chars);
            value = value.intern();
        }
        mCharacters.setLength(0);

        try
        {
            mHandlerState.handleEndElement(localName, value, namespaceURI);
        }
        catch (SAXException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new SAXException(e);
        }

        elementStarted = false;
    }

    public void characters(char[] ch, int start, int length)
    {
        if (!elementStarted)
        {
            return;
        }
        String s = new String(ch, start, length);
        s = HTMLConverter.convertHTMLstring(s);
        mCharacters.append(s);
    }

    public void validateElement(String name, Attributes attrs) throws SAXException
    {
        String xmiValue = attrs.getValue(XMIConstants.XMIversion);
        if (xmiValue == null)
        {
            xmiValue = attrs.getValue(XMIConstants.XMI21_VERSION);
        }
        if (xmiValue == null)
        {
            xmiValue = attrs.getValue(XMIConstants.XMIVersion21);
        }
        if (!wasXMIBlock && (name.equals(XMIConstants.XMI) || name.equals(XMIConstants.XMI)))
        {
            wasXMIBlock = true;
            if (!SUPPORTED_XMI_VERSION.equals(xmiValue))
            {
                // throw new SAXException( new XMIVersionException("unsupported
                // xmi version" + xmiValue));
                throw new XMIVersionException("unsupported xmi version" + xmiValue);
            }
        }

        needsValidation = !(wasXMIBlock && wasXMImetamodelBlock);
    }

    public InputSource createInputSource(InputStream info, String uri)
    {
        InputSource input;
        if (info != null)
        {
            input = new InputSource(info);
        }
        else
        {
            input = new InputSource(uri);
        }
        return input;
    }

    public void startPrefixMapping(String prefix, String uri)
    {
        mInfoContainer.getTagConverter().registerNamespace(prefix, uri);
    }
}