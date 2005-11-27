package org.andromda.repositories.emf;

import java.io.InputStream;

import java.net.URL;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.andromda.core.common.AndroMDALogger;
import org.andromda.core.common.ResourceUtils;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.impl.URIConverterImpl;


/**
 * Extends the default URIConverterImpl to be able to discover the physical path of URIs when
 * given the moduleSearchPaths.
 *
 * @author Chad Brandon
 */
public class EMFURIConverter
    extends URIConverterImpl
{
    /**
     * Creates a new instance of EMFURIConvert taking the <code>moduleSearchPaths</code>
     * as an argument. These are the paths used to attempt to normalize a given URI during
     * the call to {@link #normalize(URI)} provided that it couldn't be found in the normal manner.
     * @param moduleSearchPaths
     */
    public EMFURIConverter(final List moduleSearchPaths)
    {
        this.moduleSearchPaths = moduleSearchPaths;
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
     * Overwridden to provide the normalization of uris given the module searech paths.
     * 
     * @see org.eclipse.emf.ecore.resource.URIConverter#normalize(org.eclipse.emf.common.util.URI)
     */
    public URI normalize(final URI uri)
    {
        URI normalizedUri = super.normalize(uri);
        if (normalizedUri.equals(uri))
        {
            final String resourceName = uri.toString().replaceAll(
                    ".*(\\\\+|/)",
                    "");
            if (this.moduleSearchPaths != null)
            {
                if (!normalizedUris.containsKey(uri))
                {
                    for (final Iterator iterator = this.moduleSearchPaths.iterator(); iterator.hasNext();)
                    {
                        String searchPath = (String)iterator.next();
                        final String completePath = ResourceUtils.normalizePath(searchPath + '/' + resourceName);

                        try
                        {
                            InputStream stream = null;
                            URL url = ResourceUtils.toURL(completePath);
                            if (url != null)
                            {
                                try
                                {
                                    stream = url.openStream();
                                    stream.close();
                                    AndroMDALogger.info("referenced model --> '" + url + "'");
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
                                    normalizedUri = EMFRepositoryFacadeUtils.createUri(completePath);
                                    this.normalizedUris.put(
                                        uri,
                                        normalizedUri);
                                    break;
                                }
                            }
                        }
                        catch (final Exception exception)
                        {
                            // - ignore
                        }
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