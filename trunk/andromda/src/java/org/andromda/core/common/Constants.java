package org.andromda.core.common;

import org.apache.commons.lang.StringUtils;

/**
 * Stores any constants used throughout the
 * AndroMDA codebase.
 * 
 * @author Chad Brandon
 */
public class Constants
{
    /**
     * The location of the AndroMDA temporary directory. This is where any
     * temporary resources are placed during AndroMDA execution.
     */
    public static final String TEMPORARY_DIRECTORY;
    
    /**
     * Perform any constant initialization.
     */
    static
    {
        // initialize the ANDROMDA_TEMP_DIRECTORY
        final String tmpDir = System.getProperty("java.io.tmpdir");
        final StringBuffer directory = new StringBuffer(tmpDir);
        if (!directory.toString().endsWith("/"))
        {
            directory.append("/");
        }
        directory.append(".andromda/");
        final String userName = System.getProperty("user.name");
        if (StringUtils.isNotBlank(userName))
        {
            directory.append(userName + "/");
        }
        TEMPORARY_DIRECTORY = directory.toString();        
    }
}