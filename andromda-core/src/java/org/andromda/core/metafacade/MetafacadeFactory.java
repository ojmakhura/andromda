package org.andromda.core.metafacade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import org.andromda.core.common.AndroMDALogger;
import org.andromda.core.common.ExceptionUtils;
import org.andromda.core.profile.Profile;
import org.apache.commons.collections.keyvalue.MultiKey;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * The factory in charge of constructing Metafacade instances. In order for a
 * metafacade (i.e. a facade around a meta model element) to be constructed, it
 * must be constructed through this factory.
 *
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen </a>
 * @author Chad Brandon
 * @author Peter Friese
 * @author Bob Fields
 */
public class MetafacadeFactory
    implements Serializable
{
    /**
     * Caches the registered properties used within metafacades.
     */
    private final Map<String, Map<String, Map<String, Object>>> metafacadeNamespaces = new LinkedHashMap<String, Map<String, Map<String, Object>>>();

    /**
     * The shared instance of this factory.
     */
    private static MetafacadeFactory instance = null;

    private MetafacadeFactory()
    {
        // make sure that nobody instantiates it
    }

    /**
     * Returns the facade factory singleton.
     *
     * @return the only instance
     */
    public static MetafacadeFactory getInstance()
    {
        if (instance == null)
        {
            instance = new MetafacadeFactory();
        }
        return instance;
    }

    /**
     * The metafacade cache for this factory.
     */
    private final MetafacadeCache cache = MetafacadeCache.newInstance();

    /**
     * The metafacade mappings instance for this factory.
     */
    private final MetafacadeMappings mappings = MetafacadeMappings.newInstance();

    /**
     * Performs any initialization required by the factory (i.e. discovering all
     * <code>metafacade</code> mappings, etc).
     */
    public void initialize()
    {
        this.mappings.initialize();
    }

    /**
     * The shared profile instance.
     */
    private final Profile profile = Profile.instance();

    /**
     * The namespace that is currently active (i.e. being used) within the factory
     */
    private String namespace;

    /**
     * Sets the active namespace. The AndroMDA core and each cartridge have their own namespace for metafacade
     * registration.
     *
     * @param namespace the name of the active namespace.
     */
    public void setNamespace(final String namespace)
    {
        this.namespace = namespace;
        this.profile.setNamespace(namespace);
        this.cache.setNamespace(this.namespace);
    }

    /**
     * Returns the name of the active namespace.
     *
     * @return String the namespace name
     */
    public String getNamespace()
    {
        if (this.namespace == null)
        {
            throw new MetafacadeFactoryException("This metafacade factory's namespace must be populated before " +
                "metafacade construction can occur");
        }
        return this.namespace;
    }

    /**
     * Returns a metafacade for a mappingObject, depending on its
     * <code>mappingClass</code> and (optionally) its <code>stereotypes</code>
     * and <code>context</code>.
     *
     * @param mappingObject the object used to map the metafacade (a meta model
     *        object or a metafacade itself).
     * @param context the name of the context the meta model element is
     *        registered under.
     * @return the new metafacade
     */
    public MetafacadeBase createMetafacade(
        final Object mappingObject,
        final String context)
    {
        return this.createMetafacade(
            mappingObject,
            context,
            null);
    }

    /**
     * Creates a metafacade given the <code>mappingObject</code>,
     * <code>contextName</code> and <code>metafacadeClass</code>.
     *
     * @param mappingObject the object used to map the metafacade (a meta model
     *        object or a metafacade itself).
     * @param context the name of the context the meta model element (if the
     *        mappObject is a meta model element) is registered under.
     * @param metafacadeClass if not null, it contains the name of the
     *        metafacade class to be used. This is used ONLY when instantiating
     *        super metafacades in an inheritance chain. The final metafacade
     *        will NEVER have a metafacadeClass specified (it will ALWAYS be
     *        null).
     * @return the new metafacade
     */
    private MetafacadeBase createMetafacade(
        final Object mappingObject,
        final String context,
        Class metafacadeClass)
    {
        final String methodName = "MetafacadeFactory.createMetafacade";
        ExceptionUtils.checkNull(
            "mappingObject",
            mappingObject);

        // - register the namespace properties (if they haven't been)
        this.registerNamespaceProperties();

        // if the mappingObject is REALLY a metafacade, just return it
        if (mappingObject instanceof MetafacadeBase)
        {
            return (MetafacadeBase)mappingObject;
        }
        try
        {
            final Collection<String> stereotypes = this.getModel().getStereotypeNames(mappingObject);
            if (this.getLogger().isDebugEnabled())
            {
                this.getLogger().debug("mappingObject stereotypes --> '" + stereotypes + "'");
            }

            MetafacadeMapping mapping = null;
            if (metafacadeClass == null)
            {
                final MetafacadeMappings modelMetafacadeMappings = this.getModelMetafacadeMappings();
                mapping = modelMetafacadeMappings.getMetafacadeMapping(
                    mappingObject,
                    this.getNamespace(),
                    context,
                    stereotypes);
                if (this.getLogger().isDebugEnabled())
                {
                    this.getLogger().debug("mappingObject getModelMetafacadeMappings for " + mappingObject + " namespace " + this.getNamespace() + " context " + context);
                }
                if (mapping != null)
                {
                    metafacadeClass = mapping.getMetafacadeClass();
                }
                else
                {
                    // get the default since no mapping was found.
                    metafacadeClass = modelMetafacadeMappings.getDefaultMetafacadeClass(this.getNamespace());
                    if (this.getLogger().isDebugEnabled())
                    {
                        this.getLogger().warn(
                            "Meta object model class '" + mappingObject.getClass() +
                            "' has no corresponding meta facade class, default is being used --> '" + metafacadeClass +
                            "'");
                    }
                }
            }

            if (metafacadeClass == null)
            {
                throw new MetafacadeMappingsException(methodName + " metafacadeClass was not retrieved from mappings" +
                    " or specified as an argument in this method for mappingObject --> '" + mappingObject + "'");
            }
            final MetafacadeBase metafacade = this.getMetafacade(
                    metafacadeClass,
                    mappingObject,
                    context,
                    mapping);

            // IMPORTANT: initialize each metafacade ONLY once (otherwise we
            // get stack overflow errors)
            if (metafacade != null && !metafacade.isInitialized())
            {
                metafacade.setInitialized();
                metafacade.initialize();
            }
            return metafacade;
        }
        catch (final Throwable throwable)
        {
            final String message =
                "Failed to construct a meta facade of type '" + metafacadeClass + "' with mappingObject of type --> '" +
                mappingObject.getClass() + "'";
            this.getLogger().error(message);
            throw new MetafacadeFactoryException(message, throwable);
        }
    }

    /**
     * Gets the model metafacade mappings (the mappings that correspond
     * to the current metafacade model namespace).
     *
     * @return the model metafacade mappings.
     */
    private MetafacadeMappings getModelMetafacadeMappings()
    {
        return this.mappings.getModelMetafacadeMappings(this.metafacadeModelNamespace);
    }

    /**
     * Validates all metafacades for the current namespace
     * and collects the messages in the internal validation messages
     * collection.
     *
     * @see #getValidationMessages()
     */
    public void validateAllMetafacades()
    {
        for (final MetafacadeBase metafacadeBase : this.getAllMetafacades())
        {
            metafacadeBase.validate(this.validationMessages);
        }
    }

    /**
     * Creates a metafacade from the passed in <code>mappingObject</code>, and
     * <code>mapping</code> instance.
     *
     * @param mappingObject the mapping object for which to create the
     *        metafacade.
     * @param mapping the mapping from which to create the metafacade
     * @return the metafacade, or null if it can't be created.
     */
    protected MetafacadeBase createMetafacade(
        final Object mappingObject,
        final MetafacadeMapping mapping)
    {
        try
        {
            return this.getMetafacade(
                mapping.getMetafacadeClass(),
                mappingObject,
                mapping.getContext(),
                mapping);
        }
        catch (final Throwable throwable)
        {
            final String message =
                "Failed to construct a meta facade of type '" + mapping.getMetafacadeClass() +
                "' with mappingObject of type --> '" + mapping.getMappingClassName() + "'";
            this.getLogger().error(message);
            throw new MetafacadeFactoryException(message, throwable);
        }
    }

    /**
     * Retrieves (if one has already been constructed) or constructs a new
     * <code>metafacade</code> from the given <code>metafacadeClass</code>
     * and <code>mappingObject</code>.
     *
     * @param metafacadeClass the metafacade class.
     * @param mappingObject the object to which the metafacade is mapped.
     * @param context the context to which the metafacade applies
     * @param mapping the optional MetafacadeMapping instance from which the
     *        metafacade is mapped.
     * @return the new (or cached) metafacade.
     * @throws Exception if any error occurs during metafacade creation
     */
    private MetafacadeBase getMetafacade(
        final Class metafacadeClass,
        final Object mappingObject,
        final String context,
        final MetafacadeMapping mapping)
        throws Exception
    {
        MetafacadeBase metafacade = this.cache.get(
                mappingObject,
                metafacadeClass);
        if (metafacade == null)
        {
            final MultiKey key = new MultiKey(mappingObject, metafacadeClass);
            if (!this.metafacadesInCreation.contains(key))
            {
                this.metafacadesInCreation.add(
                    key);
                if (mapping != null && mapping.isContextRoot())
                {
                    metafacade = MetafacadeUtils.constructMetafacade(
                            metafacadeClass,
                            mappingObject,
                            null);
                    // set whether or not this metafacade is a context root
                    metafacade.setContextRoot(mapping.isContextRoot());
                }
                else
                {
                    metafacade = MetafacadeUtils.constructMetafacade(
                            metafacadeClass,
                            mappingObject,
                            context);
                }
                this.metafacadesInCreation.remove(key);

                this.cache.add(
                    mappingObject,
                    metafacade);
            }
        }

        if (metafacade != null)
        {
            // if the requested metafacadeClass is different from the one in the mapping, contextRoot should be reset
            if (mapping != null && !mapping.getMetafacadeClass().equals(metafacadeClass))
            {
                metafacade.setContextRoot(false);
                metafacade.resetMetafacadeContext(context);
            }
            // we need to set some things each time
            // we change a metafacade's namespace
            final String metafacadeNamespace = metafacade.getNamespace();
            if (metafacadeNamespace == null || !metafacadeNamespace.equals(this.getNamespace()))
            {
                // assign the logger and active namespace
                metafacade.setNamespace(this.getNamespace());
                metafacade.setLogger(this.getLogger());
            }
        }
        return metafacade;
    }

    /**
     * Stores the metafacades being created, so that we don't get stuck in
     * endless recursion during creation.
     */
    private final Collection<MultiKey> metafacadesInCreation = new ArrayList<MultiKey>();

    /**
     * Returns a metafacade for a mappingObject, depending on its <code>mappingClass</code>.
     *
     * @param mappingObject the object which is used to map to the metafacade
     * @return MetafacadeBase the facade object (not yet attached to mappingClass object)
     */
    public MetafacadeBase createMetafacade(final Object mappingObject)
    {
        return this.createMetafacade(
            mappingObject,
            null,
            null);
    }

    /**
     * Create a facade implementation object for a mappingObject. The facade
     * implementation object must be found in a way that it implements the
     * interface <code>interfaceName</code>.
     *
     * @param interfaceName the name of the interface that the implementation
     *        object has to implement
     * @param mappingObject the mappingObject for which a facade shall be
     *        created
     * @param context the context in which this metafacade will be created.
     * @return MetafacadeBase the metafacade
     */
    public MetafacadeBase createFacadeImpl(
        final String interfaceName,
        final Object mappingObject,
        final String context)
    {
        ExceptionUtils.checkEmpty(
            "interfaceName",
            interfaceName);
        ExceptionUtils.checkNull(
            "mappingObject",
            mappingObject);

        Class metafacadeClass = null;
        try
        {
            metafacadeClass = this.metafacadeImpls.getMetafacadeImplClass(interfaceName);
            return this.createMetafacade(
                mappingObject,
                context,
                metafacadeClass);
        }
        catch (final Throwable throwable)
        {
            final String message =
                "Failed to construct a meta facade of type '" + metafacadeClass + "' with mappingObject of type --> '" +
                mappingObject.getClass().getName() + "'";
            this.getLogger().error(message);
            throw new MetafacadeFactoryException(message, throwable);
        }
    }

    /**
     * Returns a metafacade for each mappingObject, contained within the
     * <code>mappingObjects</code> collection depending on its
     * <code>mappingClass</code> and (optionally) its <code>stereotypes</code>,
     * and <code>contextName</code>.
     *
     * @param mappingObjects the meta model element.
     * @param contextName the name of the context the meta model element is
     *        registered under.
     * @return the Collection of newly created Metafacades.
     */
    protected Collection<MetafacadeBase> createMetafacades(
        final Collection mappingObjects,
        final String contextName)
    {
        final Collection<MetafacadeBase> metafacades = new ArrayList<MetafacadeBase>();
        if (mappingObjects != null && !mappingObjects.isEmpty())
        {
            for (final Object mappingObject : mappingObjects)
            {
                metafacades.add(this.createMetafacade(
                        mappingObject,
                        contextName,
                        null));
            }
        }
        return metafacades;
    }

    /**
     * Returns a metafacade for each mappingObject, contained within the
     * <code>mappingObjects</code> collection depending on its
     * <code>mappingClass</code>.
     *
     * @param mappingObjects the objects used to map the metafacades (can be a
     *        meta model element or an actual metafacade itself).
     * @return Collection of metafacades
     */
    public Collection<MetafacadeBase> createMetafacades(final Collection mappingObjects)
    {
        return this.createMetafacades(
            mappingObjects,
            null);
    }

    /**
     * The model facade which provides access to the underlying meta model.
     */
    private ModelAccessFacade model;

    /**
     * Gets the model which provides access to the underlying model and is used
     * to construct metafacades.
     *
     * @return the model access facade.
     */
    public ModelAccessFacade getModel()
    {
        if (this.model == null)
        {
            throw new MetafacadeFactoryException("This metafacade factory's model must be populated before " +
                "metafacade construction can occur");
        }
        return this.model;
    }

    /**
     * The shared metafacade impls instance.
     */
    private MetafacadeImpls metafacadeImpls = MetafacadeImpls.instance();

    /**
     * Stores the namespace that contains the metafacade model implementation.
     */
    private String metafacadeModelNamespace;

    /**
     * The model access facade instance (provides access to the meta model).
     *
     * @param model the model
     * @param metafacadeModelNamespace the namespace that contains the metafacade facade implementation.
     */
    public void setModel(
        final ModelAccessFacade model,
        final String metafacadeModelNamespace)
    {
        this.metafacadeModelNamespace = metafacadeModelNamespace;

        // - set the model type as the namespace for the metafacade impls so we have
        //   access to the correct metafacade classes
        this.metafacadeImpls.setMetafacadeModelNamespace(metafacadeModelNamespace);
        this.model = model;
    }

    /**
     * Gets the correct logger based on whether or not an namespace logger should be used
     *
     * @return the logger
     */
    final Logger getLogger()
    {
        return AndroMDALogger.getNamespaceLogger(this.getNamespace());
    }

    /**
     * Registers a property with the specified <code>name</code> in the given
     * <code>namespace</code>.
     *
     * @param namespace the namespace in which the property is stored.
     * @param metafacadeName the name of the metafacade under which the property is registered
     * @param name the name of the property
     * @param value to give the property
     */
    final void registerProperty(
        final String namespace,
        final String metafacadeName,
        final String name,
        final Object value)
    {
        ExceptionUtils.checkEmpty(
            "name",
            name);
        Map<String, Map<String, Object>> metafacadeNamespace = this.metafacadeNamespaces.get(namespace);
        if (metafacadeNamespace == null)
        {
            metafacadeNamespace = new LinkedHashMap<String, Map<String, Object>>();
            this.metafacadeNamespaces.put(
                namespace,
                metafacadeNamespace);
        }
        Map<String, Object> propertyNamespace = metafacadeNamespace.get(metafacadeName);
        if (propertyNamespace == null)
        {
            propertyNamespace = new LinkedHashMap<String, Object>();
            metafacadeNamespace.put(
                metafacadeName,
                propertyNamespace);
        }
        propertyNamespace.put(
            name,
            value);
    }

    /**
     * Registers a property with the specified <code>name</code> in the namespace
     * that is currently set within the factory.
     *
     * @param metafacadeName the name of the metafacade under which the property is registered
     * @param name the name of the property
     * @param value to give the property
     */
    final void registerProperty(
        final String metafacadeName,
        final String name,
        final Object value)
    {
        this.registerProperty(
            this.getNamespace(),
            metafacadeName,
            name,
            value);
    }

    /**
     * Gets the metafacade's property namespace (or returns null if hasn't be registered).
     *
     * @param metafacade the metafacade
     * @return the metafacade's namespace
     */
    private Map<String, Object> getMetafacadePropertyNamespace(final MetafacadeBase metafacade)
    {
        Map<String, Object> metafacadeNamespace = null;
        if (metafacade != null)
        {
            Map<String, Map<String, Object>> namespace = this.metafacadeNamespaces.get(this.getNamespace());
            if (namespace != null)
            {
                metafacadeNamespace = namespace.get(metafacade.getName());
            }
        }
        return metafacadeNamespace;
    }

    /**
     * Returns true if this property is registered under the given
     * <code>namespace</code>, false otherwise.
     *
     * @param metafacade the metafacade to search.
     * @param name the name of the property.
     * @return true if the property is registered, false otherwise.
     */
    final boolean isPropertyRegistered(
        final MetafacadeBase metafacade,
        final String name)
    {
        final Map<String, Object> propertyNamespace = this.getMetafacadePropertyNamespace(metafacade);
        return propertyNamespace != null && propertyNamespace.containsKey(name);
    }

    /**
     * Finds the first property having the given <code>namespaces</code>, or
     * <code>null</code> if the property can <strong>NOT </strong> be found.
     *
     * @param metafacade the metafacade to search.
     * @param name the name of the property to find.
     * @return the property or null if it can't be found.
     */
    private Object findProperty(
        final MetafacadeBase metafacade,
        final String name)
    {
        final Map<String, Object> propertyNamespace = this.getMetafacadePropertyNamespace(metafacade); //final Map<String, Map>
        return propertyNamespace != null ? propertyNamespace.get(name) : null;
    }

    /**
     * Gets the registered property registered under the <code>namespace</code>
     * with the <code>name</code>
     *
     * @param metafacade the metafacade to search
     * @param name the name of the property to check.
     * @return the registered property
     */
    final Object getRegisteredProperty(
        final MetafacadeBase metafacade,
        final String name)
    {
        final String methodName = "MetafacadeFactory.getRegisteredProperty";
        final Object registeredProperty = this.findProperty(
                metafacade,
                name);
        if (registeredProperty == null && !this.isPropertyRegistered(
                metafacade,
                name))
        {
            throw new MetafacadeFactoryException(methodName + " - no property '" + name +
                "' registered under metafacade '" + metafacade.getName() + "' for namespace '" + this.getNamespace() +
                "'");
        }
        return registeredProperty;
    }

    /**
     * The validation messages that have been collected during the
     * execution of this factory.
     */
    private final Collection<ModelValidationMessage> validationMessages = new LinkedHashSet<ModelValidationMessage>();

    /**
     * Gets the list of all validation messages collection during model processing.
     *
     * @return Returns the validationMessages.
     * @see #validateAllMetafacades()
     */
    public List<ModelValidationMessage> getValidationMessages()
    {
        return new ArrayList<ModelValidationMessage>(this.validationMessages);
    }

    /**
     * Stores the collection of all metafacades for
     * each namespace.
     */
    private final Map<String, Collection<MetafacadeBase>> allMetafacades = new LinkedHashMap<String, Collection<MetafacadeBase>>();

    /**
     * <p>
     * Gets all metafacades for the entire model for the
     * current namespace set within the factory.
     * </p>
     * <p>
     * <strong>NOTE:</strong> The model package filter is applied
     * before returning the results (if defined within the factory).
     * </p>
     *
     * @return all metafacades
     */
    public Collection<MetafacadeBase> getAllMetafacades()
    {
        final String namespace = this.getNamespace();
        Collection<MetafacadeBase> metafacades = null;
        if (this.getModel() != null)
        {
            metafacades = allMetafacades.get(namespace);
            if (metafacades == null)
            {
                metafacades = this.createMetafacades(this.getModel().getModelElements());
                allMetafacades.put(
                    namespace,
                    metafacades);
            }
            if (metafacades != null)
            {
                metafacades = new ArrayList<MetafacadeBase>(metafacades);
            }
        }
        return metafacades;
    }

    /**
     * Caches the metafacades by stereotype.
     */
    private final Map<String, Map<String, Collection<MetafacadeBase>>> metafacadesByStereotype 
    = new LinkedHashMap<String, Map<String, Collection<MetafacadeBase>>>();

    /**
     * <p>
     * Gets all metafacades for the entire model having the given
     * stereotype.
     * </p>
     * <p>
     * <strong>NOTE:</strong> The model package filter is applied
     * before returning the results (if defined within the factory).
     * </p>
     *
     * @param stereotype the stereotype by which to perform the search.
     * @return the metafacades having the given <code>stereotype</code>.
     */
    public Collection<MetafacadeBase> getMetafacadesByStereotype(final String stereotype)
    {
        final String namespace = this.getNamespace();
        Collection<MetafacadeBase> metafacades = null;
        if (this.getModel() != null)
        {
            Map<String, Collection<MetafacadeBase>> stereotypeMetafacades = this.metafacadesByStereotype.get(namespace);
            if (stereotypeMetafacades == null)
            {
                stereotypeMetafacades = new LinkedHashMap<String, Collection<MetafacadeBase>>();
            }
            metafacades = stereotypeMetafacades.get(stereotype);
            if (metafacades == null)
            {
                metafacades = this.createMetafacades(this.getModel().findByStereotype(stereotype));
                stereotypeMetafacades.put(
                    stereotype,
                    metafacades);
                this.metafacadesByStereotype.put(
                    namespace,
                    stereotypeMetafacades);
            }
            if (metafacades != null)
            {
                metafacades = new ArrayList<MetafacadeBase>(metafacades);
            }
        }
        return metafacades;
    }

    /**
     * Performs shutdown procedures for the factory. This should be called <strong>ONLY</code> when model processing has
     * completed.
     */
    public void shutdown()
    {
        this.clearCaches();
        this.metafacadeNamespaces.clear();
        this.mappings.shutdown();
        this.model = null;
        instance = null;

        // - shutdown the profile instance
        this.profile.shutdown();
    }

    /**
     * Registers all namespace properties (if required).
     */
    private void registerNamespaceProperties()
    {
        // - only register them if they already aren't registered
        if (this.metafacadeNamespaces.isEmpty())
        {
            if (StringUtils.isNotBlank(this.metafacadeModelNamespace))
            {
                final MetafacadeMappings modelMappings = this.getModelMetafacadeMappings();
                if (modelMappings != null)
                {
                    modelMappings.registerAllProperties();
                }
            }
        }
    }

    /**
     * Entirely resets all the internal resources within this factory instance (such
     * as the caches, etc).
     */
    public void reset()
    {
        // - refresh the profile
        this.profile.refresh();

        // - clear out the namespace properties so we can re-register them next run
        this.metafacadeNamespaces.clear();

        // - re-register the namespace properties (if we're running again)
        this.registerNamespaceProperties();

        // - clear out the rest of the factory's caches
        this.clearCaches();
    }

    /**
     * Clears out the factory's internal caches (other
     * than namespace properties, which can be cleared by
     * calling {@link org.andromda.core.configuration.Namespaces#clear()}.
     */
    public void clearCaches()
    {
        this.validationMessages.clear();
        this.allMetafacades.clear();
        this.metafacadesByStereotype.clear();
        this.cache.clear();
        this.metafacadesInCreation.clear();
    }
}