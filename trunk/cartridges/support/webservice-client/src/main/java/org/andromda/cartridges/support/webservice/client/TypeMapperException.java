package org.andromda.cartridges.support.webservice.client;

/**
 * An exception thrown for unexpected errors that
 * occur during {@link TypeMapper} execution.
 *
 * @author Chad Brandon
 */
public class TypeMapperException
    extends RuntimeException
{
    /**
     * Constructs an instance of TypeMapperException.
     *
     * @param parent
     */
    public TypeMapperException(Throwable parent)
    {
        super(parent);
    }

    /**
     * Constructs an instance of TypeMapperException.
     *
     * @param message
     */
    public TypeMapperException(String message)
    {
        super(message);
    }

    /**
     * Constructs an instance of TypeMapperException.
     *
     * @param message
     * @param parent
     */
    public TypeMapperException(
        String message,
        Throwable parent)
    {
        super(message, parent);
    }
}
