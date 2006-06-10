package org.andromda.android.core.internal.project.generator;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.andromda.android.core.AndroidCore;
import org.andromda.android.core.util.ResourceResolver;
import org.apache.commons.beanutils.MethodUtils;

/**
 * This class wraps {@link org.andromda.andromdapp.AndroMDApp} and helps with setting up the class path for running the
 * AndroMDA project generator.
 * 
 * @author Peter Friese
 * @since 16.04.2006
 */
public class AndroMDAppRunner
{
    /** Apache commons digester */
    private static final String COMMONS_DIGESTER = "commons-digester";

    /** Apache commons logging */
    private static final String COMMONS_LOGGING = "commons-logging";

    /** Log4J */
    private static final String LOG4J = "log4j";

    /** AndroMDA integration for velocity */
    private static final String ANDROMDA_TEMPLATEENGINE_VELOCITY = "andromda-templateengine-velocity";

    /** Velocity */
    private static final String VELOCITY = "velocity-dep";

    /** AndroMDA project cartridge for rich clients */
    private static final String ANDROMDA_ANDROMDAPP_PROJECT_RICHCLIENT_ANT = "andromda-andromdapp-project-richclient-ant";

    /** AndroMDA project cartridge for J2EE applications */
    private static final String ANDROMDA_ANDROMDAPP_PROJECT_J2EE_MAVEN2 = "andromda-andromdapp-project-j2ee-maven2";

    /** AndroMDA core */
    private static final String ANDROMDA_CORE = "andromda-core";

    /** AndroMDA project generator core */
    private static final String ANDROMDA_ANDROMDAPP_CORE = "andromda-andromdapp-core";

    /** Holds the configuration for the project generator. */
    private Map configuration;

    /**
     * Creates a new classloader that has access to the required libraries.
     * 
     * @return a classloader that has access to the required libraries.
     * @throws MalformedURLException
     */
    private URLClassLoader getClassLoader() throws MalformedURLException
    {
        // load class
        String libraryRepository = AndroidCore.getAndroidSettings().getAndroMDACartridgesLocation();
        
        String androMDAVersion = "3.2-SNAPSHOT";

        String andromdappcore = ResourceResolver.findLibrary(libraryRepository, ANDROMDA_ANDROMDAPP_CORE,
                androMDAVersion, false);
        String andromdacore = ResourceResolver.findLibrary(libraryRepository, ANDROMDA_CORE, androMDAVersion, false);
        String j2eeproject = ResourceResolver.findLibrary(libraryRepository, ANDROMDA_ANDROMDAPP_PROJECT_J2EE_MAVEN2,
                androMDAVersion, false);
        String richclientproject = ResourceResolver.findLibrary(libraryRepository,
                ANDROMDA_ANDROMDAPP_PROJECT_RICHCLIENT_ANT, androMDAVersion, false);
        String velocity = ResourceResolver.findLibrary(libraryRepository, VELOCITY, "1.4", false);
        String andromdaVelocityEngine = ResourceResolver.findLibrary(libraryRepository,
                ANDROMDA_TEMPLATEENGINE_VELOCITY, androMDAVersion, false);
        String digester = ResourceResolver.findLibrary(libraryRepository, COMMONS_DIGESTER, "1.7", false);
        String log4J = ResourceResolver.findLibrary(libraryRepository, LOG4J, "1.2.12", false);
        String commonsLogging = ResourceResolver.findLibrary(libraryRepository, COMMONS_LOGGING, "1.0.4", false);

        URL[] urls = new URL[] { new URL(andromdappcore), new URL(andromdacore), new URL(j2eeproject),
                new URL(richclientproject), new URL(velocity), new URL(andromdaVelocityEngine), new URL(digester),
                new URL(log4J), new URL(commonsLogging) };

        URLClassLoader loader = new URLClassLoader(urls, AndroMDAppRunner.class.getClassLoader());
        return loader;
    }

    /**
     * Set the configuraton for the project generator.
     * 
     * @param configuration the configuration.
     */
    public void setConfiguration(Map configuration)
    {
        this.configuration = configuration;
    }

    /**
     * Creates the project generator configuration.
     * 
     * @return the project generator configuration.
     */
    private String getConfigurationAsString()
    {
        StringBuffer config = new StringBuffer();
        config.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        config.append("<andromdapp>\n");
        config.append("   <properties>\n");
        Set keys = configuration.keySet();
        for (Iterator iter = keys.iterator(); iter.hasNext();)
        {
            String key = (String)iter.next();
            String value = (String)configuration.get(key);
            config.append("       <property name=\"" + key + "\">" + value + "</property>\n");
        }
        config.append("   </properties>\n");
        config.append("</andromdapp>\n");
        return config.toString();
    }

    /**
     * Execute the project generator.
     */
    public void run()
    {
        try
        {
            // load class
            ClassLoader loader = getClassLoader();
            Class androMDAppClass = Class.forName("org.andromda.andromdapp.AndroMDApp", true, loader);
            Object androMDApp = androMDAppClass.newInstance();

            // set configuration
            Thread.currentThread().setContextClassLoader(loader);
            String configurationString = getConfigurationAsString();
            MethodUtils.invokeMethod(androMDApp, "addConfiguration", new Object[] { configurationString });

            // run generator
            Object result = MethodUtils.invokeMethod(androMDApp, "run", null);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    /**
     * Demo.
     * 
     * @param args
     */
    public static void main(String[] args)
    {
        AndroMDAppRunner runner = new AndroMDAppRunner();

        Map configurationMap = new HashMap();
        configurationMap.put("andromdaVersion", "3.2-RC1-SNAPSHOT");
        configurationMap.put("andromdappType", "j2ee");
        configurationMap.put("applicationParentDirectory", "c:/temp/test");
        configurationMap.put("author", "Peter");
        configurationMap.put("applicationName", "Test");
        configurationMap.put("applicationId", "test");
        configurationMap.put("applicationVersion", "1.0");
        configurationMap.put("applicationPackage", "de.test");
        configurationMap.put("applicationType", "war");
        configurationMap.put("databaseType", "mysql");
        configurationMap.put("workflow", "no");
        configurationMap.put("hibernateVersion", "3");
        configurationMap.put("web", "no");
        configurationMap.put("webservice", "no");
        runner.setConfiguration(configurationMap);

        runner.run();

    }

}
