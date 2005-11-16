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
     * Stores all <code>metafacade</code> implementation classes keyed by <code>metafacade</code> interface class.
     */
    private Map implsByMetafacades = null;

    /**
     * Stores all <code>metafacade</code> interface classes keyed by <code>metafacade</code> implementation class.
     */
    private Map metafacadesByImpls = null;

    /**
     * The shared instance.
     */
    private static final MetafacadeImpls instance = new MetafacadeImpls();

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
     * The extension for the metafacade implementation files.
     */
    private final static String METAFACADE_IMPLEMENTATION_SUFFIX =
        MetafacadeConstants.METAFACADE_IMPLEMENTATION_SUFFIX + ClassUtils.CLASS_EXTENSION;

    /**
     * The shared namespaces instance.
     */
    private Namespaces namespaces = Namespaces.instance();

    /**
     * Discovers all metafacade implementation classes and interfaces in each namespace. Note that this method must be called before any
     * metafacade implementation classes will be able to be retrieved when calling {@link #getMetafacadeClass(String)}or
     * {@link #getMetafacadeImplClass(String)}.
     */
    public void discover()
    {
        this.implsByMetafacades = new LinkedHashMap();
        this.metafacadesByImpls = new LinkedHashMap();
        try
        {
            for (final Iterator iterator = this.namespaces.getNamespaceRegistries().iterator(); iterator.hasNext();)
            {
                final NamespaceRegistry namespaceRegistry = (NamespaceRegistry)iterator.next();
                final String namespace = namespaceRegistry.getName();
                if (this.namespaces.isComponentPresent(
                        namespace,
                        Constants.COMPONENT_METAFACADES))
                {
                    final URL namespaceRoot = this.namespaces.getResourceRoot(namespace);
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
                                        "\\+|/+",
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
                            if (implementationClass != null &&
                                MetafacadeBase.class.isAssignableFrom(implementationClass))
                            {
                                final List allInterfaces = ClassUtils.getInterfaces(implementationClass);
                                if (!allInterfaces.isEmpty())
                                {
                                    final Class interfaceClass = (Class)allInterfaces.iterator().next();
                                    final String implementationClassName = implementationClass.getName();
                                    final String interfaceClassName = interfaceClass.getName();
                                    this.metafacadesByImpls.put(
                                        implementationClassName,
                                        interfaceClassName);
                                    this.implsByMetafacades.put(
                                        interfaceClassName,
                                        implementationClassName);
                                }
                            }
                        }
                    }
                }
            }
        }
        catch (final Throwable throwable)
        {
            throw new MetafacadeImplsException(throwable);
        }
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
        Class metafacadeImplementationClass = null;
        if (this.implsByMetafacades != null)
        {
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
        }
        return metafacadeImplementationClass;
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
        Class metafacadeClass = null;
        if (this.metafacadesByImpls != null)
        {
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
        }
        return metafacadeClass;
    }
}