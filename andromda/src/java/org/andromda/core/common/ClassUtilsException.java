package org.andromda.core.common;

/**
 * Any unchecked exception that will be thrown during the execution of
 * ClassUtils.
 */
public class ClassUtilsException
    extends RuntimeException
{

    /**
     * Constructs an instance of ClassUtilsException.
     * 
     * @param th
     */
    public ClassUtilsException(
        Throwable th)
    {
        super(th);
    }

    /**
     * Constructs an instance of ClassUtilsException.
     * 
     * @param msg
     */
    public ClassUtilsException(
        String msg)
    {
        super(msg);
    }

    /**
     * Constructs an instance of ClassUtilsException.
     * 
     * @param msg
     * @param th
     */
    public ClassUtilsException(
        String msg,
        Throwable th)
    {
        super(msg, th);
    }

}