package org.andromda.core.common;


/**
 * Any unchecked exception that will be thrown during the execution of ClassUtils.
 */
public class ClassUtilsException
    extends RuntimeException
{
    /**
     * Constructs an instance of ClassUtilsException.
     *
     * @param parent the parent throwable
     */
    public ClassUtilsException(Throwable parent)
    {
        super(parent);
    }

    /**
     * Constructs an instance of ClassUtilsException.
     *
     * @param message the exception message
     */
    public ClassUtilsException(String message)
    {
        super(message);
    }

    /**
     * Constructs an instance of ClassUtilsException.
     *
     * @param message the exception message
     * @param parent the parent throwable
     */
    public ClassUtilsException(
        String message,
        Throwable parent)
    {
        super(message, parent);
    }
}