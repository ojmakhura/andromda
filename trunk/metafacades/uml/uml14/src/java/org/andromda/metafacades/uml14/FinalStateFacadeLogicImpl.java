package org.andromda.metafacades.uml14;

import org.omg.uml.behavioralelements.statemachines.FinalState;

/**
 * Metaclass facade implementation.
 * @author Bob Fields
 */
public class FinalStateFacadeLogicImpl
    extends FinalStateFacadeLogic
{
    /**
     * @param metaObject
     * @param context
     */
    public FinalStateFacadeLogicImpl(
        FinalState metaObject,
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