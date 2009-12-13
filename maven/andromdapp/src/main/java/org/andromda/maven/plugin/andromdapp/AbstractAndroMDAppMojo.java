package org.andromda.maven.plugin.andromdapp;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import org.andromda.core.common.ResourceUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.resources.PropertyUtils;
import org.apache.maven.project.MavenProject;
import org.apache.maven.settings.Settings;
import org.codehaus.plexus.util.InterpolationFilterReader;

/**
 * The abstract AndroMDAapp mojo (this should be extended by any Mojo that 
 * executes AndroMDApp.
 * 
 * @author Chad Brandon
 * @author Bob Fields
 */
public abstract class AbstractAndroMDAppMojo
    extends AbstractMojo
{
    
    /**
     * The URI to an optional AndroMDApp configuration file.
     *
     * @parameter expression="${configuration.uri}"
     */
    private String configurationUri;
    
    /**
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    private MavenProject project;
    
    /**
     * The current user system settings for use in Maven. (allows us to pass the user
     * settings to the AndroMDA configuration).
     *
     * @parameter expression="${settings}"
     * @required
     * @readonly
     */
    protected Settings settings;

    /**
     * @parameter expression="${project.build.filters}"
     */
    protected List propertyFiles;
    
    /**
     * Set this to 'true' to bypass cartridge tests entirely. Its use is NOT RECOMMENDED, but quite convenient on occasion.
     *
     * @parameter expression="${maven.test.skip}"
     */
    protected boolean skip;

    /**
     *  Set this to 'true' to skip running tests, but still compile them. Its use is NOT RECOMMENDED, but quite convenient on occasion. 
     *
     * @parameter expression="${skipTests}"
     */
    protected boolean skipTests;

    /**
     * Set this to true to ignore a failure during testing. Its use is NOT RECOMMENDED, but quite convenient on occasion.
     *
     * @parameter expression="${maven.test.failure.ignore}" default-value="false"
     */
    protected boolean testFailureIgnore;

    /**
     * Whether or not processing should be skipped (this is if you want to completely skip code generation, i.e. if 
     * code is already generated and you are creating the site from already generated source code).
     * 
     * @parameter expression="${andromdapp.run.skip}"
     */
    protected boolean skipProcessing = false;

    /**
     * Collects and returns all properties as a Properties instance.
     * 
     * @return the properties including those from the project, settings, etc.
     * @throws IOException
     */
    private Properties getProperties()
        throws IOException
    {
        // System properties
        final Properties properties = new Properties();

        properties.put(
            "settings",
            this.settings);

        // - project properties
        properties.putAll(this.project.getProperties());
        if (this.propertyFiles != null)
        {
            for (final Iterator iterator = this.propertyFiles.iterator(); iterator.hasNext();)
            {
                final String propertiesFile = (String)iterator.next();
                final Properties projectProperties = PropertyUtils.loadPropertyFile(
                        new File(propertiesFile),
                        true,
                        true);
    
                properties.putAll(projectProperties);
            }
        }

        for (final Iterator iterator = properties.keySet().iterator(); iterator.hasNext();)
        {
            final String property = (String)iterator.next();
            final String value = this.replaceProperties(properties, ObjectUtils.toString(properties.get(property)));
            properties.put(property, value);
        }
        
        properties.putAll(System.getProperties());
        
        return properties;
    }

    /**
     * Replaces all properties having the style
     * <code>${some.property}</code> with the value
     * of the specified property if there is one.
     *
     * @param string the string to perform replacement on.
     * @return this.replaceProperties(this.getProperties(), string);
     * @throws IOException 
     */
    protected String replaceProperties(final String string)
        throws IOException
    {
        return this.replaceProperties(this.getProperties(), string);
    }
    
    /**
     * Retrieves the interpolated {@link #configurationUri} contents as a String.
     * 
     * @return the contents of the configuration as a string.
     * @throws MojoExecutionException
     * @throws IOException
     */
    protected String getConfigurationContents() throws MojoExecutionException, IOException
    {
        String contents = null;
        if (StringUtils.isNotBlank(this.configurationUri))
        {
            final URL configuration = ResourceUtils.toURL(this.configurationUri);
            if (configuration == null)
            {
                throw new MojoExecutionException("No configuration could be loaded from --> '" + this.configurationUri + '\'');
            }
            contents = this.replaceProperties(ResourceUtils.getContents(configuration));
        }
        return contents;
    }
    
    /**
     * The begin token for interpolation (we need it different 
     * than what Maven uses so that Maven variables aren't replace).
     */
    private static final String BEGIN_TOKEN = "$${";
    
    /**
     * The end token for interpolation.
     */
    private static final String END_TOKEN = "}";
    
    /**
     * Replaces all properties having the style
     * <code>${some.property}</code> with the value
     * of the specified property if there is one.
     *
     * @param properties the properties used to perform the replacement.
     * @param fileContents the fileContents to perform replacement on.
     */
    private String replaceProperties(final Properties properties, final String string) throws IOException
    {
        final StringReader stringReader = new StringReader(string);
        InterpolationFilterReader reader = new InterpolationFilterReader(stringReader, properties, "${", "}");
        reader.reset();
        reader = new InterpolationFilterReader(
                reader,
                new BeanProperties(this.project),
                BEGIN_TOKEN,
                END_TOKEN);
        reader = new InterpolationFilterReader(
                reader,
                new BeanProperties(this.project),
                BEGIN_TOKEN,
                END_TOKEN);
        return ResourceUtils.getContents(reader);
    }

}
