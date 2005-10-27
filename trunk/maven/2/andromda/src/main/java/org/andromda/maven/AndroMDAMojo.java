package org.andromda.maven;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;

import java.net.MalformedURLException;
import java.net.URL;

import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.andromda.core.AndroMDA;
import org.andromda.core.common.ResourceUtils;
import org.andromda.core.configuration.Configuration;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.resources.PropertyUtils;
import org.apache.maven.project.MavenProject;
import org.apache.maven.settings.Settings;
import org.codehaus.plexus.util.InterpolationFilterReader;


/**
 * A Maven2 plugin to run AndroMDA.
 *
 * @author Chad Brandon
 * @goal run
 * @phase generate-sources
 */
public class AndroMDAMojo
    extends AbstractMojo
{
    /**
     * This is the URI to the AndroMDA configuration file.
     *
     * @parameter expression="file:mda/conf/andromda.xml"
     * @required
     */
    private String configurationUri;

    /**
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    private MavenProject project;

    /**
     * @parameter expression="${project.build.filters}"
     */
    private List propertyFiles;

    /**
     * The path to the mappings within the plugin.
     */
    private static final String MAPPINGS_PATH = "META-INF/andromda/mappings";

    public void execute()
        throws MojoExecutionException
    {
        try
        {
            final AndroMDA andromda = AndroMDA.newInstance();
            andromda.run(this.getConfiguration());
        }
        catch (final Throwable throwable)
        {
            String message = "Error running AndroMDA";
            if (throwable instanceof MalformedURLException)
            {
                message = "Configuration is not a valid URI --> '" + configurationUri + "'";
            }
            throw new MojoExecutionException(message, throwable);
        }
    }

    /**
     * The current user system settings for use in Maven. This is used for
     * <br/>
     * plugin manager API calls.
     *
     * @parameter expression="${settings}"
     * @required
     * @readonly
     */
    private Settings settings;

    /**
     * Creates the Configuration instance from the {@link #configurationUri}
     * @return the configuration instance
     * @throws MalformedURLException if the URL is invalid.
     */
    private Configuration getConfiguration()
        throws IOException
    {
        final URL uri = new URL(this.configurationUri);
        final String contents = this.replaceProperties(ResourceUtils.getContents(uri));
        final Configuration configuration = Configuration.getInstance(contents);
        final URL mappingsUrl = ResourceUtils.getResource(MAPPINGS_PATH);
        if (mappingsUrl != null)
        {
            configuration.addMappingsSearchLocation(mappingsUrl.toString());
        }
        return configuration;
    }

    /**
     * Stores all properties.
     */
    private Properties properties;

    protected Properties getProperties()
        throws IOException
    {
        if (this.properties == null)
        {
            // System properties
            this.properties = new Properties(System.getProperties());

            this.properties.put(
                "settings",
                this.settings);

            // Project properties
            this.properties.putAll(this.project.getProperties());
            for (final Iterator iterator = propertyFiles.iterator(); iterator.hasNext();)
            {
                final String propertiesFile = (String)iterator.next();

                final Properties properties = PropertyUtils.loadPropertyFile(
                        new File(propertiesFile),
                        true,
                        true);

                this.properties.putAll(properties);
            }
        }
        return this.properties;
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
        reader = new InterpolationFilterReader(
                reader,
                new BeanProperties(project),
                "${",
                "}");
        reader = new InterpolationFilterReader(
                reader,
                new BeanProperties(settings),
                "${",
                "}");
        return ResourceUtils.getContents(reader);
    }
}