package org.andromda.cartridges.bpm4jsf.components.validator;

/**
 * An exception thrown when a validation configuration error occurs.
 * 
 * @author Chad Brandon
 */
public class BPM4JSFValidatorException
    extends RuntimeException
{

    /**
     * Constructor for BPM4JSFValidatorException.
     *
     * @param message
     */
    public BPM4JSFValidatorException(String message)
    {
        super(message);
    }

    /**
     * Constructor for BPM4JSFValidatorException.
     *
     * @param message
     * @param parent
     */
    public BPM4JSFValidatorException(
        String message,
        Throwable parent)
    {
        super(message, parent);
    }

    /**
     * Constructor for BPM4JSFValidatorException.
     *
     * @param message
     */
    public BPM4JSFValidatorException(Throwable message)
    {
        super(message);
    }
}
