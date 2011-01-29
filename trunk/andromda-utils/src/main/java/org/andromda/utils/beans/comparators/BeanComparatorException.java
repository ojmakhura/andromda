package org.andromda.utils.beans.comparators;

import org.andromda.utils.beans.BeanSorter;

/**
 * Thrown when an unexpected error occurs during {@link BeanSorter}
 * execution.
 *
 * @author Chad Brandon
 */
public class BeanComparatorException
    extends RuntimeException
{
    private static final long serialVersionUID = 34L;

    /**
     * @param throwable
     */
    public BeanComparatorException(final Throwable throwable)
    {
        super(throwable);
    }

    /**
     * @param message
     */
    public BeanComparatorException(final String message)
    {
        super(message);
    }

    /**
     * @param message
     * @param throwable
     */
    public BeanComparatorException(
        final String message,
        final Throwable throwable)
    {
        super(message, throwable);
    }
}