package org.andromda.core.common;

import java.io.InputStream;
import java.net.URL;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

/**
 * This is the logger used to write <em>AndroMDA</em> prefixed messages so
 * that our informational logging is nice looking.
 * 
 * @since 26.11.2003
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen </a>
 * @author Chad Brandon
 */
public class AndroMDALogger
{
    /**
     * The default name to give the logger (if one isn't set)
     */
    private static final String DEFAULT_LOGGER_NAME = "AndroMDA";

    private static Logger logger = Logger.getLogger(DEFAULT_LOGGER_NAME);

    /**
     * Configures logging for the AndroMDA application from the the xml resource
     * "log4j.xml" found within the same package as this class.
     */
    public static void configure()
    {
        String defaultConfiguration = "log4j.xml";
        URL url = null;
        String configuration = loggingConfigurationUri;
        if (StringUtils.isNotEmpty(configuration))
        {
            try
            {
                url = new URL(configuration);
                InputStream stream = url.openStream();
                stream.close();
                stream = null;
                configure(url);
                logger.info("Logging configured from external source --> '"
                    + configuration + "'");
            }
            catch (Throwable th)
            {
                url = AndroMDALogger.class.getResource(defaultConfiguration);
                configure(url);
                logger.warn("Invalid logging configuration uri --> '"
                    + configuration + "'");
            }
        }
        if (url == null)
        {
            url = AndroMDALogger.class.getResource(defaultConfiguration);
            configure(url);
        }
        if (url == null)
        {
            throw new RuntimeException(
                "Could not find default logging configuration file '"
                    + defaultConfiguration + "'");
        }
    }

    /**
     * The URI to an external logging configuration file.
     */
    private static String loggingConfigurationUri = null;

    /**
     * Sets the URI to an external logging configuration file. This will
     * override the default log4j.xml.
     * 
     * @param loggingConfigurationUri the URI to the logging configuraiton file.
     */
    public static void setLoggingConfigurationUri(String loggingConfigurationUri)
    {
        AndroMDALogger.loggingConfigurationUri = loggingConfigurationUri;
    }

    /**
     * Configures the Logger from the passed in logConfigurationXml
     * 
     * @param logConfigurationXml
     */
    protected static void configure(URL logConfigurationXml)
    {
        try
        {
            DOMConfigurator.configure(logConfigurationXml);
        }
        catch (Exception ex)
        {
            System.err.println("Unable to initialize logging system "
                + "with configuration file '" + logConfigurationXml
                + "' --> using basic configuration.");
            BasicConfigurator.configure();
        }
    }

    /**
     * Retrieves the plugin logger (if one is available) otherwise returns the
     * root logger.
     * 
     * @param pluginName the name of the plugin for which we'll retrieve the
     *        logger instance.
     * @return the plugin or root logger instance.
     */
    public static Logger getPluginLogger(String pluginName)
    {
        Logger logger;
        if (pluginName != null && !Namespaces.DEFAULT.equals(pluginName))
        {
            logger = Logger.getLogger(getPluginLoggerName(pluginName));
        }
        else
        {
            logger = Logger.getRootLogger();
        }
        return logger;
    }

    /**
     * Gets the name of the logger.
     * 
     * @param pluginName the name of the plugin for which this logger is used.
     * @return the logger name.
     */
    public static String getPluginLoggerName(String pluginName)
    {
        return "org.andromda.plugins." + pluginName;
    }

    /**
     * Gets the name of the file to which plugin logging output will be written.
     * 
     * @param pluginName the name of the plugin for which this logger is used.
     * @return the plugin logging file name.
     */
    public static String getPluginLogFileName(String pluginName)
    {
        return "andromda-" + pluginName + ".log";
    }

    /**
     * Allows us to add a suffix to the logger name.
     * 
     * @param name
     */
    public static void setSuffix(String suffix)
    {
        logger = Logger.getLogger(DEFAULT_LOGGER_NAME + ':' + suffix);
    }

    /**
     * Resets the logger to the default name.
     */
    public static void reset()
    {
        logger = Logger.getLogger(DEFAULT_LOGGER_NAME);
    }

    public static void debug(Object object)
    {
        logger.debug(object);
    }

    public static void info(Object object)
    {
        logger.info(object);
    }

    public static void warn(Object object)
    {
        logger.warn(object);
    }

    public static void error(Object object)
    {
        logger.error(object);
    }
}