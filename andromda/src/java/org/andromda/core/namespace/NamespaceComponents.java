package org.andromda.core.namespace;

import java.io.InputStream;

import java.net.URL;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.andromda.core.common.AndroMDALogger;
import org.andromda.core.common.ComponentContainer;
import org.andromda.core.common.Merger;
import org.andromda.core.common.ResourceFinder;
import org.andromda.core.common.ResourceUtils;
import org.andromda.core.common.XmlObjectFactory;
import org.andromda.core.configuration.Namespaces;
import org.apache.commons.lang.StringUtils;


/**
 * The registry for namespace components.
 * Namespace components are components that reside
 * within a namespace and can be configured by a namespace.
 *
 * @author Chad Brandon
 */
public class NamespaceComponents
{
    /**
     * The shared registry instance.
     */
    private static NamespaceComponents instance;

    /**
     * Gets the shared instance of this registry.
     *
     * @return the shared registry instance.
     */
    public static final NamespaceComponents instance()
    {
        if (instance == null)
        {
            final XmlObjectFactory factory = XmlObjectFactory.getInstance(NamespaceComponents.class);
            instance = (NamespaceComponents)factory.getObject(ResourceUtils.getResource(CONFIGURATION_URI));
        }
        return instance;
    }

    /**
     * The URI to the descriptor for this instance.
     */
    private static final String CONFIGURATION_URI = "META-INF/andromda/namespace-components.xml";

    /**
     * This class should not be instantiated
     * through this constructor, it is only
     * here to allow construction by the {@link XmlObjectFactory}.
     * The instance of this class should be retrieved through
     * the call to {@link #instance()).
     */
    public NamespaceComponents()
    {
    }

    /**
     * Discovers all namespaces found on the classpath.
     */
    public void discover()
    {
        AndroMDALogger.info("- discovering namespaces -");
        final URL[] resources = ResourceFinder.findResources(this.getPath());
        final XmlObjectFactory registryFactory = XmlObjectFactory.getInstance(NamespaceRegistry.class);
        if (resources != null && resources.length > 0)
        {
            final int resourceNumber = resources.length;
            final ComponentContainer container = ComponentContainer.instance();
            for (int ctr = 0; ctr < resourceNumber; ctr++)
            {
                final URL resource = resources[ctr];
                NamespaceRegistry registry = (NamespaceRegistry)registryFactory.getObject(resource);
                final String registryName = registry.getName();

                // - only register if we haven't yet registered the namespace
                if (!this.registeredNamespaces.contains(registryName))
                {
                    AndroMDALogger.info("found namespace --> '" + registryName + "'");
                    final String namespace = registry.isShared() ? Namespaces.DEFAULT : registry.getName();

                    // - now perform a merge of the descriptor (if we require one)
                    final Merger merger = Merger.instance();

                    // - perform a merge on the namespace registry descriptor (if needed)
                    if (merger.requiresMerge(namespace))
                    {
                        registry =
                            (NamespaceRegistry)registryFactory.getObject(
                                merger.getMergedString(
                                    ResourceUtils.getContents(resource),
                                    namespace));
                    }

                    // - add the registry to the namespaces instance
                    Namespaces.instance().addRegistry(registry);
                    final String[] components = registry.getRegisteredComponents();
                    final int componentNumber = components.length;
                    for (int componentCtr = 0; componentCtr < componentNumber; componentCtr++)
                    {
                        final String componentName = components[componentCtr];
                        final Component component = this.getComponent(componentName);
                        if (!container.isRegisteredByNamespace(
                                namespace,
                                component.getType()))
                        {
                            AndroMDALogger.info("  +  registering component '" + componentName + "'");
                            if (component == null)
                            {
                                throw new NamespaceComponentsException(
                                    "'" + componentName + "' is not a valid namespace component");
                            }
                            final XmlObjectFactory componentFactory = XmlObjectFactory.getInstance(component.getType());
                            final URL componentResource = this.getRelativeResource(
                                    resource,
                                    component.getPath());
                            if (componentResource == null)
                            {
                                throw new NamespaceComponentsException(
                                    "'" + componentName + "' is not a valid component with namespace '" + namespace +
                                    "'");
                            }
                            NamespaceComponent namespaceComponent =
                                (NamespaceComponent)componentFactory.getObject(componentResource);

                            // - now perform a merge of the descriptor (if we require one)
                            if (merger.requiresMerge(namespace))
                            {
                                namespaceComponent =
                                    (NamespaceComponent)componentFactory.getObject(
                                        merger.getMergedString(
                                            ResourceUtils.getContents(componentResource),
                                            namespace));
                            }

                            namespaceComponent.setNamespace(namespace);
                            namespaceComponent.setResource(componentResource);
                            container.registerComponentByNamespace(
                                registry.getName(),
                                component.getType(),
                                namespaceComponent);
                        }
                    }
                }
                this.registeredNamespaces.add(registryName);
            }
        }
    }

    /**
     * Keeps track of the namespaces that have been already registered.
     */
    private Collection registeredNamespaces = new ArrayList();

    /**
     * Attempts to retrieve a resource relative to the given <code>resource</code>
     * by computing the complete path from the given relative <code>path</code>.
     *
     * @param resource the resource from which to search.
     * @param path the relative path.
     * @return the resource found or null if invalid.
     */
    private final URL getRelativeResource(
        final URL resource,
        final String path)
    {
        URL relativeResource = null;
        InputStream stream = null;
        try
        {
            relativeResource = new URL(StringUtils.replace(
                        resource.toString(),
                        this.getPath(),
                        path));
            stream = relativeResource.openStream();
            stream.close();
        }
        catch (final Throwable throwable)
        {
            relativeResource = null;
        }
        finally
        {
            stream = null;
        }
        return relativeResource;
    }

    /**
     * The path to search for the namespace descriptor.
     */
    private String path;

    /**
     * Gets the path to the namespace registry descriptor.
     *
     * @return The path to a namespace registry descriptor.
     */
    public String getPath()
    {
        return this.path;
    }

    /**
     * Sets the path to the namespace registry descriptor.
     *
     * @param path The path to a namespace registry descriptor.
     */
    public void setPath(String path)
    {
        this.path = path;
    }

    /**
     * Stores the actual component definitions for this
     * namespace registry.
     */
    private final Map components = new HashMap();

    /**
     * Adds a new component to this namespace registry.
     *
     * @param component the component to add to this namespace registry.
     */
    public void addComponent(final Component component)
    {
        if (component != null)
        {
            this.components.put(
                component.getName(),
                component);
        }
    }

    /**
     * Shuts down this component registry and reclaims
     * any resources used.
     */
    public void shutdown()
    {
        this.components.clear();
        instance = null;
    }

    /**
     * Retrieves a component by name (or returns null if one
     * can not be found).
     *
     * @param name the name of the component to retrieve.
     * @return the component instance or null.
     */
    private final Component getComponent(final String name)
    {
        return (Component)this.components.get(name);
    }
}