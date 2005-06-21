package org.andromda.modules.xmilink;

import com.togethersoft.openapi.ide.message.IdeMessageManagerAccess;

/**
 * TODO Specify purpose, please.
 * 
 * @author Peter Friese
 * @version 1.0
 * @since 26.11.2004
 */
public class Logger
{

    private static boolean logEnabled = false;

    public static void setLogEnabled(boolean logEnabled)
    {
        Logger.logEnabled = logEnabled;
    }

    public static boolean isLogEnabled()
    {
        return logEnabled;
    }

    public static void info(String msg)
    {
        if (isLogEnabled())
        {
            IdeMessageManagerAccess.printMessage(0, msg);
        }
    }

    public static void warn(String msg)
    {
        if (isLogEnabled())
        {
            IdeMessageManagerAccess.printMessage(1, msg);
        }
    }

    public static void error(String msg)
    {
        if (isLogEnabled())
        {
            IdeMessageManagerAccess.printMessage(2, msg);
        }
    }

}
