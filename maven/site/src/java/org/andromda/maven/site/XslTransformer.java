package org.andromda.maven.site;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.net.URL;

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
import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


/**
 * Used to perform the transformation of XSL documents
 * within the site plugin.
 *
 * @author Chad Brandon
 */
public class XslTransformer
{
    /**
     * Applies the given XSLT files to the model in the order in which they are found.
     */
    public void transform(
        final String xmlDocument,
        final String transformation,
        final String outputLocation)
    {
        try
        {
            if (StringUtils.isNotBlank(xmlDocument))
            {
                final Source xmlSource = new DOMSource(this.urlToDocument(xmlDocument));
                final TransformerFactory factory = TransformerFactory.newInstance();
                final URL xslt = new File(transformation).toURL();
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
                    if (StringUtils.isNotBlank(outputLocation))
                    {
                        final File fileOutput = new File(outputLocation);
                        final File parent = fileOutput.getParentFile();
                        if (parent != null)
                        {
                            parent.mkdirs();
                        }
                        FileOutputStream outputStream = new FileOutputStream(fileOutput);
                        outputStream.write(outputResult);
                        outputStream.flush();
                        outputStream.close();
                        outputStream = null;
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
                URL uri = null;
                InputStream inputStream = null;
                uri = new File(StringUtils.replace(
                            xmlResource,
                            xmlResourceName,
                            path)).toURL();
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
}