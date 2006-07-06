package org.andromda.android.core.internal.project.runner;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map;

import org.andromda.android.core.AndroidCore;
import org.andromda.android.core.internal.project.generator.AndroMDAppRunner;
import org.andromda.android.core.util.ResourceResolver;
import org.apache.commons.beanutils.MethodUtils;

/**
 *
 * @author Peter Friese
 * @since 03.06.2006
 */
public class AndroMDARunner
{

    /**
     * AndroMDA core
     */
    private static final String ANDROMDA_CORE = "andromda-core";

    /**
     * AndroMDA integration for velocity
     */
    private static final String ANDROMDA_TEMPLATEENGINE_VELOCITY = "andromda-templateengine-velocity";

    /**
     * Apache commons digester
     */
    private static final String COMMONS_DIGESTER = "commons-digester";

    /**
     * Apache commons logging
     */
    private static final String COMMONS_LOGGING = "commons-logging";

    /**
     * Log4J
     */
    private static final String LOG4J = "log4j";

    /**
     * Velocity
     */
    private static final String VELOCITY = "velocity-dep";

    /**
     * Holds the configuration for the project generator.
     */
    private Map configuration;

    private URL configurationURL;

    /**
     * Execute the project generator.
     */
    public void run()
    {
        try
        {
            // load class
            ClassLoader loader = getClassLoader();
            Class androMDAClass = Class.forName("org.andromda.core.AndroMDA", true, loader);
            Method newInstanceMethod = androMDAClass.getMethod("newInstance", null);
            Object androMDA = newInstanceMethod.invoke(null, null);

            // set configuration
            Thread.currentThread().setContextClassLoader(loader);

            // run generator
            Object result = MethodUtils.invokeMethod(androMDA, "run", configurationURL);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    /**
     * Creates a new classloader that has access to the required libraries.
     *
     * @return a classloader that has access to the required libraries.
     * @throws MalformedURLException
     */
    private URLClassLoader getClassLoader() throws MalformedURLException
    {
        String libraryRepository = AndroidCore.getAndroidSettings().getAndroMDACartridgesLocation();
        String androMDAVersion = AndroidCore.getAndroidSettings().getAndroMDAPreferredVersion();

        String andromdacore = ResourceResolver.findLibrary(libraryRepository, ANDROMDA_CORE, androMDAVersion, true);
        String velocity = ResourceResolver.findLibrary(libraryRepository, VELOCITY, "1.4", true);
        String andromdaVelocityEngine = ResourceResolver.findLibrary(libraryRepository,
                ANDROMDA_TEMPLATEENGINE_VELOCITY, androMDAVersion, true);
        String digester = ResourceResolver.findLibrary(libraryRepository, COMMONS_DIGESTER, "1.7", true);
        String log4J = ResourceResolver.findLibrary(libraryRepository, LOG4J, "1.2.12", true);
        String commonsLogging = ResourceResolver.findLibrary(libraryRepository, COMMONS_LOGGING, "1.0.4", true);

        // MDR Repository and friends
        String netbeansRepo = ResourceResolver.findLibrary(libraryRepository, "andromda-repository-mdr",
                androMDAVersion, true);
        String jmi = ResourceResolver.findLibrary(libraryRepository, "jmi", "20030918", true);
        String mdrapi = ResourceResolver.findLibrary(libraryRepository, "mdrapi", "20050711", true);
        String jmiutils = ResourceResolver.findLibrary(libraryRepository, "jmiutils", "20050711", true);
        String jmiuml = ResourceResolver.findLibrary(libraryRepository, "jmiuml", "1.4di", true);
        String openideutils = ResourceResolver.findLibrary(libraryRepository, "openide-util", "20050711", true);
        String nbmdr = ResourceResolver.findLibrary(libraryRepository, "nbmdr", "20050711", true);
        String mof = ResourceResolver.findLibrary(libraryRepository, "mof", "20030918", true);

        String oclValidation = ResourceResolver.findLibrary(libraryRepository, "andromda-ocl-validation-library",
                androMDAVersion, true);
        String oclQuery = ResourceResolver.findLibrary(libraryRepository, "andromda-ocl-query-library",
                androMDAVersion, true);
        String oclTransCore = ResourceResolver.findLibrary(libraryRepository, "andromda-ocl-translation-core",
                androMDAVersion, true);

        // profiles
        // String profile = ResourceResolver.findProfile(libraryRepository, null, androMDAVersion, true);
        // String datatypeProfile = ResourceResolver.findProfile(libraryRepository, "datatype", androMDAVersion, true);

        // cartridges
        String springCartridge = ResourceResolver.findCartridge(libraryRepository, "spring", androMDAVersion, true);
        String hibernateCartridge = ResourceResolver.findCartridge(libraryRepository, "hibernate", androMDAVersion,
                true);
        String javaCartridge = ResourceResolver.findCartridge(libraryRepository, "java", androMDAVersion, true);

        // Metafacades
        String andromdaMetafacadesUML = ResourceResolver.findLibrary(libraryRepository, "andromda-metafacades-uml",
                androMDAVersion, true);
        String andromdaMetafacadesUML14 = ResourceResolver.findLibrary(libraryRepository, "andromda-metafacades-uml14",
                androMDAVersion, true);
        String andromdaMetafacadesUML2 = ResourceResolver.findLibrary(libraryRepository, "andromda-metafacades-uml2",
                androMDAVersion, true);

        URL[] urls = new URL[] { new URL(andromdacore), new URL(velocity), new URL(andromdaVelocityEngine),
                new URL(digester), new URL(log4J), new URL(commonsLogging), new URL(netbeansRepo), new URL(jmi),
                new URL(mdrapi), new URL(jmiutils), new URL(openideutils), new URL(nbmdr), new URL(mof),
                new URL(andromdaMetafacadesUML14), new URL(andromdaMetafacadesUML2), new URL(jmiuml),
                new URL(andromdaMetafacadesUML), new URL(springCartridge), new URL(hibernateCartridge),
                new URL(javaCartridge), new URL(oclQuery), new URL(oclValidation), new URL(oclTransCore) };

        URLClassLoader loader = new URLClassLoader(urls, AndroMDAppRunner.class.getClassLoader());
        return loader;
    }

    /**
     * Set the configuraton for the MDA engine.
     *
     * @param configuration the configuration URL
     */
    public void setConfiguration(URL configuration)
    {
        configurationURL = configuration;
    }

}
