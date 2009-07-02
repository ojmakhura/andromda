package org.andromda.core.translation;


/**
 * Any unchecked exception that will be thrown when an Expression exception occurs.
 */
public class ExpressionException
    extends RuntimeException
{
    /**
     * Constructs an instance of ExpressionException.
     *
     * @param parent the parent exception
     */
    public ExpressionException(Throwable parent)
    {
        super(parent);
    }

    /**
     * Constructs an instance of ExpressionException.
     *
     * @param message the exception message
     */
    public ExpressionException(String message)
    {
        super(message);
    }

    /**
     * Constructs an instance of ExpressionException.
     *
     * @param message the exception message
     * @param parent the parent exception
     */
    public ExpressionException(
        String message,
        Throwable parent)
    {
        super(message, parent);
    }
}