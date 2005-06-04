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
     * @param parent
     */
    public ResourceFinderException(Throwable parent)
    {
        super(parent);
    }

    /**
     * Constructs an instance of ResourceFinderException.
     *
     * @param message
     */
    public ResourceFinderException(String message)
    {
        super(message);
    }

    /**
     * Constructs an instance of ResourceFinderException.
     *
     * @param message
     * @param parent
     */
    public ResourceFinderException(
        String message,
        Throwable parent)
    {
        super(message, parent);
    }
}