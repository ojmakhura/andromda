package org.andromda.core.metafacade;


/**
 * Any unchecked exception that will be thrown when a Metafacade processing error occurs.
 */
public class MetafacadeException
    extends RuntimeException
{
    /**
     * Constructs an instance of MetafacadeException.
     *
     * @param parent
     */
    public MetafacadeException(Throwable parent)
    {
        super(parent);
    }

    /**
     * Constructs an instance of MetafacadeException.
     *
     * @param message
     */
    public MetafacadeException(String message)
    {
        super(message);
    }

    /**
     * Constructs an instance of MetafacadeException.
     *
     * @param message
     * @param parent
     */
    public MetafacadeException(
        String message,
        Throwable parent)
    {
        super(message, parent);
    }
}