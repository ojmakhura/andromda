package org.andromda.core.common;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.defaults.DefaultPicoContainer;

/**
 * <p>
 *  This handles all registration and retrieval 
 *  of components within the framework.  The purpose
 *  of this container is so that we can register default
 *  services in a consistent manner by creating
 *  a component interface and then placing the file
 *  which defines the default implementation in the
 *  'META-INF/services/' directory found on the classpath.
 * </p>
 * <p>
 *  In order to create a new component
 *  that can be registered/found through this container
 *  you must perform the following steps:
 *  <ol>
 *      <li>
 *          Create the component interface (i.e. 
 *          org.andromda.core.repository.RepositoryFacade)
 *      </li>
 *      <li>
 *          Create the component implementation (i.e.
 *          org.andromda.repositories.mdr.MDRepositoryFacade)
 *      </li>
 *      <li>
 *          Create a file with the exact same name as the fully
 *          qualified name of the component 
 *          (i.e. org.andromda.core.repository.RepositoryFacade)
 *          and place this in the META-INF/services/ directory
 *          within the core.
 *      </li>
 *  </ol>
 *  After you perform the above steps, the component can be found
 *  by the methods within this class.  See each below method
 *  for more information on how each performs lookup/retrieval
 *  of the components.
 * </p>
 * @author Chad Brandon
 */
public class ComponentContainer {
    
    private static Logger logger = Logger.getLogger(ComponentContainer.class);
    
    /**
     * Where all component default implementations are found.
     */
    private static final String SERVICES = "META-INF/services/";

    /**
     * The container instance
     */
	private MutablePicoContainer container;
    
    private static final ComponentContainer instance = new ComponentContainer();
    
    /**
     * Constructs an instance of ComponentContainer.
     */
    protected ComponentContainer() {
    	this.container = new DefaultPicoContainer();
    }
    
    /**
     * Gets the shared instance of this ComponentContainer.
     * 
     * @return PluginDiscoverer the static instance.
     */
    public static ComponentContainer instance() {
        return instance;
    }
    
    /**
     * Finds the component with the specified <code>key</code>.
     * 
     * @param key the unique key of the component as an Object.
     * @return Object the component instance.
     */
    public Object findComponent(Object key) {
    	return this.container.getComponentInstance(key);
    }
    
    /**
     * Finds the component with the specified Class <code>key</code>.
     * If the component wasn't explicitly registered then the META-INF/services
     * directory on the classpath will be searched in order
     * to find the default component implementation.
     * 
     * @param key the unique key as a Class.
     * @return Object the component instance.
     */
    public Object findComponent(Class key) {
    	final String methodName = "ComponentContainer.findComponent";
    	ExceptionUtils.checkNull(methodName, "key", key);
    	return this.findComponent(null, key);
    }
    
    /**
     * Attempts to find the component with the 
     * specified unique <code>key</code>, if it can't be found, 
     * the default of the specified <code>type</code> is
     * returned, if no default is set, null is returned.
     * The default is the service found within the META-INF/services
     * directory on your classpath.
     * 
     * @param key the unique key of the component.
     * @param type the default type to retrieve if the component can
     *        not be found.
     * @return Object the component instance.
     */
    public Object findComponent(String key, Class type) {
        final String methodName = "ComponentContainer.findComponent";
        ExceptionUtils.checkNull(methodName, "type", type);
        try {

            Object component = this.container.getComponentInstance(key);
            if (component == null) {
                String typeName = type.getName();
            	component = this.container.getComponentInstance(typeName);
                //if the component doesn't have a default already 
                //(componet == null), then see if we can find the default 
                //configuration file.
                if (component == null) {
                    String defaultImpl = 
                        StringUtils.trimToEmpty(
                            ResourceUtils.getContents(
                                SERVICES + type.getName()));
                   
                    if (StringUtils.isNotEmpty(defaultImpl)) {
                    	component = this.registerDefaultComponent(
                            ClassUtils.loadClass(typeName), 
                    		ClassUtils.loadClass(defaultImpl));
                    } else {
                        logger.warn("WARNING! No default implementation for '" 
                                + type + "' found, check services directory --> '" 
                                + SERVICES + "'");
                    }
                }
            }
            return component;
        } catch (Throwable th) {
        	String errMsg = "Error performing " + methodName;
            logger.error(errMsg, th);
            throw new ComponentContainerException(errMsg, th);
        }
    }
    
