package org.andromda.core.translation.library;


/**
 * Any unchecked exception that will be thrown when any translation rules exception occurs.
 */
public class LibraryException
    extends RuntimeException
{
    /**
     * Constructs an instance of LibraryException.
     *
     * @param parent the parent exception
     */
    public LibraryException(Throwable parent)
    {
        super(parent);
    }

    /**
     * Constructs an instance of LibraryException.
     *
     * @param message the exception message
     */
    public LibraryException(String message)
    {
        super(message);
    }

    /**
     * Constructs an instance of LibraryException.
     *
     * @param message the exception message
     * @param parent the parent exception
     */
    public LibraryException(
        String message,
        Throwable parent)
    {
        super(message, parent);
    }
}