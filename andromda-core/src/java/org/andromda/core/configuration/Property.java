package org.andromda.core.configuration;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * This class represents properties which are used to configure Namespace objects which are made available to configure
 * Plugin instances.
 *
 * @author Chad Brandon
 * @see org.andromda.core.configuration.Namespace
 * @see org.andromda.core.configuration.Namespaces
 */
public class Property
    implements Serializable
{
    /**
     * The property name.
     */
    private String name;

    /**
     * Returns the name. This is used by Namespaces to find this property.
     *
     * @return String
     */
    public String getName()
    {
        return name;
    }

    /**
     * Sets the name.
     *
     * @param name The name to set
     */
    public void setName(final String name)
    {
        this.name = StringUtils.trimToEmpty(name);
    }

    /**
     * The property value.
     */
    private String value;

    /**
     * Returns the value. This is the value that is stored in this property.
     *
     * @return the value as a String
     */
    public String getValue()
    {
        return value;
    }

    /**
     * Sets the value.
     *
     * @param value The value to set
     */
    public void setValue(final String value)
    {
        this.value = StringUtils.trimToEmpty(value);
    }

    /**
     * Stores whether or not this property should be ignored.
     */
    private boolean ignore = false;

    /**
     * If a property is set to ignore then Namespaces will ignore it if it doesn't exist on lookup (otherwise errors
     * messages are output). This is useful if you have a plugin on a classpath (its unavoidable), but you don't want to
     * see the errors messages (since it really isn't an error). Another use of it would be to ignore outlet entires for
     * cartridges if you wanted to generate some from the cartridge outlets, but not others.
     *
     * @return Returns the ignore value true/false.
     */
    public boolean isIgnore()
    {
        return ignore;
    }

    /**
     * @param ignore The ignore to set.
     * @see #isIgnore()
     */
    public void setIgnore(final boolean ignore)
    {
        this.ignore = ignore;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }
}