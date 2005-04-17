package org.andromda.core.transformation;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.URIResolver;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.andromda.core.common.AndroMDALogger;
import org.apache.commons.lang.StringUtils;

/**
 * An implementation of Transformer that provides
 * XSLT transformations.  The {@link #transform(URL, URL[])}
 * operation will apply the given XSLT files to the model
 * in the order which they are found.
 * 
 * @author Chad Brandon
 */
public class XslTransformer
    implements Transformer
{
    /**
     * The shared instance.
     */
    private static final Transformer transformer = new XslTransformer();
    
    /**
     * Gets the shared instance of this class.
     * 
     * @return the shared transformer instance.
     */
    public static Transformer instance()
    {
        return transformer;
    }

    /**
     * Applies the given XSLT files to the model in the order which they are found.
     * 
     * @see org.andromda.core.transformation.Transformer#transform(java.net.URL, java.net.URL[])
     */
    public InputStream transform(final URL model, final URL[] xsltTransformations)
    {
        try
        {
            InputStream stream = null;
            if (xsltTransformations != null && xsltTransformations.length > 0)
            {
                Source modelSource = new StreamSource(model.openStream());
                final List xslts = Arrays.asList(xsltTransformations);     
                final TransformerFactory factory = TransformerFactory.newInstance();
                final TransformerURIResolver resolver = new TransformerURIResolver();
                factory.setURIResolver(resolver);
                for (final Iterator xsltIterator = xslts.iterator(); xsltIterator.hasNext();)
                {
                    final URL xslt = (URL)xsltIterator.next();
                    resolver.setLocation(xslt);
                    if (xslt != null)
                    {
                        AndroMDALogger.info("applying transformation --> '" + xslt + "'");
                        final Source xsltSource = new StreamSource(xslt.openStream());
                        javax.xml.transform.Transformer transformer = factory.newTransformer(xsltSource);
                        final ByteArrayOutputStream output = new ByteArrayOutputStream();
                        final Result result = new StreamResult(output);
                        transformer.transform(modelSource, result);
                        stream = new ByteArrayInputStream(output.toByteArray());
                        if (xsltIterator.hasNext())
                        {
                            modelSource = new StreamSource(stream);
                        }     
                    }
                }
            }
            else if (model != null)
            {
                stream = model.openStream();
            }
            return stream;
        } 
        catch (Exception ex)
        {
            ex.printStackTrace();
            throw new XslTransformerException(ex);
        }
    }
    
    /**
     * The system property for setting the transformer factory implementation.
     */
    protected static final String TRANSFORMER_FACTORY = "javax.xml.transform.TransformerFactory";
    
    /**
     * The default transformer factory implementation used (if one isn't present).
     */
    protected static final String DEFAULT_TRANSFORMER_FACTORY_IMPLEMENTATION = "org.apache.xalan.processor.TransformerFactoryImpl";
     
    /**
     * Set the default transformer factory (if one has not
     * been set).
     */
    static
    {
        if (StringUtils.isBlank(System.getProperty(TRANSFORMER_FACTORY)))
        {
            System.setProperty(XslTransformer.TRANSFORMER_FACTORY, XslTransformer.DEFAULT_TRANSFORMER_FACTORY_IMPLEMENTATION);      
        }      
    }
    
    /**
     * Provides the URI resolving capabilities for the 
     * {@ XslTransformer}
     */
    private static class TransformerURIResolver
        implements URIResolver
    {
        /**
         * @see javax.xml.transform.URIResolver#resolve(java.lang.String, java.lang.String)
         */
        public Source resolve(String href, String base) throws TransformerException
        {
            Source source = null;
            if (this.location != null)
            {
                String locationUri = new String(location.toString().replace('\\', '/'));
                locationUri = locationUri.substring(0, locationUri.toString().lastIndexOf('/') + 1);
                source = new StreamSource(locationUri + href);
            }
            return source;
        }
        
        /**
         * The current transformation location.
         */
        private URL location;
        
        /**
         * Sets the location of the current transformation.
         * 
         * @param location the transformation location as a URI.
         */
        public void setLocation(URL location)
        {
            this.location = location;
        }
    }
}