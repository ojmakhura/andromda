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
    private static final long serialVersionUID = -217280751630264167L;

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
     * @return metaObject.getIncludingCase()
     * @see org.andromda.metafacades.uml.IncludeFacade#getBase()
     */
    protected Object handleGetBase()
    {
        return this.metaObject.getIncludingCase();
    }

    /**
     * @return metaObject.getAddition()
     * @see org.andromda.metafacades.uml.IncludeFacade#getAddition()
     */
    protected Object handleGetAddition()
    {
        return this.metaObject.getAddition();
    }
}