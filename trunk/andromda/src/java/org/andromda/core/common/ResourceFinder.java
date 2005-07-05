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
     * @return a <code>array of resource URLs<code>
     */
    public static URL[] findResources(final String resource)
    {
        final String methodName = "ResourceFinder.findResource";
        ExceptionUtils.checkEmpty(methodName, "resource", resource);
        try
        {
            Collection resources = new ArrayList();
            final Enumeration resourceEnumeration = Thread.currentThread().getContextClassLoader().getResources(resource);\
            while (resourceEnumeration.hasMoreElements())
            {
                resources.add(resourceEnumeration.nextElement());
            }
            return (URL[])resources.toArray(new URL[0]);
        }
        catch (final Exception exception)
        {
            throw new ResourceFinderException(exception);
        }
    }
}