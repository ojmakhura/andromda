package org.andromda.metafacades.emf.uml2;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.FinalStateFacade.
 *
 * @see org.andromda.metafacades.uml.FinalStateFacade
 */
public class FinalStateFacadeLogicImpl
    extends FinalStateFacadeLogic
{
    public FinalStateFacadeLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.core.metafacade.MetafacadeBase#getValidationOwner()
     */
    public Object getValidationOwner()
    {
        return getStateMachine();
    }
}