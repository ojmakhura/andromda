package org.andromda.transformers.atl;

import java.net.URL;

import org.andromda.core.common.ResourceUtils;

/**
 * Utilities used with the ATLTransformer and its supporting
 * classes.
 * 
 * @author Chad Brandon
 */
public class ATLTransformerUtils
{
    /**
     * The directory that stores the ATL resources.
     */
    private static final String RESOURCES_DIRECTORY = "META-INF/resources";
    
    /**
     * Attempts to find the resource with that path relative
     * to the resources directory.  An exception is thrown
     * if the resource can not be found.
     * 
     * @param path the relative path.
     * @return the URL.
     */
    public static URL getResource(final String path)
    {
        final String resourcePath = RESOURCES_DIRECTORY + '/' + path;
        final URL resource = ResourceUtils.getResource(resourcePath);
        if (resource == null)
        {
            throw new TransformerException("Resource could not be found '" + resourcePath + "'");
        }
        return resource;
    }
}
