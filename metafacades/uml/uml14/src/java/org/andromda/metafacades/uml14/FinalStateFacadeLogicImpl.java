package org.andromda.metafacades.uml14;


/**
 * Metaclass facade implementation.
 */
public class FinalStateFacadeLogicImpl
    extends FinalStateFacadeLogic
{
    public FinalStateFacadeLogicImpl(
        org.omg.uml.behavioralelements.statemachines.FinalState metaObject,
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