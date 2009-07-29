package org.andromda.maven.plugin.site;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import java.net.URL;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


/**
 * Used to perform the transformation of XSL documents
 * within the site plugin.
 *
 * @author Chad Brandon
 * @author Vance Karimi
 */
public class XslTransformer
{
    private String projectName;
    
    /**
     * Default constructor
     *
     */
    public XslTransformer()
    {
        // Default constructor
    }
    
    /**
     * Constructor that sets the project name used to replace variable inside generated 
     * xdoc xml.
     * 
     * @param projectName
     */
    public XslTransformer(String projectName)
    {
        this.setProjectName(projectName);
    }
    
    /**
     * Applies the given XSLT files to the model in the order in which they are found.
     * 
     * @param xmlDocument The full path of the original XML
     * @param transformation The full path of the XSLT
     * @param outputLocation The full path of the output xdoc XML
     */
    public void transform(
        final String xmlDocument,
        final String transformation,
        final String outputLocation)
    {
        try
        {
            this.transform(xmlDocument, new File(transformation).toURI().toURL(), outputLocation);
        }
        catch (final Exception exception)
        {
            throw new RuntimeException(exception);
        }
    }
    
    /**
     * Applies the given XSLT files to the model in the order in which they are found.
     * 
     * @param xmlDocument The full path of the original XML
     * @param xslt The URL of the XSLT
     * @param outputLocation The full path of the output xdoc XML
     */
    public void transform(
        final String xmlDocument,
        final URL xslt,
        final String outputLocation)
    {
        try
        {
            if (StringUtils.isNotBlank(xmlDocument))
            {
                final Source xmlSource = new DOMSource(this.urlToDocument(xmlDocument));
                final TransformerFactory factory = TransformerFactory.newInstance();
                if (xslt != null)
                {
                    final Source xsltSource = new StreamSource(xslt.openStream());
                    final javax.xml.transform.Transformer transformer = factory.newTransformer(xsltSource);
                    final ByteArrayOutputStream output = new ByteArrayOutputStream();
                    final Result result = new StreamResult(output);
                    transformer.transform(
                        xmlSource,
                        result);
                    
                    final byte[] outputResult = output.toByteArray();
                    final org.dom4j.Document document = replaceVariableProperties(outputResult);
                    
                    if (StringUtils.isNotBlank(outputLocation))
                    {
                        final File fileOutput = new File(outputLocation);
                        final File parent = fileOutput.getParentFile();
                        if (parent != null)
                        {
                            parent.mkdirs();
                        }
                        
                        XMLWriter writer = new XMLWriter(new FileWriter(fileOutput));
                        writer.write(document);
                        writer.flush();
                        writer.close();
                    }
                }
            }
        }
        catch (final Exception exception)
        {
            throw new RuntimeException(exception);
        }
    }

    /**
     * Parses the XML retrieved from the String URL and returns a Document object.
     * @param url the url of the XML to parse.
     * @return Document newly created Document object.
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    private Document urlToDocument(String url)
        throws Exception
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        builder.setEntityResolver(new XslTransformerEntityResolver(url));
        return builder.parse(new InputSource(url));
    }

    /**
     * The prefix that the systemId should start with when attempting
     * to resolve it within a jar.
     */
    private static final String SYSTEM_ID_FILE = "file:";

    /**
     * Provides the resolution of external entities.
     */
    private static final class XslTransformerEntityResolver
        implements EntityResolver
    {
        private String xmlDocument;

        XslTransformerEntityResolver(final String xmlDocument)
        {
            this.xmlDocument = xmlDocument;
        }

        /**
         * @see org.xml.sax.EntityResolver#resolveEntity(java.lang.String, java.lang.String)
         */
        public InputSource resolveEntity(
            final String publicId,
            final String systemId)
            throws SAXException, IOException
        {
            InputSource source = null;
            String path = systemId;
            if (path != null && path.startsWith(SYSTEM_ID_FILE))
            {
                final String xmlResource = this.xmlDocument;
                path = path.replaceFirst(
                        SYSTEM_ID_FILE,
                        "");

                // - remove any extra starting slashes
                path = path.replaceAll(
                        "\\+",
                        "/").replaceAll(
                        "/+",
                        "/");

                // - if we still have one starting slash, remove it
                if (path.startsWith("/"))
                {
                    path = path.substring(
                            1,
                            path.length());
                }
                final String xmlResourceName = xmlResource.replaceAll(
                        ".*(\\+|/)",
                        "");
                
                InputStream inputStream = null;
                URL uri = new File(StringUtils.replace(
                            xmlResource,
                            xmlResourceName,
                            path)).toURI().toURL();
                if (uri != null)
                {
                    inputStream = uri.openStream();
                }
                if (inputStream != null)
                {
                    source = new InputSource(inputStream);
                    source.setPublicId(publicId);
                    source.setSystemId(uri.toString());
                }
            }
            return source;
        }
    }
    
    /**
     * Replace the variable property defined by %module% in the output generated
     * xdoc file.  Uses dom4j XPath to locate the variable.
     * 
     * @param documentBuffer The byte array representing the xdoc XML
     * @return the org.dom4j.Document object of the xdoc XML
     * @throws Exception
     */
    private org.dom4j.Document replaceVariableProperties(byte[] documentBuffer)
        throws Exception
    {
        SAXReader reader = new SAXReader();
        org.dom4j.Document document = reader.read(new ByteArrayInputStream(documentBuffer));
        
        // List elements = document.selectNodes("//*[contains(text(),'%module%')]");
        List elements = document.selectNodes("//*");
        for (final Iterator it = elements.iterator(); it.hasNext(); )
        {
            Element element = (Element)it.next();
            if (StringUtils.contains(element.getText(), "%module%"))
            {
                element.setText(
                        StringUtils.replace(
                                element.getText(), 
                                "%module%", 
                                this.getProjectName()));
            }
        }
        elements.clear();
        
        elements = document.selectNodes("//*[contains(@*,'%module%')]");
        for (final Iterator it = elements.iterator(); it.hasNext(); )
        {
            Element element = (Element)it.next();
            element.addAttribute(
                    "name", 
                    StringUtils.replace(
                            element.attributeValue("name"), 
                            "%module%", 
                            this.getProjectName()));
        }
        return document;
    }

    /**
     * @return Returns the projectName.
     */
    public String getProjectName()
    {
        return projectName;
    }

    /**
     * @param projectName The projectName to set.
     */
    public void setProjectName(String projectName)
    {
        this.projectName = projectName;
    }
}
