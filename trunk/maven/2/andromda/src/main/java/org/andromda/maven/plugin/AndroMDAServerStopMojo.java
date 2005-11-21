package org.andromda.maven.plugin;

import org.andromda.core.AndroMDAServer;
import org.andromda.core.configuration.Configuration;


/**
 * Provides the ability to stop the AndroMDA server.
 *
 * @author Chad Brandon
 * @goal stop-server
 * @requiresProject false
 * @requiresDependencyResolution runtime
 */
public class AndroMDAServerStopMojo
    extends AbstractAndroMDAMojo
{
    /**
     * @see org.andromda.maven.plugin.AbstractAndroMDAMojo#execute(org.andromda.core.configuration.Configuration)
     */
    public void execute(final Configuration configuration)
    {
        final AndroMDAServer server = AndroMDAServer.newInstance();
        server.stop(configuration);
    }

}