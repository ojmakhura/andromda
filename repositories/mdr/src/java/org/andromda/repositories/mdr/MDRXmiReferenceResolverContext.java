package org.andromda.repositories.mdr;

import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import javax.jmi.reflect.RefPackage;

import org.andromda.core.common.ResourceUtils;
import org.andromda.core.common.AndroMDALogger;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.netbeans.api.xmi.XMIInputConfig;
import org.netbeans.lib.jmi.xmi.XmiContext;

/**
 * This class supports the expansion of XML HREF references to other modules
 * within a model. The result of the resolver should be a valid URL. This is
 * necessary for Magic Draw as it doesn't have the entire model referenced but
 * just the archived model.
 * 
 * @author Matthias Bohlen
 * @author Chad Brandon
 */
public class MDRXmiReferenceResolverContext
    extends XmiContext
{

    private String[] moduleSearchPath;

    private static Logger logger = Logger
        .getLogger(MDRXmiReferenceResolverContext.class);

    private static final HashMap urlMap = new HashMap();

    /**
     * Constructs an instance of this class.
     * 
     * @param extents
     * @param config
     */
    public MDRXmiReferenceResolverContext(
        RefPackage[] extents,
        XMIInputConfig config,
        String[] moduleSearchPath)
    {
        super(extents, config);
        this.moduleSearchPath = moduleSearchPath;
    }

    /**
     * @see org.netbeans.lib.jmi.xmi.XmiContext#toURL(java.lang.String)
     */
    public URL toURL(String systemId)
    {
        if (logger.isDebugEnabled())
            logger.debug("attempting to resolve Xmi Href --> '" + systemId
                + "'");
        // if the model URL has a '.zip', remove it
        systemId = systemId.replaceAll("\\.zip", "");
        String suffix = getSuffix(systemId);
        URL modelUrl = (URL)urlMap.get(suffix);
        
        // Several tries to construct a URL that really exists.
        if (modelUrl == null)
        {
            modelUrl = this.getValidURL(systemId);
	        if (modelUrl == null)
	        {
		        // Try to find suffix in module list.
		        String modelUrlAsString = findModuleURL(suffix);	   
		        if (StringUtils.isNotBlank(modelUrlAsString)) 
		        {
		            modelUrl = getValidURL(modelUrlAsString);
		        }	        
		        if (modelUrl == null) 
		        {
		            // search the classpath	
		            modelUrl = this.findModelUrlOnClasspath(systemId);
		        }
		        if (modelUrl == null)
		        {
		            // Give up and let superclass deal with it.
		            modelUrl = super.toURL(systemId);		            
		        }
	        }
	        // if we've found the module model, log it 
	        // and place it in the map so we don't have to 
	        // find it if we need it again.
	        if (modelUrl != null) 
	        {
	            AndroMDALogger.info("Referenced model --> '" 
	                + modelUrl + "'");
	            urlMap.put(suffix, modelUrl);
	        }
        }
        return modelUrl;
    }

    /**
     * Finds a module in the module search path.
     * 
     * @param moduleName
     *            the name of the module without any path
     * @return the complete URL string of the module if found (null if not
     *         found)
     */
    private String findModuleURL(String moduleName)
    {
        if (moduleSearchPath == null)
            return null;

        if (logger.isDebugEnabled())
            logger.debug("findModuleURL: moduleSearchPath.length="
                + moduleSearchPath.length);
        for (int i = 0; i < moduleSearchPath.length; i++)
        {
            File candidate = new File(moduleSearchPath[i], moduleName);
            if (logger.isDebugEnabled())
                logger.debug("candidate '" + candidate.toString() + "' exists="
                    + candidate.exists());
            if (candidate.exists())
            {
                String urlString;
                try
                {
                    urlString = candidate.toURL().toExternalForm();
                }
                catch (MalformedURLException e)
                {
                    return null;
                }

                if (moduleName.endsWith(".zip"))
                {
                    // typical case for MagicDraw
                    urlString = "jar:" + urlString + "!/"
                        + moduleName.substring(0, moduleName.length() - 4);
                }
                return urlString;
            }
        }
        return null;
    }

    /**
     * Gets the suffix of the <code>systemId</code>
     * 
     * @param systemId
     *            the system identifier.
     * @return the suffix as a String.
     */
    private String getSuffix(String systemId)
    {
        int lastSlash = systemId.lastIndexOf("/");
        if (lastSlash > 0)
        {
            String suffix = systemId.substring(lastSlash + 1);
            return suffix;
        }
        return systemId;
    }
    
    /**
     * The suffixes to use when searching for referenced models on the
     * classpath.
     */
    protected final static String[] CLASSPATH_MODEL_SUFFIXES = new String[]
    {
        "xml",
        "xmi"
    };

    /**
     * Searches for the model URL on the classpath.
     * 
     * @param systemId
     *            the system identifier.
     * @return the suffix as a String.
     */
    private URL findModelUrlOnClasspath(String systemId)
    {
        String modelName = StringUtils.substringAfterLast(systemId, "/");
        String dot = ".";
        // remove the first prefix because it may be an archive 
        // (like magicdraw)
        modelName = StringUtils.substringBeforeLast(modelName, dot);

        URL modelUrl = ResourceUtils.getResource(modelName);
        if (modelUrl == null)
        {
            if (CLASSPATH_MODEL_SUFFIXES != null
                && CLASSPATH_MODEL_SUFFIXES.length > 0)
            {
                int suffixNum = CLASSPATH_MODEL_SUFFIXES.length;
                for (int ctr = 0; ctr < suffixNum; ctr++)
                {
                    if (logger.isDebugEnabled())
                        logger.debug("searching for model reference --> '"
                            + modelUrl + "'");
                    String suffix = CLASSPATH_MODEL_SUFFIXES[ctr];
                    modelUrl = ResourceUtils.getResource(modelName + dot
                        + suffix);
                    if (modelUrl != null)
                    {
                        break;
                    }
                }
            }
        }
        return modelUrl;
    }

    /**
     * Returns a URL if the systemId is valid. Returns null otherwise. Catches
     * exceptions as necessary.
     * 
     * @param systemId
     *            the system id
     * @return the URL (if valid)
     */
    private URL getValidURL(String systemId)
    {
        try
        {
            URL url = new URL(systemId);
            InputStream stream = url.openStream();
            stream.close();
            return url;
        }
        catch (Exception e)
        {
            return null;
        }
    }
}