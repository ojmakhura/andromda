package org.andromda.cartridges.jsf.validator;

/**
 * An exception thrown when a validation configuration error occurs.
 *
 * @author Chad Brandon
 */
public class JSFValidatorException
    extends RuntimeException
{

    /**
     * Constructor for JSFValidatorException.
     *
     * @param message
     */
    public JSFValidatorException(String message)
    {
        super(message);
    }

    /**
     * Constructor for JSFValidatorException.
     *
     * @param message
     * @param parent
     */
    public JSFValidatorException(
        String message,
        Throwable parent)
    {
        super(message, parent);
    }

    /**
     * Constructor for JSFValidatorException.
     *
     * @param message
     */
    public JSFValidatorException(Throwable message)
    {
        super(message);
    }
}
