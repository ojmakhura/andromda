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
     * @param parent
     */
    public MetafacadeFactoryException(Throwable parent)
    {
        super(parent);
    }

    /**
     * Constructs an instance of MetafacadeFactoryException.
     *
     * @param message
     */
    public MetafacadeFactoryException(String message)
    {
        super(message);
    }

    /**
     * Constructs an instance of MetafacadeFactoryException.
     *
     * @param message
     * @param parent
     */
    public MetafacadeFactoryException(
        String message,
        Throwable parent)
    {
        super(message, parent);
    }
}