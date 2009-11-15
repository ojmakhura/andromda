package org.andromda.core.configuration;


/**
 * This exception is thrown when an error occurs during AndroMDA configuration.
 *
 * @author Chad Brandon
 */
public class ConfigurationException
    extends RuntimeException
{
    /**
     * Constructor for ConfigurationException.
     */
    public ConfigurationException()
    {
        super();
    }

    /**
     * Constructor for ConfigurationException.
     *
     * @param message
     */
    public ConfigurationException(String message)
    {
        super(message);
    }

    /**
     * Constructor for ConfigurationException.
     *
     * @param message
     * @param parent
     */
    public ConfigurationException(
        String message,
        Throwable parent)
    {
        super(message, parent);
    }

    /**
     * Constructor for ConfigurationException.
     *
     * @param parent
     */
    public ConfigurationException(Throwable parent)
    {
        super(parent);
    }
}