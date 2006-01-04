package org.andromda.mdr.xmi.uml20.load.handlers.model;

import javax.jmi.reflect.RefObject;
import javax.jmi.xmi.MalformedXMIException;

import org.andromda.mdr.xmi.uml20.load.core.XMIConstants;
import org.andromda.mdr.xmi.uml20.load.handlers.DefaultHandler;
import org.andromda.mdr.xmi.uml20.load.handlers.SkipElementHandler;
import org.xml.sax.Attributes;

/**
 * Responsible to check for the right extension handler
 * 
 * @author Adriano Andrulis
 * @version $Revision: 1.1 $, $Date: 2006-01-04 19:53:36 $
 */
public class ElementExtensionHandler
    extends ModelHandler
{
    private RefObject mCurrentElement;
    private DefaultHandler mHandler;
    private boolean mExtensionIgnoreInModuleHandler;
    private boolean mElementDisposed;
    private final String mCurrentFeature;

    public ElementExtensionHandler(
        RefObject currentElement,
        String currentFeature)
    {
        super();
        mCurrentElement = currentElement;
        mCurrentFeature = currentFeature;
    }

    public void handleStartElement(String localName, Attributes attrs, String namespace)
        throws MalformedXMIException
    {
        if (localName.equals(getTag(XMIConstants.referenceExtension)))
        {
            mHandler = new SkipElementHandler();
            mHandler.handleStartElement(localName, attrs, namespace);
        }
        else if (localName.equals(getTag(XMIConstants.moduleExtension)))
        {
            if (attrs.getValue("", XMIConstants.ignoredInModule) != null)
            {
                mHandler = new ExtensionIgnoreInModuleHandler(this);
                mExtensionIgnoreInModuleHandler = true;
            }
            else if (attrs.getValue("", XMIConstants.MODULE_ROOT) != null)
            {
                mHandler = new ExtensionModuleRootHandler(mCurrentElement);
                mHandler.setMainHandler(getMainHandler());
                mHandler.handleStartElement(localName, attrs, namespace);
            }
        }
        else if (localName.equals(XMIConstants.iconData))
        {
            DefaultHandler handler = new SkipElementHandler();

            // ExtensionIconDataHandler handler = new
            // ExtensionIconDataHandler(mCurrentElement);
            handler.inc();
            handler.handleStartElement(localName, null, null);
            getMainHandler().pushAndReplaceHandler(handler);
        }
        else if (localName.equals(getTag(XMIConstants.rtProperties)))
        {
            // RTExtensionHandler extensionHandler = new
            // RTExtensionHandler(getMainHandler(),mCurrentElement,
            // getEnvironment());
            DefaultHandler extensionHandler = new SkipElementHandler();
            getMainHandler().pushHandler(extensionHandler);
            extensionHandler.beforeStart();
            mHandler = extensionHandler;
        }
        else if (localName.equals(getTag(XMIConstants.modelExtension)))
        {
            DefaultHandler h = new XMIContentHandler(mCurrentElement, mCurrentFeature, true);
            getMainHandler().pushHandler(h);
            mHandler = h;
        }
        else
        {
            System.out.println("Delegation extension handler - Unexpected element: " + localName);
            return;
        }
    }

    public boolean ignoreInModule()
    {
        return mExtensionIgnoreInModuleHandler;
    }

    public void handleFinish(XMIContentHandler contentHandler)
    {
        if (mHandler instanceof ElementExtensionHandler)
        {
            ((ElementExtensionHandler)mHandler).handleFinish(contentHandler);
        }
    }

    public boolean isElementDisposed()
    {
        return mElementDisposed;
    }

    public void setElementDisposed(boolean elementDisposed)
    {
        mElementDisposed = elementDisposed;
    }
}