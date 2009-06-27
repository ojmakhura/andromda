package org.andromda.maven.plugin.andromdapp;

/**
 * An exception that is thrown when an unexpected error
 * occurs during execution of a SchemaManagement instance.
 * 
 * @author Chad Brandon
 */
public class SchemaManagementException
    extends RuntimeException
{
    /**
     * Constructor for SchemaManagementException.
     *
     * @param message
     */
    public SchemaManagementException(String message)
    {
        super(message);
    }

    /**
     * Constructor for SchemaManagementException.
     *
     * @param message
     * @param parent
     */
    public SchemaManagementException(
        String message,
        Throwable parent)
    {
        super(message, parent);
    }

    /**
     * Constructor for SchemaManagementException.
     *
     * @param message
     */
    public SchemaManagementException(Throwable message)
    {
        super(message);
    }
}
