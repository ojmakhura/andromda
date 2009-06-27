package org.andromda.metafacades.emf.uml22;

import org.eclipse.uml2.uml.Include;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.IncludeFacade.
 *
 * @see org.andromda.metafacades.uml.IncludeFacade
 */
public class IncludeFacadeLogicImpl
    extends IncludeFacadeLogic
{
    /**
     * @param metaObject
     * @param context
     */
    public IncludeFacadeLogicImpl(
        Include metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.IncludeFacade#getBase()
     */
    @Override
    protected Object handleGetBase()
    {
        return this.metaObject.getIncludingCase();
    }

    /**
     * @see org.andromda.metafacades.uml.IncludeFacade#getAddition()
     */
    @Override
    protected Object handleGetAddition()
    {
        return this.metaObject.getAddition();
    }
}
