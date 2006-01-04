package org.andromda.mdr.xmi.uml20.load.handlers.model;

import javax.jmi.reflect.RefObject;

import org.andromda.mdr.xmi.uml20.load.configuration.Configuration;
import org.xml.sax.Attributes;

public class ExtensionModuleRootHandler
    extends ModelHandler
{
    public ExtensionModuleRootHandler(
        RefObject currentElement)
    {
        mCurrentElement = currentElement;
    }

    private RefObject mCurrentElement;

    public void handleStartElement(String localName, Attributes attrs, String namespace)
    {
        if (mCurrentElement != null)
        {
            Configuration configuration = getMainHandler().getInfoContainer().getConfiguration();
            configuration.setReadingModuleContents(true);
        }
    }
}