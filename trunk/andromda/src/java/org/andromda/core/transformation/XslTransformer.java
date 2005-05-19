package org.andromda.core.transformation;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
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
import org.andromda.core.configuration.Transformation;
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
    public static final Transformer instance()
    {
        return transformer;
    }

    /**
     * Applies the given XSLT files to the model in the order which they are found.
     * 
     * @see org.andromda.core.transformation.Transformer#transform(java.net.URL, org.andromda.core.configuration.Transformation[])
     */
    public InputStream transform(final URL model, final Transformation[] xsltTransformations)
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
                    final Transformation transformation = (Transformation)xsltIterator.next();
                    final URL xslt = transformation.getUri();
                    resolver.setLocation(xslt);
                    if (xslt != null)
                    {
                        AndroMDALogger.info("Applying transformation --> '" + xslt + "'");
                        final Source xsltSource = new StreamSource(xslt.openStream());
                        final javax.xml.transform.Transformer transformer = factory.newTransformer(xsltSource);
                        final ByteArrayOutputStream output = new ByteArrayOutputStream();
                        final Result result = new StreamResult(output);
                        transformer.transform(modelSource, result);
                        final byte[] outputResult = output.toByteArray();
                        stream = new ByteArrayInputStream(outputResult);
                        // if we have an output location specified, write the result
                        final String outputLocation = transformation.getOutputLocation();
                        if (StringUtils.isNotBlank(outputLocation))
                        {
                            final File fileOutput = new File(outputLocation);
                            final File parent = fileOutput.getParentFile();
                            if (parent != null)
                            {
                                parent.mkdirs();
                            }
                            FileOutputStream outputStream = new FileOutputStream(fileOutput);
                            AndroMDALogger.info("Transformation output: '" + outputLocation + "'");
                            outputStream.write(outputResult);
                            outputStream.flush();
                            outputStream.close();
                            outputStream = null;
                        }
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
            throw new XslTransformerException(ex);
        }
    }
    
    /**
     * Provides the URI resolving capabilities for the 
     * {@ XslTransformer}
     */
    private static final class TransformerURIResolver
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