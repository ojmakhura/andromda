package org.andromda.utils.beans.comparators;

import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Comparator;
import java.util.Properties;

import org.andromda.core.common.ClassUtils;
import org.andromda.core.common.ExceptionUtils;
import org.andromda.core.common.Introspector;
import org.andromda.utils.beans.BeanSorter;
import org.andromda.utils.beans.SortCriteria;
import org.apache.commons.lang.StringUtils;


/**
 * Used by BeanSorter to provide sorting capabilities for
 * beans.  Currently supports sorting by all simple properties
 *
 * @see BeanSorter
 *
 * @author Chad Brandon
 */
public class BeanComparator
    implements Comparator,
        Serializable
{
    /**
     * Stores the comparator mappings.
     */
    private static final Properties comparators = new Properties();

    static
    {
        try
        {
            final String comparatorsFile = "Comparators.properties";
            final URL comparatorsUri = BeanComparator.class.getResource(comparatorsFile);
            if (comparatorsUri == null)
            {
                throw new BeanComparatorException("The comparators resource '" + comparatorsFile +
                    " could not be loaded");
            }
            InputStream stream = comparatorsUri.openStream();
            comparators.load(stream);
            stream.close();
        }
        catch (final Throwable throwable)
        {
            throw new RuntimeException(throwable);
        }
    }

    private Comparator comparator = null;
    private SortCriteria sortCriteria;

    public BeanComparator(SortCriteria sortCriteria)
    {
        ExceptionUtils.checkNull(
            "sortCriteria",
            sortCriteria);
        this.sortCriteria = sortCriteria;
    }

    // - The following variables are saved since we only need to check some things once
    //   within the method and checking each time slows performance.
    private boolean assignableTypes = false;

    /**
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    public int compare(
        final Object objectA,
        Object objectB)
    {
        final String methodName = "BeanComparator.compare";
        ExceptionUtils.checkNull(
            "objectA",
            objectA);
        ExceptionUtils.checkNull(
            "objectB",
            objectB);

        // we'll assume that if the first set of objects are equal types
        // then all must be (this of course could turn out to be false, but may hinder
        // performance to check each object, each time).
        if (!assignableTypes)
        {
            if (!objectA.getClass().isInstance(objectB) && !objectB.getClass().isInstance(objectA))
            {
                String errMsg =
                    methodName + " - objectA '" + objectA + "' and objectB '" + objectB + " must be of assignable types ";
                throw new ClassCastException(errMsg);
            }
            assignableTypes = true;
        }
        try
        {
            Object aValue;
            Object bValue;
            if (this.sortCriteria.getOrdering().equals(SortCriteria.Ordering.DESCENDING))
            {
                // - since its decending switch the objects we are getting the values from,
                //   so the order will be reversed
                aValue = getProperty(
                        objectB,
                        sortCriteria.getSortBy());
                bValue = getProperty(
                        objectA,
                        sortCriteria.getSortBy());
            }
            else
            {
                // - otherwise we assume its ascending
                aValue = getProperty(
                        objectA,
                        sortCriteria.getSortBy());
                bValue = getProperty(
                        objectB,
                        sortCriteria.getSortBy());
            }

            //we default result to zero (zero result would mean aValue and bValue equal each other)
            int result = 0;

            //first sort for null values, null values will always come last
            if (aValue != null || bValue != null)
            {
                if (aValue == null)
                {
                    if (sortCriteria.isNullsFirst())
                    {
                        result = -1;
                    }
                    else
                    {
                        result = 1;
                    }
                }
                else if (bValue == null)
                {
                    if (sortCriteria.isNullsFirst())
                    {
                        result = 1;
                    }
                    else
                    {
                        result = -1;
                    }
                }
                else
                {
                    result = this.getComparator(aValue.getClass()).compare(
                        aValue,
                        bValue);
                }
            }
            return result;
        }
        catch (final Throwable throwable)
        {
            throw new ComparatorException(throwable);
        }
    }

    /**
     * Gets the property specified by propertyName from the bean.
     * Checks each nested parent to see if its null and if it is
     * returns null.
     * @param bean
     * @param propertyName
     * @return
     */
    private Object getProperty(
        final Object bean,
        String propertyName)
        throws IllegalAccessException, InvocationTargetException, NoSuchMethodException
    {
        Object value = null;
        if (bean != null && StringUtils.isNotEmpty(propertyName))
        {
            int index = getNestedIndex(propertyName);
            if (index != -1)
            {
                String simpleProp = propertyName.substring(
                        0,
                        index);
                value = Introspector.instance().getProperty(
                        bean,
                        simpleProp);
                if (value != null)
                {
                    if (getNestedIndex(propertyName) != -1)
                    {
                        propertyName = propertyName.substring(
                                index + 1,
                                propertyName.length());
                        value = getProperty(
                                value,
                                propertyName);
                    }
                }
            }
            else
            {
                value = Introspector.instance().getProperty(
                        bean,
                        propertyName);
            }
        }
        return value;
    }

    /**
     * Gets the index of the given <code>propertyName</code> if
     * its a nested property (nested meaning names separated by
     * a '.').
     * @param propertyName the name of the nested property.
     * @return the index.
     */
    private final int getNestedIndex(final String propertyName)
    {
        int index = -1;
        if (StringUtils.isNotEmpty(propertyName))
        {
            index = propertyName.indexOf('.');
        }
        return index;
    }

    /**
     * Retrieves the associated Comparator for the type
     * @param type tye class of which to retrieve the comparator.
     * @return appropriate comparator or null if one wasn't defined.
     */
    private final Comparator getComparator(final Class type)
    {
        try
        {
            if (this.comparator == null)
            {
                final String comparatorName = findComparatorName(type);
                if (StringUtils.isNotEmpty(comparatorName))
                {
                    this.comparator = (Comparator)ClassUtils.loadClass(comparatorName).newInstance();
                }
                else
                {
                    throw new ComparatorException("No comparator defined for the given type '" + type.getName() + "'");
                }
            }
            return this.comparator;
        }
        catch (final Throwable throwable)
        {
            throw new ComparatorException(throwable);
        }
    }

    private String findComparatorName(final Class type)
    {
        String comparatorName = comparators.getProperty(type.getName());
        if (StringUtils.isEmpty(comparatorName) && type.getSuperclass() != null)
        {
            comparatorName = findComparatorName(type.getSuperclass());
        }
        return comparatorName;
    }

    /**
     * Returns the current sortCriteria value
     * @return String
     */
    public SortCriteria getSortCriteria()
    {
        return this.sortCriteria;
    }

    /**
     * Sets the new sortCriteria value
     * @param string
     */
    public void setSortCriteria(final SortCriteria sortCriteria)
    {
        ExceptionUtils.checkNull(
            "sortCriteria",
            sortCriteria);
        this.sortCriteria = sortCriteria;
    }
}