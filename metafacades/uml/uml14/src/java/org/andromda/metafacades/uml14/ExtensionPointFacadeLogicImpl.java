package org.andromda.metafacades.uml14;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.ExtensionPointFacade.
 *
 * @see org.andromda.metafacades.uml.ExtensionPointFacade
 */
public class ExtensionPointFacadeLogicImpl
    extends ExtensionPointFacadeLogic
{
    public ExtensionPointFacadeLogicImpl (org.omg.uml.behavioralelements.usecases.ExtensionPoint metaObject, String context)
    {
        super (metaObject, context);
    }

    protected Object handleGetUseCase()
    {
        return metaObject.getUseCase();
    }
}