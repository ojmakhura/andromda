package org.andromda.core.metafacade;

import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
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
 * @author Bob Fields
 */
public class MetafacadeImpls
    implements Serializable
{
    /**
     * The shared instance.
     */
    private static final MetafacadeImpls instance = new MetafacadeImpls();

    /**
     * Stores each metafacade classes instance keyed by namespace.
     */
    private final Map<String, MetafacadeClasses> metafacadeClasses = new LinkedHashMap<String, MetafacadeClasses>();

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
    private String metafacadeModelNamespace;

    /**
     * Sets the current model type to which this instance's metafacade class retrieval
     * should apply.
     *
     * @param metafacadeModelNamespace the namespace that has the metafacade model implementation.
     */
    public void setMetafacadeModelNamespace(final String metafacadeModelNamespace)
    {
        this.metafacadeModelNamespace = metafacadeModelNamespace;
    }

    /**
     * The extension for the metafacade implementation files.
     */
    private final static String METAFACADE_IMPLEMENTATION_SUFFIX =
        MetafacadeConstants.METAFACADE_IMPLEMENTATION_SUFFIX + ClassUtils.CLASS_EXTENSION;

    /**
     * Discovers and loads all metafacade implementation classes and interfaces in each available namespace registry into
     * each given namespace in the <code>modelTypeNamespaces</code> list.
     * Note that this method must be called before any metafacade implementation classes will be able to be retrieved
     * when calling {@link #getMetafacadeClass(String)}or {@link #getMetafacadeImplClass(String)}.
     *
     * @param metafacadeModelNamespaces a list of each namespace containing a metafacade model facade implementation.
     */
    public void discover(final String[] metafacadeModelNamespaces)
    {
        ExceptionUtils.checkNull(
            "modelTypes",
            metafacadeModelNamespaces);
        final List<String> modelNamespaces = new ArrayList<String>(Arrays.asList(metafacadeModelNamespaces));
        for (final String modelNamespace : metafacadeModelNamespaces)
        {
            if (modelNamespace != null) 
            {
                // - remove the current model type so that we don't keep out the namespace
                //   that stores the metafacade model
                modelNamespaces.remove(modelNamespace);

                MetafacadeClasses metafacadeClasses = this.metafacadeClasses.get(modelNamespace);
                if (metafacadeClasses == null) 
                {
                    metafacadeClasses = new MetafacadeClasses();
                    this.metafacadeClasses.put(
                            modelNamespace,
                            metafacadeClasses);
                }
                metafacadeClasses.clear();
                try 
                {
                    final Namespaces namespacesConfiguration = Namespaces.instance();
                    for (final NamespaceRegistry namespaceRegistry : namespacesConfiguration.getNamespaceRegistries())
                    {
                        final String namespaceRegistryName = namespaceRegistry.getName();
                        if (!modelNamespaces.contains(namespaceRegistryName))
                        {
                            this.registerMetafacadeClasses(
                                    metafacadeClasses,
                                    namespacesConfiguration,
                                    namespaceRegistry);
                        }
                    }
                }
                catch (final Throwable throwable)
                {
                    throw new MetafacadeImplsException(throwable);
                }

                // - add the metafacade model namespace back
                modelNamespaces.add(modelNamespace);
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
                for (final URL namespaceRoot : namespaceRoots)
                {
                    final Collection<String> contents = ResourceUtils.getDirectoryContents(
                            namespaceRoot,
                            false,
                            null);
                    for (final String path : contents)
                    {
                        if (path.endsWith(METAFACADE_IMPLEMENTATION_SUFFIX))
                        {
                            final String typeName =
                                    StringUtils.replace(
                                            ResourceUtils.normalizePath(path).replace(
                                                    '/',
                                                    '.'),
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
                            if (implementationClass != null &&
                                    MetafacadeBase.class.isAssignableFrom(implementationClass))
                            {
                                final List<Class> allInterfaces = ClassUtils.getInterfaces(implementationClass);
                                if (!allInterfaces.isEmpty())
                                {
                                    final Class interfaceClass = allInterfaces.iterator().next();
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
        final MetafacadeClasses classes = this.metafacadeClasses.get(this.metafacadeModelNamespace);
        if (classes == null)
        {
            throw new MetafacadeImplsException("Namespace '" + this.metafacadeModelNamespace + "' is not a registered metafacade model facade namespace");
        }
        return classes;
    }

    /**
     * Retrieves the metafacade class from the passed in <code>metafacadeImplClass</code>. Will return a
     * MetafacadeImplsException if a metafacade class can not be found for the <code>metafacadeImplClass</code>
     *
     * @param metafacadeImplClass the name of the metafacade implementation class.
     * @return the metafacade Class
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
     * @return the metafacade implementation Class
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
    static final class MetafacadeClasses
    {
        /**
         * Stores all <code>metafacade</code> implementation classes keyed by <code>metafacade</code> interface class.
         */
        Map<String, String> implsByMetafacades = new LinkedHashMap<String, String>();

        /**
         * Stores all <code>metafacade</code> interface classes keyed by <code>metafacade</code> implementation class.
         */
        Map<String, String> metafacadesByImpls = new LinkedHashMap<String, String>();

        /**
         * Retrieves the metafacade class from the passed in <code>metafacadeImplClass</code>. Will return a
         * MetafacadeImplsException if a metafacade class can not be found for the <code>metafacadeImplClass</code>
         *
         * @param metafacadeImplClass the name of the metafacade implementation class.
         * @return the metafacade Class
         */
        Class getMetafacadeClass(final String metafacadeImplClass)
        {
            ExceptionUtils.checkEmpty(
                "metafacadeImplClass",
                metafacadeImplClass);
            Class metafacadeClass = null;
            try
            {
                final String metafacadeClassName = this.metafacadesByImpls.get(metafacadeImplClass);
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
         * @return the metafacade implementation Class
         */
        Class getMetafacadeImplClass(final String metafacadeClass)
        {
            ExceptionUtils.checkEmpty(
                "metafacadeClass",
                metafacadeClass);
            Class metafacadeImplementationClass = null;
            try
            {
                final String metafacadeImplementationClassName = this.implsByMetafacades.get(metafacadeClass);
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

        /**
         * @see Object#toString()
         */
        public String toString()
        {
            return super.toString() + "[" + this.metafacadesByImpls + "]";
        }
    }
}