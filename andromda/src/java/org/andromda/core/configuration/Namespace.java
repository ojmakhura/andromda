package org.andromda.core.configuration;

import org.andromda.core.common.ExceptionUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * A configurable namespace object. These are passed to Plugin instances (Cartridges, etc.).
 *
 * @author Chad Brandon
 */
public class Namespace
{
    private Map properties;
    private final Collection initCollection = new ArrayList();

    /**
     * This method normally would be unnecessary. It is here because of the way Ant behaves. Ant calls addProperty()
     * before the PropertyReference javabean is fully initialized (therefore the 'name' isn't set). So we kept the
     * javabeans in an ArrayList that we have to copy into the properties Map.
     */
    public void init()
    {
        if (this.properties == null)
        {
            this.properties = new HashMap();
            for (Iterator iter = initCollection.iterator(); iter.hasNext();)
            {
                Property property = (Property)iter.next();
                this.properties.put(property.getName(), property);
            }
        }
    }
    
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
        this.init();
        return name;
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
     * Adds a property to this Namespace object. A property must correspond to a java bean property name on a Plugin in
     * order for it to be set during processing. Otherwise the property will just be ignored.
     *
     * @param property the property to add to this namespace.
     */
    public void addProperty(final Property property)
    {
        final String methodName = "Namespace.addProperty";
        ExceptionUtils.checkNull(methodName, "property", property);
        this.initCollection.add(property);
    }

    /**
     * Retrieves the property with the specified name.
     *
     * @param name
     * @return PropertyReference.
     */
    public Property getProperty(final String name)
    {
        this.init();
        return (Property)this.properties.get(name);
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }
}