package org.andromda.core.mapping;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.andromda.core.common.ExceptionUtils;
import org.andromda.core.common.XmlObjectFactory;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;

/**
 * An object responsible for mapping types in the object model to other language identifiers/types.
 * (For example, Java, SQL, Jdbc, etc).  The public constructor should NOT be used to 
 * construct this instance.  An instance of this object should be retrieved
 * through the method getInstance(java.net.URL). 
 * 
 * <p> The mappings will change based upon the language, database, etc being used. <p>
 * 
 * @see org.andromda.common.XmlObjectFactory
 * 
 * @author Chad Brandon
 */
public class Mappings {
	
	private static Logger logger = Logger.getLogger(Mappings.class);
	
	/**
	 * Holds the name of this mapping. This corresponds usually to some language (i.e. Java,
	 * or a database such as Oracle, Sql Server, etc).
	 */
	private String name = null;
	
	/**
	 * Contains the set of Mapping objects keyed by the 'type'
	 * element defined within the type mapping XML file.
	 */
	private Map mappings = new HashMap();
	
	/**
	 * Holds the resource path from which this Mappings object
	 * was loaded.
	 */
	private URL resource;
	
	/**
	 * Returns a new configured instance of this Mappings
	 * configured from the mappings configuration URI string.
	 * 
	 * @param mappingsUri the URI to the XML type mappings configuration file.
	 * @return Mappings the configured Mappings instance.
	 * @throws MalformedURLException when the mappingsUri is invalid (not a valid URL).
	 */
	public static Mappings getInstance(String mappingsUri) throws MalformedURLException {
		final String methodName = "Mappings.getInstance";
		mappingsUri = StringUtils.trimToEmpty(mappingsUri);
		ExceptionUtils.checkEmpty(methodName, "mappingsUri", mappingsUri);
		Mappings mappings = getInstance(new URL(mappingsUri));
		return mappings;
	}
	
	/**
	 * Returns a new configured instance of this Mappings
	 * configured from the mappings configuration URI.
	 * 
	 * @param mappingsUri the URI to the XML type mappings configuration file.
	 * @return Mappings the configured Mappings instance.
	 */
	public static Mappings getInstance(URL mappingsUri) {
		final String methodName = "Mappings.getInstance";
		ExceptionUtils.checkNull(methodName, "mappingsUri", mappingsUri);
		Mappings mappings = (Mappings)XmlObjectFactory.getInstance(Mappings.class).getObject(mappingsUri);
		mappings.resource = mappingsUri;
		return mappings;
	}

	/**
	 * Returns the name name (this is the
	 * name for which the type mappings are for).
	 * 
	 * @return String the name name
	 */
	public String getName() {
		final String methodName = "Mappings.getName";
		if (StringUtils.isEmpty(this.name)) {
			throw new MappingsException(methodName
					+ " - name can not be null or empty");
		}
		return name;
	}

	/**
	 * Sets the name name.
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Adds a Mapping object to the set of current mappings.
	 * 
	 * @param mapping the Mapping instance.
	 */
	public void addMapping(Mapping mapping) {
		final String methodName = "Mappings.addMapping";
		ExceptionUtils.checkNull(methodName, "mapping", mapping);
		
		Collection fromTypes = mapping.getFroms();
		ExceptionUtils.checkNull(methodName, "mapping.fromTypes", fromTypes);
		
		Iterator typeIt = fromTypes.iterator();
		while (typeIt.hasNext()) {
			String type = (String)typeIt.next();
			this.mappings.put(type, mapping);	
		}
	}
	
    /**
    * Returns the <code>to</code> mapping from a given <code>from</code>
    * mapping.
    *
    * @param from the <code>from</code> mapping, this is the type/identifier
    *        that is in the model.
    * @return String to the <code>to</code> mapping (this is the mapping that
    *         can be retrieved if a corresponding 'from' is found.
    */
	public String getTo(String from) {
        from = StringUtils.deleteWhitespace(from);
        String initialFrom = from;
        
        String to = null;
            
        String arraySuffix = "[]";
        //if the type is an array suffix, then strip the array off
        //so we can find the mapping
        int suffixIndex = from.indexOf(arraySuffix);
        if (suffixIndex != -1) {
            from = StringUtils.replace(from, arraySuffix, "");
        }
        
        Mapping mapping = this.getMapping(from);
        
        if (mapping != null) {
            StringBuffer buf = new StringBuffer(mapping.getTo());
        
            if (suffixIndex != -1) {
                //append the suffix back to the return value;
                buf.append(arraySuffix);
            }
            to = buf.toString();    
        }
        
        if (to == null) {
            if (logger.isDebugEnabled())
                logger.debug("no mapping for type '" 
                        + from 
                        + "' found, using default name --> '" + initialFrom + "'");
            to = initialFrom;
        } else {
            if (logger.isDebugEnabled())
                logger.debug("mapping for type '" 
                        + from
                        + "' found, using found mapping --> '" + to + "'");
        }   
        
        return StringUtils.trimToEmpty(to);
	}
	
	/**
	 * Returns true if the mapping contains the <code>from</code>
	 * value
	 * @param from
	 * @return true if it contains <code>from</code>, false otherwise.
	 */
	public boolean containsFrom(String from) {
		return this.getMapping(StringUtils.trimToEmpty(from)) != null;
	}
	
	/**
	 * Returns the resource URI from 
	 * which this Mappings object was loaded.
	 * @return URL
	 */
	public URL getResource() {
		return this.resource;
	}
	
	private Mapping getMapping(String from) {
		return (Mapping)mappings.get(from);
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
