package org.andromda.maven;

/**
 * This exception is thrown when a special situation an unexpeced
 * error occurs within the MavenPluginUtils. 
 *
 * @author Chad Brandon
 */
public class MavenPluginUtilsException extends RuntimeException
{  

    /**
     * Constructor for MavenPluginUtilsException.
     * 
     * @param message the exception message.
     */
    public MavenPluginUtilsException(String message)
    {
        super(message);
    }

    /**
     * Constructor for MavenPluginUtilsException.
     * 
     * @param message the exception message
     * @param parent the parent throwable.
     */
    public MavenPluginUtilsException(String message, Throwable parent)
    {
        super(message, parent);
    }

}
