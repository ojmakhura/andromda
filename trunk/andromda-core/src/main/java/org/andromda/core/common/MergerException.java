package org.andromda.core.common;


/**
 * Wraps any exception that occurs when merging.
 *
 * @see org.andromda.core.common.Merger
 */
public class MergerException
    extends RuntimeException
{
    /**
     * Constructs an instance of MergerException.
     *
     * @param parent the parent throwable
     */
    public MergerException(Throwable parent)
    {
        super(parent);
    }

    /**
     * Constructs an instance of MergerException.
     *
     * @param message the exception message
     */
    public MergerException(String message)
    {
        super(message);
    }

    /**
     * Constructs an instance of MergerException.
     *
     * @param message the exception message
     * @param parent the parent throwable
     */
    public MergerException(
        String message,
        Throwable parent)
    {
        super(message, parent);
    }
}