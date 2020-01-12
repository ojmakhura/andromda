package org.andromda.core.common;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;

/**
 * Finds and loads file resources from the current classpath.
 *
 * @author Chad Brandon
 */
public class ResourceFinder
{
    /**
     * Returns a URL[] containing the URL of each resource and the File which represents the library the resource was
     * found in.
     *
     * @param resource the resource to find
     * @return a <code>array of resource URLs</code>
     */
    public static URL[] findResources(final String resource)
    {
        ExceptionUtils.checkEmpty("resource", resource);
        try
        {
            final Collection<URL> resources = new ArrayList<URL>();
            for (final Enumeration<URL> enumeration = ClassUtils.getClassLoader().getResources(resource);
                 enumeration.hasMoreElements();)
            {
                resources.add(enumeration.nextElement());
            }


            return resources.toArray(new URL[resources.size()]);
        }
        catch (final Exception exception)
        {
            throw new ResourceFinderException(exception);
        }
    }
}
