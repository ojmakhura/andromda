package org.andromda.repositories.emf;

import org.andromda.core.repository.RepositoryFacadeException;
import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.common.util.URI;

/**
 * Contains some utilities methods for dealing with the EMF repository 
 * facade functionality.
 * 
 * @author Chad Brandon
 */
public class EMFRepositoryFacadeUtils
{
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
    public static URI createUri(String uri)
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
