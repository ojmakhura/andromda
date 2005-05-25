package org.andromda.core;


/**
 * The exception thrown when model validation fails.
 */
public class ModelValidationException
    extends RuntimeException
{
    /**
     * Constructs an instance of ModelValidationException.
     *
     * @param message
     */
    public ModelValidationException(String message)
    {
        super(message);
    }
}