    /**
     * Finds all components having the given <code>type</code>.
     * 
     * @param type the component type.
     * @return Collection all components
     */
    public Collection findComponentsOfType(final Class type) {
        class ClassMatcher implements Predicate {
            public boolean evaluate(Object object) {
                boolean match = false;
                if (object != null) {
                    match = type.isAssignableFrom(object.getClass());
                }
                return match;
            }
        }
        Collection components = new ArrayList(this.container.getComponentInstances());
        CollectionUtils.filter(
            components, 
            new ClassMatcher());        
        return components;
    }
    
    /**
     * Unregisters the component in this container
     * with a unique (within this container) <code>key</code>.
     * 
     * @param key the unique key.
     * @return Object the registered component.
     */
    public Object unregisterComponent(String key) {
    	final String methodName = "ComponentContainer.unregisterComponent";
    	ExceptionUtils.checkEmpty(methodName, "key", key);
    	if (logger.isDebugEnabled())
    		logger.debug("unregistering component with key --> '" + key + "'");
    	return container.unregisterComponent(key);
    }
    
    /**
     * Finds a component in this container
     * with a unique (within this container) <code>key</code> 
     * registered by the specified <code>namespace</code>.
     * 
     * @param namespace the namespace for which to search.
     * @param key the unique key.
     * @return the found component, or null.
     */
    public Object findComponentByNamespace(String namespace, String key) {
    	final String methodName = "findComponentByNamespace";
    	ExceptionUtils.checkEmpty(methodName, "namespace", namespace);
    	ExceptionUtils.checkNull(methodName, "key", key);
    	
    	Object component = null;
    	ComponentContainer container = 
    		(ComponentContainer)this.findComponent(namespace);
    	if (container != null) {
    		component = container.findComponent(key);
    	}
    	return component;
    } 
    
    /**
     * Registers true (false otherwise) if the component in this container
     * with a unique (within this container) <code>key</code> 
     * is registered by the specified <code>namespace</code>.
     * 
     * @param namespace the namespace for which to register the component.
     * @param key the unique key.
     * @return boolean true/false depending on whether or not it is registerd.
     */
    public boolean isRegisteredByNamespace(String namespace, String key) {
    	final String methodName = "ComponentContainer.isRegisteredByNamespace";
    	ExceptionUtils.checkEmpty(methodName, "namespace", namespace);
    	ExceptionUtils.checkNull(methodName, "key", key);
    	
    	ComponentContainer container = 
    		(ComponentContainer)this.findComponent(namespace);
    	
    	return container != null && container.isRegistered(key);
    } 
    
    /**
     * Registers true (false otherwise) if the component in this container
     * with a unique (within this container) <code>key</code> is registered.
     * 
     * @param key the unique key.
     * @return boolean true/false depending on whether or not it is registerd.
     */
    public boolean isRegistered(String key) {
    	return this.findComponent(key) != null;
    }
    
    /**
     * Registers the component in this container
     * with a unique (within this container) <code>key</code> 
     * by the specified <code>namespace</code>.
     * 
     * @param namespace the namespace for which to register the component.
     * @param key the unique key.
     * @return Object the registered component.
     */
    public Object registerComponentByNamespace(String namespace, String key, Object component) {
    	final String methodName = "ComponentContainer.registerComponentByNamespace";
    	ExceptionUtils.checkEmpty(methodName, "namespace", namespace);
    	ExceptionUtils.checkNull(methodName, "component", component);
    	if (logger.isDebugEnabled())
    		logger.debug("registering component '" 
    				+ component + "' with key --> '" + key + "'");
    	
    	ComponentContainer container = 
    		(ComponentContainer)this.findComponent(namespace);
    	if (container == null) {
    		container = new ComponentContainer();
    		this.registerComponent(namespace, container);
    	}
    	return container.registerComponent(key, component);
    } 
   
