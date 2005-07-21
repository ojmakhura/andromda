package org.andromda.transformers.atl;

/**
 * An exception thrown when an unexpected transformer error occurs.
 * 
 * @author Chad Brandon
 */
public class TransformerException
    extends RuntimeException
{
    /**
     * Constructs an instance of TransformerException.
     *
     * @param parent the parent throwable
     */
    public TransformerException(Throwable parent)
    {
        super(parent);
    }

    /**
     * Constructs an instance of TransformerException.
     *
     * @param message any message to give
     */
    public TransformerException(String message)
    {
        super(message);
    }

    /**
     * Constructs an instance of TransformerException.
     *
     * @param message any message to give.
     * @param parent the parent throwab.e
     */
    public TransformerException(
        String message,
        Throwable parent)
    {
        super(message, parent);
    }
}