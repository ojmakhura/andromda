package org.andromda.core.metafacade;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import org.andromda.core.common.ExceptionUtils;
import org.andromda.core.common.Namespaces;
import org.andromda.core.common.Property;
import org.apache.commons.beanutils.ConstructorUtils;
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
    private static MetafacadeFactory factory = new MetafacadeFactory();

    private String activeNamespace;
    private ModelAccessFacade model;

    /**
     * Any validation messages stored during processing.
     */
    private Collection validationMessages;

    /**
     * The cache for already created metafacades.
     */
    private Map metafacadeCache;

    /**
     * Caches the registered properties used within metafacades.
     */
    private Map registeredProperties = null;

    // constructor is private to make sure that nobody instantiates it
    private MetafacadeFactory()
    {
        this.metafacadeCache = new HashMap();
        this.registeredProperties = new HashMap();
        this.validationMessages = new HashSet();
    }

    /**
     * Whether or not model validation should be performed during metafacade
     * creation
     */
    private boolean modelValidation = true;

    /**
     * Returns the facade factory singleton.
     * 
     * @return the only instance
     */
    public static MetafacadeFactory getInstance()
    {
        return factory;
    }

    /**
     * Performs any initialization required by the factory (i.e. discovering all
     * <code>metafacade</code> mappings, etc).
     */
    public void initialize()
    {
        MetafacadeMappings.instance().discoverMetafacades();
        MetafacadeImpls.instance().discoverMetafacadeImpls();
    }

    /**
     * Sets the active namespace. The AndroMDA core and each cartridge have
     * their own namespace for facade registrations.
     * 
     * @param namespaceName the name of the namespace
     */
    public void setActiveNamespace(String namespaceName)
    {
        this.activeNamespace = namespaceName;
    }

    /**
     * Returns the name of the active namespace.
     * 
     * @return String the namespace name
     */
    public String getActiveNamespace()
    {
        return this.activeNamespace;
    }

    /**
     * Sets whether or not model validation should occur during
     * <code>metafacade</code> creation. This is useful for performance
     * reasons (i.e. if you have a large model it can significatly descrease the
     * amount of time it takes for AndroMDA to process a model). By default this
     * is set to <code>true</code>.
     * 
     * @param modelValidation The modelValidation to set.
     */
    public void setModelValidation(boolean modelValidation)
    {
        this.modelValidation = modelValidation;
    }

    /**
     * Returns a metafacade for a metaObject, depending on its
     * <code>metaclass</code> and (optionally) its sterotype and
     * <code>contextName</code>.
     * 
     * @param metaObject the meta model element.
     * @param contextName the name of the context the meta model element is
     *        registered under.
     * @return the new metafacade
     */
    public MetafacadeBase createMetafacade(Object metaObject, String contextName)
    {
        return this.internalCreateMetafacade(metaObject, contextName, null);
    }

    /**
     * Internal helper method.
     * 
     * @param metaObject the meta model element.
     * @param contextName the name of the context the meta model element is
     *        registered under.
     * @param metafacadeClass if not null, it contains the name of the
     *        metafacade class to be used. This is used ONLY when instantiating
     *        super metafacades in an inheritance chain. The final metafacade
     *        will NEVER have a metafacadeClass specified (it will ALWAYS be
     *        null).
     * @return the new metafacade
     */
    private MetafacadeBase internalCreateMetafacade(
        Object metaObject,
        String contextName,
        Class metafacadeClass)
    {
        // @todo the source code for this class looks complicated and has to be
        // refactored.
        final String methodName = "MetafacadeFactory.internalCreateMetafacade";
        ExceptionUtils.checkNull(methodName, "metaObject", metaObject);

        //if the metaObject is REALLY a metafacade, get the metaObject
        // from the metafacade since we don't want to try and create a
        //metafacade from a metafacade.
        if (MetafacadeBase.class.isAssignableFrom(metaObject.getClass()))
        {
            return (MetafacadeBase)metaObject;
        }

        Class metaObjectClass = null;
        try
        {
            metaObjectClass = metaObject.getClass();
            String metaObjectClassName = metaObjectClass.getName();

            MetafacadeMappings mappings = MetafacadeMappings.instance();

            Collection stereotypeNames = this.getModel().getStereotypeNames(
                metaObject);

            if (stereotypeNames == null)
            {
                throw new MetafacadeFactoryException(methodName
                    + " - could not retrieve stereotypes for metaObject --> '"
                    + metaObject + "'");
            }

            MetafacadeMapping mapping = null;

            if (this.internalGetLogger().isDebugEnabled())
                this.internalGetLogger()
                    .debug(
                        "metaObject stereotype names --> '" + stereotypeNames
                            + "'");

            mapping = mappings.getMetafacadeMapping(
                metaObjectClassName,
                stereotypeNames,
                this.getActiveNamespace(),
                contextName);

            if (metafacadeClass == null)
            {
                if (mapping != null)
                {
                    metafacadeClass = mapping.getMetafacadeClass();
                }
                else
                {
                    // get the default since no mapping was found.
                    metafacadeClass = mappings
                        .getDefaultMetafacadeClass(this.activeNamespace);
                    if (this.internalGetLogger().isDebugEnabled())
                        this
                            .internalGetLogger()
                            .debug(
                                "Meta object model class '"
                                    + metaObjectClass
                                    + "' has no corresponding meta facade class, default is being used --> '"
                                    + metafacadeClass + "'");
                }
            }

            if (metafacadeClass == null)
            {
                throw new MetafacadeMappingsException(
                    methodName
                        + " metafacadeClass was not retrieved from mappings"
                        + " or specified as an argument in this method for metaObject --> '"
                        + metaObject + "'");
            }

            Object metafacadeCacheKey;
            if (mapping != null)
            {
                metafacadeCacheKey = mapping.getKey();
                // if the mapping has a context defined, add the context
                // to the cache key
                if (mapping.hasContext())
                {
                    metafacadeCacheKey = MetafacadeMappingsUtils.constructKey(
                        metafacadeCacheKey,
                        contextName);
                }
            }
            else
            {
                // if there is no mapping, then the metafacadeClass
                // will be the default metafacade class, so use
                // that as the cache key
                metafacadeCacheKey = metafacadeClass;
            }

            // attempt to get the metafacade from the cache
            // since we don't want to recreate if one already
            // has been created
            MetafacadeBase metafacade = this.getFromMetafacadeCache(
                metaObject,
                metafacadeClass,
                metafacadeCacheKey);

            if (metafacade == null)
            {
                if (internalGetLogger().isDebugEnabled())
                    internalGetLogger().debug(
                        "lookupFacadeClass: " + metaObjectClassName
                            + " -> " + metafacadeClass);

                metafacade = (MetafacadeBase)ConstructorUtils
                    .invokeConstructor(metafacadeClass, new Object[]
                    {
                        metaObject,
                        contextName
                    }, new Class[]
                    {
                        metaObject.getClass(),
                        java.lang.String.class
                    });

                // make sure that the facade has a proper logger associated
                // with it.
                metafacade.setLogger(internalGetLogger());

                // set this namespace to the metafacade's namespace
                metafacade.setNamespace(this.getActiveNamespace());

                if (mapping != null)
                {
                    // check to see if the metafacade has a context root
                    // defined (if so, set the context to the interface
                    // name of the metafacade)
                    if (mapping.isContextRoot())
                    {
                        metafacade.setContext(MetafacadeImpls.instance()
                            .getMetafacadeClass(
                                mapping.getMetafacadeClass().getName())
                            .getName());
                    }
                }

                metafacade.initialize();
                // IMPORTANT: we must add the metafacade to the cache
                // before validate is called below, (so ordering matters here)
                // do NOT call validate method before adding the
                // metafacade to the cache, this will cause endless loops
                this.addToMetafacadeCache(
                    metaObject,
                    metafacadeClass,
                    metafacadeCacheKey,
                    metafacade);
                if (this.modelValidation)
                {
                    // validate the meta-facade and collect the messages
                    Collection validationMessages = new ArrayList();
                    metafacade.validate(validationMessages);
                    this.validationMessages.addAll(validationMessages);
                }
            }

            // Populate the global metafacade properties
            // NOTE: ordering here matters, we populate the global
            // properties BEFORE the context properties so that the
            // context properties can override (if duplicate properties
            // exist)
            this.populatePropertyReferences(metafacade, mappings
                .getPropertyReferences(this.getActiveNamespace()));

            if (mapping != null)
            {
                // Populate any context property references (if any)
                this.populatePropertyReferences(metafacade, mapping
                    .getPropertyReferences());
            }

            return metafacade;
        }
        catch (Throwable th)
        {
            String errMsg = "Failed to construct a meta facade of type '"
                + metafacadeClass + "' with metaObject of type --> '"
                + metaObjectClass + "'";
            internalGetLogger().error(errMsg, th);
            throw new MetafacadeFactoryException(errMsg, th);
        }
    }

    /**
     * Returns a metafacade for a metaObject, depending on its metaclass and
     * (optionally) its stereotype.
     * 
     * @param metaObject the model element
     * @return MetafacadeBase the facade object (not yet attached to metaclass
     *         object)
     */
    public MetafacadeBase createMetafacade(Object metaObject)
    {
        return this.internalCreateMetafacade(metaObject, null, null);
    }

    /**
     * Create a facade implementation object for a metaObject. The facade
     * implementation object must be found in a way that it implements the
     * interface <code>interfaceName</code>.
     * 
     * @param interfaceName the name of the interface that the implementation
     *        object has to implement
     * @param metaObject the metaObject for which a facade shall be created
     * @param contextName the contextName which will be used to create other
     *        metafacades.
     * @return MetafacadeBase the metafacade
     */
    public MetafacadeBase createFacadeImpl(
        String interfaceName,
        Object metaObject,
        String contextName)
    {
        final String methodName = "MetafacadeFactory.createFacadeImpl";
        ExceptionUtils.checkEmpty(methodName, "interfaceName", interfaceName);
        ExceptionUtils.checkNull(methodName, "metaObject", metaObject);

        Class metafacadeClass = null;
        try
        {
            metafacadeClass = MetafacadeImpls.instance()
                .getMetafacadeImplClass(interfaceName);

            MetafacadeBase metafacade = this.internalCreateMetafacade(
                metaObject,
                contextName,
                metafacadeClass);

            return metafacade;
        }
        catch (Throwable th)
        {
            String errMsg = "Failed to construct a meta facade of type '"
                + metafacadeClass + "' with metaObject of type --> '"
                + metaObject.getClass().getName() + "'";
            internalGetLogger().error(errMsg, th);
            throw new MetafacadeFactoryException(errMsg, th);
        }
    }

    /**
     * <p>
     * Returns the metafacade from the metafacade cache. The Metafacades are
     * cached first by according to its <code>metaObject</code>, then the
     * <code>metafacadeClass</code>, then according to to the given
     * <code>key</code> and finally by current active namespace.
     * </p>
     * <p>
     * Metafacades must be cached in order to keep track of the state of its
     * validation. If we keep creating a new one each time, we can never tell
     * whether or not a metafacade has been previously validated. Not to mention
     * tremendous performance gains.
     * </p>
     * 
     * @param metaObject the metaObject for which to cache the metafacade.
     * @param metafacadeClass the class of the metafacade.
     * @param key the unique key for the given metaObject
     * @return MetafacadeBase stored in the cache.
     */
    private MetafacadeBase getFromMetafacadeCache(
        Object metaObject,
        Class metafacadeClass,
        Object key)
    {
        MetafacadeBase metafacade = null;
        Map namespaceMetafacadeCache = (Map)this.metafacadeCache
            .get(metaObject);
        if (namespaceMetafacadeCache != null)
        {
            Map metafacadeCache = (Map)namespaceMetafacadeCache
                .get(metafacadeClass);
            if (metafacadeCache != null)
            {
                metafacade = (MetafacadeBase)metafacadeCache.get(this
                    .getActiveNamespace()
                    + key);
            }
        }
        return metafacade;
    }

    /**
     * Adds the <code>metafacade</code> to the cache according to first
     * <code>metaObject</code>, second <code>metafacadeClass</code>, third
     * <code>key</code>, and finally current the current active namespace.
     * 
     * @param metaObject the metaObject for which to cache the metafacade.
     * @param metafacadeClass the class of the metafacade
     * @param key the unique key by which the metafacade is cached (within the
     *        scope of the <code>metaObject</code.
     * @param metafacade the metafacade to cache.
     */
    private void addToMetafacadeCache(
        Object metaObject,
        Class metafacadeClass,
        Object key,
        MetafacadeBase metafacade)
    {
        Map namespaceMetafacadeCache = (Map)this.metafacadeCache
            .get(metaObject);
        if (namespaceMetafacadeCache == null)
        {
            namespaceMetafacadeCache = new HashMap();
        }
        Map metafacadeCache = (Map)namespaceMetafacadeCache
            .get(metafacadeClass);
        if (metafacadeCache == null)
        {
            metafacadeCache = new HashMap();
        }
        metafacadeCache.put(this.getActiveNamespace() + key, metafacade);
        namespaceMetafacadeCache.put(metafacadeClass, metafacadeCache);
        this.metafacadeCache.put(metaObject, namespaceMetafacadeCache);
    }

    /**
     * Populates the metafacade with the values retrieved from the property
     * references found in the <code>propertyReferences</code> Map.
     * 
     * @param propertyReferences the Map of property references which we'll
     *        populate.
     */
    protected void populatePropertyReferences(
        MetafacadeBase metafacade,
        Map propertyReferences)
    {
        final String methodName = "MetafacadeFactory.populatePropertyReferences";
        ExceptionUtils.checkNull(methodName, "metafacade", metafacade);
        ExceptionUtils.checkNull(
            methodName,
            "propertyReferences",
            propertyReferences);

        Iterator referenceIt = propertyReferences.keySet().iterator();
        while (referenceIt.hasNext())
        {
            String reference = (String)referenceIt.next();
            // ensure that each property is only set once per context
            // for performance reasons
            if (!this.isPropertyRegistered(
                metafacade.getPropertyNamespace(),
                reference))
            {
                String defaultValue = (String)propertyReferences.get(reference);

                // if we have a default value, then don't warn
                // that we don't have a property, otherwise we'll
                // show the warning.
                boolean showWarning = false;
                if (defaultValue == null)
                {
                    showWarning = true;
                }

                Property property = Namespaces.instance()
                    .findNamespaceProperty(
                        this.getActiveNamespace(),
                        reference,
                        showWarning);
                // don't attempt to set if the property is null, or it's set to
                // ignore.
                if (property != null && !property.isIgnore())
                {
                    String value = property.getValue();
                    if (this.internalGetLogger().isDebugEnabled())
                        this.internalGetLogger().debug(
                            "setting context property '" + reference
                                + "' with value '" + value
                                + "' for namespace '"
                                + this.getActiveNamespace() + "'");

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
     * Returns a metafacade for each metaObject, contained within the
     * <code>metaObjects</code> collection depending on its
     * <code>metaclass</code> and (optionally) its sterotype and
     * <code>contextName</code>.
     * 
     * @param metaObjects the meta model element.
     * @param contextName the name of the context the meta model element is
     *        registered under.
     * @return the Collection of newly created Metafacades.
     */
    protected Collection createMetafacades(
        Collection metaObjects,
        String contextName)
    {
        Collection metafacades = new ArrayList();
        if (metaObjects != null && !metaObjects.isEmpty())
        {
            Iterator metaObjectIt = metaObjects.iterator();
            while (metaObjectIt.hasNext())
            {
                metafacades.add(internalCreateMetafacade(
                    metaObjectIt.next(),
                    contextName,
                    null));
            }
        }
        return metafacades;
    }

    /**
     * Returns a metafacade for each metaObject, contained within the
     * <code>metaObjects</code> collection depending on its
     * <code>metaclass</code>.
     * 
     * @param metaObjects the meta model element.
     * @return Collection of metafacades
     */
    public Collection createMetafacades(Collection metaObjects)
    {
        return this.createMetafacades(metaObjects, null);
    }

    /**
     * @return the model
     */
    public ModelAccessFacade getModel()
    {
        final String methodName = "MetafacadeFactory.getModel";
        if (this.model == null)
        {
            throw new MetafacadeFactoryException(methodName
                + " - model is null!");
        }
        return model;
    }

    /**
     * @param model the model
     */
    public void setModel(ModelAccessFacade model)
    {
        this.model = model;
    }

    private Logger internalGetLogger()
    {
        if (!"core".equals(activeNamespace))
            return Logger.getLogger("org.andromda.cartridges."
                + activeNamespace);
        return Logger.getRootLogger();
    }

    /**
     * Registers a property with the specified <code>name</code> in the given
     * <code>namespace</code>.
     * 
     * @param namespace the namespace in which the property is stored.
     * @param name the name of the property
     */
    protected void registerProperty(String namespace, String name, Object value)
    {
        final String methodName = "MetafacadeFactory.registerProperty";
        ExceptionUtils.checkEmpty(methodName, "namespace", namespace);
        ExceptionUtils.checkEmpty(methodName, "name", name);
        ExceptionUtils.checkNull(methodName, "value", value);

        Map propertyNamespace = (Map)this.registeredProperties.get(namespace);
        if (propertyNamespace != null)
        {
            propertyNamespace.put(name, value);
        }
        else
        {
            propertyNamespace = new HashMap();
            propertyNamespace.put(name, value);
            this.registeredProperties.put(namespace, propertyNamespace);
        }
    }

    /**
     * Returns true if this property is registered under the given
     * <code>namespace</code>, false otherwise.
     * 
     * @param namespace the namespace to check.
     * @param name the name of the property.
     * @return true if the property is registered, false otherwise.
     */
    boolean isPropertyRegistered(final String namespace, final String name)
    {
        return this.findProperty(namespace, name) != null;
    }

    /**
     * Finds the first property having the given <code>namespaces</code>, or
     * <code>null</code> if the property can <strong>NOT </strong> be found.
     * 
     * @param namespace the namespace to search.
     * @param name the name of the property to find.
     * @return the property or null if it can't be found.
     */
    private Object findProperty(final String namespace, final String name)
    {
        Object property = null;
        Map propertyNamespace = (Map)registeredProperties.get(namespace);
        if (propertyNamespace != null)
        {
            property = propertyNamespace.get(name);
        }
        return property;
    }

    /**
     * Gets the registered property registered under the <code>namespace</code>
     * with the <code>name</code>
     * 
     * @param namespace the namespace of the property to check.
     * @param name the name of the property to check.
     * @return the registered property
     */
    Object getRegisteredProperty(String namespace, String name)
    {
        final String methodName = "MetafacadeFactory.getRegisteredProperty";
        Object registeredProperty = this.findProperty(namespace, name);
        if (registeredProperty == null)
        {
            throw new MetafacadeFactoryException(methodName
                + " - no property '" + name
                + "' registered under namespace --> '" + namespace + "'");
        }
        return registeredProperty;
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
}