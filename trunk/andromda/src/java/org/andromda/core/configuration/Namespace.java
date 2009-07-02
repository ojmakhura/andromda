package org.andromda.core.configuration;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * A configurable namespace object. These are passed to Plugin instances (Cartridges, etc.).
 *
 * @author Chad Brandon
 */
public class Namespace
    implements Serializable
{
    /**
     * The namespace name.
     */
    private String name;

    /**
     * Returns name of this Namespace. Will correspond to a Plugin name (or it can be be 'default' if we want it's
     * settings to be used everywhere).
     *
     * @return String
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * Sets the name of this Namespace.
     *
     * @param name The name to set
     */
    public void setName(final String name)
    {
        this.name = name;
    }

    /**
     * Stores the collected properties
     */
    private final Map<String, Collection<Property>> properties = new LinkedHashMap<String, Collection<Property>>();

    /**
     * Adds a property to this Namespace object. A property must correspond to a java bean property name on a Plugin in
     * order for it to be set during processing. Otherwise the property will just be ignored.
     *
     * @param property the property to add to this namespace.
     */
    public void addProperty(final Property property)
    {
        if (property != null)
        {
            Collection<Property> properties = this.properties.get(property.getName());
            if (properties == null)
            {
                properties = new ArrayList<Property>();
                this.properties.put(
                    property.getName(),
                    properties);
            }
            properties.add(property);
        }
    }

    /**
     * Retrieves the properties with the specified name.
     *
     * @param name the name of the property.
     *
     * @return the property
     */
    public Collection<Property> getProperties(final String name)
    {
        return this.properties.get(name);
    }

    /**
     * Retrieves the property (the first one found) with the specified name.
     *
     * @param name the name of the property.
     *
     * @return the property
     */
    public Property getProperty(final String name)
    {
        final Collection<Property> properties = this.getProperties(name);
        return properties == null || properties.isEmpty() ?
            null : properties.iterator().next();
    }

    /**
     * Gets all namespaces belonging to this namespaces instance.
     *
     * @return all namespaces.
     */
    public Collection<Collection<Property>> getProperties()
    {
        return this.properties.values();
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return super.toString() + "[" + this.name + "]";
    }
}