package org.andromda.utils.beans.comparators;


/**
 * An exception thrown during execution of a comparator.
 */
public class ComparatorException
    extends RuntimeException
{
    public ComparatorException(final Throwable throwable)
    {
        super(throwable);
    }

    public ComparatorException(String msg)
    {
        super(msg);
    }

    public ComparatorException(
        final String message,
        final Throwable throwable)
    {
        super(message, throwable);
    }
}