package org.andromda.maven.plugin.configuration;

import java.net.URL;
import java.net.URLClassLoader;


/**
 * A custom class loader nessary to avoid class loading errors
 * when running within Maven2.
 *
 * @author Chad Brandon
 */
public class ConfigurationClassLoader
    extends URLClassLoader
{
    public ConfigurationClassLoader(
        final URL[] urls,
        final ClassLoader parent)
    {
        super(urls, parent);
    }

    /**
     * @see java.lang.ClassLoader#loadClass(java.lang.String)
     */
    public Class loadClass(final String name)
        throws ClassNotFoundException
    {
        return this.loadClass(
            name,
            false);
    }

    /**
     * @see java.lang.ClassLoader#loadClass(java.lang.String, boolean)
     */
    protected Class loadClass(
        final String name,
        final boolean resolve)
        throws ClassNotFoundException
    {
        // - first, we check if the class has already been loaded
        Class loadedClass = this.findLoadedClass(name);

        // - if we could not find it, try to find it in the parent
        if (loadedClass == null)
        {
            final ClassLoader parent = this.getParent();
            if (parent != null)
            {
                try
                {
                    loadedClass = parent.loadClass(name);
                }
                catch (final ClassNotFoundException exception)
                {
                    // - ignore
                }
            }
            else
            {
                loadedClass = getSystemClassLoader().loadClass(name);
            }
        }

        // - if not loaded from the parent, search this classloader
        if (loadedClass == null)
        {
            loadedClass = findClass(name);
        }

        if (resolve)
        {
            this.resolveClass(loadedClass);
        }

        return loadedClass;
    }
}