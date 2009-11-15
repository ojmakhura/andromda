package org.andromda.core.repository;


/**
 * An exception thrown whenever an unexpected occurs while configuring
 * a repository instance.
 *
 * @author Chad Brandon
 * @author Bob Fields
 */
public final class RepositoryException
    extends RuntimeException
{
    /**
     * Constructor for the RepositoryException object
     *
     * @param message describes cause of the exception
     */
    public RepositoryException(String message)
    {
        super(message);
    }

    /**
     * Constructor for the RepositoryException object
     *
     * @param parent describes cause of the exception
     */
    public RepositoryException(final Throwable parent)
    {
        super(parent);
    }

    /**
     * Constructor for the RepositoryException object
     *
     * @param message describes cause of the exception
     * @param cause original exception that caused this exception
     */
    public RepositoryException(
        String message,
        Throwable cause)
    {
        super(message, cause);
    }
}