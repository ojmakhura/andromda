package org.andromda.core.server;

import org.andromda.core.configuration.Configuration;


/**
 * The AndroMDA server instance.  The server
 * is configured from an AndroMDA {@link Configuration}
 * instance.
 *
 * @see Client
 * @author Chad Brandon
 * @author Bob Fields
 */
public interface Server
{
    /**
     * Starts the server instance listening for requests with the given
     * configuration.
     * @param configuration 
     */
    public void start(final Configuration configuration);
}