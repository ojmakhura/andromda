package org.atl.engine.repositories.mdr4atl;

/**
 * An exception thrown when an invalid state has occured in an {@link ASMMDRModel}
 * instance.
 * 
 * @author Chad Brandon
 */
public class ASMMDRModelException
    extends RuntimeException
{
    /**
     * Constructor for ServerServerException.
     *
     * @param message
     */
    public ASMMDRModelException(String message)
    {
        super(message);
    }

    /**
     * Constructor for ServerServerException.
     *
     * @param message
     * @param parent
     */
    public ASMMDRModelException(
        String message,
        Throwable parent)
    {
        super(message, parent);
    }

    /**
     * Constructor for ServerServerException.
     *
     * @param parent
     */
    public ASMMDRModelException(Throwable parent)
    {
        super(parent);
    }
}
