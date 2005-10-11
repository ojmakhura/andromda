package org.andromda.core.server;


/**
 * This exception is thrown when a special situation is encountered within an
 * {@link DefaultServer} instance.
 *
 * @author Chad Brandon
 */
public class ServerException
    extends RuntimeException
{
    /**
     * Constructor for ServerServerException.
     *
     * @param message
     */
    public ServerException(String message)
    {
        super(message);
    }

    /**
     * Constructor for ServerServerException.
     *
     * @param message
     * @param parent
     */
    public ServerException(
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
    public ServerException(Throwable parent)
    {
        super(parent);
    }
}