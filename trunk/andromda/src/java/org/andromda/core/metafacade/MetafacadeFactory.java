package org.andromda.core.metafacade;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import org.andromda.core.common.AndroMDALogger;
import org.andromda.core.common.ExceptionUtils;
import org.andromda.core.configuration.ModelPackages;
import org.andromda.core.configuration.Namespaces;
import org.andromda.core.configuration.Property;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.log4j.Logger;


/**
 * The factory in charge of constucting Metafacade instances. In order for a
 * metafacade (i.e. a facade around a meta model element) to be constructed, it
 * must be constructed through this factory.
 *
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen </a>
 * @author Chad Brandon
 */
public class MetafacadeFactory
{
    /**
     * The namespace that is currently active (i.e. being used) within the factory
     */
    private String namespace;

    /**
     * The model facade which provides access to the underlying meta model.
     */
    private ModelAccessFacade model;

    /**
     * Any validation messages stored during processing.
     */
    private final Collection validationMessages = new HashSet();

    /**
     * Caches the registered properties used within metafacades.
     */
    private final Map metafacadeNamespaces = new HashMap();

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
     * Performs any initialization required by the factory (i.e. discovering all <code>metafacade</code> mappings,
     * etc).
     */
    public void initialize()
    {
        this.mappings.discoverMetafacades();
        MetafacadeImpls.instance().discoverMetafacadeImpls();
    }

    /**
     * Sets the active namespace. The AndroMDA core and each cartridge have their own namespace for metafacade
     * registration.
     *
     * @param namespace the name of the active namespace.
     */
    public void setNamespace(final String namespace)
    {
        this.namespace = namespace;
        this.cache.setNamespace(this.namespace);
    }

    /**
     * Returns the name of the active namespace.
     *
     * @return String the namespace name
     */
    public String getNamespace()
    {
        return this.namespace;
    }

    /**
     * Whether or not model validation should be performed during metafacade creation
     */
    private boolean modelValidation = true;

    /**
     * Sets whether or not model validation should occur during <code>metafacade</code> creation. This is useful for
     * performance reasons (i.e. if you have a large model it can significatly descrease the amount of time it takes for
     * AndroMDA to process a model). By default this is set to <code>true</code>.
     *
     * @param modelValidation The modelValidation to set.
     */
    public void setModelValidation(final boolean modelValidation)
    {
        this.modelValidation = modelValidation;
    }

