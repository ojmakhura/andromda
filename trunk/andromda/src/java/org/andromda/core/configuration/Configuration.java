package org.andromda.core.configuration;

import org.andromda.core.common.XmlObjectFactory;
import org.andromda.core.mapping.Mappings;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;

import java.net.URL;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;


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
        return (Configuration)XmlObjectFactory.getInstance(Configuration.class).getObject(uri);
    }

    /**
     * Gets a Configuration instance from the given <code>stream</code>.
     *
     * @param stream the InputStream containing the configuration file.
     * @return the configured instance.
     */
    public final static Configuration getInstance(final InputStream stream)
    {
        return (Configuration)XmlObjectFactory.getInstance(Configuration.class).getObject(
            new InputStreamReader(stream));
    }

    /**
     * Gets a Configuration instance from the given <code>string</code>.
     *
     * @param stream the String containing the configuration.
     * @return the configured instance.
     */
    public final static Configuration getInstance(final String string)
    {
        return (Configuration)XmlObjectFactory.getInstance(Configuration.class).getObject(string);
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
     * Stores the models for this Configuration instance.
     */
    private final Collection models = new ArrayList();

    /**
     * Adds the URI of a model to this configuration.
     *
     * @param modelUri the URI of the model.
     */
    public void addModel(final Model model)
    {
        this.models.add(model);
    }

    /**
     * Gets the mode instances belonging to this configuration.
     *
     * @return the collection of model instances.
     */
    public Model[] getModels()
    {
        return (Model[])this.models.toArray(new Model[0]);
    }

    /**
     * Stores the transformations for this Configuration instance.
     */
    private final Collection transformations = new ArrayList();

    /**
     * Adds a transformation to this configuration instance.
     *
     * @param transformation the transformation instance to add.
     */
    public void addTransformation(final Transformation transformation)
    {
        this.transformations.add(transformation);
    }

    /**
     * Gets the transformations belonging to this configuration.
     *
     * @return the array of {@link Transformation} instances.
     */
    public Transformation[] getTransformations()
    {
        return (Transformation[])this.transformations.toArray(new Transformation[0]);
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
    private Namespace[] getNamespaces()
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
     * @param location a file location.
     */
    public void addMappingsSearchLocation(final String location)
    {
        if (location != null)
        {
            this.mappingsSearchLocations.add(location);
        }
    }

    /**
     * Gets the mappings searach location for this configuration instance.
     *
     * @return the mappings search locations.
     */
    String[] getMappingsSearchLocations()
    {
        return (String[])this.mappingsSearchLocations.toArray(new String[0]);
    }

    /**
     * Initializes the namespaces with the namespaces from
     * this configuration.
     */
    private final void initializeNamespaces()
    {
        final Namespaces namespaces = Namespaces.instance();
        namespaces.clear();
        namespaces.addNamespaces(this.getNamespaces());
    }

    /**
     * Loads all mappings from the specified mapping search locations If the location points to a directory the directory
     * contents will be loaded, otherwise just the mapping itself will be loaded.
     */
    private final void initializeMappings()
    {
        final Collection mappingsLocations = new ArrayList();
        if (mappingsLocations != null)
        {
            final String[] locations = this.getMappingsSearchLocations();
            final int locationNumber = locations.length;
            for (int ctr = 0; ctr < locationNumber; ctr++)
            {
                final File mappingsPath = new File(locations[ctr]);
                if (mappingsPath.isDirectory())
                {
                    final File[] mappingsFiles = mappingsPath.listFiles();
                    if (mappingsFiles != null)
                    {
                        for (int ctr2 = 0; ctr2 < mappingsFiles.length; ctr2++)
                        {
                            mappingsLocations.add(mappingsFiles[ctr2]);
                        }
                    }
                }
                else
                {
                    mappingsLocations.add(mappingsPath);
                }
            }
            for (final Iterator iterator = mappingsLocations.iterator(); iterator.hasNext();)
            {
                try
                {
                    Mappings.addLogicalMappings(((File)iterator.next()).toURL());
                }
                catch (final Throwable throwable)
                {
                    // ignore the exception (probably means its a file
                    // other than a mapping and in that case we don't care
                }
            }
        }
    }
}