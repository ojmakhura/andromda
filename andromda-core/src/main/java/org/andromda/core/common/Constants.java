package org.andromda.core.common;

import org.apache.commons.lang3.StringUtils;

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
        // - initialize the TEMPORARY_DIRECTORY
        final String tmpDir = System.getProperty("java.io.tmpdir");
        final StringBuilder directory = new StringBuilder(tmpDir);
        if (!directory.toString().endsWith("/"))
        {
            directory.append('/');
        }
        final String userName = System.getProperty("user.name");
        if (StringUtils.isNotBlank(userName))
        {
            directory.append(userName).append('/');
        }
        directory.append(".andromda/");
        TEMPORARY_DIRECTORY = directory.toString();
    }

    /**
     * The name of the metafacades component.
     */
    public static final String COMPONENT_METAFACADES = "metafacades";
}