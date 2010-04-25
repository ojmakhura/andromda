package org.andromda.metafacades.uml14;

import org.omg.uml.behavioralelements.usecases.UseCase;
import org.omg.uml.behavioralelements.usecases.Include;

/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.IncludeFacade.
 *
 * @see org.andromda.metafacades.uml.IncludeFacade
 * @author Bob Fields
 */
public class IncludeFacadeLogicImpl
    extends IncludeFacadeLogic
{

    /**
     * @param metaObject
     * @param context
     */
    public IncludeFacadeLogicImpl (Include metaObject, String context)
    {
        super (metaObject, context);
    }
    /**
     * @see org.andromda.metafacades.uml.IncludeFacade#getAddition()
     */
    @Override
    protected UseCase handleGetAddition()
    {
        return metaObject.getAddition();
    }

    /**
     * @see org.andromda.metafacades.uml.IncludeFacade#getBase()
     */
    @Override
    protected UseCase handleGetBase()
    {
        return metaObject.getBase();
    }

}