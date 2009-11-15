package org.andromda.core.namespace;


/**
 * Any unchecked exception that will be thrown during execution of the NamespaceComponents.
 */
public class NamespaceComponentsException
    extends RuntimeException
{
    /**
     * Constructs an instance of NamespaceComponentsException.
     *
     * @param parent the parent throwable
     */
    public NamespaceComponentsException(Throwable parent)
    {
        super(parent);
    }

    /**
     * Constructs an instance of NamespaceComponentsException.
     *
     * @param message the exception message
     */
    public NamespaceComponentsException(String message)
    {
        super(message);
    }

    /**
     * Constructs an instance of NamespaceComponentsException.
     *
     * @param message the exception message
     * @param parent the parent throwable
     */
    public NamespaceComponentsException(
        String message,
        Throwable parent)
    {
        super(message, parent);
    }
}