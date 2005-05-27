package org.andromda.core.configuration;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

/**
 * Represents the configuration for the 
 * AndroMDA server.
 * 
 * @author Chad Brandon 
 */
public class Server
    implements Serializable
{
    /**
     * Stores the port the server should be run on.
     */
    private int port;

    /**
     * The port the server should be run on.
     * 
     * @return Returns the port.
     */
    public int getPort()
    {
        return port;
    }

    /**
     * The port the server should be run on.
     * 
     * @param port The port to set.
     */
    public void setPort(final String port)
    {
        if (StringUtils.isNotBlank(port))
        {
            this.port = Integer.parseInt(port);
        }
    }
    
    /**
     * The host the server is running on.
     */
    private String host;

    /**
     * gets the host the server should be run on.
     * 
     * @return Returns the host.
     */
    public String getHost()
    {
        return host;
    }

    /**
     * Sets the host the server should be run on.
     * 
     * @param host The host to set.
     */
    public void setHost(String host)
    {
        this.host = host;
    }
}
