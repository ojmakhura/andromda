package org.andromda.maven;

/**
 * This exception is thrown when a special situation an unexpeced
 * error occurs within the MavenPluginUtils. 
 *
 * @author Chad Brandon
 *
 */
public class MavenPluginUtilsException extends RuntimeException
{  

    /**
     * Constructor for MavenPluginUtilsException.
     */
    public MavenPluginUtilsException()
    {
        super();
    }

    /**
     * Constructor for MavenPluginUtilsException.
     * @param arg0
     */
    public MavenPluginUtilsException(String arg0)
    {
        super(arg0);
    }

    /**
     * Constructor for MavenPluginUtilsException.
     * @param arg0
     * @param arg1
     */
    public MavenPluginUtilsException(String arg0, Throwable arg1)
    {
        super(arg0, arg1);
    }

    /**
     * Constructor for MavenPluginUtilsException.
     * @param arg0
     */
    public MavenPluginUtilsException(Throwable arg0)
    {
        super(arg0);
    }

}
