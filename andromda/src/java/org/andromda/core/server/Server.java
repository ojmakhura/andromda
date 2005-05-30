package org.andromda.core.server;

import org.andromda.core.configuration.Configuration;


/**
 * The AndroMDA server instance.  The server
 * is configured from an AndroMDA {@link Configuration}
 * instance.
 *
 * @author Chad Brandon
 */
public interface Server
{
    /**
     * Starts the server instance listening for requests with the given
     * configuration.
     *
     * @throws Exception
     */
    public void start(final Configuration configuration);
}