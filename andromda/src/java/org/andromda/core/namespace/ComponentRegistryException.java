package org.andromda.core.namespace;


/**
 * Any unchecked exception that will be thrown during execution of the ComponentRegistry.
 */
public class ComponentRegistryException
    extends RuntimeException
{
    /**
     * Constructs an instance of ComponentRegistryException.
     *
     * @param parent
     */
    public ComponentRegistryException(Throwable parent)
    {
        super(parent);
    }

    /**
     * Constructs an instance of ComponentRegistryException.
     *
     * @param message
     */
    public ComponentRegistryException(String message)
    {
        super(message);
    }

    /**
     * Constructs an instance of ComponentRegistryException.
     *
     * @param message
     * @param parent
     */
    public ComponentRegistryException(
        String message,
        Throwable parent)
    {
        super(message, parent);
    }
}