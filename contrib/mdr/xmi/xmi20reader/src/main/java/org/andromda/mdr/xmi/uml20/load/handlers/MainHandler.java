package org.andromda.mdr.xmi.uml20.load.handlers;

import java.util.Stack;

import javax.jmi.xmi.MalformedXMIException;

import org.andromda.mdr.xmi.uml20.load.reader.LoadInformationContainer;
import org.xml.sax.Attributes;

public class MainHandler
    implements HandlerState
{
    private LoadInformationContainer mInfoContainer;
    private Stack mHandlers;

    public MainHandler()
    {
        mHandlers = new Stack();
    }

    /**
     * Create the main handler with the current resource information and reader
     * 
     * @param infoContainer
     */
    public MainHandler(
        LoadInformationContainer infoContainer)
    {
        this();
        mInfoContainer = infoContainer;
    }

    public void handleStartElement(String localName, Attributes attrs, String namespace)
        throws MalformedXMIException
    {
        DefaultHandler currentHandler = getCurrentHandler();
        currentHandler.inc();
        currentHandler.handleStartElement(localName, attrs, namespace);
    }

    public void handleEndElement(String name, String value, String namespace)
    {
        boolean inScope;
        do
        {
            DefaultHandler currentHandler = getCurrentHandler();
            inScope = currentHandler.dec();
            if (inScope)
            {
                currentHandler.handleEndElement(name, value, namespace);
            }
            else
            {
                popHandler();
            }
        }
        while (!inScope);
    }

    public void pushHandler(DefaultHandler handler)
    {
        mHandlers.push(handler);
        handler.setMainHandler(this);
    }

    public void pushAndReplaceHandler(DefaultHandler handler)
    {
        getCurrentHandler().dec();
        pushHandler(handler);
    }

    public void popHandler()
    {
        int lastIndexOf = mHandlers.lastIndexOf(getCurrentHandler());
        getCurrentHandler().finish();

        // Remember index because new handler can be pushed in the stack
        mHandlers.remove(lastIndexOf);
    }

    public DefaultHandler getCurrentHandler()
    {
        if (mHandlers.size() > 0)
        {
            return (DefaultHandler)mHandlers.peek();
        }
        return null;
    }

    public LoadInformationContainer getInfoContainer()
    {
        return mInfoContainer;
    }
}