package org.andromda.core.common;

import org.apache.log4j.Logger;

/**
 * This is a logger that writes to stdout. At the moment,
 * it is written in the simplest possible way.
 * 
 * @since 26.11.2003
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen</a>
 *
 */
public class StdoutLogger
{
    private static Logger logger = Logger.getLogger("andromda");
    
    public static void debug (Object o)
    {
        logger.debug(o);
    }
    public static void info (Object o)
    {
        logger.info(o);
    }
    public static void warn (Object o)
    {
        logger.warn(o);
    }
    public static void error (Object o)
    {
        logger.error(o);
    }
}
