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
    public AndroMDAppException(final Throwable parent)
    {
        super(parent);
    }

    /**
     * Constructs an instance of AndroMDAppException.
     *
     * @param message exception message
     */
    public AndroMDAppException(final String message)
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
        final String message,
        final Throwable parent)
    {
        super(message, parent);
    }
}
