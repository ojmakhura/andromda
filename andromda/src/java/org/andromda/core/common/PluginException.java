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
     */
    public PluginException()
    {
        super();
    }

    /**
     * Constructor for PluginException.
     *
     * @param message
     */
    public PluginException(String message)
    {
        super(message);
    }

    /**
     * Constructor for PluginException.
     *
     * @param message
     * @param parent
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
     * @param message
     */
    public PluginException(Throwable message)
    {
        super(message);
    }
}