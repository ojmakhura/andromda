package org.andromda.core.configuration;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;

import java.net.URL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import org.andromda.core.common.ResourceUtils;
import org.andromda.core.common.XmlObjectFactory;
import org.andromda.core.mapping.Mappings;
import org.apache.commons.lang.StringUtils;


/**
 * This object is configured from the AndroMDA configuration
 * XML file.  Its used to configure AndroMDA before modeling
 * processing occurs.
 *
 * @author Chad Brandon
 */
public class Configuration
    implements Serializable
{
    /**
     * Gets a Configuration instance from the given <code>uri</code>.
     *
     * @param uri the URI to the configuration file.
     * @return the configured instance.
     */
    public final static Configuration getInstance(final URL uri)
    {
        final Configuration configuration =
            (Configuration)XmlObjectFactory.getInstance(Configuration.class).getObject(uri);
        configuration.setContents(ResourceUtils.getContents(uri));
        return configuration;
    }

    /**
     * Gets a Configuration instance from the given <code>stream</code>.
     *
     * @param stream the InputStream containing the configuration file.
     * @return the configured instance.
     */
    public final static Configuration getInstance(final InputStream stream)
    {
        final Configuration configuration =
            (Configuration)XmlObjectFactory.getInstance(Configuration.class).getObject(new InputStreamReader(stream));
        configuration.setContents(ResourceUtils.getContents(new InputStreamReader(stream)));
        return configuration;
    }

    /**
     * Gets a Configuration instance from the given <code>string</code>.
     *
     * @param string the String containing the configuration.
     * @return the configured instance.
     */
    public final static Configuration getInstance(final String string)
    {
        final Configuration configuration =
            (Configuration)XmlObjectFactory.getInstance(Configuration.class).getObject(string);
        configuration.setContents(string);
        return configuration;
    }

    /**
     * Initializes this configuration instance.
     */
    public void initialize()
    {
        this.initializeNamespaces();
        this.initializeMappings();
    }

    /**
     * Stores the repositories for this Configuration instance.
     */
    private final Collection repositories = new ArrayList();

    /**
     * Adds the repository to this configuration.
     *
     * @param repository the repository instance.
     */
    public void addRepository(final Repository repository)
    {
        this.repositories.add(repository);
    }

    /**
     * Gets the repository instances belonging to this configuration.
     *
     * @return the collection of repository instances.
     */
    public Repository[] getRepositories()
    {
        return (Repository[])this.repositories.toArray(new Repository[0]);
    }

    /**
     * Stores the configuration namespaces.
     */
    private final Collection namespaces = new ArrayList();

    /**
     * Adds a namespace to this configuration.
     *
     * @param namespace the configured namespace to add.
     */
    public void addNamespace(final Namespace namespace)
    {
        this.namespaces.add(namespace);
    }

    /**
     * Gets the configuration namespaces.
     *
     * @return the array of {@link Namespace} instances.
     */
    public Namespace[] getNamespaces()
    {
        return (Namespace[])this.namespaces.toArray(new Namespace[0]);
    }

    /**
     * Stores the properties for this configuration (these
     * gobally configure AndroMDA).
     */
    private final Collection properties = new ArrayList();

    /**
     * Adds a property to this configuration instance.
     *
     * @param property the property to add.
     */
    public void addProperty(final Property property)
    {
        this.properties.add(property);
    }

    /**
     * Gets the properties belonging to this configuration.
     *
     * @return the collection of {@link Property} instances.
     */
    public Property[] getProperties()
    {
        return (Property[])this.properties.toArray(new Property[0]);
    }

    /**
     * Stores the AndroMDA server configuration information.
     */
    private Server server;

    /**
     * Sets the server instance for this configuration.
     *
     * @param server the information which configures the AndroMDA server.
     */
    public void setServer(final Server server)
    {
        this.server = server;
    }

    /**
     * Gets the server instance for this configuration.
     * The {@link Server} holds the information to configure
     * the AndroMDA server.
     *
     * @return the andromda server.
     */
    public Server getServer()
    {
        return this.server;
    }

    /**
     * The locations in which to search for mappings.
     */
    private final Collection mappingsSearchLocations = new ArrayList();

    /**
     * Adds a mappings search location (these are the locations
     * in which a search for mappings is performed).
     *
     * @param location a location path.
     * @see #addMappingsSearchLocation(String)
     */
    public void addMappingsSearchLocation(final Location location)
    {
        if (location != null)
        {
            this.mappingsSearchLocations.add(location);
        }
    }

    /**
     * Adds a mappings search location path (a location
     * without a pattern defined).
     *
     * @param path a location path.
     * @see #addMappingsSearchLocation(Location)
     */
    public void addMappingsSearchLocation(final String path)
    {
        if (path != null)
        {
            final Location location = new Location();
            location.setPath(path);
            this.mappingsSearchLocations.add(location);
        }
    }

    /**
     * Gets the mappings search locations for this configuration instance.
     *
     * @return the mappings search locations.
     */
    public Location[] getMappingsSearchLocations()
    {
        return (Location[])this.mappingsSearchLocations.toArray(new Location[0]);
    }

    /**
     * Stores the contents of the configuration as a string.
     */
    private String contents = null;

    /**
     * Gets the URI from which this instance was
     * configured or null (it it was not set).
     * @return
     */
    public String getContents()
    {
        return this.contents;
    }
    
    /**
     * Clears out any caches used by this configuration.
     */
    public static void clearCaches()
    {
        Model.clearLastModifiedTimes();
    }

    /**
     * Sets the contents of this configuration as a
     * string.
     * @param contents the contents of this configuration as a string.
     */
    private void setContents(final String contents)
    {
        this.contents = StringUtils.trimToEmpty(contents);
    }

    /**
     * Initializes the namespaces with the namespaces from
     * this configuration.
     */
    private void initializeNamespaces()
    {
        final Namespaces namespaces = Namespaces.instance();
        namespaces.clear();
        namespaces.addNamespaces(this.getNamespaces());
    }

    /**
     * Loads all mappings from the specified mapping search locations If the location points to a directory the directory
     * contents will be loaded, otherwise just the mapping itself will be loaded.
     */
    private void initializeMappings()
    {
        final Collection mappingsLocations = new ArrayList();
        if (mappingsLocations != null)
        {
            final Location[] locations = this.getMappingsSearchLocations();
            final int locationNumber = locations.length;
            for (int ctr = 0; ctr < locationNumber; ctr++)
            {
                mappingsLocations.addAll(Arrays.asList(locations[ctr].getFiles()));
            }
            for (final Iterator iterator = mappingsLocations.iterator(); iterator.hasNext();)
            {
                try
                {
                    Mappings.addLogicalMappings(((File)iterator.next()).toURL());
                }
                catch (final Throwable throwable)
                {
                    // - ignore the exception (probably means its a file
                    //   other than a mapping and in that case we don't care)
                }
            }
        }
    }
}