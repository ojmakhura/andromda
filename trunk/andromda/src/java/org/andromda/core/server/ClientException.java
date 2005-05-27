package org.andromda.core.server;


/**
 * This exception is thrown when a special situation is encountered within an
 * {@link DefaultClient} instance.
 *
 * @author Chad Brandon
 */
public class ClientException
    extends RuntimeException
{
    /**
     * Constructor for ClientClientException.
     */
    public ClientException()
    {
        super();
    }

    /**
     * Constructor for ClientClientException.
     *
     * @param message
     */
    public ClientException(String message)
    {
        super(message);
    }

    /**
     * Constructor for ClientClientException.
     *
     * @param message
     * @param parent
     */
    public ClientException(
        String message,
        Throwable parent)
    {
        super(message, parent);
    }

    /**
     * Constructor for ClientClientException.
     *
     * @param parent
     */
    public ClientException(Throwable parent)
    {
        super(parent);
    }
}