package org.andromda.core.translation.library;

/**
 * Any unchecked exception that will be thrown when any translation rules
 * exception occurs.
 */
public class LibraryException
    extends RuntimeException
{

    /**
     * Constructs an instance of LibraryException.
     * 
     * @param th
     */
    public LibraryException(
        Throwable th)
    {
        super(th);
    }

    /**
     * Constructs an instance of LibraryException.
     * 
     * @param msg
     */
    public LibraryException(
        String msg)
    {
        super(msg);
    }

    /**
     * Constructs an instance of LibraryException.
     * 
     * @param msg
     * @param th
     */
    public LibraryException(
        String msg,
        Throwable th)
    {
        super(msg, th);
    }

}