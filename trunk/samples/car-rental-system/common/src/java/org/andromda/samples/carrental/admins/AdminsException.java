package org.andromda.samples.carrental.admins;

/**
 * Represents an exception that has been thrown
 * from inside the "admins" component.
 * 
 * @author Matthias Bohlen
 */
public class AdminsException extends Exception
{

    /**
     * Constructor for AdminsException.
     */
    public AdminsException()
    {
        super();
    }

    /**
     * Constructor for AdminsException.
     * @param message
     */
    public AdminsException(String message)
    {
        super(message);
    }

    /**
     * Constructor for AdminsException.
     * @param message
     * @param cause
     */
    public AdminsException(String message, Throwable cause)
    {
        super(message, cause);
    }

    /**
     * Constructor for AdminsException.
     * @param cause
     */
    public AdminsException(Throwable cause)
    {
        super(cause);
    }

}
