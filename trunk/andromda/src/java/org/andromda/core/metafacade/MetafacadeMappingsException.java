package org.andromda.core.metafacade;


/**
 * Any unchecked exception that will be thrown when a MetafacadeMappings processing error occurs.
 */
public class MetafacadeMappingsException
    extends RuntimeException
{
    /**
     * Constructs an instance of MetafacadeMappingsException.
     *
     * @param parent the parent throwable
     */
    public MetafacadeMappingsException(Throwable parent)
    {
        super(parent);
    }

    /**
     * Constructs an instance of MetafacadeMappingsException.
     *
     * @param message the exception message
     */
    public MetafacadeMappingsException(String message)
    {
        super(message);
    }

    /**
     * Constructs an instance of MetafacadeMappingsException.
     *
     * @param message the exception message
     * @param parent the parent throwable
     */
    public MetafacadeMappingsException(
        String message,
        Throwable parent)
    {
        super(message, parent);
    }
}