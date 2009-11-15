package org.andromda.core.namespace;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Represents a namespace registry.  This is where
 * all components within a namespace are registered.
 *
 * @author Chad Brandon
 * @author Bob Fields
 */
public class NamespaceRegistry
{
    /**
     * The name of the namespace registry
     */
    private String name;

    /**
     * Gets the name of the namespace registry.
     *
     * @return Returns the name.
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * SEts the name of the namespace registry.
     *
     * @param name The name to set.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Whether or not this is a shared namespace.
     */
    private boolean shared = false;

    /**
     * Gets whether or not the namespace defined by this registry
     * is shared. By default namespaces are <strong>NOT </strong> shared.
     *
     * @return Returns the shared.
     */
    public boolean isShared()
    {
        return shared;
    }

    /**
     * Sets whether or not the namespace defined by this registry is shared.
     *
     * @param shared The shared to set.
     */
    public void setShared(final boolean shared)
    {
        this.shared = shared;
    }

    /**
     * Stores the names of the components registered
     * within this namespace registry and the paths from which
     * they can be initialized.
     */
    private final Map<String, String[]> components = new LinkedHashMap<String, String[]>();

    /**
     * Registers the component with the
     * give name in this registry.
     *
     * @param component the component of the registry.
     */
    public void registerComponent(final Component component)
    {
        if (component != null)
        {
            this.components.put(
                component.getName(),
                component.getPaths());
        }
    }

    /**
     * Gets the names registered components.
     *
     * @return the names of the registered components.
     */
    public String[] getRegisteredComponents()
    {
        return this.components.keySet().toArray(new String[this.components.size()]);
    }

    /**
     * Gets the initialization paths for the given component name.
     *
     * @param name the name of the component.
     * @return the paths or null if none are found.
     */
    public String[] getPaths(final String name)
    {
        return this.components.get(name);
    }

    /**
     * Stores the property definitions.
     */
    private final Map<String, PropertyDefinition> definitions = new LinkedHashMap<String, PropertyDefinition>();

    /**
     * Attempts to retrieve the property definition for the given
     * <code>name</code>.
     * @param name 
     * @return the property definition or null if one could not be found.
     */
    public PropertyDefinition getPropertyDefinition(final String name)
    {
        return this.definitions.get(name);
    }

    /**
     * Adds all property definitions to the current property definitions.
     *
     * @param propertyDefinitions the collection of property definitions.
     */
    public void addPropertyDefinitions(final PropertyDefinition[] propertyDefinitions)
    {
        for (PropertyDefinition propertyDefinition : propertyDefinitions)
        {
            this.addPropertyDefinition(propertyDefinition);
        }
    }
    
    /**
     * Copies all contents from the <code>registry</code>
     * to this instance.
     * 
     * @param registry the registry to copy.
     */
    final void copy(final NamespaceRegistry registry)
    {
        if (registry != null)
        {
            this.addPropertyDefinitions(registry.getPropertyDefinitions());
            this.components.putAll(registry.components);
            if (registry.isShared())
            {
                this.shared = registry.isShared();
            }
            this.resourceRoots.addAll(registry.resourceRoots);
        }
    }

    /**
     * Gets all property definitions belonging to this registry.
     *
     * @return all property definitions.
     */
    public PropertyDefinition[] getPropertyDefinitions()
    {
        return this.definitions.values().toArray(new PropertyDefinition[this.definitions.size()]);
    }

    /**
     * Adds a property definition to the group of defintions.
     *
     * @param propertyDefinition the property definition.
     */
    public void addPropertyDefinition(final PropertyDefinition propertyDefinition)
    {
        if (propertyDefinition != null)
        {
            this.definitions.put(
                propertyDefinition.getName(),
                propertyDefinition);
        }
    }

    /**
     * The root of this namespace which stores all resources.
     */
    private Collection<URL> resourceRoots = new ArrayList<URL>();

    /**
     * Gets the resource root of this namespace.
     *
     * @return Returns the resource.
     */
    public URL[] getResourceRoots()
    {
        return this.resourceRoots.toArray(new URL[this.resourceRoots.size()]);
    }

    /**
     * Adds a resource root to this namespace (since a namespace can consist of multiple
     * locations)
     *
     * @param resourceRoot The resource root to set.
     */
    final void addResourceRoot(final URL resourceRoot)
    {
        this.resourceRoots.add(resourceRoot);
    }

    /**
     * @see Object#toString()
     */
    public String toString()
    {
        return super.toString() + "[" + this.getName() + "]";
    }
}