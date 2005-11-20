package org.andromda.core.metafacade;

import java.net.URL;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.andromda.core.common.ClassUtils;
import org.andromda.core.common.Constants;
import org.andromda.core.common.ExceptionUtils;
import org.andromda.core.common.ResourceUtils;
import org.andromda.core.configuration.Namespaces;
import org.andromda.core.namespace.NamespaceRegistry;
import org.apache.commons.lang.StringUtils;


/**
 * Discovers all metafacade interfaces and implementation classes in each namespace registry. This class is
 * then used to retrieve both the appropriate metafacade interface and/or metafacade implementation class based
 * on one or the other.
 *
 * @author Chad Brandon
 */
public class MetafacadeImpls
{
    /**
     * The shared instance.
     */
    private static final MetafacadeImpls instance = new MetafacadeImpls();

    /**
     * Stores each metafacade classes instance keyed by namespace.
     */
    private final Map metafacadeClasses = new LinkedHashMap();

    /**
     * Returns the shared instance of this class.
     *
     * @return MetafacadeImpls the shared instance.
     */
    public static MetafacadeImpls instance()
    {
        return instance;
    }

    /**
     * The current model type to which metafacade class retrieval applies.
     */
    private String modelType;

    /**
     * Sets the current model type to which this instance's metafacade class retrieval
     * should apply.
     *
     * @param modelType the namespace storing the available model access facade.
     */
    public void setModelType(final String modelType)
    {
        this.modelType = modelType;
    }

    /**
     * The extension for the metafacade implementation files.
     */
    private final static String METAFACADE_IMPLEMENTATION_SUFFIX =
        MetafacadeConstants.METAFACADE_IMPLEMENTATION_SUFFIX + ClassUtils.CLASS_EXTENSION;

    /**
     * Discovers and loads all metafacade implementation classes and interfaces in each avaiable namespace registry into
     * each given namespace in the <code>modelTypeNamespaces</code> list.
     * Note that this method must be called before any metafacade implementation classes will be able to be retrieved
     * when calling {@link #getMetafacadeClass(String)}or {@link #getMetafacadeImplClass(String)}.
     *
     * @param modelTypes a list of each namespace used as a model type in the current andromda configuration.
     */
    public void discover(final String[] modelTypes)
    {
        ExceptionUtils.checkNull(
            "modelTypes",
            modelTypes);
        final int numberOfNamespaces = modelTypes.length;
        for (int ctr = 0; ctr < numberOfNamespaces; ctr++)
        {
            final String namespace = modelTypes[ctr];
            if (namespace != null)
            {
                MetafacadeClasses metafacadeClasses = (MetafacadeClasses)this.metafacadeClasses.get(namespace);
                if (metafacadeClasses == null)
                {
                    metafacadeClasses = new MetafacadeClasses();
                    this.metafacadeClasses.put(
                        namespace,
                        metafacadeClasses);
                }
                metafacadeClasses.clear();
                try
                {
                    NamespaceRegistry storageRegistry = null;
                    final Namespaces namespacesConfiguration = Namespaces.instance();
                    for (final Iterator iterator = namespacesConfiguration.getNamespaceRegistries().iterator();
                        iterator.hasNext();)
                    {
                        final NamespaceRegistry namespaceRegistry = (NamespaceRegistry)iterator.next();
                        final String namespaceRegistryName = namespaceRegistry.getName();

                        // - if the namespace equals the registry name, then we set the storageRegistry
                        //   since we want that added last.
                        if (namespace.equals(namespaceRegistryName))
                        {
                            storageRegistry = namespaceRegistry;
                        }
                        else
                        {
                            this.registerMetafacadeClasses(
                                metafacadeClasses,
                                namespacesConfiguration,
                                namespaceRegistry);
                        }
                    }

                    // - we make sure the storageRegistry classes are registered last so that they override any
                    //   clases that were previously stored.
                    if (storageRegistry != null)
                    {
                        this.registerMetafacadeClasses(
                            metafacadeClasses,
                            namespacesConfiguration,
                            storageRegistry);
                    }
                }
                catch (final Throwable throwable)
                {
                    throw new MetafacadeImplsException(throwable);
                }
            }
        }
    }

