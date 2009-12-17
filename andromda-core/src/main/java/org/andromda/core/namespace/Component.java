package org.andromda.core.namespace;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;

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
    private final Collection<String> paths = new LinkedHashSet<String>();

    /**
     * Gets the component's descriptor paths (these are the paths
     * where the component's descriptor may be found).
     *
     * @return the path to the component's descriptor.
     */
    public String[] getPaths()
    {
        return paths.toArray(new String[paths.size()]);
    }

    /**
     * Adds a path to the component's descriptor.
     *
     * @param path that path to the component's descriptor.
     */
    public void addPath(final String path)
    {
        this.paths.add(path);
    }

    /**
     * Adds the given <code>paths</code> to the existing paths
     * contained within this component.
     *
     * @param paths the paths to add.
     */
    final void addPaths(final String[] paths)
    {
        if (paths != null && paths.length > 0)
        {
            this.paths.addAll(Arrays.asList(paths));
        }
    }

    /**
     * Stores the interface name that defines this component.
     */
    private Class type;

    /**
     * Sets the type class  that defines this component.
     *
     * @param typeClass the name of the type.
     */
    public void setTypeClass(final String typeClass)
    {
        final Class type = ClassUtils.loadClass(typeClass);
        if (!NamespaceComponent.class.isAssignableFrom(type))
        {
            throw new NamespaceComponentsException(
                "namespace component '" + type + "' must implement --> '" + NamespaceComponent.class.getName() + '\'');
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