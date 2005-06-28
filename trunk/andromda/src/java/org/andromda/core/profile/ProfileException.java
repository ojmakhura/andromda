package org.andromda.core.profile;


/**
 * This exception is thrown when an unexpected exception occurs when plugin processing occurs.
 *
 * @author Chad Brandon
 */
public class ProfileException
    extends RuntimeException
{
    /**
     * Constructor for ProfileException.
     */
    public ProfileException()
    {
        super();
    }

    /**
     * Constructor for ProfileException.
     *
     * @param message
     */
    public ProfileException(String message)
    {
        super(message);
    }

    /**
     * Constructor for ProfileException.
     *
     * @param message
     * @param parent
     */
    public ProfileException(
        String message,
        Throwable parent)
    {
        super(message, parent);
    }

    /**
     * Constructor for ProfileException.
     *
     * @param message
     */
    public ProfileException(Throwable message)
    {
        super(message);
    }
}