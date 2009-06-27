package org.andromda.utils.beans;


/**
 * Thrown when an unexpected exception occurs during sorting.
 *
 * @author Chad Brandon
 */
public class SortException
    extends RuntimeException
{
    public SortException(final Throwable throwable)
    {
        super(throwable);
    }

    public SortException(final String message)
    {
        super(message);
    }

    public SortException(
        final String message,
        final Throwable throwable)
    {
        super(message, throwable);
    }
}