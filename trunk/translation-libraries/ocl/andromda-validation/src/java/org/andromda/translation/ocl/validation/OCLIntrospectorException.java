package org.andromda.translation.ocl.validation;

/**
 * Thrown when any unexpected error occurs during execution of the
 * Introspector.
 * 
 * @author Chad Brandon
 */
public class OCLIntrospectorException
    extends RuntimeException
{
    /**
     * Constructs an instance of OCLIntrospectorException taking the
     * <code>parent</code> Throwable.
     * 
     * @param parent the cause of the exception
     */
    public OCLIntrospectorException(
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
    public OCLIntrospectorException(
        String message)
    {
        super(message);
    }
}
