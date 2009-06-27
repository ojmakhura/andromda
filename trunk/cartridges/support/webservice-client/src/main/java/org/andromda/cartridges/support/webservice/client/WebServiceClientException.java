package org.andromda.cartridges.support.webservice.client;

/**
 * An exception thrown during an unexpected error during the {@link WebServiceClient}
 * execution.
 *
 * @author Chad Brandon
 */
public class WebServiceClientException
    extends RuntimeException
{
    /**
     * Constructs an instance of WebServiceClientException.
     *
     * @param parent
     */
    public WebServiceClientException(Throwable parent)
    {
        super(parent);
    }

    /**
     * Constructs an instance of WebServiceClientException.
     *
     * @param message
     */
    public WebServiceClientException(String message)
    {
        super(message);
    }

    /**
     * Constructs an instance of WebServiceClientException.
     *
     * @param message
     * @param parent
     */
    public WebServiceClientException(
        String message,
        Throwable parent)
    {
        super(message, parent);
    }
}