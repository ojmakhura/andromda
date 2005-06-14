package org.andromda.core.namespace;

import org.andromda.core.common.ClassUtils;


/**
 * Stores information about a namespace component.
 *
 * @author Chad Brandon
 */
public class Component
{
    /**
     * The name of the component
     */
    private String name;

    /**
     * Gets the name of the component.
     *
     * @return the component name.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Sets the name of the component.
     *
     * @param name the component's name.
     */
    public void setName(final String name)
    {
        this.name = name;
    }

    /**
     * The path to the compoment's descriptor.
     */
    private String path;

    /**
     * Gets the path to the component's descriptor.
     *
     * @return the path to the component's descriptor.
     */
    public String getPath()
    {
        return path;
    }

    /**
     * Sets the path to the component's descriptor.
     *
     * @param path that path to the component's descriptor.
     */
    public void setPath(final String path)
    {
        this.path = path;
    }

    /**
     * Stores the interface name that defines this component.
     */
    private Class type;

    /**
     * Sets the type class  that defines this component.
     *
     * @param interfaceName the name of the interface.
     */
    public void setTypeClass(final String typeClass)
    {
        final Class type = ClassUtils.loadClass(typeClass);
        if (!NamespaceComponent.class.isAssignableFrom(type))
        {
            throw new ComponentRegistryException(
                "namespace component '" + type + "' must implement --> '" + NamespaceComponent.class.getName() + "'");
        }
        this.type = type;
    }

    /**
     * Gets the class that defines this component.
     *
     * @return the  class that defines this component.
     */
    public Class getType()
    {
        return this.type;
    }
}