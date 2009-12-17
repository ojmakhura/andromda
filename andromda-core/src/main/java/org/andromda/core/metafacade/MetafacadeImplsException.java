package org.andromda.core.metafacade;


/**
 * Any unchecked exception that will be thrown when a MetafacadeImpls processing error occurs.
 */
public class MetafacadeImplsException
    extends RuntimeException
{
    /**
     * Constructs an instance of MetafacadeImplsException.
     *
     * @param parent the parent throwable
     */
    public MetafacadeImplsException(Throwable parent)
    {
        super(parent);
    }

    /**
     * Constructs an instance of MetafacadeImplsException.
     *
     * @param message the exception message
     */
    public MetafacadeImplsException(String message)
    {
        super(message);
    }

    /**
     * Constructs an instance of MetafacadeImplsException.
     *
     * @param message the exception message
     * @param parent the parent throwable
     */
    public MetafacadeImplsException(
        String message,
        Throwable parent)
    {
        super(message, parent);
    }
}