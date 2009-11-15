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
    public BeanComparatorException(final Throwable throwable)
    {
        super(throwable);
    }

    public BeanComparatorException(final String message)
    {
        super(message);
    }

    public BeanComparatorException(
        final String message,
        final Throwable throwable)
    {
        super(message, throwable);
    }
}