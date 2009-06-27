package org.andromda.utils.beans;

import java.util.Collection;

import org.andromda.core.common.ClassUtils;
import org.andromda.core.common.ExceptionUtils;


/**
 * Used to contain sort criteria.
 *
 * @author Chad Brandon
 */
public class SortCriteria
{
    /**
     * Creates a SortCriteria object with the default ascending ordering and nulls
     * placed first.
     *
     * @param sortBy the property to sort by, this can
     *        be nested (i.e. person.name.firstName will be sorted).
     */
    public SortCriteria(final String sortBy)
    {
        this(sortBy, true);
    }

    /**
     * Creates a new instance of this SortCriteria class.
     *
     * @param sortBy the property to sort by, this can
     *        be nested (i.e. person.name.firstName will be sorted).
     * @param nullsFirst whether or not nulls will be placed first or last when sorting occurs.
     */
    public SortCriteria(
        final String sortBy,
        final boolean nullsFirst)
    {
        this(sortBy, Ordering.ASCENDING, nullsFirst);
    }

    /**
     * Creates a new instance of this SortCriteria class.
     *
     * @param sortBy the property to sort by, this can
     *        be nested (i.e. person.name.firstName will be sorted).
     * @param ordering the ordering to sort by: {@link Ordering#ASCENDING} or {@link Ordering#DESCENDING}.
     */
    public SortCriteria(
        final String sortBy,
        final Ordering ordering)
    {
        this(sortBy, ordering, true);
    }

    /**
     * Creates a new instance of this SortCriteria class.
     *
     * @param sortBy the property to sort by, this can
     *        be nested (i.e. person.name.firstName will be sorted).
     * @param ordering the ordering to sort by: {@link Ordering#ASCENDING} or {@link Ordering#DESCENDING}.
     * @param nullsFirst whether or not nulls will be placed first or last when sorting occurs.
     */
    public SortCriteria(
        final String sortBy,
        final Ordering ordering,
        final boolean nullsFirst)
    {
        ExceptionUtils.checkEmpty(
            "sortBy",
            sortBy);
        try
        {
            if (ordering != null)
            {
                final Collection validOrderings = ClassUtils.getStaticFieldValues(
                        String.class,
                        SortCriteria.class);
                if (validOrderings.contains(ordering))
                {
                    throw new IllegalArgumentException("ordering must be of one of the following types: " +
                        validOrderings);
                }
            }
        }
        catch (final Throwable throwable)
        {
            throw new SortException(throwable);
        }
        this.sortBy = sortBy;
        this.ordering = ordering;
        this.nullsFirst = nullsFirst;
    }

    /**
     * The ordering by which sorting shall occur.
     */
    private Ordering ordering;

    /**
     * Gets the current ordering to be used.
     *
     * @return Ordering
     */
    public Ordering getOrdering()
    {
        return ordering;
    }

    /**
     * Sets the ordering to use for sorting.
     *
     * @param ordering the ordering.
     */
    public void setOrdering(final Ordering ordering)
    {
        this.ordering = ordering;
    }

    /**
     * Stores the name of the property to sort by.
     */
    private String sortBy;

    /**
     * Gets the sort by name.
     *
     * @return String
     */
    public String getSortBy()
    {
        return sortBy;
    }

    /**
     * Sets the name of the property by which to sort.
     *
     * @param sortBy the name of the property by which to sort.
     */
    public void setSortBy(final String sortBy)
    {
        this.sortBy = sortBy;
    }

    private boolean nullsFirst;

    /**
     * @return the nullsFirst
     */
    public boolean isNullsFirst()
    {
        return nullsFirst;
    }

    /**
     * @param nullsFirst the nullsFirst to set
     */
    public void setNullsFirst(boolean nullsFirst)
    {
        this.nullsFirst = nullsFirst;
    }

    /**
     * Represents the types of ordering that may occur when sorting
     * with the {@link BeanSorter}.
     *
     * @author Chad Brandon
     */
    public static final class Ordering
    {
        /**
         * Indicates sorting should be performed <em>ascending</em>.
         */
        public static final Ordering ASCENDING = new Ordering("ASCENDING");

        /**
         * Indicates sorting should be performed <em>descending</em>.
         */
        public static final Ordering DESCENDING = new Ordering("DESCENDING");

        /**
         * The actual value of the enumeration.
         */
        private String value;

        private Ordering(final String ordering)
        {
            this.value = ordering;
        }

        /**
         * @see java.lang.Object#toString()
         */
        public String toString()
        {
            return this.value;
        }
    }
}