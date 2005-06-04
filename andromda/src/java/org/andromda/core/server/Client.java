package org.andromda.core.server;

import java.net.ConnectException;

import org.andromda.core.configuration.Configuration;


/**
 * A client used to connect to an AndroMDA
 * {@link Server} instance.
 *
 * @see Server
 * @author Chad Brandon
 */
public interface Client
{
    /**
     * Connects to and starts an AndroMDA server with the given <code>configuration</code>.
     *
     * @param configuration the AndroMDA configuration instance.
     * @throws ConnectException if the client can not connect to an
     *         AndroMDA server instance.
     */
    public void start(final Configuration configuration)
        throws ConnectException;

    /**
     * Connects to and stops an AndroMDA server with the given <code>configuration</code>.
     *
     * @param configuration the AndroMDA configuration instance.
     * @throws ConnectException if the client can not connect to an
     *         AndroMDA server instance.
     */
    public void stop(final Configuration configuration)
        throws ConnectException;
}