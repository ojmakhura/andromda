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
     * @param parent
     */
    public ClassUtilsException(Throwable parent)
    {
        super(parent);
    }

    /**
     * Constructs an instance of ClassUtilsException.
     *
     * @param message
     */
    public ClassUtilsException(String message)
    {
        super(message);
    }

    /**
     * Constructs an instance of ClassUtilsException.
     *
     * @param message
     * @param parent
     */
    public ClassUtilsException(
        String message,
        Throwable parent)
    {
        super(message, parent);
    }
}