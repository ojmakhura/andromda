package org.andromda.core.metafacade;

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
 * Finds and loads metafacade-impl.properties files on the classpath. These
 * files are used to map metafacades and their associated implementation classes
 * in a simple manner.
 * 
 * @author Chad Brandon
 */
public class MetafacadeImpls
{

    private static final Logger logger = Logger
        .getLogger(MetafacadeImpls.class);

    /**
     * Stores all <code>metafacade</code> implementation classes keyed by
     * <code>metafacade</code> interface class.
     */
    private Map implsByMetafacades = null;

    /**
     * Stores all <code>metafacade</code> interface classes keyed by
     * <code>metafacade</code> implementation class.
     */
    private Map metafacadesByImpls = null;

    /**
     * This contains a key/value mapping of metafacades to their implementation
     * classes.
     */
    private static final String METAFACADE_IMPLS = "metafacade-impls.properties";

    /**
     * The shared instance.
     */
    private static MetafacadeImpls instance = new MetafacadeImpls();

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
     * Discovers all metafacade-impls.properties files on the classpath. Note
     * that this method must be called before any metafacade implementation
     * classes will be able to be retrieved when calling
     * getMetafacadeImplClass(java.lang.String)
     */
    public void discoverMetafacadeImpls()
    {
        String methodName = "MetafacadeImpls.discoverMetafacadeImpls";
        if (logger.isDebugEnabled())
        {
            logger.debug("performing " + methodName + " with propertiesName '"
                + METAFACADE_IMPLS + "'");
        }
        try
        {
            this.implsByMetafacades = new HashMap();
            this.metafacadesByImpls = new HashMap();

            Properties properties = new Properties();
            URL resources[] = ResourceFinder.findResources(METAFACADE_IMPLS);
            if (resources != null && resources.length > 0)
            {
                for (int ctr = 0; ctr < resources.length; ctr++)
                {
                    URL resource = resources[ctr];
                    if (logger.isDebugEnabled())
                    {
                        logger
                            .debug("loading metafacade implementations from resource --> '"
                                + resource + "'");
                    }
                    properties.load(resource.openStream());
                }
                Iterator propertyIt = properties.keySet().iterator();
                while (propertyIt.hasNext())
                {
                    String metafacade = (String)propertyIt.next();
                    String metafacadeImpl = properties.getProperty(metafacade);
                    this.metafacadesByImpls.put(metafacadeImpl, metafacade);
                    this.implsByMetafacades.put(metafacade, metafacadeImpl);
                }
            }
        }
        catch (Throwable th)
        {
            String errMsg = "Error performing " + methodName;
            logger.error(errMsg, th);
            throw new MetafacadeImplsException(errMsg, th);
        }
    }

    /**
     * Retrieves the metafacade implementation class from the passed in
     * <code>metafacadeClass</code>. Will return a MetafacadeImplsException
     * if a metafacade implementation class can not be found for the
     * <code>metafacadeClass</code>
     * 
     * @param metafacadeClass the name of the metafacade class.
     * @return the metafacacade implementation Class
     */
    public Class getMetafacadeImplClass(String metafacadeClass)
    {
        final String methodName = "MetafacadeImpls.getMetafacadeImplClass";
        ExceptionUtils.checkEmpty(
            methodName,
            "metafacadeClass",
            metafacadeClass);
        Class metafacadeImplClass = null;
        if (this.implsByMetafacades != null)
        {
            try
            {
                String metafacadeImplClassName = (String)this.implsByMetafacades
                    .get(metafacadeClass);
                if (StringUtils.isEmpty(metafacadeImplClassName))
                {
                    throw new MetafacadeImplsException(
                        "Can not find a metafacade implementation class for --> '"
                            + metafacadeClass
                            + "', please check your classpath");
                }
                metafacadeImplClass = ClassUtils
                    .loadClass(metafacadeImplClassName);
            }
            catch (Throwable th)
            {
                String errMsg = "Error performing " + methodName;
                logger.error(errMsg, th);
                throw new MetafacadeImplsException(errMsg, th);
            }
        }
        return metafacadeImplClass;
    }

    /**
     * Retrieves the metafacade class from the passed in
     * <code>metafacadeImplClass</code>. Will return a
     * MetafacadeImplsException if a metafacade class can not be found for the
     * <code>metafacadeImplClass</code>
     * 
     * @param metafacadeImplClass the name of the metafacade implementation
     *        class.
     * @return the metafacacade Class
     */
    public Class getMetafacadeClass(String metafacadeImplClass)
    {
        final String methodName = "MetafacadeImpls.getMetafacadeClass";
        ExceptionUtils.checkEmpty(
            methodName,
            "metafacadeImplClass",
            metafacadeImplClass);
        Class metafacadeClass = null;
        if (this.metafacadesByImpls != null)
        {
            try
            {
                String metafacadeClassName = (String)this.metafacadesByImpls
                    .get(metafacadeImplClass);
                if (StringUtils.isEmpty(metafacadeImplClass))
                {
                    throw new MetafacadeImplsException(
                        "Can not find a metafacade class for --> '"
                            + metafacadeImplClass
                            + "', please check your classpath");
                }
                metafacadeClass = ClassUtils.loadClass(metafacadeClassName);
            }
            catch (Throwable th)
            {
                String errMsg = "Error performing " + methodName;
                logger.error(errMsg, th);
                throw new MetafacadeImplsException(errMsg, th);
            }
        }
        return metafacadeClass;
    }

}