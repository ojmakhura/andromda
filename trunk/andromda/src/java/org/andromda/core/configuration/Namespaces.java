package org.andromda.core.configuration;

import java.io.Serializable;

import java.net.URL;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.andromda.core.common.ExceptionUtils;
import org.andromda.core.namespace.NamespaceComponent;
import org.andromda.core.namespace.NamespaceRegistry;
import org.andromda.core.namespace.PropertyDefinition;
import org.apache.log4j.Logger;


/**
 * Directory of configurable Namespace objects. Namespace objects are used for configuring AndroMDA
 * namespaces.
 *
 * @author Chad Brandon
 * @see org.andromda.core.configuration.Namespace
 */
public class Namespaces
    implements Serializable
{
    /**
     * The logger instance.
     */
    private static final Logger logger = Logger.getLogger(Namespaces.class);

    /**
     * This is passed as the cartridge name for the {@link #getProperty} method if we wish to use a 'default' Namespace
     * for Plugins. This is so we don't need to define a specific mapping for each Plugin if we don't want. If a
     * namespaceName exists with a specific Plugin name, then that will be used instead of the 'default'
     */
    public static final String DEFAULT = "default";

    /**
     * Stores all namespaces.
     */
    private final Map namespaces = new LinkedHashMap();

    /**
     * The shared instance.
     */
    private static Namespaces instance = null;

    /**
     * Returns the singleton instance of this Namespaces
     *
     * @return instance.
     */
    public static final Namespaces instance()
    {
        if (instance == null)
        {
            instance = new Namespaces();
        }
        return instance;
    }

    /**
     * Gets the namespaces registered in this namespaces instance.
     *
     * @return all namespaces.
     */
    public Collection getNamespaces()
    {
        return this.namespaces.values();
    }

    /**
     * Adds a namespace to this collection of namespaces.
     *
     * @param namespace the Namespace to add to this instance.
     */
    public void addNamespace(final Namespace namespace)
    {
        this.namespaces.put(
            namespace.getName(),
            namespace);
    }

    /**
     * Adds all <code>namespaces</code> to this instance.
     *
     * @param namespaces the array of namespaces to add.
     */
    public void addNamespaces(final Namespace[] namespaces)
    {
        if (namespaces != null && namespaces.length > 0)
        {
            final int namespaceNumber = namespaces.length;
            for (int ctr = 0; ctr < namespaceNumber; ctr++)
            {
                this.addNamespace(namespaces[ctr]);
            }
        }
    }

    /**
     * Gets the Namespace with the corresponding <code>namespaceName</code>.
     *
     * @param namespaceName
     * @return the found Namespace
     */
    public Namespace getNamespace(final String namespaceName)
    {
        return (Namespace)namespaces.get(namespaceName);
    }

    /**
     * Indicates if the namespace is present within this instance.
     *
     * @param namespaceName the name of the namespace.
     * @return true/false
     */
    public boolean namespacePresent(final String namespaceName)
    {
        return this.getNamespace(namespaceName) != null;
    }

    /**
     * Retrieves a property from the Namespace with the namespaceName. If the <code>ignore</code> attribute of the
     * Property instance is set to <code>true</code> then lookup of the property will not be attempted and null will
     * just be returned instead. If the propety is not found and <code>ignore<code> is not <code>true</code> a warning
     * message is logged.
     *
     * @param namespaceName name of the Plugin to which the namespace applies
     * @param propertyName  name of the namespace property to find.
     * @return String the namespace property value.
     */
    public Property getProperty(
        final String namespaceName,
        final String propertyName)
    {
        return this.getProperty(
            namespaceName,
            propertyName,
            true);
    }

    /**
     * Retrieves a property from the Namespace with the namespaceName. If the <code>ignore</code> attribute of the
     * Property instance is set to <code>true</code> then lookup of the property will not be attempted and null will
     * just be returned instead.
     *
     * @param namespaceName name of the Plugin to which the namespace applies
     * @param propertyName  name of the namespace property to find.
     * @param showWarning   true/false if we'd like to display a warning if the property/namespace can not be found.
     * @return String the namespace property value.
     */
    public Property getProperty(
        final String namespaceName,
        final String propertyName,
        final boolean showWarning)
    {
        ExceptionUtils.checkEmpty(
            "namespaceName",
            namespaceName);
        ExceptionUtils.checkEmpty(
            "propertyName",
            propertyName);

        Property property = null;
        final Namespace namespace = (Namespace)namespaces.get(namespaceName);
        if (namespace != null)
        {
            property = namespace.getProperty(propertyName);
        }

        // - since we couldn't find a Namespace for the specified cartridge,
        //   try to lookup the default
        Namespace defaultNamespace = null;
        if (property == null)
        {
            if (logger.isDebugEnabled())
            {
                logger.debug("no namespace with name '" + namespaceName + "' found, looking for '" + DEFAULT + "'");
            }
            defaultNamespace = (Namespace)namespaces.get(DEFAULT);
            if (defaultNamespace != null)
            {
                property = defaultNamespace.getProperty(propertyName);
            }
        }

        if (namespace == null && defaultNamespace == null && showWarning)
        {
            logger.warn(
                "WARNING! No '" + DEFAULT + "' or '" + namespaceName + "' namespace found, " +
                "--> please define a namespace with at least one of these names, if you would like " +
                "to ignore this message, define the namespace with " + "ignore set to 'true'");
        }
        else if (property == null && showWarning)
        {
            logger.warn(
                "WARNING! Namespaces '" + DEFAULT + "' and '" + namespaceName + "' have no property '" + propertyName +
                "' defined --> please define this property in AT LEAST ONE of these two namespaces. " +
                " If you want to 'ignore' this message, add the property to the namespace with ignore set to 'true'");
        }
        return property;
    }

    /**
     * Stores the namespace registries
     */
    private final Map registries = new LinkedHashMap();

    /**
     * Gets all available namespace registries (these are namespaces
     * which have been discovered but are not necessarily configured).
     *
     * @return the collection of namespace registries
     */
    public Collection getNamespaceRegistries()
    {
        return this.registries.values();
    }

    /**
     * Adds a namespace registry to this instance.  Namespace registries contain
     * property definitions that are defined within a {@link NamespaceRegistry}
     * descriptor (used to describe {@link NamespaceComponent}) instances.
     *
     * @param registry the {@link NamespaceRegistry} instance to add.
     */
    public void addRegistry(final NamespaceRegistry registry)
    {
        if (registry != null)
        {
            // - first add the registry directly under its own name
            this.registries.put(
                registry.getName(),
                registry);

            // - if the registry is shared, we add the registry to the default namespace as well
            if (registry.isShared())
            {
                NamespaceRegistry defaultRegistry = this.getNamespaceRegistry(DEFAULT);
                if (defaultRegistry == null)
                {
                    defaultRegistry = registry;
                }
                else
                {
                    defaultRegistry.addPropertyDefinitions(registry.getPropertyDefinitions());
                }
                this.registries.put(
                    DEFAULT,
                    defaultRegistry);
            }
        }
    }

    /**
     * Indicates if the given <code>namespace</code> is
     * shared or not.
     *
     * @param namespace the namespace to check.
     * @return true/false.
     */
    public boolean isShared(final String namespace)
    {
        return Namespaces.DEFAULT.equals(namespace);
    }

    /**
     * Attempts to get the value of a property from the given
     * <code>namespace</code> with the given <code>name</code> by first attempting
     * to retrieve it from the namespace and if no property is defined
     * in the namespace we retrieve the default value (if one is defined).
     *
     * @param namespace the namespace for which to retreive the value.
     * @param name the name of the value to retrieve.
     * @return the value (or null if one couldn't be retrieved).
     */
    public String getPropertyValue(
        final String namespace,
        final String name)
    {
        final PropertyDefinition definition = this.getPropertyDefinition(
                namespace,
                name);
        if (definition == null)
        {
            throw new NamespacesException("Property '" + name + "' is not registered in either the '" + namespace +
                "' or '" + Namespaces.DEFAULT + "' namespaces");
        }
        final String defaultValue = definition != null ? definition.getDefaultValue() : null;
        boolean warning = defaultValue == null && definition != null ? definition.isRequired() : false;
        final Property property = this.getProperty(
                namespace,
                name,
                warning);
        return property != null && !property.isIgnore() ? property.getValue() : defaultValue;
    }

    /**
     * Attempts to retrieve the resource root of the namespace. The resource root is the directory
     * or archive root which contains all namespace resources.
     *
     * @param namespace the namespace of which to retrieve the resource.
     * @return the resource or null if it could not be found.
     */
    public URL getResourceRoot(final String namespace)
    {
        final NamespaceRegistry registry = this.getNamespaceRegistry(namespace);
        if (registry == null)
        {
            throw new NamespacesException("'" + namespace + "' is not a registered namespace");
        }

        final URL resourceRoot = registry.getResourceRoot();
        if (resourceRoot == null)
        {
            throw new NamespacesException("No resource root could be retrieved for namespace '" + namespace + "'");
        }
        return resourceRoot;
    }

    /**
     * Indicates whether or not the <code>component</code> is present within the given
     * <code>namespace</code>
     * @param namespace the name of the namespace.
     * @param component the name of the component type.
     * @return true/false
     */
    public boolean isComponentPresent(
        final String namespace,
        final String component)
    {
        boolean present = false;
        final NamespaceRegistry registry = this.getNamespaceRegistry(namespace);
        if (namespace != null && component != null && registry != null)
        {
            final String[] components = registry.getRegisteredComponents();
            final int numberOfComponents = components.length;
            for (int ctr = 0; ctr < numberOfComponents; ctr++)
            {
                if (component.equals(components[ctr]))
                {
                    present = true;
                    break;
                }
            }
        }
        return present;
    }

    /**
     * Attempts to get the value of a property from the given
     * <code>namespace</code> with the given <code>name</code> by first attempting
     * to retreive it from the namespace and if no property is defined
     * in the namespace we retrieve the default value (if one is defined).
     *
     * @param namespace the namespace for which to retreive the value.
     * @param name the name of the value to retrieve.
     * @return the value (or null if one couldn't be retrieved).
     */
    private final PropertyDefinition getPropertyDefinition(
        final String namespace,
        final String name)
    {
        final NamespaceRegistry registry = this.getNamespaceRegistry(namespace);
        PropertyDefinition definition = null;
        if (registry != null)
        {
            definition = registry.getPropertyDefinition(name);
        }
        if (definition == null)
        {
            final NamespaceRegistry defaultRegistry = this.getNamespaceRegistry(Namespaces.DEFAULT);
            if (defaultRegistry != null)
            {
                definition = defaultRegistry.getPropertyDefinition(name);
            }
        }
        return definition;
    }

    /**
     * Retrieves the namespace registry for the given namespace, or returns null
     * if it doesn't exist.
     *
     * @param namespace the namespace name.
     * @return the registry, or null if not found.
     */
    private NamespaceRegistry getNamespaceRegistry(final String namespace)
    {
        return (NamespaceRegistry)this.registries.get(namespace);
    }

    /**
     * Clears out the current namespaces.
     */
    public void clear()
    {
        this.namespaces.clear();
    }
}