package org.andromda.core.mapping;


/**
 * Any error occurring during processing of Mappings should through this exception.
 */
public class MappingsException
    extends RuntimeException
{
    /**
     * Constructs an instance of MappingsException.
     *
     * @param parent
     */
    public MappingsException(Throwable parent)
    {
        super(parent);
    }

    /**
     * Constructs an instance of MappingsException.
     *
     * @param message
     */
    public MappingsException(String message)
    {
        super(message);
    }

    /**
     * Constructs an instance of MappingsException.
     *
     * @param message
     * @param parent
     */
    public MappingsException(
        String message,
        Throwable parent)
    {
        super(message, parent);
    }
}