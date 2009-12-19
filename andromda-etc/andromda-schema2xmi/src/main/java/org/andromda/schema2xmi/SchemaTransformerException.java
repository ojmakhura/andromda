package org.andromda.schema2xmi;


/**
 * An exception thrown when an unexpected error occurs during processing of the
 * SchemaTransformer.
 *
 * @author Chad Brandon
 */
public class SchemaTransformerException
    extends RuntimeException
{
    /**
     * Constructs a new instance of SchemaTransformerException
     *
     * @param throwable the parent Throwable
     */
    public SchemaTransformerException(Throwable throwable)
    {
        super(throwable);
    }

    /**
     * Constructs a new instance of SchemaTransformerException
     *
     * @param message the throwable message.
     */
    public SchemaTransformerException(String message)
    {
        super(message);
    }

    /**
     * Constructs a new instance of SchemaTransformerException
     *
     * @param message the throwable message.
     * @param throwable the parent of this Throwable.
     */
    public SchemaTransformerException(
        String message,
        Throwable throwable)
    {
        super(message, throwable);
    }
}