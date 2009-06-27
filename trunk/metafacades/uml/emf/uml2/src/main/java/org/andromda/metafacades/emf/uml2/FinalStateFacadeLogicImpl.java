package org.andromda.metafacades.emf.uml2;


/**
 * MetafacadeLogic implementation for
 * org.andromda.metafacades.uml.FinalStateFacade.
 *
 * @see org.andromda.metafacades.uml.FinalStateFacade
 */
public class FinalStateFacadeLogicImpl
    extends FinalStateFacadeLogic
{
    public FinalStateFacadeLogicImpl(
        final org.eclipse.uml2.FinalState metaObject,
        final String context)
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