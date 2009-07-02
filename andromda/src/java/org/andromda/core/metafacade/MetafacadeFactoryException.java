package org.andromda.core.metafacade;


/**
 * Any unchecked exception that will be thrown when a MetafacadeFactory processing error occurs.
 */
public class MetafacadeFactoryException
    extends RuntimeException
{
    /**
     * Constructs an instance of MetafacadeFactoryException.
     *
     * @param parent the parent throwable
     */
    public MetafacadeFactoryException(Throwable parent)
    {
        super(parent);
    }

    /**
     * Constructs an instance of MetafacadeFactoryException.
     *
     * @param message the exception message
     */
    public MetafacadeFactoryException(String message)
    {
        super(message);
    }

    /**
     * Constructs an instance of MetafacadeFactoryException.
     *
     * @param message the exception message
     * @param parent the parent throwable
     */
    public MetafacadeFactoryException(
        String message,
        Throwable parent)
    {
        super(message, parent);
    }
}