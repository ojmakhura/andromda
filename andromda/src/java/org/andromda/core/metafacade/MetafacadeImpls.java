package org.andromda.core.metafacade;

import java.io.InputStream;

import java.net.URL;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.andromda.core.common.ClassUtils;
import org.andromda.core.common.ExceptionUtils;
import org.andromda.core.common.ResourceFinder;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;


/**
 * Finds and loads metafacade-impl.properties files on the classpath. These files are used to map metafacades and their
 * associated implementation classes in a simple manner.
 *
 * @author Chad Brandon
 */
public class MetafacadeImpls
{
    private static final Logger logger = Logger.getLogger(MetafacadeImpls.class);

    /**
     * Stores all <code>metafacade</code> implementation classes keyed by <code>metafacade</code> interface class.
     */
    private Map implsByMetafacades = null;

    /**
     * Stores all <code>metafacade</code> interface classes keyed by <code>metafacade</code> implementation class.
     */
    private Map metafacadesByImpls = null;

    /**
     * This contains a key/value mapping of metafacades to their implementation classes.
     */
    private static final String METAFACADE_IMPLS = "metafacade-impls.properties";

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
     * Discovers all metafacade-impls.properties files on the classpath. Note that this method must be called before any
     * metafacade implementation classes will be able to be retrieved when calling {@link #getMetafacadeClass(String)}or
     * {@link #getMetafacadeImplClass(String)}.
     */
    public void discoverMetafacadeImpls()
    {
        final String methodName = "MetafacadeImpls.discoverMetafacadeImpls";
        if (logger.isDebugEnabled())
        {
            logger.debug("performing " + methodName + " with propertiesName '" + METAFACADE_IMPLS + "'");
        }
        try
        {
            this.implsByMetafacades = new HashMap();
            this.metafacadesByImpls = new HashMap();

            final Properties properties = new Properties();
            final URL[] resources = ResourceFinder.findResources(METAFACADE_IMPLS);
            if (resources != null && resources.length > 0)
            {
                for (int ctr = 0; ctr < resources.length; ctr++)
                {
                    URL resource = resources[ctr];
                    if (logger.isDebugEnabled())
                    {
                        logger.debug("loading metafacade implementations from resource --> '" + resource + "'");
                    }
                    InputStream stream = resource.openStream();
                    properties.load(stream);
                    stream.close();
                    stream = null;
                }
                for (final Iterator iterator = properties.keySet().iterator(); iterator.hasNext();)
                {
                    final String metafacade = (String)iterator.next();
                    final String metafacadeImplementation = properties.getProperty(metafacade);
                    this.metafacadesByImpls.put(metafacadeImplementation, metafacade);
                    this.implsByMetafacades.put(metafacade, metafacadeImplementation);
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
        ExceptionUtils.checkEmpty("metafacadeClass", metafacadeClass);
        Class metafacadeImplementationClass = null;
        if (this.implsByMetafacades != null)
        {
            try
            {
                final String metafacadeImplementationClassName = (String)this.implsByMetafacades.get(metafacadeClass);
                if (StringUtils.isEmpty(metafacadeImplementationClassName))
                {
                    throw new MetafacadeImplsException(
                        "Can not find a metafacade implementation class for --> '" + metafacadeClass +
                        "', please check your classpath and verify you have a '" + METAFACADE_IMPLS +
                        "' file available with this mapping.");
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
        ExceptionUtils.checkEmpty("metafacadeImplClass", metafacadeImplClass);
        Class metafacadeClass = null;
        if (this.metafacadesByImpls != null)
        {
            try
            {
                final String metafacadeClassName = (String)this.metafacadesByImpls.get(metafacadeImplClass);
                if (StringUtils.isEmpty(metafacadeClassName))
                {
                    throw new MetafacadeImplsException(
                        "Can not find a metafacade interface for --> '" + metafacadeImplClass +
                        "', please check your classpath and verify you have a '" + METAFACADE_IMPLS +
                        "' file available with this mapping.  If you're sure its on your classpath with this mapping" +
                        ", then make sure you don't have another '" + METAFACADE_IMPLS 
                        + "' on your classpath with the metafacade interface that '" + metafacadeImplClass + "' is mapped to.");
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