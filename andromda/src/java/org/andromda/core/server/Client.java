package org.andromda.core.server;

import java.net.ConnectException;

import org.andromda.core.configuration.Configuration;

/**
 * A client used to communicate to the the AndroMDA
 * {@link DefaultServer} instance.
 *
 * @author Chad Brandon
 */
public interface Client
{

    /**
     * Runs the client with the given <code>configuration</code>.
     *
     * @param configuration the AndroMDA configuration instance.
     * @throws ConnectException if the client can not connect to an
     *         AndroMDA server instance.
     */
    public void run(final Configuration configuration) throws ConnectException;

}