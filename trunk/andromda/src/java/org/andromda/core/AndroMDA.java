package org.andromda.core;

import java.io.InputStream;
import java.net.ConnectException;
import java.net.URL;

import org.andromda.core.common.AndroMDALogger;
import org.andromda.core.common.ComponentContainer;
import org.andromda.core.configuration.Configuration;
import org.andromda.core.engine.Engine;
import org.andromda.core.server.Client;
import org.apache.log4j.Logger;


/**
 * The main entry point to the framework.  Handles the processing of models. Facilitates Model Driven
 * Architecture by enabling the generation of source code, configuration files, and other such artifacts from a single
 * or multiple models.
 *
 * @see Engine
 * @author Chad Brandon
 */
public class AndroMDA
{
    /**
     * The logger instance.
     */
    private static final Logger logger = Logger.getLogger(AndroMDA.class);

    /**
     * The AndroMDA engine instance.
     */
    private Engine engine;

    /**
     * Gets a new instance of AndroMDA.
     *
     * @param configurationUri the URI to the configuration file
     *        that configures AndroMDA.
     *
     * @return the new instance of AndroMDA.
     */
    public static final AndroMDA newInstance()
    {
        return new AndroMDA();
    }

    private AndroMDA()
    {
        AndroMDALogger.initialize();
        this.engine = Engine.newInstance();
    }

    /**
     * Runs AndroMDA with the given configuration.
     *
     * @param configurationUri the URI to the configuration file
     *        that configures AndroMDA.
     *
     * @return the new instance of AndroMDA.
     */
    public void run(final URL configurationUri)
    {
        this.run(Configuration.getInstance(configurationUri));
    }

    /**
     * Runs AndroMDA with the given configuration.
     *
     * @param configurationStream the InputStream that contains the configuration
     *        contents for configuring AndroMDA.
     *
     * @return the new instance of AndroMDA.
     */
    public void run(final InputStream configurationStream)
    {
        this.run(Configuration.getInstance(configurationStream));
    }

    /**
     * Runs AndroMDA with the given configuration.
     *
     * @param configuration the String that contains the configuration
     *        contents for configuring AndroMDA.
     *
     * @return the new instance of AndroMDA.
     */
    public void run(final String configuration)
    {
        this.run(Configuration.getInstance(configuration));
    }

    /**
     * Runs AndroMDA with the given configuration.  Determines whether or
     * not AndroMDA should be run in client/server mode (if the client
     * can contact the AndroMDA server), or just stand-alone mode if the
     * server can NOT be contacted.
     *
     * @param configuration the String that contains the configuration
     *        contents for configuring AndroMDA.
     *
     * @return the new instance of AndroMDA.
     */
    public void run(final Configuration configuration)
    {
        if (configuration != null)
        {
            final Client serverClient = (Client)ComponentContainer.instance().findRequiredComponent(Client.class);
            boolean client = true;

            // only attempt to run with the client, if they
            // have a server defined in their configuration
            if (configuration.getServer() != null)
            {
                try
                {
                    serverClient.start(configuration);
                }
                catch (final ConnectException exception)
                {
                    client = false;
                }
            }
            else
            {
                client = false;
            }
            if (!client)
            {
                this.engine.initialize();
                this.engine.run(configuration);
            }
        }
        else
        {
            logger.warn("AndroMDA could not run because no configuration was defined");
        }
    }

    /**
     * Shuts down AndroMDA.
     */
    public void shutdown()
    {
        this.engine.shutdown();
    }
}