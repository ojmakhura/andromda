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
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;

/**
 * The factory in charge of constucting Metafacade
 * instances.  In order for a metafacade (i.e. a facade
 * around a meta model element) to be constructed, it 
 * must be constructed through this factory.
 * 
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen</a>
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
     * Caches the registered properties used
     * within metafacades.
     */
    private Map registeredProperties = null;

    // constructor is private to make sure that nobody instantiates it
    private MetafacadeFactory()
    {
        this.metafacadeCache = new HashMap();
        this.registeredProperties = new HashMap();
        this.validationMessages = new HashSet();
        MetafacadeMappings.instance().discoverMetafacades();
        MetafacadeImpls.instance().discoverMetafacadeImpls();
    }

    /**
     * Returns the facade factory singleton.
     * @return the only instance
     */
    public static MetafacadeFactory getInstance()
    {
        return factory;
    }

    /**
     * Sets the active namespace. The AndroMDA core and each cartridge
     * have their own namespace for facade registrations.
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
     * Returns a metafacade for a metaobject, depending on its <code>metaclass</code> and
     * (optionally) its sterotype and <code>contextName</code>.
     * @param metaobject the meta model element.
     * @param contextName the name of the context the meta
     *                    model element is registered under.
     * @return the new metafacade
     */
    public MetafacadeBase createMetafacade(
        Object metaobject,
        String contextName)
    {
        return this.internalCreateMetafacade(
            metaobject,
            contextName,
            null);
    }

    /**
     * Internal helper method.
     * 
     * @param metaobject the meta model element.
     * @param contextName the name of the context the meta
     *                    model element is registered under.
     * @param metafacadeClass if not null, it contains the name of the metafacade class to be used
     * @return the new metafacade 
     */
    private MetafacadeBase internalCreateMetafacade(
        Object metaobject,
        String contextName,
        Class metafacadeClass)
    {
        // TODO: the source code for this class looks complicated and has to be refactored.

        final String methodName = "MetafacadeFactory.internalCreateMetafacade";

        ExceptionUtils.checkNull(methodName, "metaobject", metaobject);
        
        //if the metaobject ALREADY IS a metafacade
        //return the metaobject since we don't want to try and create a
        //metafacade from a metafacade.
        if (metaobject instanceof MetafacadeBase)
        {
            return (MetafacadeBase)metaobject;
        }
        
        Class metaobjectClass = null;
        try
        {
            metaobjectClass = metaobject.getClass();
            String metaobjectClassName = metaobjectClass.getName();

            MetafacadeMappings mappings = MetafacadeMappings.instance();

            Collection stereotypeNames =
                this.getModel().getStereotypeNames(metaobject);
            
            if (stereotypeNames == null) {
            	throw new MetafacadeFactoryException(methodName
                    + " - could not retrieve stereotypes for metaobject --> '" 
                    + metaobject + "'");
            }
            
            MetafacadeMapping mapping = null;

            if (this.internalGetLogger().isDebugEnabled())
                this.internalGetLogger().debug(
                    "metaobject stereotype names --> '"
                        + stereotypeNames
                        + "'");
            mapping =
                mappings.getMetafacadeMapping(
                    metaobjectClassName,
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
                    metafacadeClass =
                        mappings.getDefaultMetafacadeClass(
                            this.activeNamespace);
                    if (this.internalGetLogger().isDebugEnabled())
                        this.internalGetLogger().debug(
                            "Meta object model class '"
                                + metaobjectClass
                                + "' has no corresponding meta facade class, default is being used --> '"
                                + metafacadeClass
                                + "'");
                }
            }
            
            if (metafacadeClass == null)
            {
                throw new MetafacadeMappingsException(
                    methodName
                        + " metafacadeClass was not retrieved from mappings"
                        + " or specified as an argument in this method for metaobject --> '"
                        + metaobject
                        + "'");
            }

            
            Object metafacadeCacheKey;
            if (mapping != null) {
                metafacadeCacheKey = mapping.getKey();
            } else {
                // if there is no mapping, then the metafacadeClass
                // will be the default metafacade class, so use
                // that as the cache key.
                metafacadeCacheKey = metafacadeClass;
            }
            
            // attempt to get the metafacade from the cache
            // since we don't want to recreate if one already 
            // has been created
            MetafacadeBase metafacade = 
                this.getFromMetafacadeCache(
                    metaobject, 
                    metafacadeCacheKey);

            if (metafacade == null) {
	            if (internalGetLogger().isDebugEnabled())
	                if (internalGetLogger().isDebugEnabled())
	                    internalGetLogger().debug(
	                        "lookupFacadeClass: "
	                            + metaobjectClassName
	                            + " -> "
	                            + metafacadeClass);
	
	            metafacade =
	                (MetafacadeBase) ConstructorUtils.invokeConstructor(
	                    metafacadeClass,
	                    new Object[] {
	                        metaobject, 
	                        contextName
	                    },
	                    new Class[] {
	                        metaobject.getClass(), 
	                        java.lang.String.class
	                    });
	            
	            // make sure that the facade has a proper logger associated
	            // with it.
	            metafacade.setLogger(internalGetLogger());

	            // set this namespace to the metafacade's namespace
	            metafacade.setNamespace(this.getActiveNamespace());	            
	            
	            this.populatePropertyReferences(
	                metafacade,
	                mappings.getPropertyReferences(this.getActiveNamespace()));
	
	            // now populate any context property references (if
	            // we have any)
	            if (mapping != null)
	            {
	                this.populatePropertyReferences(
	                    metafacade,
	                    mapping.getPropertyReferences());
	            } 
                // validate the meta-facade and collect the messages
                Collection validationMessages = new ArrayList();
	            metafacade.validate(validationMessages);
	            this.validationMessages.addAll(validationMessages); 
	            this.addToMetafacadeCache(
	                metaobject, 
	                metafacadeCacheKey, 
	                metafacade);	            
            }
                     
            return metafacade;
        }
        catch (Throwable th)
        {
            String errMsg =
                "Failed to construct a meta facade of type '"
                    + metafacadeClass
                    + "' with metaobject of type --> '"
                    + metaobjectClass
                    + "'";
            internalGetLogger().error(errMsg, th);
            throw new MetafacadeFactoryException(errMsg, th);
        }
    }

    /**
     * Returns a metafacade for a metaobject, depending on its
     * metaclass and (optionally) its stereotype.
     *
     * @param metaobject the model element
     * @return MetafacadeBase the facade object (not yet attached to metaclass object)
     */
    public MetafacadeBase createMetafacade(Object metaobject)
    {
        return this.internalCreateMetafacade(metaobject, null, null);
    }

    /**
     * Create a facade implementation object for a metaobject. The facade implementation
     * object must be found in a way that it implements the interface <code>interfaceName</code>.
     * 
     * @param interfaceName the name of the interface that the implementation object has to implement 
     * @param metaObject the metaobject for which a facade shall be created
     * @param contextName the contextName which will be used to create other metafacades.
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
                                    
            metafacadeClass = 
                MetafacadeImpls.instance().getMetafacadeImplClass(
                    interfaceName);
            
            MetafacadeBase metafacade = 
                this.internalCreateMetafacade(
                        metaObject,
                        contextName,
                        metafacadeClass);  
            
            return metafacade;   
        }
        catch (Throwable th)
        {
            String errMsg =
                "Failed to construct a meta facade of type '"
                    + metafacadeClass
                    + "' with metaobject of type --> '"
                    + metaObject.getClass().getName()
                    + "'";
            internalGetLogger().error(errMsg, th);
            throw new MetafacadeFactoryException(errMsg, th);
        }
    }
    
    /**
     * Returns the metafacade from the metafacade cache. T
     * Metafacades are cached first by according to its <code>metaobject</code>
     * and then according to the given <code>key</code> and current
     * active namespace.  Metafacades
     * must be cached in order to keep track of the state of its validation.
     * If we keep creating a new one each time, we can never tell whether or
     * not a metafacade has been previously validated.
     * 
     * @param metaobject the metaobject for which to cache the metafacade.
     * @param key the unique key for the given metaobject
     * @return MetafacadeBase stored in the cache.
     */
    private MetafacadeBase getFromMetafacadeCache(Object metaobject, Object key) {
        MetafacadeBase metafacade = null;
        Map namespaceMetafacadeCache = (Map)
        	this.metafacadeCache.get(metaobject);
        if (namespaceMetafacadeCache != null) {
            metafacade = (MetafacadeBase)namespaceMetafacadeCache.get(
                this.getActiveNamespace() + key);
        }
        return metafacade;
    }
    
    /**
     * Adds the <code>metafacade</code> to the cache accorinding
     * to first <code>metaobject</code> and then by <code>key</code>
     * and current active namespace.
     * 
     * @param metaobject the metaobject for which to cache the metafacade.
     * @param key the unique key by which the metafacade is cached (within 
     *        the scope of the <code>metaobject</code.
     * @param metafacade the metafacade to cache.
     */
    private void addToMetafacadeCache(Object metaobject, Object key, MetafacadeBase metafacade) {
        Map namespaceMetafacadeCache = (Map)
            this.metafacadeCache.get(metaobject);
        if (namespaceMetafacadeCache == null) {
            namespaceMetafacadeCache = new HashMap();
        }
        namespaceMetafacadeCache.put(
            this.getActiveNamespace() + key, 
            metafacade);
        this.metafacadeCache.put(
            metaobject, 
            namespaceMetafacadeCache);
    }

    /**
     * Populates the metafacade with the values retrieved from the property references
     * found in the <code>propertyReferences</code> Map.
     *
     * @param propertyReferences the Map of property references which we'll populate.
     */
    protected void populatePropertyReferences(
        MetafacadeBase metafacade,
        Map propertyReferences)
    {

        final String methodName =
            "MetafacadeFactory.populatePropertyReferences";
        ExceptionUtils.checkNull(methodName, "metafacade", metafacade);
        ExceptionUtils.checkNull(
            methodName,
            "propertyReferences",
            propertyReferences);

        Iterator referenceIt = propertyReferences.keySet().iterator();
        while (referenceIt.hasNext())
        {
            String reference = (String) referenceIt.next();

            // ensure that each property is only set once per context
            // for performance reasons
            if (PropertyUtils.isWriteable(metafacade, reference) &&
                !this.isPropertyRegistered(
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
                
                Property property =
                    Namespaces.instance().findNamespaceProperty(
                        this.getActiveNamespace(),
                        reference,
                        showWarning);

                // don't attempt to set if the property is null, or it's set to ignore.
                if (property != null && !property.isIgnore())
                {
                    String value = property.getValue();
                    if (this.internalGetLogger().isDebugEnabled())
                        this.internalGetLogger().debug(
                            "setting context property '"
                                + reference
                                + "' with value '"
                                + value
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
     * Returns a metafacade for each metaobject, contained within the <code>metaobjects</code>
     * collection depending on its <code>metaclass</code> and
     * (optionally) its sterotype and <code>contextName</code>.
     * @param metaobjects the meta model element.
     * @param contextName the name of the context the meta
     *                    model element is registered under.
     * @return the Collection of newly created Metafacades.
     */
    protected Collection createMetafacades(
        Collection metaobjects,
        String contextName)
    {
        Collection metafacades = new ArrayList();
        if (metaobjects != null && !metaobjects.isEmpty())
        {
            Iterator metaobjectIt = metaobjects.iterator();
            while (metaobjectIt.hasNext())
            {
                metafacades.add(
                    internalCreateMetafacade(
                        metaobjectIt.next(),
                        contextName,
                        null));
            }
        }
        return metafacades;
    }
    
    /**
     * Returns a metafacade for each metaobject, contained within the <code>metaobjects</code>
     * collection depending on its <code>metaclass</code>.
     * @param metaobjects the meta model element.
     * @return Collection of metafacades
     */
    public Collection createMetafacades(
        Collection metaobjects)
    {
        return this.createMetafacades(metaobjects, null);
    }

    /**
     * @return the model
     */
    public ModelAccessFacade getModel()
    {
        final String methodName = "MetafacadeFactory.getModel";
        if (this.model == null) {
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
            return Logger.getLogger(
                "org.andromda.cartridges." + activeNamespace);
        return Logger.getRootLogger();
    }

    /**
     * Registers a property with the specified <code>name</code>
     * in the given <code>namespace</code>>.
     * @param namespace the namespace in which the property is stored.
     * @param name the name of the property
     */
    protected void registerProperty(
        String namespace,
        String name,
        Object value)
    {
        final String methodName = "MetafacadeFactory.registerProperty";
        ExceptionUtils.checkEmpty(methodName, "namespace", namespace);
        ExceptionUtils.checkEmpty(methodName, "name", name);
        ExceptionUtils.checkNull(methodName, "value", value);

        Map propertyNamespace =
            (Map) this.registeredProperties.get(namespace);
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
     * Returns true if this property is registered, false otherwise.
     * @param namespace
     * @param name
     * @return boolean
     */
    protected boolean isPropertyRegistered(String namespace, String name)
    {
        boolean registered = false;
        Map propertyNamespace =
            (Map) this.registeredProperties.get(namespace);
        if (propertyNamespace != null)
        {
            registered = propertyNamespace.containsKey(name);
        }
        return registered;
    }

    /**
     * Gets the registered property registered under the <code>namespace</code>
     * with the <code>name</code>
     * @param namespace the namespace of the property to check.
     * @param name the name of the property to check.
     * @return boolean
     */
    protected Object getRegisteredProperty(String namespace, String name)
    {
        final String methodName = "MetafacadeFactory.getRegisteredProperty";
        Object registeredProperty = null;
        Map propertyNamespace =
            (Map) this.registeredProperties.get(namespace);
        if (propertyNamespace == null)
        {
            throw new MetafacadeFactoryException(
                methodName
                    + " - no properties registered under namespace '"
                    + namespace
                    + "', can't retrieve property --> '" 
                    + name + "'");
        }
        registeredProperty = propertyNamespace.get(name);
        if (registeredProperty == null)
        {
            throw new MetafacadeFactoryException(
                methodName
                    + " - no property '"
                    + name
                    + "' registered under namespace '"
                    + namespace
                    + "'");
        }
        return registeredProperty;
    }
    
    /**
     * Gets the validation messages collection during
     * model processing.
     * 
     * @return Returns the validationMessages.
     */
    public Collection getValidationMessages()
    {
        return validationMessages;
    }
}
