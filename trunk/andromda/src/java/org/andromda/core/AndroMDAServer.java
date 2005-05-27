package org.andromda.core;

import org.andromda.core.common.AndroMDALogger;
import org.andromda.core.common.ComponentContainer;
import org.andromda.core.configuration.Configuration;
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
        final ComponentContainer container = ComponentContainer.instance();

        // can not be instantiated
        this.server = (Server)container.findComponent(Server.class);
        if (this.server == null)
        {
            throw new ServerException(
                "No Server implementation could be found, please make sure you have a '" +
                container.getComponentDefaultConfigurationPath(Server.class) + "' file on your classpath");
        }
    }

    /**
     * Starts the AndroMDA server instance listening for requests.
     *
     * @param configuration the Configuration instance used to configure the
     *        server.
     */
    public void start(final Configuration configuration)
    {
        if (configuration == null)
        {
            throw new ServerException("You must specify a valid 'configuration' in order to start the server");
        }
        if (configuration.getServer() == null)
        {
            AndroMDALogger.warn(
                "Can not start the AndroMDA Server, you must define the " +
                "server element within your AndroMDA configuration");
        }
        else
        {
            this.server.start(configuration);
        }
    }

    /**
     * Stops the AndroMDA server instance.
     */
    public void stop()
    {
        this.server.shutdown();
    }
}