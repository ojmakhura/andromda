package org.andromda.maven;

import org.andromda.core.AndroMDA;
import org.andromda.core.common.ResourceUtils;
import org.andromda.core.configuration.Configuration;
import org.apache.commons.lang.StringUtils;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.MatchingTask;

import java.net.URL;

import java.util.Iterator;
import java.util.Map;


/**
 * This task is used with the AndroMDA Maven plugin.
 *
 * @author Chad Brandon
 * @see org.andromda.core.ModelProcessor
 */
public class AndroMDAMavenRunner
    extends MatchingTask
{
    static
    {
        Thread.currentThread().setContextClassLoader(AndroMDAMavenRunner.class.getClassLoader());
    }

    /**
     * The configuration instance (configures AndroMDA).
     */
    private Configuration configuration;

    /**
     * Sets the URI to the configuration file.
     *
     * @param configurationUri
     */
    public void setConfigurationUri(final String configurationUri)
        throws Exception
    {
        this.configuration =
            Configuration.getInstance(this.replaceProperties(ResourceUtils.getContents(new URL(configurationUri))));
    }

    /**
     * Stores the search location for mapping files.
     */
    private String mappingsSearchLocation;

    /**
     * Sets the mappings search location.
     *
     * @param MappingsSearchLocation
     */
    public void setMappingsSearchLocation(final String mappingsSearchLocation)
    {
        this.mappingsSearchLocation = mappingsSearchLocation;
    }

    /**
     *  Runs AndroMDA.
     */
    public void execute()
        throws BuildException
    {
        try
        {
            this.configuration.addMappingsSearchLocation(this.mappingsSearchLocation);
            final AndroMDA andromda = AndroMDA.getInstance(configuration);
            if (andromda != null)
            {
                andromda.run();
                andromda.shutdown();
            }
        }
        finally
        {
            // Set the context class loader back ot its system class loaders
            // so that any processes running after won't be trying to use
            // the ContextClassLoader for this class.
            Thread.currentThread().setContextClassLoader(ClassLoader.getSystemClassLoader());
        }
    }

    /**
     * Replaces all properties having the style
     * <code>${some.property}</code> with the value
     * of the specified property if there is one.
     *
     * @param fileContents the fileContents to perform replacement on.
     */
    protected String replaceProperties(String string)
        throws Exception
    {
        final Map properties = this.getProject().getProperties();
        if (properties != null && !properties.isEmpty())
        {
            for (final Iterator iterator = properties.keySet().iterator(); iterator.hasNext();)
            {
                final String name = (String)iterator.next();
                final String property = "${" + name + "}";
                final String value = (String)properties.get(name);
                string = StringUtils.replace(string, property, value);
            }
        }
        return string;
    }
}