package org.andromda.core.common;


/**
 * Any unchecked exception that will be thrown during the execution of the PluginDiscoverer
 */
public class PluginDiscovererException
    extends RuntimeException
{
    /**
     * Constructs an instance of PluginDiscovererException.
     *
     * @param parent
     */
    public PluginDiscovererException(Throwable parent)
    {
        super(parent);
    }

    /**
     * Constructs an instance of PluginDiscovererException.
     *
     * @param message
     */
    public PluginDiscovererException(String message)
    {
        super(message);
    }

    /**
     * Constructs an instance of PluginDiscovererException.
     *
     * @param message
     * @param parent
     */
    public PluginDiscovererException(
        String message,
        Throwable parent)
    {
        super(message, parent);
    }
}