    /**
     * Registers the component in this container
     * with a unique (within this container) <code>key</code>.
     * 
     * @param key the unique key.
     * @return Object the registered component.
     */
    public Object registerComponent(String key, Object component) {
        final String methodName = "ComponentContainer.registerComponent";
        ExceptionUtils.checkNull(methodName, "component", component);
        if (logger.isDebugEnabled())
        	logger.debug("registering component '" 
                + component + "' with key --> '" + key + "'");
        return container.registerComponentInstance(
            key, component).getComponentInstance();
    }
    
    /**
     * Registers the "default" for the specified componentInterface.
     * 
     * @param componentInterface the interface for the component.
     * @param defaultTypeName the name of the "default" type of the 
     *        implementation to use for the componentInterface. Its expected
     *        that this is the name of a class.
     * @return Object the registered component.
     */
    public Object registerDefaultComponent(Class componentInterface, String defaultTypeName) {
        final String methodName = "ComponentContainer.registerDefaultComponent";        
        ExceptionUtils.checkNull(
                methodName, 
                "componentInterface", 
                componentInterface);
            ExceptionUtils.checkEmpty(
                methodName, 
                "defaultTypeName", 
                defaultTypeName);   
        try {
	        return this.registerDefaultComponent(
	            componentInterface, 
	            ClassUtils.loadClass(defaultTypeName));
        } catch (Throwable th) {
        	String errMsg = "Error performing " + methodName;
            logger.error(errMsg, th);
            throw new ComponentContainerException(errMsg, th);
        }
    }
    
    /**
     * Registers the "default" for the specified componentInterface.
     * 
     * @param componentInterface the interface for the component.
     * @param defaultType the "default" implementation to use for the
     *        componentInterface.
     * @return Object the registered component.
     */
    public Object registerDefaultComponent(Class componentInterface, Class defaultType) {
        final String methodName = "ComponentContainer.registerDefaultComponent";
        ExceptionUtils.checkNull(
            methodName, 
            "componentInterface", 
            componentInterface);
        ExceptionUtils.checkNull(
            methodName, 
            "defaultType", 
            defaultType);
        if (logger.isDebugEnabled())
            logger.debug("registering default for component '" 
                    + componentInterface  + "' as type --> '" + defaultType + "'");
        try {
            String interfaceName = componentInterface.getName();
            // check and unregister the component if its registered
            // so that we can register a new default component.
            if (this.isRegistered(interfaceName)) {
                this.unregisterComponent(interfaceName);
            }
            return container.registerComponentInstance(
                interfaceName,
                defaultType.newInstance()).getComponentInstance();
        } catch (Throwable th) {
        	String errMsg = "Error performing " + methodName;
            logger.error(errMsg, th);
            throw new ComponentContainerException(errMsg, th);
        }
    }
    
    /**
     * Registers the component of the specified <code>type</code>.
     * 
     * @param type the type Class.
     * @return Object an instance of the type registered.
     */
    public Object registerComponentType(Class type) {
        final String methodName = "ComponentContainer.registerComponent";
        ExceptionUtils.checkNull(methodName, "type", type);
        return this.container.registerComponentImplementation(
            type).getComponentInstance();
    }
    
    /**
     * Registers the components of the specified 
     * <code>type</code>.
     * @param type the name of a type (must have be able
     *        to be instantiated into a Class instance)
     * @return Object an instance of the type registered.
     */
    public Object registerComponentType(String type) {
        final String methodName = "ComponentContainer.registerComponent";
        ExceptionUtils.checkNull(methodName, "type", type);
        try {
            Object component = ClassUtils.loadClass(type).newInstance();
        	return this.registerComponent(type, component);
        } catch (Throwable th) {
        	String errMsg = "Error performing "
                + methodName;
        	logger.error(errMsg, th);
            throw new ComponentContainerException(errMsg, th);
        }
    }
    
}
