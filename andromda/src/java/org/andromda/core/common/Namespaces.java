package org.andromda.core.common;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * Dictionary of configurable Namespace objects.  
 * Namespace objects are used for configuring Plugin
 * instances.
 * 
 * @see org.andromda.core.common.Namespace
 * 
 * @author Chad Brandon
 */
public class Namespaces {
	
	private static Logger logger = Logger.getLogger(Namespaces.class);
	
	private final static Namespaces instance = new Namespaces();

	/**
	 * This is passed as the cartridge name for the findNamespaceProperty
	 * method if we wish to use a 'default' Namespace for Plugins.
	 * This is so we don't need to define a specific mapping for each Plugin
	 * if we don't want.  If a namespaceName exists with
	 * a specific Plugin name, then that will be used instead
	 * of the 'default'
	 */
	public static final String DEFAULT = "default";

	private Map namespaces;

	/**
	 * Constructs an instance of Namespaces.
	 */
	public Namespaces() {
		this.namespaces = new HashMap();
	}

	/**
	 * Returns the singleton instance of this Namespaces
	 * @return instance.
	 */
	public static Namespaces instance() {
		return instance;
	}

	/**
	 * Adds a mapping for a namespaces name to a physical directory.
	 * 
	 * @param namespace the Namespace to add to this instance.
	 */
	public void addNamespace(Namespace namespace) {
		namespaces.put(namespace.getName(), namespace);
	}
    
    /**
     * Finds the Namespace with the corresponding 
     * <code>namespaceName</code>.
     * 
     * @param namespaceName
     * @return the found Namespace
     */
    public Namespace findNamespace(String namespaceName) {
        return (Namespace)namespaces.get(namespaceName);
    }

	/**
	 * Retrieves a property from the Namespace with the namespaceName. If
     * the <code>ignore</code> attribute of the Property instance is set to
     * <code>true</code> then lookup of the property will not be attempted
     * and null will just be returned instead.
	 * 
	 * @param namespaceName name of the Plugin to which the contexdt applies
	 * @param propertyName name of the namespace property to find.
	 * @return String the namespace property value.
	 */
	public Property findNamespaceProperty(String namespaceName, String propertyName) {
		final String methodName = "Namespaces.findNamespaceProperty";
		ExceptionUtils.checkEmpty(methodName, "namespaceName", namespaceName);
		ExceptionUtils.checkEmpty(methodName, "propertyName", propertyName);
        
		Property property = null;
		
		Namespace namespace = (Namespace)namespaces.get(namespaceName);

		if (namespace != null) {
			property = namespace.getProperty(propertyName);
		}
		
		//since we couldn't find a Namespace for the specified cartridge,
		//try to lookup the default
		if (property == null) {
			if (logger.isDebugEnabled())
				logger.debug("no namespace with name '" 
					+ namespaceName 
                    + "' found, looking for '" 
                    + DEFAULT + "'");
			namespace = (Namespace)namespaces.get(DEFAULT);

			if (namespace != null) {
				property = namespace.getProperty(propertyName);
			}
		}
        
        if (namespace == null) {
            
            logger.warn("WARNING! No '" + DEFAULT + "' or '" 
                + namespaceName + "' namespace found, "
                + "--> please define a namespace with" 
                + " at least one of these names, if you would like "
                + "to ignore this message, define the namespace with "
                + "ignore set to 'true'");            
            
        } else if (property == null) {
            
			logger.warn("WARNING! Namespaces '" + DEFAULT + "' and '" 
				+ namespaceName + "' have no property '"
				+ propertyName + "' defined --> please define this " 
                + "property in AT LEAST ONE of these two namespaces. " 
                + " If you want to 'ignore' this message, add the " 
                + "property to the namespace with ignore set to 'true'");
		}

		return property;
	}
}
