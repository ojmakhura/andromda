package org.andromda.core.common;

/**
 * This exception is thrown when an unexpected exception
 * occurs when plugin processing occurs.
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
     * @param arg0
     */
    public PluginException(
        String arg0)
    {
        super(arg0);
    }

    /**
     * Constructor for PluginException.
     * 
     * @param arg0
     * @param arg1
     */
    public PluginException(
        String arg0,
        Throwable arg1)
    {
        super(arg0, arg1);
    }

    /**
     * Constructor for PluginException.
     * 
     * @param arg0
     */
    public PluginException(
        Throwable arg0)
    {
        super(arg0);
    }

}