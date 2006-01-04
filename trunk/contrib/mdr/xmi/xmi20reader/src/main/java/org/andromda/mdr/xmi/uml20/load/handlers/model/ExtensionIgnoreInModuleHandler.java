package org.andromda.mdr.xmi.uml20.load.handlers.model;

import org.andromda.mdr.xmi.uml20.load.handlers.SkipElementHandler;
import org.andromda.mdr.xmi.uml20.load.reader.LoadInformationContainer;

public class ExtensionIgnoreInModuleHandler
    extends SkipElementHandler
    implements ElementExtensionFinishHandler
{
    private final ElementExtensionHandler mExtensionHandler;

    public ExtensionIgnoreInModuleHandler(
        ElementExtensionHandler handler)
    {
        mExtensionHandler = handler;
    }

    public void handleFinish(XMIContentHandler contentHandler)
    {
        LoadInformationContainer infoContainer = contentHandler.getMainHandler().getInfoContainer();
        if (infoContainer.getConfiguration().handleIgnoreInModule(
            contentHandler.getCurrentElement(),
            infoContainer.getInnerElementsCreator()))
        {
            mExtensionHandler.setElementDisposed(true);
        }
    }
}