package org.andromda.core.metafacade;

import java.util.ArrayList;
import java.util.Collection;

import org.andromda.core.common.ClassUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * A meta facade mapping class.  This class is a child
 * of the {@link MetafacadeMappings} class. 
 * 
 * @author Chad Brandon
 */
public class MetafacadeMapping {
	
	private static Logger logger = Logger.getLogger(MetafacadeMapping.class);
	
	/**
	 * The meta facade for which this
	 * mapping applies.
	 */
	private Class metafacadeClass = null;
	
	/**
	 * The meta model class to for which this
	 * mapping applies. The <code>stereotypes</code>
	 * and this variable make up the identifying key
	 * for this mappping.
	 */
	private String metaobjectClassName = null;
	
	/**
	 * The stereotype to which this mapping applies.
	 */
	private String stereotype = null;
	
	/**
	 * Used to hold references to language mapping classes.
	 */
	private Collection propertyReferences = new ArrayList();
	
	/**
	 * The parent mappings instance that owns
	 * this mapping.
	 */
	private MetafacadeMappings mappings;
	
	/**
	 * The key used to uniquely identify this mapping.
	 */
	private String key;
	
	/**
	 * The context to which this mapping applies.
	 */
	private String context;

	/**
	 * Gets the metafacadeClass for this mapping.
	 * 
	 * @return Returns the metafacadeClass.
	 */
	public Class getMetafacadeClass() {
		return metafacadeClass;
	}

	/**
	 * Sets the metafacadeClassName for this mapping.
	 * 
	 * @param metafacadeClass The metafacadeClass to set. 
	 */
	public void setMetafacadeClassName(String metafacadeClassName) {
		try {
		    this.metafacadeClass = 
		        ClassUtils.loadClass(StringUtils.trimToEmpty(metafacadeClassName));
		} catch (Throwable th) {
			String errMsg = "Error performing setMetafacadeClassName";
			logger.error(errMsg, th);
			throw new MetafacadeMappingsException(errMsg, th);
		}
	}

	/**
	 * Gets the name of the metaobject class used
	 * for this mapping.
	 * 
	 * @return Returns the metaobjectClass.
	 */
	protected String getMetaobjectClassName() {
		return this.metaobjectClassName;
	}

	/**
	 * The name of the metaobject class to use for this mapping.
	 * 
	 * @param metaobjectClassName The metaobjectClassName to set.
	 */
	public void setMetaobjectClassName(String metaobjectClassName) {
		try {
			this.metaobjectClassName = StringUtils.trimToEmpty(metaobjectClassName);
		} catch (Throwable th) {
			String errMsg = "Error performing setMetafacadeClass";
			logger.error(errMsg, th);
			throw new MetafacadeMappingsException(errMsg, th);
		}
	}
	
	/**
	 * Adds a <code>stereotype</code> to the stereotypes
	 * for which the <code>metafacadeClass</code> 
	 * should be instead. 
	 * @param stereotype
	 */
	public void setStereotype(String stereotype) {
		this.stereotype = StringUtils.trimToEmpty(stereotype);
	}
	
	/**
	 * Adds a mapping reference.  This are used
	 * to populate metafacade impl classes with mapping
	 * files, etc. If its added here as opposed to each
	 * child MetafacadeMapping, then the reference will
	 * apply to all mappings.
	 * 
	 * @param reference
	 */
	public void addPropertyReference(String reference) {
		this.propertyReferences.add(StringUtils.trimToEmpty(reference));
	}
	
	/**
	 * Returns all mapping references for this
	 * MetafacadeMapping instance.
	 *
	 */
	public Collection getPropertyReferences() {
		return this.propertyReferences;
	}
	
	/**
	 * Sets the MetafacadeMappings to which this MetafacadeMapping belongs.
	 * 
	 * @param mappings
	 */
	protected void setMetafacadeMappings(MetafacadeMappings mappings) {
		this.mappings = mappings;
	}
	
	/**
	 * Returns the MetafacadeMappings to which this mapping
	 * belongs.
	 * @return the MetafacadeParent.
	 */
	protected MetafacadeMappings getMetafacadeMappings() {
		return this.mappings;
	}
	
	/**
	 * Gets the unique key that identifies this mapping
	 */
	protected String getKey() {
		if (StringUtils.isEmpty(this.key)) {
			if (StringUtils.isEmpty(this.context)) {
				key = MetafacadeMappingsUtils.constructKey(
				    this.metaobjectClassName,
					this.stereotype);
			} else {
				key = MetafacadeMappingsUtils.constructKey(
					this.metaobjectClassName,
					this.context);				
			}
		}
		return key;
	}

	/**
	 * Sets the context to which this mapping
	 * applies.
	 * 
	 * @param metafacadeContext The metafacadeContext to set.
	 */
	public void setContext(String context) {
		this.context = StringUtils.trimToEmpty(context);
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return MetafacadeMappingsUtils.appendContext(
				super.toString(),
				this.getKey());
	}

}
