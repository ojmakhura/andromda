package org.andromda.core.common;

/**
 * This is a placeholder for log4j which should be
 * integrated soon.
 * 
 * @since 30.07.2003
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen</a>
 *
 */
public class Logger
{
    public static final int MSG_INFO  = 1;
    public static final int MSG_WARN  = 2;
    public static final int MSG_ERROR = 3;
    
    public static void log (String msg, int logLevel)
    {
        System.out.println(msg);
    }
}
