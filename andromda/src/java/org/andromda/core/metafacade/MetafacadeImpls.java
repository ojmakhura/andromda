package org.andromda.core.metafacade;

import java.net.URL;
import java.util.Properties;

import org.andromda.core.common.ClassUtils;
import org.andromda.core.common.ExceptionUtils;
import org.andromda.core.common.ResourceFinder;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * Finds and loads metafacade-impl.properties files on the classpath.  
 * These files are used to map metafacades and their associated 
 * implementation classes in a simple manner.
 * 
 * @author Chad Brandon
 */
public class MetafacadeImpls {
    
    private static final Logger logger = Logger.getLogger(MetafacadeImpls.class);
    
    /**
     * The properties which contain all the metafacade to metafacade impls
     * discovered.
     */
    private Properties properties = null;
    
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
    public static MetafacadeImpls instance() {
        return instance;
    }
    
    /**
     * Discovers all metafacade-impls.properties files on the
     * classpath.  Note that this method must be called
     * before any metafacade implementation classes will be able 
     * to be retrieved when calling getMetafacadeImplClass(java.lang.String)
     */
    public void discoverMetafacadeImpls() {
        String methodName = "MetafacadeImpls.discoverMetafacadeImpls";
        if (logger.isDebugEnabled()) {
            logger.debug("performing " + methodName
                + " with propertiesName '" + METAFACADE_IMPLS + "'"); 
        }
        try {
            properties = new Properties();
            
            URL resources[] = ResourceFinder.findResources(METAFACADE_IMPLS);
            
            if (resources != null && resources.length > 0) {
                for (int ctr = 0; ctr < resources.length; ctr++) {
                    URL resource = resources[ctr];
                    if (logger.isDebugEnabled()) {
                        logger.debug("loading metafacade implementations from resource --> '" 
                             + resource + "'");
                    }
                    properties.load(resource.openStream());
                }
            }            
        } catch (Throwable th) {
        	String errMsg = "Error performing " + methodName;
            logger.error(errMsg, th);
            throw new MetafacadeImplsException(errMsg, th);
        }
    }
    
    /**
     * Retrieves the metafacade implementation
     * class from the passed in <code>metafacadeClass</code>.
     * Will return a MetafacadeImplsException if a metafacade
     * implementation class can not be found for the 
     * <code>metafacadeClass</code>
     * 
     * @param metafacadeClass the name of the metafacade class.
     * @return the metafacacade implementation Class
     */
    public Class getMetafacadeImplClass(String metafacadeClass) {
        final String methodName = "MetafacadeImpls.getMetafacadeImpl";
        ExceptionUtils.checkEmpty(methodName, "metafacadeClass", metafacadeClass);
        Class metafacadeImplClass = null;
        if (this.properties != null) {
            try {
                String metafacadeImplClassName = 
                    this.properties.getProperty(metafacadeClass);
                if (StringUtils.isEmpty(metafacadeImplClassName)) {
                	throw new MetafacadeImplsException(
                        "Can not find a metafacade implementation class for --> '" 
                            + metafacadeClass 
                            + "', please check your classpath");
                }
            	metafacadeImplClass = ClassUtils.loadClass(metafacadeImplClassName);
            } catch (Throwable th) {
            	String errMsg = "Error performing " + methodName;
                logger.error(errMsg, th);
                throw new MetafacadeImplsException(errMsg, th);
            }
        }
        return metafacadeImplClass;
    }
    
}
