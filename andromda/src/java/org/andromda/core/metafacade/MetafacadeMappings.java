package org.andromda.core.metafacade;

import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import org.andromda.core.common.ClassUtils;
import org.andromda.core.common.ExceptionUtils;
import org.andromda.core.common.Namespaces;
import org.andromda.core.common.ResourceFinder;
import org.andromda.core.common.AndroMDALogger;
import org.andromda.core.common.XmlObjectFactory;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;

/**
 * The Metafacade mapping class. Used to map
 * meta facade objects to meta model objects.
 * 
 * @see org.andromda.core.common.XmlObjectFactory
 * 
 * @author Chad Brandon
 */
public class MetafacadeMappings {
	
	private Logger logger = Logger.getLogger(MetafacadeMappings.class);
	
	/**
	 * Contains the mappings XML used for mapping Metafacades.
	 */
	private static String METAFACADES_URI = "META-INF/andromda-metafacades.xml";
	
	/**
	 * Holds the references to the child MetafacadeMapping instances.
	 */
	private Map mappings = new HashMap();

	/**
	 * The namespace to which this MetafacadeMappings
	 * instance applies.
	 */
	private String namespace = null;
	
	/**
	 * Holds the namespace MetafacadeMappings. This are child
	 * MetafacadeMappings keyed by namespace name.
	 */
	private Map namespaceMetafacadeMappings = new HashMap();
	
	/**
	 * Holds the resource path from which this MetafacadeMappings object
	 * was loaded.
	 */
	private URL resource;
	
	/**
	 * Contains references to properties populated in the Namespaces.
	 */
	private Collection propertyRefs = new HashSet();
	
	/**
	 * Property references keyed by namespace, these
	 * are populated on the first call to getPropertyReferences
	 * below.
	 */
	private Map namespacePropertyRefs = null;
	
	/**
	 * The shared static instance.
	 */
	private static MetafacadeMappings instance = null;
	
	/**
	 * The default meta facade to use when there isn't a 
	 * mapping found.
	 */
	private Class defaultMetafacadeClass = null;
	
	/**
	 * Gets the shared instance.
	 * 
	 * @return MetafacadeMappings
	 */
	public static MetafacadeMappings instance() {
		if (instance == null) {
			instance = new MetafacadeMappings();
		}
		return instance;
	}
	
	/**
	 * Returns a new configured instance of this MetafacadeMappings
	 * configured from the mappings configuration URI.
	 * 
	 * @param mappingsUri the URI to the XML type mappings configuration file.
	 * @return MetafacadeMappings the configured MetafacadeMappings instance.
	 */
	protected static MetafacadeMappings getInstance(URL mappingsUri) {
		final String methodName = "MetafacadeMappings.getInstance";
		ExceptionUtils.checkNull(methodName, "mappingsUri", mappingsUri);
		MetafacadeMappings mappings = 
			(MetafacadeMappings)XmlObjectFactory.getInstance(
				MetafacadeMappings.class).getObject(mappingsUri);
		mappings.resource = mappingsUri;
		return mappings;
	}


	/**
	 * @return Returns the namespace.
	 */
	public String getNamespace() {
		final String methodName = "MetafacadeMappings.getNamespace";
		ExceptionUtils.checkEmpty(methodName, "namespace", this.namespace);
		return this.namespace;
	}

	/**
	 * @param namespace The namepace to set.
	 */
	public void setNamespace(String namespace) {
		this.namespace = StringUtils.trimToEmpty(namespace);
	}
	
	/**
	 * Adds a MetafacadeMapping object to the set of current mappings.
	 * 
	 * @param mapping the MetafacadeMapping instance.
	 */
	public void addMapping(MetafacadeMapping mapping) {
		final String methodName = "MetafacadeMappings.addMapping";
		ExceptionUtils.checkNull(methodName, "mapping", mapping);
		
		String metaobjectClassName = mapping.getMetaobjectClassName();
		ExceptionUtils.checkEmpty(
			methodName, 
			"mapping.metaobjectClassName", 
			metaobjectClassName);
		ExceptionUtils.checkNull(
			methodName,
			"mapping.metafacadeClass",
			mapping.getMetafacadeClass());
		
		mapping.setMetafacadeMappings(this);
		this.mappings.put(mapping.getKey(), mapping);
	}
	
	/**
	 * Copies all data from <code>mappings<code> to this
	 * instance.
	 * 
	 * @param mappings
	 */
	private void copyMappings(MetafacadeMappings mappings) {
		final String methodName = "MetafacadeMappings.copyMappings";
		ExceptionUtils.checkNull(
			methodName, 
			"mappings", 
			mappings);
		this.setNamespace(mappings.getNamespace());
		Iterator keyIt = mappings.mappings.keySet().iterator();
		while (keyIt.hasNext()) {
			String metaobjectClass = (String)keyIt.next();
			this.addMapping((MetafacadeMapping)mappings.mappings.get(metaobjectClass));
		}
		Collection propertyRefs = mappings.propertyRefs;
		if (propertyRefs != null && !propertyRefs.isEmpty()) {
			this.propertyRefs.addAll(propertyRefs);
		}
		this.defaultMetafacadeClass = mappings.defaultMetafacadeClass;
		
	}
	
