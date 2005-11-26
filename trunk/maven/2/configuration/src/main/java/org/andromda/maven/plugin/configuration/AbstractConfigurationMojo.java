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
import org.apache.commons.lang.ObjectUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Plugin;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.resources.PropertyUtils;
import org.apache.maven.project.MavenProject;
import org.apache.maven.settings.Settings;
import org.codehaus.plexus.util.InterpolationFilterReader;


/**
 * An abstract Mojo for dealing with the AndroMDA configuration,
 * if a plugin needs to use the AndroMDA configuration, it should extend this
 * class.
 *
 * @author Chad Brandon
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
        final Properties properties = new Properties();

        properties.put(
            "settings",
            this.getSettings());

        // - project properties
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

        for (final Iterator iterator = properties.keySet().iterator(); iterator.hasNext();)
        {
            final String property = (String)iterator.next();
            final String value = this.replaceProperties(properties, ObjectUtils.toString(properties.get(property)).trim());
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
     */
    protected String replaceProperties(final String string)
        throws IOException
    {
        return this.replaceProperties(this.getProperties(), string);
    }
    
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
                new BeanProperties(this.getProject()),
                "${",
                "}");
        reader = new InterpolationFilterReader(
                reader,
                new BeanProperties(this.getSettings()),
                "${",
                "}");
        return ResourceUtils.getContents(reader);
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
     * Adds any dependencies to the current project from the plugin
     * having the given <code>pluginArtifactId</code>.
     *
     * @param pluginArtifactId the artifactId of the plugin of which to add its dependencies.
     * @param scope the artifact scope in which to add them (runtime, compile, etc).
     */
    protected void addPluginDependencies(final String pluginArtifactId, final String scope)
    {
        if (pluginArtifactId != null)
        {
            final List plugins = this.getPlugins();
            if (plugins != null && !plugins.isEmpty())
            {
                for (final Iterator iterator = plugins.iterator(); iterator.hasNext();)
                {
                    final Plugin plugin = (Plugin)iterator.next();
                    if (pluginArtifactId.equals(plugin.getArtifactId()))
                    {
                        final List dependencies = plugin.getDependencies();
                        if (dependencies != null)
                        {
                            for (final Iterator dependencyIterator = plugin.getDependencies().iterator();
                                dependencyIterator.hasNext();)
                            {
                                this.addDependency((Dependency)dependencyIterator.next(), scope);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Adds a dependency to the current project's dependencies.
     *
     * @param dependency
     */
    private void addDependency(final Dependency dependency, final String scope)
    {
        final ArtifactRepository localRepository = this.getLocalRepository();
        final MavenProject project = this.getProject();
        if (project != null && localRepository != null)
        {
            if (dependency != null)
            {
                final Artifact artifact =
                    this.getFactory().createArtifact(
                        dependency.getGroupId(),
                        dependency.getArtifactId(),
                        dependency.getVersion(),
                        scope,
                        dependency.getType());
                final File file = new File(
                        localRepository.getBasedir(),
                        localRepository.pathOf(artifact));
                artifact.setFile(file);
                project.getDependencies().add(dependency);
                project.getArtifacts().add(artifact);
            }
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

    /**
     * Gets the artifact factory used to construct any new required artifacts.
     */
    protected abstract ArtifactFactory getFactory();

    /**
     * Gets the current project's registered plugin implementations.
     *
     * @parameter expression="${project.build.plugins}"
     * @required
     * @readonlya
     */
    protected abstract List getPlugins();

    /**
     * Gets the current local repository instance.
     *
     * @return the local repository instance of the current project.
     */
    protected abstract ArtifactRepository getLocalRepository();
}