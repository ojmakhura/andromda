package org.andromda.metafacades.uml14;

import java.util.List;
import org.omg.uml.behavioralelements.usecases.UseCase;
import org.omg.uml.behavioralelements.usecases.Extend;

/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.ExtendFacade.
 *
 * @see org.andromda.metafacades.uml.ExtendFacade
 * @author Bob Fields
 */
public class ExtendFacadeLogicImpl
    extends ExtendFacadeLogic
{
    /**
     * @param metaObject
     * @param context
     */
    public ExtendFacadeLogicImpl (Extend metaObject, String context)
    {
        super (metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.ExtendFacade#getBase()
     */
    @Override
    protected UseCase handleGetBase()
    {
        return metaObject.getBase();
    }

    /**
     * @see org.andromda.metafacades.uml.ExtendFacade#getExtensionPoints()
     */
    @Override
    protected List handleGetExtensionPoints()
    {
        return metaObject.getExtensionPoint();
    }

    /**
     * @see org.andromda.metafacades.uml.ExtendFacade#getExtension()
     */
    @Override
    protected UseCase handleGetExtension()
    {
        return metaObject.getExtension();
    }
}