	/**
	 * Retrieves the MetafacadeMapping belonging to the unique
	 * <code>key</code> created from the <code>metaobjectClass</code>
	 * and <code>stereotypes</code>.
	 * @param metaobjectClass the class name of the meta model object.
	 * @param stereotypes the stereotypes to check.
     * @param context the context within the namespace for which the mapping applies
	 * @return MetafacadeMapping
	 */
	protected MetafacadeMapping getMapping(
		String metaobjectClass, 
		Collection stereotypes,
		String context) {
		
		MetafacadeMapping mapping = null;
		String key =  null;
		// loop through stereotypes and if we find a mapping 
		// that matches when constructing a key, break 
        // out of it with the mapping
		if (stereotypes != null && !stereotypes.isEmpty()) {
			Iterator stereotypeIt = stereotypes.iterator();
			while (stereotypeIt.hasNext()) {
				String stereotype = 
					StringUtils.trimToEmpty(
						(String)stereotypeIt.next());
				key = MetafacadeMappingsUtils.constructKey(
						metaobjectClass, 
						stereotype);
				mapping = (MetafacadeMapping)this.mappings.get(key);
				if (mapping != null) {
					break;
				}
			}
		} 
		
		// try getting the mapping with the context since there
		// wasn't any mapping with the matching stereotype
		if (mapping == null && StringUtils.isNotEmpty(context)) {
			// try constructing key that has the context
			key = MetafacadeMappingsUtils.constructKey(
				metaobjectClass, 
				context);	
			mapping = (MetafacadeMapping)this.mappings.get(key);
		}
		if (mapping == null) {
			if (logger.isDebugEnabled())
				logger.debug("could not find mapping for '" 
					+ key + "' find default --> '" 
					+ metaobjectClass + "'");
			mapping = (MetafacadeMapping)this.mappings.get(metaobjectClass);
		}
		return mapping;
	}
	
	/**
	 * Gets the resource that configured this instance.
	 * 
	 * @return URL to the resource.
	 */
	protected URL getResource() {
		return this.resource;
	}

	/**
	 * Adds a language mapping reference.  This are used
	 * to populate metafacade impl classes with mapping
	 * files (such as those that map from model types to Java, JDBC, SQL
	 * types, etc). If its added here as opposed to each
	 * child MetafacadeMapping, then the reference will
	 * apply to all mappings.
	 * 
	 * @param reference
	 */
	public void addPropertyReference(String reference) {
		this.propertyRefs.add(StringUtils.trimToEmpty(reference));
	}
	
	/**
	 * Returns all property references for this MetafacadeMappings
	 * by <code>namespace</code> (these include all default mapping
	 * references also).
	 * 
	 * @param namespace the namespace to search
	 */
	public Collection getPropertyReferences(String namespace) {
		Collection propertyReferences = null;
		if (this.namespacePropertyRefs == null) {
			this.namespacePropertyRefs = new HashMap();
		} else {
			propertyReferences = 
				(Collection)namespacePropertyRefs.get(namespace);			
		}
		
		if (propertyReferences == null) {
			
			// first load the property references from 
			// the mappings
			propertyReferences = new HashSet();
			propertyReferences.addAll(this.propertyRefs);
			MetafacadeMappings metafacades = this.getNamespaceMappings(namespace);
			if (metafacades != null) {
				propertyReferences.addAll(metafacades.propertyRefs);
			} 	
			this.namespacePropertyRefs.put(namespace, propertyReferences);
		}		

		return propertyReferences;
	}
	
	/**
	 * Gets all the child MetafacadeMapping instances for this MetafacadeMappings by
	 * <code>namespace</code> (these include all child mappings
	 * from the <code>default</code> mapping reference also).
	 * @param namespace the namespace of the mappings to retrieve.
	 * @return Map the child mappings (MetafacadeMapping instances)
	 */
	protected Map getMappings(String namespace) {
		MetafacadeMappings metafacades = this.getNamespaceMappings(namespace);
		if (metafacades != null) {
			this.mappings.putAll(metafacades.mappings);
		} 
		return this.mappings;
	}
	
