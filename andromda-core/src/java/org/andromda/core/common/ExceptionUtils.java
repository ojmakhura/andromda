package org.andromda.core.common;

import org.apache.commons.lang.StringUtils;


/**
 * Contains Exception handling utilities.
 *
 * @author Chad Brandon
 */
public class ExceptionUtils
{
    /**
     * Checks if the argument is null, and if so, throws an IllegalArgumentException, does nothing if not.
     *
     * @param methodExecuteName the name of the method we are currently executing
     * @param argumentName the name of the argument we are checking for null
     * @param argument the argument we are checking
     * @deprecated used {@link #checkNull(String, Object)} instead since we can detect the method name.
     */
    public static void checkNull(
        final String methodExecuteName,
        final String argumentName,
        final Object argument)
    {
        checkNull(
            argumentName,
            argument,
            3);
    }

    /**
     * Checks if the argument is null, and if so, throws an IllegalArgumentException, does nothing if not.
     *
     * @param argumentName the name of the argument we are checking for null
     * @param argument the argument we are checking
     */
    public static void checkNull(
        final String argumentName,
        final Object argument)
    {
        checkNull(
            argumentName,
            argument,
            3);
    }

    /**
     * Checks if the argument is null, and if so, throws an IllegalArgumentException, does nothing if not.
     *
     * @param argumentName the name of the argument we are checking for null
     * @param argument the argument we are checking
     * @param stackDepth the depth of the stack from which to retrieve the methodInformation.
     */
    private static void checkNull(
        final String argumentName,
        final Object argument,
        final int stackDepth)
    {
        if (StringUtils.isEmpty(argumentName))
        {
            throw new IllegalArgumentException("'argumentName' can not be null or an empty String");
        }

        if (argument == null)
        {
            throw new IllegalArgumentException(getMethodName(stackDepth) + " - '" + argumentName + "' can not be null");
        }
    }

    /**
     * Checks if the argument is null or an empty String throws an IllegalArgumentException if it is, does nothing if
     * not.
     *
     * @param methodExecuteName the name of the method we are currently executing
     * @param argumentName the name of the argument we are checking for null
     * @param argument the argument we are checking
     * @deprecated use {@link #checkEmpty(String, String)} instead since we can detect the method name.
     */
    public static void checkEmpty(
        final String methodExecuteName,
        final String argumentName,
        final String argument)
    {
        checkEmpty(
            argumentName,
            argument,
            3);
    }

    /**
     * Checks if the argument is null or an empty String throws an IllegalArgumentException if it is, does nothing if
     * not.
     *
     * @param argumentName the name of the argument we are checking for null
     * @param argument the argument we are checking
     */
    public static void checkEmpty(
        final String argumentName,
        final String argument)
    {
        checkEmpty(
            argumentName,
            argument,
            3);
    }

    /**
     * Checks if the argument is null or an empty String throws an IllegalArgumentException if it is, does nothing if
     * not.
     *
     * @param argumentName the name of the argument we are checking for null
     * @param argument the argument we are checking
     * @param stackDepth the depth of the stack from which to retrieve the methodInformation.
     */
    private static void checkEmpty(
        final String argumentName,
        final String argument,
        final int stackDepth)
    {
        if (StringUtils.isEmpty(argumentName))
        {
            throw new IllegalArgumentException("'argumentName' can not be null or an empty String");
        }
        if (StringUtils.isEmpty(argument))
        {
            throw new IllegalArgumentException(getMethodName(stackDepth) + " - '" + argumentName +
                "' can not be null or an empty String");
        }
    }

    /**
     * Checks if the argumentClass is assignable to assignableToClass, and if not throws an IllegalArgumentException,
     * otherwise does nothing.
     *
     * @param methodExecuteName the method name of the method, this method is being executed within
     * @param assignableToClass the Class that argumentClass must be assignable to
     * @param argumentClass the argumentClass we are checking
     * @param argumentName the name of the argument we are checking
     * @deprecated use {@link #checkAssignable(Class, String, Class)} since we can detect the method name.
     */
    public static void checkAssignable(
        final String methodExecuteName,
        final Class assignableToClass,
        final String argumentName,
        final Class argumentClass)
    {
        checkAssignable(
            assignableToClass,
            argumentName,
            argumentClass,
            3);
    }

    /**
     * Checks if the argumentClass is assignable to assignableToClass, and if not throws an IllegalArgumentException,
     * otherwise does nothing.
     *
     * @param assignableToClass the Class that argumentClass must be assignable to
     * @param argumentClass the argumentClass we are checking
     * @param argumentName the name of the argument we are checking
     */
    public static void checkAssignable(
        final Class assignableToClass,
        final String argumentName,
        final Class argumentClass)
    {
        checkAssignable(
            assignableToClass,
            argumentName,
            argumentClass,
            3);
    }

    /**
     * Checks if the argumentClass is assignable to assignableToClass, and if not throws an IllegalArgumentException,
     * otherwise does nothing.
     *
     * @param assignableToClass the Class that argumentClass must be assignable to
     * @param argumentClass the argumentClass we are checking
     * @param argumentName the name of the argument we are checking
     * @param stackDepth the depth of the stack from which to retrieve the method information.
     */
    private static void checkAssignable(
        final Class assignableToClass,
        final String argumentName,
        final Class argumentClass,
        final int stackDepth)
    {
        if (assignableToClass == null)
        {
            throw new IllegalArgumentException("'assignableToClass' can not be null");
        }
        if (argumentClass == null)
        {
            throw new IllegalArgumentException("'argumentClass' can not be null");
        }
        if (StringUtils.isEmpty(argumentName))
        {
            throw new IllegalArgumentException("'argumentName can not be null or an empty String");
        }

        // this is what the method is for
        if (!assignableToClass.isAssignableFrom(argumentClass))
        {
            throw new IllegalArgumentException(getMethodName(stackDepth) + " - '" + argumentName + "' class --> '" +
                argumentClass + "' must be assignable to class --> '" + assignableToClass + "'");
        }
    }

    /**
     * Attempts to retrieve the root cause of the exception, if it can not be
     * found, the <code>throwable</code> itself is returned.
     *
     * @param throwable the exception from which to retrieve the root cause.
     * @return the root cause of the exception
     */
    public static Throwable getRootCause(Throwable throwable)
    {
        final Throwable root = org.apache.commons.lang.exception.ExceptionUtils.getRootCause(throwable);
        if (root != null)
        {
            throwable = root;
        }
        return throwable;
    }

    /**
     * Gets the appropriate method name for the method being checked.
     *
     * @return the name of the method.
     */
    private static String getMethodName(int stackDepth)
    {
        String methodName = null;
        final Throwable throwable = new Throwable();
        final StackTraceElement[] stack = throwable.getStackTrace();
        if (stack.length >= stackDepth)
        {
            final StackTraceElement element = stack[stackDepth];
            methodName = element.getClassName() + '.' + element.getMethodName();
        }
        return methodName;
    }
}