package org.andromda.core;

import org.andromda.core.common.AndroMDALogger;
import org.andromda.core.common.ComponentContainer;
import org.andromda.core.configuration.Configuration;
import org.andromda.core.server.Client;
import org.andromda.core.server.ClientException;
import org.andromda.core.server.Server;
import org.andromda.core.server.ServerException;


/**
 * Provides the ability to manage an AndroMDA server instance.
 *
 * @see org.andromda.core.server.Server
 * @author Chad Brandon
 */
public class AndroMDAServer
{
    /**
     * The actual server instance.
     */
    private Server server;

    /**
     * Creates a new instance of class.
     *
     * @return the new instance.
     */
    public static AndroMDAServer newInstance()
    {
        return new AndroMDAServer();
    }

    private AndroMDAServer()
    {
        AndroMDALogger.initialize();
        this.server = (Server)ComponentContainer.instance().findRequiredComponent(Server.class);
    }

    /**
     * Starts the AndroMDA server instance listening for requests.
     *
     * @param configuration the Configuration instance used to configure the server.
     */
    public void start(final Configuration configuration)
    {
        if (configuration == null)
        {
            throw new ServerException("You must specify a valid 'configuration' in order to start the server");
        }
        final org.andromda.core.configuration.Server serverConfiguration = configuration.getServer();
        if (serverConfiguration == null)
        {
            AndroMDALogger.warn(
                "Can not start the server, you must define the " + "server element within your AndroMDA configuration");
        }
        else
        {
            this.server.start(configuration);
        }
    }

    /**
     * Stops the AndroMDA server instance.
     * @param configuration the Configuration instance used to configure the server.
     */
    public void stop(final Configuration configuration)
    {
        final ComponentContainer container = ComponentContainer.instance();
        final Client serverClient = (Client)container.findComponent(Client.class);
        if (serverClient != null)
        {
            try
            {
                serverClient.stop(configuration);
            }
            catch (final Throwable throwable)
            {
                throw new ClientException(throwable);
            }
        }
    }
}