package org.andromda.core.metafacade;

/**
 * This exception should be thrown when any model validation error
 * occurs.
 */
public class ModelValidationException extends Exception
{
    public ModelValidationException(String modelElementClassName, String modelElementName, String message)
    {
        super(modelElementClassName + " [" + modelElementName + "] : " + message);
    }
}
