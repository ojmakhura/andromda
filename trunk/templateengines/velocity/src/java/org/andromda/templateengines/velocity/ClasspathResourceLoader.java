package org.andromda.templateengines.velocity;

import java.io.InputStream;

import org.apache.velocity.exception.ResourceNotFoundException;


/**
 * Extends the default ClasspathResourceLoader in order
 * to override getResourceAsStream(String) so that we can correctly
 * search the ContextClassLoader instead of the system class loader.
 * This class should be removed whenever the regular ClasspathResourceLoader
 * in the velocity releases searches the context class loader.
 *
 * @author Chad Brandon
 */
public class ClasspathResourceLoader
    extends org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader
{
    public synchronized InputStream getResourceStream(String name)
        throws ResourceNotFoundException
    {
        InputStream result = super.getResourceStream(name);
        if (result == null)
        {
            final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            if (classLoader != null)
            {
                result = classLoader.getResourceAsStream(name);
            }
        }
        return result;
    }
}