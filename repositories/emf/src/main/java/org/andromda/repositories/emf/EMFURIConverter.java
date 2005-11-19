package org.andromda.repositories.emf;

import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import org.andromda.core.common.ResourceUtils;
import org.andromda.core.repository.RepositoryFacadeException;
import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.impl.URIConverterImpl;

/**
 * Extends the default URIConverterImpl to be able to find the discover physical path of URIs when
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
    
    private List moduleSearchPaths;

    /**
     * Overwridden
     *
     * @see org.eclipse.emf.ecore.resource.URIConverter#normalize(org.eclipse.emf.common.util.URI)
     */
    public URI normalize(URI uri)
    {
        URI normalizedUri = super.normalize(uri);
        if (normalizedUri.equals(uri))
        {
            final String resourceName = uri.toString().replaceAll(
                    ".*(\\\\+|/)",
                    "");
            if (this.moduleSearchPaths != null)
            {
                for (final Iterator iterator = this.moduleSearchPaths.iterator(); iterator.hasNext();)
                {
                    String searchPath = (String)iterator.next();
                    final String completePath = (searchPath + "/" + resourceName).replaceAll(
                            "\\\\+|/",
                            "/");
                    try
                    {
                        InputStream stream = null;
                        URL url = ResourceUtils.toURL(completePath);
                        try
                        {
                            stream = url.openStream();
                            stream.close();
                            normalizedUri = this.createUri(completePath);
                        }
                        catch (final Exception exception)
                        {
                            url = null;
                        }
                        finally
                        {
                            stream = null;
                        }
                    }
                    catch (final Throwable throwable)
                    {
                        // - ignore
                    }
                    if (normalizedUri != null)
                    {
                        break;
                    }
                }
            }
        }
        return normalizedUri;
    }

    /**
     * The URI file prefix.
     */
    private static final String FILE_PREFIX = "file:";

    /**
     * Creates the EMF URI instance from the given <code>uri</code>.
     *
     * @param uri the path from which to create the URI.
     * @return the URI
     */
    private URI createUri(String uri)
    {
        if (uri.startsWith(FILE_PREFIX))
        {
            final String filePrefixWithSlash = FILE_PREFIX + "/";
            if (!uri.startsWith(filePrefixWithSlash))
            {
                uri = StringUtils.replaceOnce(
                        uri,
                        FILE_PREFIX,
                        filePrefixWithSlash);
            }
        }
        final URI resourceUri = URI.createURI(uri);
        if (resourceUri == null)
        {
            throw new RepositoryFacadeException("The path '" + uri + "' is not a valid URI");
        }
        return resourceUri;
    }
}