package org.andromda.translation.validation;

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

    public static boolean more(Comparable first, Comparable second)
    {
        return (first == null) ? (second == null) : (first.compareTo(second) > 0);
    }

    public static boolean more(int first, int second)
    {
        return first > second;
    }

    public static boolean more(short first, short second)
    {
        return first > second;
    }

    public static boolean more(byte first, byte second)
    {
        return first > second;
    }

    public static boolean more(char first, char second)
    {
        return first > second;
    }

    public static boolean more(float first, float second)
    {
        return first > second;
    }

    public static boolean more(double first, double second)
    {
        return first > second;
    }

    public static boolean more(long first, long second)
    {
        return first > second;
    }

    public static boolean moreOrEqual(Comparable first, Comparable second)
    {
        return (first == null) ? (second == null) : (first.compareTo(second) >= 0);
    }

    public static boolean moreOrEqual(int first, int second)
    {
        return first >= second;
    }

    public static boolean moreOrEqual(short first, short second)
    {
        return first >= second;
    }

    public static boolean moreOrEqual(byte first, byte second)
    {
        return first >= second;
    }

    public static boolean moreOrEqual(char first, char second)
    {
        return first >= second;
    }

    public static boolean moreOrEqual(float first, float second)
    {
        return first >= second;
    }

    public static boolean moreOrEqual(double first, double second)
    {
        return first >= second;
    }

    public static boolean moreOrEqual(long first, long second)
    {
        return first >= second;
    }

}
