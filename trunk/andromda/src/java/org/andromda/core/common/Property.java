package org.andromda.core.common;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * This class represents properties which are used to 
 * configure Namespace objects which are made available
 * to configure Plugin instances.
 * 
 * @see org.andromda.core.common.Namespace
 * @see org.andromda.core.common.Namespaces
 * 
 * @author Chad Brandon
 */
public class Property {
	
    private String name;
    private String value;
    private boolean ignore = false;
    
    /**
     * Returns the name.  This is used by Namespaces
     * to find this property.
     * 
     * @return String
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the value.  This is the value
     * that is stored in this property.
     * 
     * @return the value as a String
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the name.
     * @param name The name to set
     */
    public void setName(String name) {
        this.name = StringUtils.trimToEmpty(name);
    }

    /**
     * Sets the value.
     * @param value The value to set
     */
    public void setValue(String value) {
        this.value = StringUtils.trimToEmpty(value);
    }
    
    /**
     * If a property is set to ignore then Namespaces
     * will ignore it if it doesn't exist on lookup (otherwise
     * errors messages are output).  This is useful if you
     * have a plugin on a classpath (its unavoidable), 
     * but you don't want to see the errors messages (since it 
     * really isn't an error).  Another use of it would be to ignore outlet
     * entires for cartridges if you wanted to generate some from the
     * cartridge outlets, but not others.
     * 
     * @return Returns the ignore value true/false.
     */
    public boolean isIgnore() {
        return ignore;
    }
    
    /**
     * @see #isIgnore()
     * 
     * @param ignore The ignore to set.
     */
    public void setIgnore(boolean ignore) {
        this.ignore = ignore;
    }
    
    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
    	return ToStringBuilder.reflectionToString(this);
    }
}
