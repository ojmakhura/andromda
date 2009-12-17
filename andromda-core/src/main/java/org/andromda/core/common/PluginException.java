package org.andromda.core.common;


/**
 * This exception is thrown when an unexpected exception occurs when plugin processing occurs.
 *
 * @author Chad Brandon
 */
public class PluginException
    extends RuntimeException
{
    /**
     * Constructor for PluginException.
     *
     * @param message the exception message
     */
    public PluginException(String message)
    {
        super(message);
    }

    /**
     * Constructor for PluginException.
     *
     * @param message the exception message
     * @param parent the parent throwable
     */
    public PluginException(
        String message,
        Throwable parent)
    {
        super(message, parent);
    }

    /**
     * Constructor for PluginException.
     *
     * @param parent the parent throwable
     */
    public PluginException(Throwable parent)
    {
        super(parent);
    }
}