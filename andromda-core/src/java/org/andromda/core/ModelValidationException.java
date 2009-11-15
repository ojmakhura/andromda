package org.andromda.core;


/**
 * The exception thrown when model validation fails.
 *
 * @author Chad Brandon
 */
public class ModelValidationException
    extends RuntimeException
{
    /**
     * Constructs an instance of ModelValidationException.
     *
     * @param message the validation message indicating the error.
     */
    public ModelValidationException(final String message)
    {
        super(message);
    }
}