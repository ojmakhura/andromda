package org.andromda.core.configuration;


/**
 * This exception is thrown when an error occurs when dealing
 * with {@link Namespaces}.
 *
 * @author Chad Brandon
 */
public class NamespacesException
    extends RuntimeException
{
    /**
     * Constructor for NamespacesException.
     */
    public NamespacesException()
    {
        super();
    }

    /**
     * Constructor for NamespacesException.
     *
     * @param message
     */
    public NamespacesException(final String message)
    {
        super(message);
    }

    /**
     * Constructor for NamespacesException.
     *
     * @param message
     * @param parent
     */
    public NamespacesException(
        final String message,
        final Throwable parent)
    {
        super(message, parent);
    }

    /**
     * Constructor for NamespacesException.
     *
     * @param parent
     */
    public NamespacesException(final Throwable parent)
    {
        super(parent);
    }
}