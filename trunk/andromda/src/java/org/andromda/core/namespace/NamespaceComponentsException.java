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
     * @param parent
     */
    public NamespaceComponentsException(Throwable parent)
    {
        super(parent);
    }

    /**
     * Constructs an instance of NamespaceComponentsException.
     *
     * @param message
     */
    public NamespaceComponentsException(String message)
    {
        super(message);
    }

    /**
     * Constructs an instance of NamespaceComponentsException.
     *
     * @param message
     * @param parent
     */
    public NamespaceComponentsException(
        String message,
        Throwable parent)
    {
        super(message, parent);
    }
}