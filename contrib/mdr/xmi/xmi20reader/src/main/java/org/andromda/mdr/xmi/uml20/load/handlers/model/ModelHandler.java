package org.andromda.mdr.xmi.uml20.load.handlers.model;

import org.andromda.mdr.xmi.uml20.load.core.TagConverter;
import org.andromda.mdr.xmi.uml20.load.handlers.DefaultHandler;
import org.andromda.mdr.xmi.uml20.load.utils.InnerElementsCreator;
import org.andromda.mdr.xmi.uml20.load.utils.PropertiesSetter;

public abstract class ModelHandler
    extends DefaultHandler
{
    public TagConverter getTagConverter()
    {
        return getMainHandler().getInfoContainer().getTagConverter();
    }

    public PropertiesSetter getPropertiesSetter()
    {
        return getMainHandler().getInfoContainer().getPropertiesSetter();
    }

    public InnerElementsCreator getElementsCreator()
    {
        return getMainHandler().getInfoContainer().getInnerElementsCreator();
    }
}