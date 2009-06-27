package org.andromda.repositories.emf;

import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.andromda.core.common.AndroMDALogger;
import org.andromda.core.common.ResourceUtils;
import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.impl.URIConverterImpl;

/**
 * Extends the default URIConverterImpl to be able to discover the physical path of URIs when
 * given the moduleSearchPaths.
 *
 * @author Chad Brandon
 * @author Bob Fields
 */
public class EMFURIConverter
    extends URIConverterImpl
{
    /**
     * Creates a new instance of EMFURIConverter taking the <code>moduleSearchPaths</code>
     * as an argument. These are the paths used to attempt to normalize a given URI during
     * the call to {@link #normalize(URI)} provided that it couldn't be found in the normal manner.
     *
     * @param moduleSearchPaths the paths to search for modules.
     */
    public EMFURIConverter(final List moduleSearchPaths)
    {
        this.moduleSearchPaths = moduleSearchPaths;
        if (logger.isDebugEnabled())
        {
            for (final Iterator pathIterator = this.moduleSearchPaths.iterator(); pathIterator.hasNext();)
            {
                logger.debug("Model search path:" + pathIterator.next());
            }
        }
    }

    /**
     * Stores the module search paths.
     */
    private List moduleSearchPaths;

    /**
     * Stores the URIs that have been normalized.
     */
    private final Map normalizedUris = new HashMap();

    /**
     * The logger instance.
     */
    private static final Logger logger = Logger.getLogger(EMFURIConverter.class);

    /**
     * Overridden to provide the normalization of uris given the module search paths.
     *
     * @see org.eclipse.emf.ecore.resource.URIConverter#normalize(org.eclipse.emf.common.util.URI)
     */
    public URI normalize(final URI uri)
    {
        URI normalizedUri = super.normalize(uri);
        if (normalizedUri.equals(uri))
        {
            if (this.moduleSearchPaths != null)
            {
                if (!this.normalizedUris.containsKey(uri))
                {
                    final String resourceName = uri.toString().replaceAll(
                            ".*(\\\\+|/)",
                            "");
                    for (final Iterator iterator = this.moduleSearchPaths.iterator(); iterator.hasNext();)
                    {
                        Date now1 = new Date();
                        final String searchPath = (String)iterator.next();
                        final URI fileURI = EMFRepositoryFacadeUtils.createUri(ResourceUtils.normalizePath(searchPath));
                        //long ms1 = new Date().getTime() - now1.getTime();
                        if (fileURI != null && fileURI.lastSegment() != null && fileURI.lastSegment().equals(resourceName))
                        {
                            //AndroMDALogger.info("referenced model --> '" + fileURI + "' " + now1 + " ms");
                            normalizedUri = fileURI;
                            this.normalizedUris.put(
                                uri,
                                normalizedUri);
                            break;
                        }

                        final String completePath = ResourceUtils.normalizePath(searchPath + '/' + resourceName);

                        try
                        {
                            Date now = new Date();
                            InputStream stream;
                            URL url = ResourceUtils.toURL(completePath);
                            if (url != null)
                            {
                                try
                                {
                                    stream = url.openStream();
                                    stream.close();
                                    //long ms = new Date().getTime() - now.getTime();
                                    //long ms2 = new Date().getTime() - now.getTime();
                                    //AndroMDALogger.info("referenced model --> '" + url + "' " + ms + " ms");
                                }
                                catch (final Exception exception)
                                {
                                    url = null;
                                }
                                finally
                                {
                                    stream = null;
                                }
                                if (url != null)
                                {
                                    long ms2 = new Date().getTime() - now.getTime();
                                    normalizedUri = EMFRepositoryFacadeUtils.createUri(url.toString());
                                    this.normalizedUris.put(
                                        uri,
                                        normalizedUri);
                                    AndroMDALogger.debug("loaded model --> '" + url + "' " + ms2 + " ms");
                                    break;
                                }
                            }
                        }
                        catch (final Exception exception)
                        {
                            logger.debug(
                                "Caught exception in EMFURIConverter",
                                exception);
                        }
                        long ms2 = new Date().getTime() - now1.getTime();
                        AndroMDALogger.debug("loaded model --> '" + fileURI + "' " + ms2 + " ms");
                    }

                    // - if the normalized URI isn't part of the module search path,
                    //   still store it so we don't continue to look it up each time (which is really slow)
                    if (!this.normalizedUris.containsKey(uri))
                    {
                        this.normalizedUris.put(
                            uri,
                            normalizedUri);
                    }
                }
                else
                {
                    normalizedUri = (URI)this.normalizedUris.get(uri);
                }
            }
        }

        return normalizedUri;
    }
}