	/**
	 * Attempts to get the MetafacadeMapping identified by the
	 * given <code>metaobjectClass</code> and <code>stereotypes<code> 
	 * from the mappings for the given <code>namespace</code> within
     * the specified <code>context</code>. 
     * If it can not be found, it will search the default mappings 
     * and return that instead.
	 * 
	 * @param metaobjectClass the class name of the meta object for the mapping
	 *        we are trying to find.
     * @param stereotypes collection of sterotype names.  We'll check to see if 
     *        the mapping for the given <code>metaobjectClass</code> is defined for it.
	 * @param namespace the namespace (i.e. a cartridge, name, etc.)
     * @param context the within the namespace
	 */
	public MetafacadeMapping getMetafacadeMapping(
		String metaobjectClass, 
		Collection stereotypes, 
		String namespace,
		String context) {
		final String methodName = "MetafacadeMappings.getMetafacadeMapping";
		if (logger.isDebugEnabled())
			logger.debug("performing '" 
				+ methodName 
				+ "' with metaobjectClass '" 
				+ metaobjectClass 
				+ "', stereotypes '" 
				+ stereotypes 
				+ "', namespace '"
				+ namespace 
				+ "' and context '"
				+ context + "'");

		MetafacadeMappings mappings = this.getNamespaceMappings(namespace);
		Class metafacadeClass = null;
		MetafacadeMapping mapping = null;
		
		// first try the namespace mappings
		if (mappings != null) {
			mapping = mappings.getMapping(metaobjectClass, stereotypes, context);
			if (mapping != null) {
				metafacadeClass = mapping.getMetafacadeClass();				
			}
		}
		
		// if the namespace mappings weren't found, try the default
		if (metafacadeClass == null) {
            if (logger.isDebugEnabled()) 
                logger.debug("namespace mapping not found finding default");
			mapping = this.getMapping(metaobjectClass, stereotypes, context);
		}
		
        if (logger.isDebugEnabled()) 
        	logger.debug("found mapping --> '" 
                + mapping 
                + "' with metafacadeClass --> '" 
                + metafacadeClass + "'");

		return mapping;
	}
	
	/**
	 * Gets the MetafacadeMappings instance belonging to the <code>namespace</code>.
	 * @param namespace the namespace name to check.
	 * @return the found MetafacadeMappings.
	 */
	private MetafacadeMappings getNamespaceMappings(String namespace) {
		return (MetafacadeMappings)this.namespaceMetafacadeMappings.get(namespace);
	}
	
	/**
	 * Adds another MetafacadeMappings instance to the namespace
	 * meta facade mappings of this instance.
	 * @param namespace the namespace name to which the <code>mappings</code> will belong.
	 * @param mappings the MetafacadeMappings instance to add.
	 */
	private void addNamespaceMappings(String namespace, MetafacadeMappings mappings) {
		this.namespaceMetafacadeMappings.put(namespace, mappings);
	}
	
	/**
	 * Discover all metafacade mapping files on the class path.  
	 * You need to call this anytime you want to find another metafacade
	 * library that may have been made available.
	 */
	public void discoverMetafacades() {
		final String methodName = "MetafacadeMappings.discoverMetafacadeMappings";
		URL uris[] = ResourceFinder.findResources(METAFACADES_URI);
		if (uris == null || uris.length == 0) {
			logger.error("ERROR!! No metafacades found, please check your classpath");
		} else {
			try {
				for (int ctr = 0; ctr < uris.length; ctr++) {
					MetafacadeMappings mappings = MetafacadeMappings.getInstance(uris[ctr]);
					String namespace = mappings.getNamespace();
					if (StringUtils.isEmpty(namespace)) {
						throw new MetafacadeMappingsException(methodName 
								+ " no 'namespace' has been set for metafacades --> '" 
								+ mappings.getResource() + "'");
					}
				    AndroMDALogger.info("found metafacades --> '" 
							+ mappings.getNamespace() + "'");
					
					if (Namespaces.DEFAULT.equals(namespace)) {
						// set the shared instance to the default mappings.
						this.copyMappings(mappings);
					} else {
						// add all others as namespace mappings
						this.addNamespaceMappings(mappings.getNamespace(), mappings);	
					}
				} 
			} catch (Throwable th) {
				String errMsg = "Error performing " + methodName;
				logger.error(errMsg, th);
				throw new MetafacadeMappingsException(errMsg, th);
			}			
			if (StringUtils.isEmpty(this.namespace)) {
				String errMsg = "No '" + Namespaces.DEFAULT + "' metafacades " +
					"found, please check your classpath";
				logger.error("ERROR!! " + errMsg);
				throw new MetafacadeMappingsException(errMsg);
			}
		}
	}

	/**
	 * Gets the defaultMetafacadeClass, first looks for it
	 * in the namespace mapping, if it can't find it it then
	 * takes the default mappings, setting.
	 * 
	 * @return Returns the defaultMetafacadeClass.
	 */
	public Class getDefaultMetafacadeClass(String namespace) {
		Class defaultMetafacadeClass = null;
		MetafacadeMappings mappings = this.getNamespaceMappings(namespace);
		if (mappings != null) {
			defaultMetafacadeClass = mappings.defaultMetafacadeClass;
		}
		if (defaultMetafacadeClass == null) {
			defaultMetafacadeClass = this.defaultMetafacadeClass;
		}
		return defaultMetafacadeClass;
	}

	/**
	 * @param defaultMetafacadeClass The defaultMetafacadeClass to set.
	 */
	public void setDefaultMetafacadeClass(String defaultMetafacadeClass) {
		try {
			this.defaultMetafacadeClass = 
				ClassUtils.loadClass(StringUtils.trimToEmpty(defaultMetafacadeClass));
		} catch (Throwable th) {
			String errMsg = "Error performing MetafacadeMappings.setDefaultMetafacadeClass";
			logger.error(errMsg, th);
			throw new MetafacadeMappingsException(errMsg, th);
		}
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