    /**
     * Registers the metafacade classes for the given <code>namespaceRegistry</code>.
     *
     * @param metafacadeClasses the metafacade classes instance to store the registered metafacade classes.
     * @param namespaces the namespaces from which we retrieve the additional namespace information.
     * @param namespaceRegistry the registry from which we retrieve the classes.
     */
    private void registerMetafacadeClasses(
        final MetafacadeClasses metafacadeClasses,
        final Namespaces namespaces,
        final NamespaceRegistry namespaceRegistry)
    {
        final String namespaceRegistryName = namespaceRegistry.getName();
        if (namespaces.isComponentPresent(
                namespaceRegistryName,
                Constants.COMPONENT_METAFACADES))
        {
            final URL[] namespaceRoots = namespaceRegistry.getResourceRoots();
            if (namespaceRoots != null && namespaceRoots.length > 0)
            {
                final int numberOfNamespaceRoots = namespaceRoots.length;
                for (int ctr = 0; ctr < numberOfNamespaceRoots; ctr++)
                {
                    final URL namespaceRoot = namespaceRoots[ctr];
                    final Collection contents = ResourceUtils.getDirectoryContents(
                            namespaceRoot,
                            false,
                            null);
                    for (final Iterator contentsIterator = contents.iterator(); contentsIterator.hasNext();)
                    {
                        final String path = ((String)contentsIterator.next());
                        if (path.endsWith(METAFACADE_IMPLEMENTATION_SUFFIX))
                        {
                            final String typeName =
                                StringUtils.replace(
                                    path.replaceAll(
                                        "\\\\+|/+",
                                        "."),
                                    ClassUtils.CLASS_EXTENSION,
                                    "");
                            Class implementationClass = null;
                            try
                            {
                                implementationClass = ClassUtils.loadClass(typeName);
                            }
                            catch (final Exception exception)
                            {
                                // - ignore
                            }
                            if (implementationClass != null && MetafacadeBase.class.isAssignableFrom(implementationClass))
                            {
                                final List allInterfaces = ClassUtils.getInterfaces(implementationClass);
                                if (!allInterfaces.isEmpty())
                                {
                                    final Class interfaceClass = (Class)allInterfaces.iterator().next();
                                    final String implementationClassName = implementationClass.getName();
                                    final String interfaceClassName = interfaceClass.getName();
                                    metafacadeClasses.metafacadesByImpls.put(
                                        implementationClassName,
                                        interfaceClassName);
                                    metafacadeClasses.implsByMetafacades.put(
                                        interfaceClassName,
                                        implementationClassName);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Attempts to retrieve the metafacade classes instance with the current active namespace
     * and throws an exception if one can not be found.
     *
     * @return the metafacade classes instance.
     */
    private MetafacadeClasses getMetafacadeClasses()
    {
        final MetafacadeClasses classes = (MetafacadeClasses)this.metafacadeClasses.get(this.modelType);
        if (classes == null)
        {
            throw new MetafacadeImplsException("Namespace '" + this.modelType + "' is not a registered model type");
        }
        return classes;
    }

    /**
     * Retrieves the metafacade class from the passed in <code>metafacadeImplClass</code>. Will return a
     * MetafacadeImplsException if a metafacade class can not be found for the <code>metafacadeImplClass</code>
     *
     * @param metafacadeImplClass the name of the metafacade implementation class.
     * @return the metafacacade Class
     */
    public Class getMetafacadeClass(final String metafacadeImplClass)
    {
        ExceptionUtils.checkEmpty(
            "metafacadeImplClass",
            metafacadeImplClass);
        return this.getMetafacadeClasses().getMetafacadeClass(metafacadeImplClass);
    }

    /**
     * Retrieves the metafacade implementation class from the passed in <code>metafacadeClass</code>. Will return a
     * MetafacadeImplsException if a metafacade implementation class can not be found for the
     * <code>metafacadeClass</code>
     *
     * @param metafacadeClass the name of the metafacade class.
     * @return the metafacacade implementation Class
     */
    public Class getMetafacadeImplClass(final String metafacadeClass)
    {
        ExceptionUtils.checkEmpty(
            "metafacadeClass",
            metafacadeClass);
        return this.getMetafacadeClasses().getMetafacadeImplClass(metafacadeClass);
    }

    /**
     * Stores the metafacade interface and implementation classes.
     */
    private static final class MetafacadeClasses
    {
        /**
         * Stores all <code>metafacade</code> implementation classes keyed by <code>metafacade</code> interface class.
         */
        Map implsByMetafacades = new LinkedHashMap();

        /**
         * Stores all <code>metafacade</code> interface classes keyed by <code>metafacade</code> implementation class.
         */
        Map metafacadesByImpls = new LinkedHashMap();

        /**
         * Retrieves the metafacade class from the passed in <code>metafacadeImplClass</code>. Will return a
         * MetafacadeImplsException if a metafacade class can not be found for the <code>metafacadeImplClass</code>
         *
         * @param metafacadeImplClass the name of the metafacade implementation class.
         * @return the metafacacade Class
         */
        Class getMetafacadeClass(final String metafacadeImplClass)
        {
            ExceptionUtils.checkEmpty(
                "metafacadeImplClass",
                metafacadeImplClass);
            Class metafacadeClass = null;
            try
            {
                final String metafacadeClassName = (String)this.metafacadesByImpls.get(metafacadeImplClass);
                if (StringUtils.isEmpty(metafacadeClassName))
                {
                    throw new MetafacadeImplsException("Can not find a metafacade interface for --> '" +
                        metafacadeImplClass + "', check your classpath");
                }
                metafacadeClass = ClassUtils.loadClass(metafacadeClassName);
            }
            catch (final Throwable throwable)
            {
                throw new MetafacadeImplsException(throwable);
            }
            return metafacadeClass;
        }

        /**
         * Retrieves the metafacade implementation class from the passed in <code>metafacadeClass</code>. Will return a
         * MetafacadeImplsException if a metafacade implementation class can not be found for the
         * <code>metafacadeClass</code>
         *
         * @param metafacadeClass the name of the metafacade class.
         * @return the metafacacade implementation Class
         */
        Class getMetafacadeImplClass(final String metafacadeClass)
        {
            ExceptionUtils.checkEmpty(
                "metafacadeClass",
                metafacadeClass);
            Class metafacadeImplementationClass = null;
            try
            {
                final String metafacadeImplementationClassName = (String)this.implsByMetafacades.get(metafacadeClass);
                if (StringUtils.isEmpty(metafacadeImplementationClassName))
                {
                    throw new MetafacadeImplsException("Can not find a metafacade implementation class for --> '" +
                        metafacadeClass + "' check your classpath");
                }
                metafacadeImplementationClass = ClassUtils.loadClass(metafacadeImplementationClassName);
            }
            catch (final Throwable throwable)
            {
                throw new MetafacadeImplsException(throwable);
            }
            return metafacadeImplementationClass;
        }

        /**
         * Clears each map of any classes it contains.
         */
        void clear()
        {
            this.metafacadesByImpls.clear();
            this.implsByMetafacades.clear();
        }
    }
}