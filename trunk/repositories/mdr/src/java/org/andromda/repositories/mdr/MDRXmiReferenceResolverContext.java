package org.andromda.repositories.mdr;

import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import javax.jmi.reflect.RefPackage;

import org.apache.log4j.Logger;
import org.netbeans.api.xmi.XMIInputConfig;
import org.netbeans.lib.jmi.xmi.XmiContext;

/**
 * This class supports the expansion of XML HREF references to other modules within
 * a model.  The result of the resolver should be a valid URL.  This is 
 * necessary for Magic Draw as it doesn't have the entire model referenced
 * but just the archived model.
 * 
 * @author Matthias Bohlen
 * @author Chad Brandon  
 */
public class MDRXmiReferenceResolverContext extends XmiContext
{

    private String[] moduleSearchPath;

    private static Logger logger =
        Logger.getLogger(MDRXmiReferenceResolverContext.class);

    private HashMap urlMap = new HashMap();

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
            logger.debug(
                "attempting to resolve Xmi Href --> '" + systemId + "'");

        // Several tries to construct a URL that really exists.

        // If this is a valid URL, simply return it.
        URL validURL = getValidURL(systemId);
        if (validURL != null)
        {
            urlMap.put(getSuffix(systemId), validURL);
            if (logger.isDebugEnabled())
                logger.debug(systemId + " --> " + validURL);
            return validURL;
        }

        // Find URL in map. If found, return it.
        String suffix = getSuffix(systemId);
        URL mappedUrl = (URL) urlMap.get(suffix);
        if (mappedUrl != null)
        {
            if (logger.isDebugEnabled())
                logger.debug(systemId + " --> " + mappedUrl);
            return mappedUrl;
        }

        // Try to find suffix in module list.
        String moduleURL = findModuleURL(suffix);
        if (moduleURL != null)
        {
            validURL = getValidURL(moduleURL);
            if (validURL != null)
            {
                urlMap.put(getSuffix(systemId), validURL);
                if (logger.isDebugEnabled())
                    logger.debug(systemId + " --> " + validURL);
                return validURL;
            }
        }

        // If still ends with .zip, find it in map without the '.zip'.
        if (systemId.endsWith(".zip"))
        {
            String urlWithoutZip =
                systemId.substring(0, systemId.length() - 4);
            mappedUrl = (URL) urlMap.get(urlWithoutZip);
            if (mappedUrl != null)
            {
                if (logger.isDebugEnabled())
                    logger.debug(systemId + " --> " + mappedUrl);
                return mappedUrl;
            }
        }

        // Give up and let superclass deal with it.
        return super.toURL(systemId);
    }

    /**
     * Finds a module in the module search path.
     * 
     * @param moduleName the name of the module without any path
     * @return the complete URL string of the module if found (null if not found)
     */
    private String findModuleURL(String moduleName)
    {
        if (moduleSearchPath == null)
            return null;

        if (logger.isDebugEnabled())
            logger.debug(
                "findModuleURL: moduleSearchPath.length="
                    + moduleSearchPath.length);
        for (int i = 0; i < moduleSearchPath.length; i++)
        {
            File candidate = new File(moduleSearchPath[i], moduleName);
            if (logger.isDebugEnabled())
                logger.debug(
                    "candidate '"
                        + candidate.toString()
                        + "' exists="
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
                    urlString =
                        "jar:"
                            + urlString
                            + "!/"
                            + moduleName.substring(
                                0,
                                moduleName.length() - 4);
                }
                return urlString;
            }
        }
        return null;
    }

    /**
     *  Gets the suffix of the <code>systemId</code>
     * @param systemId the system identifier.
     * @return the suffix as a String.
     */
    private static String getSuffix(String systemId)
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
     * Returns a URL if the systemId is valid.
     * Returns null otherwise. Catches exceptions as necessary.
     * 
     * @param systemId the system id
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
