package org.andromda.metafacades.uml14;

import org.omg.uml.behavioralelements.usecases.UseCase;
import org.omg.uml.behavioralelements.usecases.ExtensionPoint;

/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.ExtensionPointFacade.
 *
 * @see org.andromda.metafacades.uml.ExtensionPointFacade
 * @author Bob Fields
 */
public class ExtensionPointFacadeLogicImpl
    extends ExtensionPointFacadeLogic
{
    /**
     * @param metaObject
     * @param context
     */
    public ExtensionPointFacadeLogicImpl (ExtensionPoint metaObject, String context)
    {
        super (metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.ExtensionPointFacade#getUseCase()
     */
    @Override
    protected UseCase handleGetUseCase()
    {
        return metaObject.getUseCase();
    }
}