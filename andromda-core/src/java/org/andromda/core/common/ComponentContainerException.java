package org.andromda.core.common;


/**
 * Any unchecked exception that will be thrown during the execution of ComponentContainer.
 */
public class ComponentContainerException
    extends RuntimeException
{
    /**
     * Constructs an instance of ComponentContainerException.
     *
     * @param parent the parent throwable
     */
    public ComponentContainerException(Throwable parent)
    {
        super(parent);
    }

    /**
     * Constructs an instance of ComponentContainerException.
     *
     * @param message the exception message
     */
    public ComponentContainerException(String message)
    {
        super(message);
    }

    /**
     * Constructs an instance of ComponentContainerException.
     *
     * @param message the exception message
     * @param parent the parent throwable
     */
    public ComponentContainerException(
        String message,
        Throwable parent)
    {
        super(message, parent);
    }
}