package org.andromda.core.configuration;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

import org.andromda.core.common.XmlObjectFactory;

/**
 * This object is configured from the AndroMDA configuration
 * XML file.  Its used to configure AndroMDA before modeling
 * processing occurs.
 * 
 * @author Chad Brandon
 */
public class Configuration
{
    /**
     * Gets a Configuration instance from the given <code>uri</code>.
     * 
     * @param uri the URI to the configuration file.
     * @return the configured instance.
     */
    public final static Configuration getInstance(URL uri)
    {
        final XmlObjectFactory factory = XmlObjectFactory.getInstance(Configuration.class);
        return (Configuration)factory.getObject(uri);
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
    public void addModel(final URL modelUri)
    {
        this.models.add(modelUri);
    }    
    
    /**
     * Gets the models belonging to this configuration.
     * 
     * @return the collection of models URIs.
     */
    public Collection getModels()
    {
        return this.models;
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
     * @return the collection of {@link Transformation} instances.
     */
    public Collection getTransformations()
    {
        return this.transformations;
    }
    
    /**
     * Adds a namespace to this configuration.
     * 
     * @param namespace the configured namespace to add.
     */
    public void addNamespace(final Namespace namespace)
    {
        Namespaces.instance().addNamespace(namespace);
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
    public Collection getProperties()
    {
        return this.properties;
    }
    
    /**
     * Stores the model package instances for this configuration.
     */
    private final ModelPackages modelPackages = new ModelPackages();
    
    /**
     * Adds a model package to this configuration instance.
     * 
     * @param modelPackage the model package to add.
     */
    public void addModelPackage(final ModelPackage modelPackage)
    {
        this.modelPackages.addPackage(modelPackage);
    }
    
    /**
     * Gets the model packages belonging to this configuration.
     * 
     * @return the model packages instance.
     */
    public ModelPackages getModelPackages()
    {
        return this.modelPackages;
    }
    
    /**
     * The locations in which to search for module.
     */
    private final Collection moduleSearchLocations = new ArrayList();
    
    /**
     * Adds a module search location (these are the locations 
     * in which a search for module is performed).
     * 
     * @param location a file location.
     */
    public void addModuleSearchLocation(final String location)
    {
        this.moduleSearchLocations.add(location);
    }
    
    /**
     * Gets the module searach location for this configuration instance.
     * 
     * @return the module search locations.
     */
    public Collection getModuleSearchLocations()
    {
        return this.moduleSearchLocations;
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
        this.mappingsSearchLocations.add(location);
    }
    
    /**
     * Gets the mappings searach location for this configuration instance.
     * 
     * @return the mappings search locations.
     */
    public Collection getMappingsSearchLocations()
    {
        return this.mappingsSearchLocations;
    }
}
