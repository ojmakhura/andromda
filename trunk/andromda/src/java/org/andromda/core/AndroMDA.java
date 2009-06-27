package org.andromda.core;

import java.io.EOFException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.URL;

import org.andromda.core.common.AndroMDALogger;
import org.andromda.core.common.ComponentContainer;
import org.andromda.core.common.ExceptionUtils;
import org.andromda.core.configuration.Configuration;
import org.andromda.core.engine.Engine;
import org.andromda.core.metafacade.ModelValidationMessage;
import org.andromda.core.server.Client;
import org.apache.log4j.Logger;


/**
 * The main entry point to the framework. Handles the processing of models.
 * Facilitates Model Driven Architecture by enabling the generation of source
 * code, configuration files, and other such artifacts from a single or multiple
 * models.
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
     * @return the new instance of AndroMDA.
     */
    public static AndroMDA newInstance()
    {
        return new AndroMDA();
    }

    /**
     * Hidden constructor.
     */
    private AndroMDA()
    {
        AndroMDALogger.initialize();
        this.engine = Engine.newInstance();
    }

    /**
     * Runs AndroMDA with the given configuration.
     *
     * @param configurationUri the URI to the configuration file that configures
     *        AndroMDA.
     */
    public void run(final URL configurationUri)
    {
        ExceptionUtils.checkNull(
            "configurationUri",
            configurationUri);
        this.run(Configuration.getInstance(configurationUri));
    }

    /**
     * Runs AndroMDA with the given configuration.
     *
     * @param configurationStream the InputStream that contains the
     *        configuration contents for configuring AndroMDA.
     */
    public void run(final InputStream configurationStream)
    {
        ExceptionUtils.checkNull(
            "configurationStream",
            configurationStream);
        this.run(Configuration.getInstance(configurationStream));
    }

    /**
     * Runs AndroMDA with the given configuration.
     *
     * @param configuration the String that contains the configuration contents
     *        for configuring AndroMDA.
     */
    public void run(final String configuration)
    {
        ExceptionUtils.checkEmpty(
            "configuration",
            configuration);
        this.run(Configuration.getInstance(configuration));
    }

    /**
     * Initializes AndroMDA without running the engine.
     *
     * @param configuration the configuration from which to initialize AndroMDA
     */
    public void initialize(final Configuration configuration)
    {
        ExceptionUtils.checkNull(
            "configuration",
            configuration);
        this.engine.initialize(configuration);
    }

    /**
     * Initializes AndroMDA without running the engine.
     *
     * @param configurationStream the InputStream that contains the
     *        configuration contents for configuring AndroMDA.
     */
    public void initialize(final InputStream configurationStream)
    {
        ExceptionUtils.checkNull(
            "configurationStream",
            configurationStream);
        this.engine.initialize(Configuration.getInstance(configurationStream));
    }

    /**
     * Initializes AndroMDA without running the engine.
     *
     * @param configuration the String that contains the configuration contents
     *        for configuring AndroMDA.
     */
    public void initialize(final String configuration)
    {
        ExceptionUtils.checkEmpty(
            "configuration",
            configuration);
        this.engine.initialize(Configuration.getInstance(configuration));
    }

    /**
     * Initializes AndroMDA without running the engine.
     *
     * @param configurationUri the URI to the configuration file that configures
     *        AndroMDA.
     */
    public void initialize(final URL configurationUri)
    {
        ExceptionUtils.checkNull(
            "configurationUri",
            configurationUri);
        this.run(Configuration.getInstance(configurationUri));
    }

    /**
     * Runs AndroMDA with the given configuration. Determines whether or not
     * AndroMDA should be run in client/server mode (if the client can contact
     * the AndroMDA server), or just stand-alone mode if the server can NOT be
     * contacted.
     *
     * @param configuration the configuration instance that configures AndroMDA.
     * @return an array of model validation messages (if there have been any collected
     *         during AndroMDA execution).
     */
    public ModelValidationMessage[] run(final Configuration configuration)
    {
        ModelValidationMessage[] messages = null;
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
                catch (final Throwable throwable)
                {
                    final Throwable cause = ExceptionUtils.getRootCause(throwable);
                    if (cause instanceof ConnectException ||
                        cause instanceof EOFException)
                    {
                        // - if we can't connect to the server, it means
                        //   we aren't running in client mode
                        client = false;
                    }
                    else
                    {
                        throw new RuntimeException(throwable);
                    }
                }
            }
            else
            {
                client = false;
            }

            // - since we aren't running in 'client' mode, run the engine as usual
            if (!client)
            {
                this.engine.initialize(configuration);
                messages = this.engine.run(configuration);
            }
        }
        else
        {
            logger.warn("AndroMDA could not run because no configuration was defined");
        }
        return messages == null ? new ModelValidationMessage[0] : messages;
    }

    /**
     * Shuts down AndroMDA.
     */
    public void shutdown()
    {
        this.engine.shutdown();
    }
}