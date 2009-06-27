package org.andromda.metafacades.emf.uml2;

import org.eclipse.uml2.Include;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.IncludeFacade.
 *
 * @see org.andromda.metafacades.uml.IncludeFacade
 */
public class IncludeFacadeLogicImpl
    extends IncludeFacadeLogic
{
    public IncludeFacadeLogicImpl(
        Include metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.IncludeFacade#getBase()
     */
    protected java.lang.Object handleGetBase()
    {
        return this.metaObject.getIncludingCase();
    }

    /**
     * @see org.andromda.metafacades.uml.IncludeFacade#getAddition()
     */
    protected java.lang.Object handleGetAddition()
    {
        return this.metaObject.getAddition();
    }
}