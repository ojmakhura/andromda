package org.andromda.core.configuration;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.andromda.core.common.ExceptionUtils;
import org.apache.log4j.Logger;


/**
 * Dictionary of configurable Namespace objects. Namespace objects are used for configuring Plugin instances.
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
     * This is passed as the cartridge name for the findNamespaceProperty method if we wish to use a 'default' Namespace
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
     * Gets all namespaces belonging to this namespaces instance.
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
        namespaces.put(
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
     * Finds the Namespace with the corresponding <code>namespaceName</code>.
     *
     * @param namespaceName
     * @return the found Namespace
     */
    public Namespace findNamespace(final String namespaceName)
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
        return this.findNamespace(namespaceName) != null;
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
    public Property findNamespaceProperty(
        final String namespaceName,
        final String propertyName)
    {
        return this.findNamespaceProperty(namespaceName, propertyName, true);
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
    public Property findNamespaceProperty(
        final String namespaceName,
        final String propertyName,
        final boolean showWarning)
    {
        final String methodName = "Namespaces.findNamespaceProperty";
        ExceptionUtils.checkEmpty(methodName, "namespaceName", namespaceName);
        ExceptionUtils.checkEmpty(methodName, "propertyName", propertyName);

        Property property = null;
        final Namespace namespace = (Namespace)namespaces.get(namespaceName);
        if (namespace != null)
        {
            property = namespace.getProperty(propertyName);
        }

        // since we couldn't find a Namespace for the specified cartridge,
        // try to lookup the default
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
                "--> please define a namespace with" + " at least one of these names, if you would like " +
                "to ignore this message, define the namespace with " + "ignore set to 'true'");
        }
        else if (property == null && showWarning)
        {
            logger.warn(
                "WARNING! Namespaces '" + DEFAULT + "' and '" + namespaceName + "' have no property '" + propertyName +
                "' defined --> please define this " + "property in AT LEAST ONE of these two namespaces. " +
                " If you want to 'ignore' this message, add the " +
                "property to the namespace with ignore set to 'true'");
        }
        return property;
    }
    
    /**
     * Shuts down this namespaces instance.
     */
    public void shutdown()
    {
        this.namespaces.clear();
        instance = null;
    }
}