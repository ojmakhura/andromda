package org.andromda.core.common;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;

/**
 * Contains the configuration of a template object which are
 * objects that are made available to the 
 * cartridge templates.
 * 
 * @author Chad Brandon
 */
public class TemplateObject {
	
	private static Logger logger = Logger.getLogger(TemplateObject.class);
	
	private String name;
	private String className;
	
	private Map objectCache = new HashMap();
	
	private Collection propertyReferences = new ArrayList();
	
	private URL resource;
	
	private String namespace;

	/**
	 * Gets the current name of this TemplateObject.
     * 
	 * @return String
	 */
	public String getName() {
		final String methodName = "TemplateObject.getName";
		if (StringUtils.isEmpty(name)) {
			throw new TemplateObjectException(methodName 
				+ " - templateObject '" 
				+ this 
				+ "' has no name defined");
		}
		return name;
	}

	/**
	 * Returns the TemplateObject instance.
	 * @return TemplateObject
	 */
	public Object getTemplateObject() {
		final String methodName = "TemplateObject.getTemplateObject";
		if (StringUtils.isEmpty(name)) {
			throw new TemplateObjectException(methodName 
				+ " - templateObject '" 
				+ this 
				+ "' has no className defined");
		}
		
		Object templateObject = 
			this.objectCache.get(className);
		try {
			if (templateObject == null) {
				Class templateObjectClass = ClassUtils.loadClass(className);
				templateObject = templateObjectClass.newInstance();
				this.setProperties(templateObject);
				this.objectCache.put(className, templateObject);
			}
		} catch (Exception ex) {
			String errMsg = "Error performing " + methodName;
			logger.error(errMsg, ex);
			throw new TemplateObjectException(errMsg, ex);
		}
		return templateObject;
	}
	
	/**
	 * Sets all the nested properties on the templateObject object.
	 * 
	 * @param templateObject
	 */
	protected void setProperties(Object templateObject) {
		Iterator referenceIt = this.propertyReferences.iterator();
		while (referenceIt.hasNext()) {
			String reference = (String)referenceIt.next();

			Object propertyValue =   
				Namespaces.instance().findNamespaceProperty(
					this.getNamespace(), reference);
			
			if (logger.isDebugEnabled())
				logger.debug("setting property '" 
					+ name + "' with value '" + propertyValue 
					+ "' on templateObject '" 
					+ templateObject + "'");
			try {
				PropertyUtils.setProperty(templateObject, reference, propertyValue);
			} catch (Exception ex) {
				String errMsg = "Error setting property '" 
					+ name + "' with '" + reference 
					+ "' on templateObject --> '" + templateObject + "'";
				logger.error(errMsg, ex);
				//don't throw the exception
			}

		}
	}
	

	/**
	 * Sets the name of the template object (this
	 * name will be what the template class is stored
	 * under in the template)
	 * @param name the name of the template object.
	 */
	public void setName(String name) {
		this.name = StringUtils.trimToEmpty(name);
	}

	/**
	 * Sets the class of the transformation object.
	 * 
	 * @param className 
	 */
	public void setClassName(String className) {
		final String methodName = "TemplateObject.setTransformationClass";
		ExceptionUtils.checkEmpty(methodName, "className", className);
		this.className = className;
	}
	
	/**
	 * Adds a templateObject property reference (used to customize templateObjects).
     * Property references are used to populate bean like properties
     * of template objects.
	 * 
	 * @param reference
	 */
	public void addPropertyReference(String reference) {
		this.propertyReferences.add(reference);
	}
	
	/**
	 * The resource in which the templateObject was found.
	 * 
	 * @return URL
	 */
	public URL getResource() {
		return resource;
	}

	/**
	 * Sets the resource in which the templateObject was found.
	 * @param resource
	 */
	public void setResource(URL resource) {
		this.resource = resource;
	}
    
    /**
     * @return Returns the namespace.
     */
    public String getNamespace() {
        return namespace;
    }

    /**
     * @param namespace The namespace to set.
     */
    public void setNamespace(String namespace) {
        this.namespace = StringUtils.trimToEmpty(namespace);
    }

    /**
     * @see java.lang.Object#toString()
     */
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