    /**
     * Returns a metafacade for a mappingObject, depending on its
     * <code>mappingClass</code> and (optionally) its <code>sterotypes</code>
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
        return this.createMetafacade(mappingObject, context, null);
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
    private final MetafacadeBase createMetafacade(
        final Object mappingObject,
        final String context,
        Class metafacadeClass)
    {
        final String methodName = "MetafacadeFactory.createMetafacade";
        ExceptionUtils.checkNull(methodName, "mappingObject", mappingObject);

        // if the mappingObject is REALLY a metafacade, just return it
        if (mappingObject instanceof MetafacadeBase)
        {
            return (MetafacadeBase)mappingObject;
        }
        try
        {
            final Collection stereotypes = this.getModel().getStereotypeNames(mappingObject);
            if (this.getLogger().isDebugEnabled())
            {
                this.getLogger().debug("mappingObject stereotypes --> '" + stereotypes + "'");
            }

            final MetafacadeMapping mapping =
                this.mappings.getMetafacadeMapping(
                    mappingObject,
                    this.getNamespace(),
                    context,
                    stereotypes);
            if (metafacadeClass == null)
            {
                if (mapping != null)
                {
                    metafacadeClass = mapping.getMetafacadeClass();
                }
                else
                {
                    // get the default since no mapping was found.
                    metafacadeClass = this.mappings.getDefaultMetafacadeClass(this.getNamespace());
                    if (this.getLogger().isDebugEnabled())
                    {
                        this.getLogger().debug(
                            "Meta object model class '" + mappingObject.getClass() +
                            "' has no corresponding meta facade class, default is being used --> '" + metafacadeClass +
                            "'");
                    }
                }
            }

            if (metafacadeClass == null)
            {
                throw new MetafacadeMappingsException(
                    methodName + " metafacadeClass was not retrieved from mappings" +
                    " or specified as an argument in this method for mappingObject --> '" + mappingObject + "'");
            }
            final MetafacadeBase metafacade = this.getMetafacade(metafacadeClass, mappingObject, context, mapping);

            // IMPORTANT: initialize each metafacade ONLY once (otherwise we
            // get stack overflow errors)
            if (metafacade != null && !metafacade.isInitialized())
            {
                metafacade.setInitialized();
                metafacade.initialize();
                if (this.modelValidation)
                {
                    // validate the meta-facade and collect the messages
                    final Collection validationMessages = new ArrayList();
                    metafacade.validate(validationMessages);
                    this.validationMessages.addAll(validationMessages);
                }
            }
            if (this.getLogger().isDebugEnabled())
            {
                this.getLogger().debug("constructed metafacade >> '" + metafacade + "'");
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
     * Creates a metacade from the passed in <code>mappingObject</code>, and
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
     * @param mappings the MetafacadeMappings instance to which the optional
     *        <code>mapping</code> instance belongs.
     * @param mapping the optional MetafacadeMapping instance from which the
     *        metafacade is mapped.
     * @return the new (or cached) metafacade.
     * @throws Exception if any error occurs during metafacade creation
     */
    private final MetafacadeBase getMetafacade(
        final Class metafacadeClass,
        final Object mappingObject,
        final String context,
        final MetafacadeMapping mapping)
        throws Exception
    {
        MetafacadeBase metafacade = this.cache.get(mappingObject, metafacadeClass);
        if (metafacade == null)
        {
            metafacade = MetafacadeUtils.constructMetafacade(metafacadeClass, mappingObject, context);
            if (mapping != null)
            {
                // set whether or not this metafacade is a context root
                metafacade.setContextRoot(mapping.isContextRoot());
            }
            this.cache.add(mappingObject, metafacade);
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
        this.populateMetafacadeProperties(metafacade, mapping);
        return metafacade;
    }

    /**
     * Populates the given <code>metafacade</code> with the properties from
     * the given <code>mapping</code> as well as the properties from the
     * {@link MetafacadeMappings}instance to which the <code>mapping</code>
     * belongs.
     *
     * @param metafacade the metafacade instance to populate.
     * @param mapping the mapping from which to populate the properties.
     */
    private final void populateMetafacadeProperties(
        final MetafacadeBase metafacade,
        final MetafacadeMapping mapping)
    {
        // Populate the global metafacade properties
        // NOTE: ordering here matters, we populate the global
        // properties BEFORE the context properties so that the
        // context properties can override (if duplicate properties
        // exist)
        this.populatePropertyReferences(
            metafacade,
            this.mappings.getPropertyReferences(this.getNamespace()));
        if (mapping != null)
        {
            // Populate any metafacade namespace references (if any)
            this.populatePropertyReferences(
                metafacade,
                mapping.getPropertyReferences());
        }
    }

    /**
     * Returns a metafacade for a mappingObject, depending on its <code>mappingClass</code>.
     *
     * @param mappingObject the object which is used to map to the metafacade
     * @return MetafacadeBase the facade object (not yet attached to mappingClass object)
     */
    public MetafacadeBase createMetafacade(final Object mappingObject)
    {
        return this.createMetafacade(mappingObject, null, null);
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
        final String methodName = "MetafacadeFactory.createFacadeImpl";
        ExceptionUtils.checkEmpty(methodName, "interfaceName", interfaceName);
        ExceptionUtils.checkNull(methodName, "mappingObject", mappingObject);

        Class metafacadeClass = null;
        try
        {
            metafacadeClass = MetafacadeImpls.instance().getMetafacadeImplClass(interfaceName);
            return this.createMetafacade(mappingObject, context, metafacadeClass);
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
     * Populates the metafacade with the values retrieved from the property references found in the
     * <code>propertyReferences</code> Map.
     *
     * @param propertyReferences the Map of property references which we'll populate.
     */
    protected void populatePropertyReferences(
        final MetafacadeBase metafacade,
        final Map propertyReferences)
    {
        final String methodName = "MetafacadeFactory.populatePropertyReferences";
        ExceptionUtils.checkNull(methodName, "metafacade", metafacade);
        ExceptionUtils.checkNull(methodName, "propertyReferences", propertyReferences);
        for (final Iterator iterator = propertyReferences.keySet().iterator(); iterator.hasNext();)
        {
            final String reference = (String)iterator.next();

            // ensure that each property is only set once per context
            // for performance reasons
            if (!this.isPropertyRegistered(
                    metafacade,
                    reference))
            {
                final String defaultValue = (String)propertyReferences.get(reference);

                // if we have a default value, then don't warn
                // that we don't have a property, otherwise we'll
                // show the warning.
                boolean showWarning = false;
                if (defaultValue == null)
                {
                    showWarning = true;
                }
                final Property property =
                    Namespaces.instance().findNamespaceProperty(
                        this.getNamespace(),
                        reference,
                        showWarning);

                // don't attempt to set if the property is null, or it's set to
                // ignore.
                if (property != null && !property.isIgnore())
                {
                    final String value = property.getValue();
                    if (this.getLogger().isDebugEnabled())
                    {
                        this.getLogger().debug(
                            "setting context property '" + reference + "' with value '" + value + "' for namespace '" +
                            this.getNamespace() + "'");
                    }

                    if (value != null)
                    {
                        metafacade.setProperty(reference, value);
                    }
                }
                else if (defaultValue != null)
                {
                    metafacade.setProperty(reference, defaultValue);
                }
            }
        }
    }

    /**
     * Returns a metafacade for each mappingObject, contained within the
     * <code>mappingObjects</code> collection depending on its
     * <code>mappingClass</code> and (optionally) its <code>sterotypes</code>,
     * and <code>contextName</code>.  Note that if model package information was given 
     * in the {@link MetafacadeFactoryContext#getModelPackages()} then the model
     * packages which do not apply will be filtered out.
     *
     * @param mappingObjects the meta model element.
     * @param contextName the name of the context the meta model element is
     *        registered under.
     * @return the Collection of newly created Metafacades.
     */
    protected Collection createMetafacades(
        final Collection mappingObjects,
        final String contextName)
    {
        final Collection metafacades = new ArrayList();
        if (mappingObjects != null && !mappingObjects.isEmpty())
        {
            for (final Iterator iterator = mappingObjects.iterator(); iterator.hasNext();)
            {
                Object test = this.createMetafacade(
                    iterator.next(),
                    contextName,
                    null);
                metafacades.add(test);
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
    public Collection createMetafacades(final Collection mappingObjects)
    {
        return this.createMetafacades(mappingObjects, null);
    }

    /**
     * @return the model
     */
    public ModelAccessFacade getModel()
    {
        final String methodName = "MetafacadeFactory.getModel";
        if (this.model == null)
        {
            throw new MetafacadeFactoryException(methodName + " - model is null!");
        }
        return model;
    }

    /**
     * @param model the model
     */
    public void setModel(final ModelAccessFacade model)
    {
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
     * @param metafacade the metafacade under which to register the property.
     * @param name the name of the property
     * @param the value to give the property
     */
    final void registerProperty(
        final MetafacadeBase metafacade,
        final String name,
        final Object value)
    {
        final String methodName = "MetafacadeFactory.registerProperty";
        ExceptionUtils.checkEmpty(methodName, "name", name);
        ExceptionUtils.checkNull(methodName, "value", value);

        final String namespace = this.getNamespace();
        Map metafacadeNamespace = (Map)this.metafacadeNamespaces.get(namespace);
        if (metafacadeNamespace == null)
        {
            metafacadeNamespace = new HashMap();
        }
        final String metafacadeName = metafacade.getName();
        Map propertyNamespace = (Map)metafacadeNamespace.get(metafacadeName);
        if (propertyNamespace == null)
        {
            propertyNamespace = new HashMap();
        }
        propertyNamespace.put(name, value);
        metafacadeNamespace.put(metafacadeName, propertyNamespace);
        this.metafacadeNamespaces.put(namespace, metafacadeNamespace);
    }
    
    /**
     * Gets the metafacade's property namespace (or returns null if hasn't be registered).
     * 
     * @param metafacade the metafacade
     * @return the metafacade's namespace
     */
    private Map getMetafacadePropertyNamespace(final MetafacadeBase metafacade)
    {
        Map metafacadeNamespace = null;
        if (metafacade != null)
        {
            Map namespace = (Map)this.metafacadeNamespaces.get(this.getNamespace());
            if (namespace != null)
            {
                metafacadeNamespace = (Map)namespace.get(metafacade.getName());
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
        return this.findProperty(metafacade, name) != null;
    }

    /**
     * Finds the first property having the given <code>namespaces</code>, or
     * <code>null</code> if the property can <strong>NOT </strong> be found.
     *
     * @param namespace the property namespace to search.
     * @param metafacade the metafacade to search.
     * @param name the name of the property to find.
     * @return the property or null if it can't be found.
     */
    private final Object findProperty(
        final MetafacadeBase metafacade,
        final String name)
    {
        final Map propertyNamespace = this.getMetafacadePropertyNamespace(metafacade);
        return propertyNamespace != null ? propertyNamespace.get(name) : null;
    }

    /**
     * Gets the registered property registered under the <code>namespace</code>
     * with the <code>name</code>
     *
     * @param namespace the namespace of the property to check.
     * @param metafacade the metafacade to search
     * @param name the name of the property to check.
     * @return the registered property
     */
    final Object getRegisteredProperty(
        final MetafacadeBase metafacade,
        final String name)
    {
        final String methodName = "MetafacadeFactory.getRegisteredProperty";
        final Object registeredProperty = this.findProperty(metafacade, name);
        if (registeredProperty == null)
        {
            throw new MetafacadeFactoryException(
                methodName + " - no property '" + name + "' registered under metafacade '" + metafacade.getName() +
                "' for namespace '" + this.getNamespace() + "'");
        }
        return registeredProperty;
    }

    /**
     * Performs shutdown procedures for the factory. This should be called <strong>ONLY</code> when model processing has
     * completed.
     */
    public void shutdown()
    {
        this.reset();
        this.mappings.shutdown();
        this.model = null;
        instance = null;
    }

    /**
     * Resets the required internal resources
     * without shutting down the factory.
     */
    public void reset()
    {
        this.validationMessages.clear();
        this.metafacadeNamespaces.clear();
        this.cache.clear();
    }

    /**
     * Gets the validation messages collection during model processing.
     *
     * @return Returns the validationMessages.
     */
    public Collection getValidationMessages()
    {
        return validationMessages;
    }
    
    /**
     * The model packages that are used to determine whether or not
     * some packages should be filtered out. Set as protected
     * visibility to improve innerclass access performance.
     */
    protected ModelPackages modelPackages = null;
    
    /**
     * Sets the model packages; these indicate which packages
     * should and should not be processed (if defined).
     * @param packages
     */
    public void setModelPackages(final ModelPackages modelPackages)
    {
        this.modelPackages = modelPackages;
    }
    
    /**
     * Filters out those metafacades which <strong>should </strong> be processed.
     *
     * @param modelElements the Collection of modelElements.
     */
    public void filterMetafacades(final Collection metafacades)
    {
        if (this.modelPackages != null)
        {
            CollectionUtils.filter(
               metafacades,
                new Predicate()
                {
                    public boolean evaluate(final Object modelElement)
                    {
                        return modelPackages.isProcess(getModel().getPackageName(modelElement));
                    }
                });
        }
    }
}