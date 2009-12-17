package org.andromda.core.configuration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;


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

    private Collection<String> namespaces = new ArrayList<String>();

    /**
     * Sets the comma separated list of namespaces to which the filter applies.
     *
     * @param namespaces a comma separated list of namespaces to apply to the filter.
     */
    public void setNamespaces(String namespaces)
    {
        this.namespaces.clear();
        this.namespaces.addAll(Arrays.asList(namespaces.split("\\s*,\\s*")));
    }

    /**
     * Gets the list of namespaces that this filter applies to.
     *
     * @return the list of namespaces.
     */
    public Collection<String> getNamespaceList()
    {
        return this.namespaces;
    }
}