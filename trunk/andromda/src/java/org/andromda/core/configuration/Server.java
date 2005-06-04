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

    /**
     * The interval at which the server loads
     * model(s) (if a load is required).
     */
    private int loadInterval = 1000;

    /**
     * Gets the interval at which model(s) are
     * loaded (if required).
     *
     * @return Returns the model Load interval
     */
    public int getLoadInterval()
    {
        return loadInterval;
    }

    /**
     * Sets the interval at which model(s) should be
     * loaded (if an initial load or Load is required).
     *
     * @param loadInterval The loadInterval to set.
     */
    public void setLoadInterval(final String loadInterval)
    {
        if (StringUtils.isNotBlank(loadInterval))
        {
            this.loadInterval = new Integer(loadInterval).intValue();
        }
    }

    /**
     * The maximum number of failed model load attempts
     * that can occur before we fail.
     */
    private int maximumFailedLoadAttempts = 10;

    /**
     * Gets the maximum number of failed model load attempts
     * that can occur before we fail.
     *
     * @return Returns the maximumFailedLoadAttempts.
     */
    public int getMaximumFailedLoadAttempts()
    {
        return this.maximumFailedLoadAttempts;
    }

    /**
     * Sets the maximum number of failed model load attempts
     * that can occur before we fail.
     *
     * @param maximumFailedLoadAttempts The maximumFailedLoadAttempts to set.
     */
    public void setMaximumFailedLoadAttempts(final String maximumFailedLoadAttempts)
    {
        if (StringUtils.isNotBlank(maximumFailedLoadAttempts))
        {
            this.maximumFailedLoadAttempts = Integer.parseInt(maximumFailedLoadAttempts);
        }
    }
}