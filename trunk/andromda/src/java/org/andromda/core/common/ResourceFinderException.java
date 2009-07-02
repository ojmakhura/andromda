package org.andromda.core.common;


/**
 * Any unchecked exception that will be thrown during the execution of the ResourceFinder
 */
public class ResourceFinderException
    extends RuntimeException
{
    /**
     * Constructs an instance of ResourceFinderException.
     *
     * @param parent the parent throwable
     */
    public ResourceFinderException(Throwable parent)
    {
        super(parent);
    }

    /**
     * Constructs an instance of ResourceFinderException.
     *
     * @param message the exception message
     */
    public ResourceFinderException(String message)
    {
        super(message);
    }

    /**
     * Constructs an instance of ResourceFinderException.
     *
     * @param message the exception message
     * @param parent the parent throwable
     */
    public ResourceFinderException(
        String message,
        Throwable parent)
    {
        super(message, parent);
    }
}