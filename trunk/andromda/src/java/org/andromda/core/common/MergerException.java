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
     * @param parent
     */
    public MergerException(Throwable parent)
    {
        super(parent);
    }

    /**
     * Constructs an instance of MergerException.
     *
     * @param message
     */
    public MergerException(String message)
    {
        super(message);
    }

    /**
     * Constructs an instance of MergerException.
     *
     * @param message
     * @param parent
     */
    public MergerException(
        String message,
        Throwable parent)
    {
        super(message, parent);
    }
}