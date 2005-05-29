package org.andromda.core.configuration;

import org.apache.commons.lang.StringUtils;

import java.io.Serializable;


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

    /**
     * The interval at which the server loads
     * model(s) (if a load is required).
     */
    private int modelLoadInterval = 1000;

    /**
     * Gets the interval at which model(s) are
     * Loaded (if required).
     *
     * @return Returns the model Load interval
     */
    public int getModelLoadInterval()
    {
        return modelLoadInterval;
    }

    /**
     * Sets the interval at which model(s) should be
     * loaded (if an initial load or Load is required).
     *
     * @param modelLoadInterval The loadInterval to set.
     */
    public void setModelLoadInterval(final String modelLoadInterval)
    {
        if (StringUtils.isNotBlank(modelLoadInterval))
        {
            this.modelLoadInterval = new Integer(modelLoadInterval).intValue();
        }
    }
}