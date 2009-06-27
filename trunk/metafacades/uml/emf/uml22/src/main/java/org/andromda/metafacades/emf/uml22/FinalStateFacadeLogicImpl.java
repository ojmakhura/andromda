package org.andromda.metafacades.emf.uml22;


import org.eclipse.uml2.uml.FinalState;

/**
 * MetafacadeLogic implementation for
 * org.andromda.metafacades.uml.FinalStateFacade.
 *
 * @see org.andromda.metafacades.uml.FinalStateFacade
 */
public class FinalStateFacadeLogicImpl
    extends FinalStateFacadeLogic
{
    /**
     * @param metaObject
     * @param context
     */
    public FinalStateFacadeLogicImpl(
        final FinalState metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.core.metafacade.MetafacadeBase#getValidationOwner()
     */
    @Override
    public Object getValidationOwner()
    {
        return getStateMachine();
    }
}
