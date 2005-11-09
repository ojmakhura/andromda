package org.andromda.maven.plugin.configuration;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.andromda.core.common.ResourceUtils;
import org.andromda.core.configuration.Configuration;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.resources.PropertyUtils;
import org.apache.maven.project.MavenProject;
import org.apache.maven.settings.Settings;
import org.codehaus.plexus.util.InterpolationFilterReader;


/**
 * A an abstract Mojo for dealing with the AndroMDA configuration,
 * if a plugin needs to use the AndroMDA configuration, it should extend this
 * class.
 */
public abstract class AbstractConfigurationMojo
    extends AbstractMojo
{
    /**
     * The path to the mappings within the plugin.
     */
    private static final String MAPPINGS_PATH = "META-INF/andromda/mappings";

    /**
     * Creates the Configuration instance from the {@link #configurationUri}
     *
     * @return the configuration instance
     * @throws MalformedURLException if the URL is invalid.
     */
    protected Configuration getConfiguration(final URL configurationUri)
        throws IOException
    {
        final String contents = this.replaceProperties(ResourceUtils.getContents(configurationUri));
        final Configuration configuration = Configuration.getInstance(contents);
        final URL mappingsUrl = ResourceUtils.getResource(MAPPINGS_PATH);
        if (mappingsUrl != null)
        {
            configuration.addMappingsSearchLocation(mappingsUrl.toString());
        }
        return configuration;
    }

    protected Properties getProperties()
        throws IOException
    {
        // System properties
        Properties properties = new Properties(System.getProperties());

        properties.put(
            "settings",
            this.getSettings());

        // Project properties
        properties.putAll(this.getProject().getProperties());
        for (final Iterator iterator = this.getPropertyFiles().iterator(); iterator.hasNext();)
        {
            final String propertiesFile = (String)iterator.next();

            final Properties projectProperties = PropertyUtils.loadPropertyFile(
                    new File(propertiesFile),
                    true,
                    true);

            properties.putAll(projectProperties);
        }
        return properties;
    }

    /**
     * Replaces all properties having the style
     * <code>${some.property}</code> with the value
     * of the specified property if there is one.
     *
     * @param fileContents the fileContents to perform replacement on.
     */
    protected String replaceProperties(final String string)
        throws IOException
    {
        final Properties properties = this.getProperties();
        final StringReader stringReader = new StringReader(string);
        InterpolationFilterReader reader = new InterpolationFilterReader(stringReader, properties, "${", "}");
        reader.reset();
        reader = new InterpolationFilterReader(
                reader,
                new BeanProperties(this.getProject()),
                "${",
                "}");
        reader = new InterpolationFilterReader(
                reader,
                new BeanProperties(this.getSettings()),
                "${",
                "}");
        final String contents = ResourceUtils.getContents(reader);
        return contents;
    }

    /**
     * Sets the current context class loader from the given runtime classpath elements.
     *
     * @throws DependencyResolutionRequiredException
     * @throws MalformedURLException
     */
    protected void initializeClasspathFromClassPathElements(final List classpathFiles)
        throws MalformedURLException
    {
        if (classpathFiles != null && classpathFiles.size() > 0)
        {
            final URL[] classpathUrls = new URL[classpathFiles.size()];

            for (int ctr = 0; ctr < classpathFiles.size(); ++ctr)
            {
                final File file = new File((String)classpathFiles.get(ctr));
                if (this.getLog().isDebugEnabled())
                {
                    getLog().debug("adding to classpath '" + file + "'");
                }
                classpathUrls[ctr] = file.toURL();
            }

            final URLClassLoader loader =
                new URLClassLoader(classpathUrls,
                    Thread.currentThread().getContextClassLoader());
            Thread.currentThread().setContextClassLoader(loader);
        }
    }

    /**
     * Gets the current project.
     *
     * @return Returns the project.
     */
    protected abstract MavenProject getProject();

    /**
     * Gets the property files for the profile (the ones defined
     * in the filters section of the POM).
     *
     * @return Returns the propertyFiles.
     */
    protected abstract List getPropertyFiles();

    /**
     * Gets the current settings of the project.
     *
     * @return Returns the settings.
     */
    protected abstract Settings getSettings();
}