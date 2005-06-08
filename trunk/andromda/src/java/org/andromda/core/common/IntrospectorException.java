package org.andromda.core.common;

/**
 * Thrown when any unexpected error occurs during execution of the
 * OCLIntrospector.
 * 
 * @author Chad Brandon
 */
public class IntrospectorException
    extends RuntimeException
{
    /**
     * Constructs an instance of OCLIntrospectorException taking the
     * <code>parent</code> Throwable.
     * 
     * @param parent the cause of the exception
     */
    public IntrospectorException(
        Throwable parent)
    {
        super(parent);
    }

    /**
     * Constructs an instance of OCLIntrospectorException taking the
     * <code>message</code> String.
     * 
     * @param message the message to include in the exception.
     */
    public IntrospectorException(
        String message)
    {
        super(message);
    }
}
