package org.andromda.utils.beans.comparators;

/**
 * An exception thrown during execution of a comparator.
 */
public class ComparatorException
    extends RuntimeException
{
    private static final long serialVersionUID = 34L;

    /**
     * @param throwable
     */
    public ComparatorException(final Throwable throwable)
    {
        super(throwable);
    }

    /**
     * @param msg
     */
    public ComparatorException(String msg)
    {
        super(msg);
    }

    /**
     * @param message
     * @param throwable
     */
    public ComparatorException(
        final String message,
        final Throwable throwable)
    {
        super(message, throwable);
    }
}