package org.andromda.mdr.xmi.uml20.load.handlers;

public abstract class DefaultHandler
    implements HandlerState
{
    private int count;
    private MainHandler mMainHandler;

    public void inc()
    {
        count++;
    }

    public boolean dec()
    {
        count--;
        return count >= 0;
    }

    public void setMainHandler(MainHandler handler)
    {
        mMainHandler = handler;
    }

    public MainHandler getMainHandler()
    {
        return mMainHandler;
    }

    public void handleEndElement(String name, String value, String namespace)
    {
    // nothing here
    }

    public void finish()
    {
    // Nothing here
    }

    public static String getTag(String tagName)
    {
        return tagName;
    }

    public void beforeStart()
    {
    // 
    }
}