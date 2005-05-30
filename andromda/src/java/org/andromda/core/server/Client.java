package org.andromda.core.server;

import org.andromda.core.configuration.Configuration;

import java.net.ConnectException;


/**
 * A client used to communicate to an AndroMDA
 * {@link Server} instance.
 *
 * @see Server
 * @author Chad Brandon
 */
public interface Client
{
    /**
     * Starts the AndroMDA server with the given <code>configuration</code>.
     *
     * @param configuration the AndroMDA configuration instance.
     * @throws ConnectException if the client can not connect to an
     *         AndroMDA server instance.
     */
    public void start(final Configuration configuration)
        throws ConnectException;

    /**
     * Stops the AndroMDA server with the given <code>configuration</code>.
     *
     * @param configuration the AndroMDA configuration instance.
     * @throws ConnectException if the client can not connect to an
     *         AndroMDA server instance.
     */
    public void stop(final Configuration configuration)
        throws ConnectException;
}