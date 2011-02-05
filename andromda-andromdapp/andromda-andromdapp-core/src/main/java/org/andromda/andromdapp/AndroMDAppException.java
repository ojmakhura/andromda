package org.andromda.andromdapp;

/**
 * Thrown when an unexpected occur occurs during AndroMDApp execution.
 * 
 * @author Chad Brandon
 */
public class AndroMDAppException
    extends RuntimeException
{
    private static final long serialVersionUID = 34L;
    /**
     * Constructs an instance of AndroMDAppException.
     *
     * @param parent previous exception
     */
    public AndroMDAppException(Throwable parent)
    {
        super(parent);
    }

    /**
     * Constructs an instance of AndroMDAppException.
     *
     * @param message exception message
     */
    public AndroMDAppException(String message)
    {
        super(message);
    }

    /**
     * Constructs an instance of AndroMDAppException.
     *
     * @param message exception message
     * @param parent previous exception
     */
    public AndroMDAppException(
        String message,
        Throwable parent)
    {
        super(message, parent);
    }
}
