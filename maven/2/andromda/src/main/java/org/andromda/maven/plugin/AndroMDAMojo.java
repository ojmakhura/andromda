package org.andromda.maven.plugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.andromda.core.AndroMDA;
import org.andromda.core.common.ExceptionUtils;
import org.andromda.core.common.ResourceUtils;
import org.andromda.core.configuration.Configuration;
import org.andromda.core.configuration.Model;
import org.andromda.core.configuration.Repository;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
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
 * @requiresDependencyResolution runtime
 */
public class AndroMDAMojo
    extends AbstractMojo
{
    /**
     * This is the URI to the AndroMDA configuration file.
     *
     * @parameter expression="file:${basedir}/conf/andromda.xml"
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
     * The current user system settings for use in Maven. (allows us to pass the user
     * settings to the AndroMDA configuration).
     *
     * @parameter expression="${settings}"
     * @required
     * @readonly
     */
    private Settings settings;

    /**
     * The path to the mappings within the plugin.
     */
    private static final String MAPPINGS_PATH = "META-INF/andromda/mappings";

    /**
     * Whether or not a last modified check should be performed before running AndroMDA again.
     *
     * @parameter expression="false"
     * @required
     */
    private boolean lastModifiedCheck;

    /**
     * The directory to which the build source is located (any generated source).
     *
     * @parameter expression="${project.build.directory}/src/main/java"
     */
    private String buildSourceDirectory;

    public void execute()
        throws MojoExecutionException
    {
        try
        {
            final URL configurationUri = ResourceUtils.toURL(this.configurationUri);
            final Configuration configuration = this.getConfiguration(configurationUri);
            boolean execute = true;
            if (this.lastModifiedCheck)
            {
                final File directory = new File(this.buildSourceDirectory);
                execute = ResourceUtils.modifiedAfter(
                        ResourceUtils.getLastModifiedTime(configurationUri),
                        directory);
                if (!execute)
                {
                    final Repository[] repositories = configuration.getRepositories();
                    int repositoryCount = repositories.length;
                    for (int ctr = 0; ctr < repositoryCount; ctr++)
                    {
                        final Repository repository = repositories[ctr];
                        if (repository != null)
                        {
                            final Model[] models = repository.getModels();
                            final int modelCount = models.length;
                            for (int ctr2 = 0; ctr2 < modelCount; ctr2++)
                            {
                                final Model model = models[ctr2];
                                execute = ResourceUtils.modifiedAfter(
                                        model.getLastModified(),
                                        directory);
                                if (execute)
                                {
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            if (execute)
            {
                this.initializeClasspathFromRuntimeClassPathElements();
                final AndroMDA andromda = AndroMDA.newInstance();
                andromda.run(configuration);
                andromda.shutdown();
                final File buildSourceDirectory =
                    this.buildSourceDirectory != null ? new File(this.buildSourceDirectory) : null;
                if (buildSourceDirectory != null)
                {
                    this.project.addCompileSourceRoot(buildSourceDirectory.toString());
                }
            }
            else
            {
                this.getLog().info("Files are up-to-date, skipping AndroMDA execution");
            }
        }
        catch (Throwable throwable)
        {
            String message = "Error running AndroMDA";
            throwable = ExceptionUtils.getRootCause(throwable);
            if (throwable instanceof FileNotFoundException)
            {
                message = "No configuration could be loaded from --> '" + configurationUri + "'";
            }
            else if (throwable instanceof MalformedURLException)
            {
                message = "Configuration is not a valid URI --> '" + configurationUri + "'";
            }
            throw new MojoExecutionException(message, throwable);
        }
    }

    /**
     * Creates the Configuration instance from the {@link #configurationUri}
     * @return the configuration instance
     * @throws MalformedURLException if the URL is invalid.
     */
    private Configuration getConfiguration(final URL configurationUri)
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
            this.settings);

        // Project properties
        properties.putAll(this.project.getProperties());
        for (final Iterator iterator = propertyFiles.iterator(); iterator.hasNext();)
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
                new BeanProperties(project),
                "${",
                "}");
        reader = new InterpolationFilterReader(
                reader,
                new BeanProperties(settings),
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
    private void initializeClasspathFromRuntimeClassPathElements()
        throws DependencyResolutionRequiredException, MalformedURLException
    {
        final List classpathFiles = this.project.getRuntimeClasspathElements();
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

        final URLClassLoader loader = new URLClassLoader(classpathUrls,
                Thread.currentThread().getContextClassLoader());
        Thread.currentThread().setContextClassLoader(loader);
    }

    /**
     * @return Returns the configurationUri.
     */
    public String getConfigurationUri()
    {
        return configurationUri;
    }

    /**
     * @param configurationUri The configurationUri to set.
     */
    public void setConfigurationUri(String configurationUri)
    {
        this.configurationUri = configurationUri;
    }

    /**
     * @return Returns the project.
     */
    public MavenProject getProject()
    {
        return project;
    }

    /**
     * @param project The project to set.
     */
    public void setProject(MavenProject project)
    {
        this.project = project;
    }

    /**
     * @return Returns the propertyFiles.
     */
    public List getPropertyFiles()
    {
        return propertyFiles;
    }

    /**
     * @param propertyFiles The propertyFiles to set.
     */
    public void setPropertyFiles(List propertyFiles)
    {
        this.propertyFiles = propertyFiles;
    }

    /**
     * @return Returns the settings.
     */
    public Settings getSettings()
    {
        return settings;
    }

    /**
     * @param settings The settings to set.
     */
    public void setSettings(Settings settings)
    {
        this.settings = settings;
    }
}