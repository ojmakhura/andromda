package org.andromda.core.namespace;

import java.io.InputStream;
import java.net.URL;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.andromda.core.common.AndroMDALogger;
import org.andromda.core.common.ComponentContainer;
import org.andromda.core.common.Merger;
import org.andromda.core.common.ResourceFinder;
import org.andromda.core.common.ResourceUtils;
import org.andromda.core.common.XmlObjectFactory;
import org.andromda.core.configuration.Namespaces;
import org.andromda.core.profile.Profile;
import org.apache.commons.lang.StringUtils;

/**
 * The registry for namespace components. Namespace components are components
 * that reside within a namespace and can be configured by a namespace.
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
     * This class should not be instantiated through this constructor, it is
     * only here to allow construction by the {@link XmlObjectFactory}. The
     * instance of this class should be retrieved through the call to
     * {@link #instance()}.
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

        final XmlObjectFactory registryFactory = XmlObjectFactory.getInstance(NamespaceRegistry.class);
        final ComponentContainer container = ComponentContainer.instance();

        // - discover all registries and sort them by name
        final Map<NamespaceRegistry, URL> registryMap = this.discoverAllRegistries();
        final List<NamespaceRegistry> registries = new ArrayList<NamespaceRegistry>(registryMap.keySet());
        Collections.sort(
            registries,
            new NamespaceRegistryComparator());
        for (NamespaceRegistry registry : registries)
        {
            final URL resource = registryMap.get(registry);
            final String registryName = registry.getName();

            // - only register if we haven't yet registered the namespace resource
            if (!this.registeredNamespaceResources.contains(resource))
            {
                final Namespaces namespaces = Namespaces.instance();
                final String namespace = registry.isShared() ? Namespaces.DEFAULT : registry.getName();

                // - first merge on the namespace registry descriptor (if needed)
                final Merger merger = Merger.instance();
                boolean requiresMerge = merger.requiresMerge(namespace);
                if (requiresMerge)
                {
                    registry =
                            (NamespaceRegistry) registryFactory.getObject(
                                    merger.getMergedString(
                                            ResourceUtils.getContents(resource),
                                            namespace), resource);
                }

                // - add the resource root
                registry.addResourceRoot(this.getNamespaceResourceRoot(resource));

                // - only log the fact we've found the namespace registry, if we haven't done it yet
                if (!this.registeredRegistries.contains(registryName))
                {
                    AndroMDALogger.info("found namespace --> '" + registryName + "'");
                    this.registeredRegistries.add(registryName);
                }

                final NamespaceRegistry existingRegistry = namespaces.getRegistry(registryName);
                if (existingRegistry != null)
                {
                    // - if we already have an existing registry with the same name, copy
                    //   over any resources.
                    registry.copy(existingRegistry);
                }

                // - add the registry to the namespaces instance
                namespaces.addRegistry(registry);
                final String[] components = registry.getRegisteredComponents();
                for (final String componentName : components)
                {
                    final Component component = this.getComponent(componentName);
                    if (component == null)
                    {
                        throw new NamespaceComponentsException("'" + componentName +
                                "' is not a valid namespace component");
                    }

                    // - add any paths defined within the registry
                    component.addPaths(registry.getPaths(component.getName()));
                    if (!container.isRegisteredByNamespace(
                            registryName,
                            component.getType()))
                    {
                        AndroMDALogger.info("  +  registering component '" + componentName + "'");
                        final XmlObjectFactory componentFactory = XmlObjectFactory.getInstance(component.getType());
                        final URL componentResource =
                                this.getNamespaceResource(
                                        registry.getResourceRoots(),
                                        component.getPaths());
                        if (componentResource == null)
                        {
                            throw new NamespaceComponentsException("'" + componentName +
                                    "' is not a valid component within namespace '" + namespace + "' (the " +
                                    componentName + "'s descriptor can not be found)");
                        }
                        NamespaceComponent namespaceComponent =
                                (NamespaceComponent) componentFactory.getObject(componentResource);

                        // - now perform a merge of the descriptor (if we require one)
                        if (requiresMerge)
                        {
                            namespaceComponent =
                                    (NamespaceComponent) componentFactory.getObject(
                                            merger.getMergedString(
                                                    ResourceUtils.getContents(componentResource),
                                                    namespace));
                        }

                        namespaceComponent.setNamespace(registryName);
                        namespaceComponent.setResource(componentResource);
                        container.registerComponentByNamespace(
                                registryName,
                                component.getType(),
                                namespaceComponent);
                    }
                }
            }
            this.registeredNamespaceResources.add(resource);
        }

        // - initialize the profile
        Profile.instance().initialize();
    }

    /**
     * Discovers all registries and loads them into a map with the registry as the key
     * and the resource that configured the registry as the value.
     *
     * @return the registries in a Map
     */
    private Map<NamespaceRegistry, URL> discoverAllRegistries()
    {
        final Map<NamespaceRegistry, URL> registries = new HashMap<NamespaceRegistry, URL>();
        final URL[] resources = ResourceFinder.findResources(this.getPath());
        final XmlObjectFactory registryFactory = XmlObjectFactory.getInstance(NamespaceRegistry.class);
        if (resources != null)
        {
            for (final URL resource : resources)
            {
                final NamespaceRegistry registry = (NamespaceRegistry) registryFactory.getObject(resource);
                registries.put(
                        registry,
                        resource);
            }
        }
        return registries;
    }

    /**
     * Keeps track of the namespaces resources that have been already registered.
     */
    private Collection<URL> registeredNamespaceResources = new ArrayList<URL>();

    /**
     * Keeps track of the namespace registries that have been registered.
     */
    private Collection<String> registeredRegistries = new ArrayList<String>();

    /**
     * Attempts to retrieve a resource relative to the given
     * <code>resourceRoots</code> by computing the complete path from the given
     * relative <code>path</code>. Retrieves the first valid one found.
     *
     * @param resourceRoots the resourceRoots from which to perform search.
     * @param paths the relative paths to check.
     * @return the resource found or null if invalid.
     */
    private URL getNamespaceResource(
        final URL[] resourceRoots,
        final String[] paths)
    {
        URL namespaceResource = null;
        if (resourceRoots != null)
        {
            for (final URL resource : resourceRoots)
            {
                for (final String path : paths)
                {
                    InputStream stream = null;
                    try 
                    {
                        namespaceResource = new URL(ResourceUtils.normalizePath(resource + path));
                        stream = namespaceResource.openStream();
                    }
                    catch (final Throwable throwable)
                    {
                        namespaceResource = null;
                    }
                    finally
                    {
                        if (stream != null) try {stream.close();} catch (Exception ex) {}
                        stream = null;
                    }

                    // - break if we've found one
                    if (namespaceResource != null)
                    {
                        break;
                    }
                }

                // - break if we've found one
                if (namespaceResource != null)
                {
                    break;
                }
            }
        }
        return namespaceResource;
    }

    /**
     * Attempts to retrieve the resource root of the namespace; that is the
     * directory (whether it be a regular directory or achive root) which this
     * namespace spans.
     *
     * @param resource the resource from which to retrieve the root.
     * @return the namespace root, or null if could not be found.
     */
    private URL getNamespaceResourceRoot(final URL resource)
    {
        final String resourcePath = resource != null ? resource.toString().replace(
                '\\',
                '/') : null;
        return ResourceUtils.toURL(StringUtils.replace(
                resourcePath,
                this.path,
                ""));
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
     * Stores the actual component definitions for this namespace registry.
     */
    private final Map components = new LinkedHashMap();

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
     * Shuts down this component registry and reclaims any resources used.
     */
    public void shutdown()
    {
        this.components.clear();
        this.registeredNamespaceResources.clear();
        this.registeredRegistries.clear();
        instance = null;
    }

    /**
     * Retrieves a component by name (or returns null if one can not be found).
     *
     * @param name the name of the component to retrieve.
     * @return the component instance or null.
     */
    private Component getComponent(final String name)
    {
        return (Component)this.components.get(name);
    }

    /**
     * Used to sort namespace registries by name.
     */
    private final static class NamespaceRegistryComparator
        implements Comparator<NamespaceRegistry>
    {
        private final Collator collator = Collator.getInstance();

        NamespaceRegistryComparator()
        {
            collator.setStrength(Collator.PRIMARY);
        }

        public int compare(
            final NamespaceRegistry objectA,
            final NamespaceRegistry objectB)
        {
            return collator.compare(
                objectA.getName(),
                objectB.getName());
        }
    }
}