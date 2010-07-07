package org.andromda.utils.beans;

/**
 * Thrown when an unexpected exception occurs during sorting.
 *
 * @author Chad Brandon
 */
public class SortException
    extends RuntimeException
{
    /**
     * @param throwable
     */
    public SortException(final Throwable throwable)
    {
        super(throwable);
    }

    /**
     * @param message
     */
    public SortException(final String message)
    {
        super(message);
    }

    /**
     * @param message
     * @param throwable
     */
    public SortException(
        final String message,
        final Throwable throwable)
    {
        super(message, throwable);
    }
}