package org.andromda.core.mapping;

import java.util.Collection;
import java.util.HashSet;

import org.andromda.core.common.ExceptionUtils;
import org.apache.commons.lang.StringUtils;

/**
 * A single child mapping instance belonging to 
 * a Mappings instance. It doesn't make sense to instantiate
 * this class by itself.
 * 
 * @see org.andromda.core.mapping.Mappings
 * @author Chad Brandon
 */
public class Mapping {
	
	private Collection froms = new HashSet();
	
	private String to;
	
	/** 
	 * Adds the <code>from</code> type to the mapping.
	 * 
	 * @param from the type that we are mapping from.
	 */
	public void addFrom(String from) {
		final String methodName = "Mappings.addFrom";
		ExceptionUtils.checkNull(methodName, "from", from);
		froms.add(from);
	}
	
	/**
	 * Return the Collection of froms.
	 * 
	 * @return Collection
	 */
	public Collection getFroms() {
		return froms;
	}
	
	/**
	 * Returns the to type for this mapping.
	 * 
	 * @return String the to type
	 */
	public String getTo() {
		final String methodName = "Mappings.getTo";	
		if (StringUtils.isEmpty(this.to)) {
			throw new MappingsException(methodName
				 + " - 'to' can not be null or empty");
		}			
		return this.to;
	}

	/**
	 * Sets the type for this mapping.
	 * @param to
	 */
	public void setTo(String to) {
		this.to = to;
	}

}
