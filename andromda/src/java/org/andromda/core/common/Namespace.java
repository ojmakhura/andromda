package org.andromda.core.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * A configurable namespace object.  These are passed to Plugin 
 * instances (Cartridges, etc.).
 * 
 * @author Chad Brandon
 */
public class Namespace {
	
	private String name;
	private Map properties;
    private boolean ignore = false;
	private Collection initCollection = new ArrayList();
	
	/**
	 * This method normally would be unnecessary. It is here because of the way Ant behaves.
	 * Ant calls addProperty() before the PropertyReference javabean is fully
	 * initialized (therefore the 'name' isn't set). So we kept the javabeans in an 
	 * ArrayList that we have to copy into the properties Map.
	 */
	public void init() {
		if (this.properties == null) {
			this.properties = new HashMap();
			for (Iterator iter = initCollection.iterator(); iter.hasNext();) {
				Property property = (Property)iter.next();
				this.properties.put(property.getName(), property);
			}	
		}	
	}

	/**
	 * Returns name of this Namespace.  Will correspond
     * to a Plugin name (or it can be be 'default'
     * if we want it's settings to be used everywhere).
     * 
	 * @return String
	 */
	public String getName() {
		this.init();
		return name;
	}

	/**
	 * Sets the name of this Namespace.
     * 
	 * @param name The name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Adds a property to this Namespace object.  A property
     * must correspond to a java bean property name on a
     * Plugin in order for it to be set during processing.
     * Otherwise the property will just be ignored.
	 * 
	 * @param property
	 */
	public void addProperty(Property property) {
		final String methodName = "Namespace.addProperty";
		ExceptionUtils.checkNull(methodName, "property", property);
		this.initCollection.add(property);
	}
	
	/**
	 * Retrieves the property with the specified name.
     * 
	 * @param name
     * 
	 * @return PropertyReference.
	 */
	public Property getProperty(String name) {
		this.init();
		return (Property)this.properties.get(name);
	}
	
    /**
     * @see java.lang.Object#toString()
     */
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	/**
     * If a namespace is set to ignore then anything
     * looking up or using a namespace can use it for its
     * own purposes, for example if there is a plugin
     * on the classpath (which is unavoidable) and you 
     * want to ingore that plugin, the you may check
     * to see if the namespace that configures that
     * plugin is set to <code>true</code> for ignore.
     * 
	 * @return Returns the ignore value.
	 */
	public boolean isIgnore() {
		return ignore;
	}
    
	/**
	 * @param ignore The ignore to set.
	 */
	public void setIgnore(boolean ignore) {
		this.ignore = ignore;
	}
}
