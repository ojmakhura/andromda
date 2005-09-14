package org.andromda.core.configuration;

import java.io.Serializable;


/**
 * Used to specify which filter should or should not be applied within a model.
 *
 * @author Chad Brandon
 * @see org.andromda.core.configuration.Filters
 */
public class Filter
    implements Serializable
{
    private String value;

    /**
     * Gets the value of this Filter.
     *
     * @return Returns the value.
     */
    public String getValue()
    {
        return value;
    }

    /**
     * Sets the value of this Filter.
     *
     * @param name the value of the filter.
     */
    public void setValue(final String name)
    {
        this.value = name;
    }

    /**
     * The flag indicating whether or not this filter
     * should be applied.
     */
    private boolean apply = true;

    /**
     * Whether or not this filter should be applied.
     *
     * @return Returns the shouldProcess.
     */
    public boolean isApply()
    {
        return this.apply;
    }

    /**
     * Sets whether or not this Filter should be applied.
     *
     * @param process The shouldProcess to set.
     */
    public void setApply(final boolean process)
    {
        this.apply = process;
    }
}