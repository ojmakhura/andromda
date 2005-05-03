package org.andromda.metafacades.uml14;

import java.util.Collection;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.ExtendFacade.
 *
 * @see org.andromda.metafacades.uml.ExtendFacade
 */
public class ExtendFacadeLogicImpl
    extends ExtendFacadeLogic
{
    public ExtendFacadeLogicImpl (org.omg.uml.behavioralelements.usecases.Extend metaObject, String context)
    {
        super (metaObject, context);
    }

    protected Object handleGetBase()
    {
        return metaObject.getBase();
    }

    protected Collection handleGetExtensionPoints()
    {
        return metaObject.getExtensionPoint();
    }

    protected Object handleGetExtension()
    {
        return metaObject.getExtension();
    }
}