package org.andromda.core.metafacade;

/**
 * This exception should be thrown when 
 * any model validation error occurs.
 */
public class ModelValidationException extends RuntimeException
{
    /**
     * Constructs a new instance of the ModelValidationException 
     * from a <code>modelElementClass</code>, 
     * <code>modelElementName</code> and the actual <code>message</code>.
     * @param modelElementClass the class name of the model element where
     *        the validation exception occurred.
     * @param modelElementName the name of the model element to validate.
     * @param message the message to give.
     */
    public ModelValidationException(Class modelElementClass, String modelElementName, String message)
    {
        super(modelElementClass.getName() 
            + " [" + modelElementName 
            + "] : " + message);
    }
}
