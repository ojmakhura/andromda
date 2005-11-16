package org.andromda.maven.plugin.andromdapp;

/**
 * Thrown when an unexpected occur occurs during AndroMDApp execution.
 * 
 * @author Chad Brandon
 */
public class AndroMDAppException
    extends RuntimeException
{
    /**
     * Constructs an instance of AndroMDAppException.
     *
     * @param parent
     */
    public AndroMDAppException(Throwable parent)
    {
        super(parent);
    }

    /**
     * Constructs an instance of AndroMDAppException.
     *
     * @param message
     */
    public AndroMDAppException(String message)
    {
        super(message);
    }

    /**
     * Constructs an instance of AndroMDAppException.
     *
     * @param message
     * @param parent
     */
    public AndroMDAppException(
        String message,
        Throwable parent)
    {
        super(message, parent);
    }
}
