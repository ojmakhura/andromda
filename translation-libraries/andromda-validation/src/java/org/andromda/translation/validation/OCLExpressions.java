package org.andromda.translation.validation;

import org.apache.commons.lang.BooleanUtils;

public class OCLExpressions
{
    public static boolean equal(Object first, Object second)
    {
        return (first == null) ? (second == null) : first.equals(second);
    }

    public static boolean equal(boolean first, boolean second)
    {
        return first == second;
    }

    public static boolean equal(int first, int second)
    {
        return first == second;
    }

    public static boolean equal(short first, short second)
    {
        return first == second;
    }

    public static boolean equal(byte first, byte second)
    {
        return first == second;
    }

    public static boolean equal(char first, char second)
    {
        return first == second;
    }

    public static boolean equal(float first, float second)
    {
        return first == second;
    }

    public static boolean equal(double first, double second)
    {
        return first == second;
    }

    public static boolean equal(long first, long second)
    {
        return first == second;
    }

    public static boolean equal(Object first, boolean second)
    {
        return (second)
            ? Boolean.TRUE.equals(first) || BooleanUtils.toBoolean(String.valueOf(first))
            : Boolean.FALSE.equals(first) || !BooleanUtils.toBoolean(String.valueOf(first));
    }

    public static boolean equal(boolean first, Object second)
    {
        return equal(second, first);
    }

    public static boolean notEqual(Object first, Object second)
    {
        return (first == null) ? (second != null) : !first.equals(second);
    }

    public static boolean notEqual(boolean first, boolean second)
    {
        return first != second;
    }

    public static boolean notEqual(int first, int second)
    {
        return first != second;
    }

    public static boolean notEqual(short first, short second)
    {
        return first != second;
    }

    public static boolean notEqual(byte first, byte second)
    {
        return first != second;
    }

    public static boolean notEqual(char first, char second)
    {
        return first != second;
    }

    public static boolean notEqual(float first, float second)
    {
        return first != second;
    }

    public static boolean notEqual(double first, double second)
    {
        return first != second;
    }

    public static boolean notEqual(long first, long second)
    {
        return first != second;
    }

    public static boolean notEqual(Object first, boolean second)
    {
        return !equal(first, second);
    }

    public static boolean notEqual(boolean first, Object second)
    {
        return notEqual(second, first);
    }

    public static boolean less(Comparable first, Comparable second)
    {
        return (first == null) ? (second == null) : (first.compareTo(second) < 0);
    }

    public static boolean less(int first, int second)
    {
        return first < second;
    }

    public static boolean less(short first, short second)
    {
        return first < second;
    }

    public static boolean less(byte first, byte second)
    {
        return first < second;
    }

    public static boolean less(char first, char second)
    {
        return first < second;
    }

    public static boolean less(float first, float second)
    {
        return first < second;
    }

    public static boolean less(double first, double second)
    {
        return first < second;
    }

    public static boolean less(long first, long second)
    {
        return first < second;
    }

    public static boolean lessOrEqual(Comparable first, Comparable second)
    {
        return (first == null) ? (second == null) : (first.compareTo(second) <= 0);
    }

    public static boolean lessOrEqual(int first, int second)
    {
        return first <= second;
    }

    public static boolean lessOrEqual(short first, short second)
    {
        return first <= second;
    }

    public static boolean lessOrEqual(byte first, byte second)
    {
        return first <= second;
    }

    public static boolean lessOrEqual(char first, char second)
    {
        return first <= second;
    }

    public static boolean lessOrEqual(float first, float second)
    {
        return first <= second;
    }

    public static boolean lessOrEqual(double first, double second)
    {
        return first <= second;
    }

    public static boolean lessOrEqual(long first, long second)
    {
        return first <= second;
    }

    public static boolean greater(Comparable first, Comparable second)
    {
        return (first == null) ? (second == null) : (first.compareTo(second) > 0);
    }

    public static boolean greater(int first, int second)
    {
        return first > second;
    }

    public static boolean greater(short first, short second)
    {
        return first > second;
    }

    public static boolean greater(byte first, byte second)
    {
        return first > second;
    }

    public static boolean greater(char first, char second)
    {
        return first > second;
    }

    public static boolean greater(float first, float second)
    {
        return first > second;
    }

    public static boolean greater(double first, double second)
    {
        return first > second;
    }

    public static boolean greater(long first, long second)
    {
        return first > second;
    }

    public static boolean greaterOrEqual(Comparable first, Comparable second)
    {
        return (first == null) ? (second == null) : (first.compareTo(second) >= 0);
    }

    public static boolean greaterOrEqual(int first, int second)
    {
        return first >= second;
    }

    public static boolean greaterOrEqual(short first, short second)
    {
        return first >= second;
    }

    public static boolean greaterOrEqual(byte first, byte second)
    {
        return first >= second;
    }

    public static boolean greaterOrEqual(char first, char second)
    {
        return first >= second;
    }

    public static boolean greaterOrEqual(float first, float second)
    {
        return first >= second;
    }

    public static boolean greaterOrEqual(double first, double second)
    {
        return first >= second;
    }

    public static boolean greaterOrEqual(long first, long second)
    {
        return first >= second;
    }

}
