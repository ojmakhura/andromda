package org.andromda.translation.ocl.validation;

import org.apache.commons.lang.BooleanUtils;


/**
 * Used to translate OCL expressions to their corresponding Java expressions.
 */
public final class OCLExpressions
{
    public static boolean equal(
        final Object first,
        final Object second)
    {
        return (first == null) ? (second == null) : first.equals(second);
    }

    public static boolean equal(
        final boolean first,
        final boolean second)
    {
        return first == second;
    }

    public static boolean equal(
        final int first,
        final int second)
    {
        return first == second;
    }

    public static boolean equal(
        final short first,
        final short second)
    {
        return first == second;
    }

    public static boolean equal(
        final byte first,
        final byte second)
    {
        return first == second;
    }

    public static boolean equal(
        final char first,
        final char second)
    {
        return first == second;
    }

    public static boolean equal(
        final float first,
        final float second)
    {
        return first == second;
    }

    public static boolean equal(
        final double first,
        final double second)
    {
        return first == second;
    }

    public static boolean equal(
        final long first,
        final long second)
    {
        return first == second;
    }

    public static boolean equal(
        final Object first,
        final boolean second)
    {
        return (second) ? Boolean.TRUE.equals(first) || BooleanUtils.toBoolean(String.valueOf(first))
                        : Boolean.FALSE.equals(first) || !BooleanUtils.toBoolean(String.valueOf(first));
    }

    public static boolean equal(
        final boolean first,
        final Object second)
    {
        return equal(second, first);
    }

    public static boolean notEqual(
        final Object first,
        final Object second)
    {
        return first == null ? second != null : !first.equals(second);
    }

    public static boolean notEqual(
        final boolean first,
        final boolean second)
    {
        return first != second;
    }

    public static boolean equal(
        final Object first,
        final long second)
    {
        boolean equal = first instanceof Comparable;
        if (equal)
        {
            final Comparable comparable = (Comparable)first;
            equal = comparable.equals(new Long(second));
        }
        return equal;
    }

    public static boolean notEqual(
        final int first,
        final int second)
    {
        return first != second;
    }

    public static boolean notEqual(
        final short first,
        final short second)
    {
        return first != second;
    }

    public static boolean notEqual(
        final byte first,
        final byte second)
    {
        return first != second;
    }

    public static boolean notEqual(
        final char first,
        final char second)
    {
        return first != second;
    }

    public static boolean notEqual(
        final float first,
        final float second)
    {
        return first != second;
    }

    public static boolean notEqual(
        final double first,
        final double second)
    {
        return first != second;
    }

    public static boolean notEqual(
        final long first,
        final long second)
    {
        return first != second;
    }

    public static boolean notEqual(
        final Object first,
        final boolean second)
    {
        return !equal(first, second);
    }

    public static boolean notEqual(
        final boolean first,
        final Object second)
    {
        return notEqual(second, first);
    }

    public static boolean less(
        final Comparable first,
        final Comparable second)
    {
        return first == null ? second == null : first.compareTo(second) < 0;
    }

    public static boolean less(
        final int first,
        final int second)
    {
        return first < second;
    }

    public static boolean less(
        final short first,
        final short second)
    {
        return first < second;
    }

    public static boolean less(
        final byte first,
        final byte second)
    {
        return first < second;
    }

    public static boolean less(
        final char first,
        final char second)
    {
        return first < second;
    }

    public static boolean less(
        final float first,
        final float second)
    {
        return first < second;
    }

    public static boolean less(
        final double first,
        final double second)
    {
        return first < second;
    }

    public static boolean less(
        final long first,
        final long second)
    {
        return first < second;
    }

    public static boolean lessOrEqual(
        final Comparable first,
        final Comparable second)
    {
        return first == null ? second == null : (first.compareTo(second) <= 0);
    }

    public static boolean lessOrEqual(
        final int first,
        final int second)
    {
        return first <= second;
    }

    public static boolean lessOrEqual(
        final short first,
        final short second)
    {
        return first <= second;
    }

    public static boolean lessOrEqual(
        final byte first,
        final byte second)
    {
        return first <= second;
    }

    public static boolean lessOrEqual(
        final char first,
        final char second)
    {
        return first <= second;
    }

    public static boolean lessOrEqual(
        final float first,
        final float second)
    {
        return first <= second;
    }

    public static boolean lessOrEqual(
        final double first,
        final double second)
    {
        return first <= second;
    }

    public static boolean lessOrEqual(
        final long first,
        final long second)
    {
        return first <= second;
    }

    public static boolean greater(
        final Comparable first,
        final Comparable second)
    {
        return first == null ? second == null : first.compareTo(second) > 0;
    }

    public static boolean greater(
        final Object first,
        final long second)
    {
        boolean greater = first != null && Comparable.class.isAssignableFrom(first.getClass());
        if (greater)
        {
            final Comparable comparable = (Comparable)first;
            greater = comparable.compareTo(new Long(second)) > 0;
        }
        return greater;
    }

    public static boolean greater(
        final int first,
        final int second)
    {
        return first > second;
    }

    public static boolean greater(
        final short first,
        final short second)
    {
        return first > second;
    }

    public static boolean greater(
        final byte first,
        final byte second)
    {
        return first > second;
    }

    public static boolean greater(
        final char first,
        final char second)
    {
        return first > second;
    }

    public static boolean greater(
        final float first,
        final float second)
    {
        return first > second;
    }

    public static boolean greater(
        final double first,
        final double second)
    {
        return first > second;
    }

    public static boolean greater(
        final long first,
        final long second)
    {
        return first > second;
    }

    public static boolean greaterOrEqual(
        final Comparable first,
        final Comparable second)
    {
        return first == null ? second == null : first.compareTo(second) >= 0;
    }

    public static boolean greaterOrEqual(
        final int first,
        final int second)
    {
        return first >= second;
    }

    public static boolean greaterOrEqual(
        final short first,
        final short second)
    {
        return first >= second;
    }

    public static boolean greaterOrEqual(
        final byte first,
        final byte second)
    {
        return first >= second;
    }

    public static boolean greaterOrEqual(
        final char first,
        final char second)
    {
        return first >= second;
    }

    public static boolean greaterOrEqual(
        final float first,
        final float second)
    {
        return first >= second;
    }

    public static boolean greaterOrEqual(
        final double first,
        final double second)
    {
        return first >= second;
    }

    public static boolean greaterOrEqual(
        final long first,
        final long second)
    {
        return first >= second;
    }
}