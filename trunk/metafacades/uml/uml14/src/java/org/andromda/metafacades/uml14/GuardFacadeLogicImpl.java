package org.andromda.metafacades.uml14;

import org.omg.uml.behavioralelements.statemachines.Transition;
import org.omg.uml.foundation.datatypes.BooleanExpression;
import org.omg.uml.behavioralelements.statemachines.Guard;

/**
 * MetafacadeLogic implementation.
 *
 * @see org.andromda.metafacades.uml.GuardFacade
 * @author Bob Fields
 */
public class GuardFacadeLogicImpl
        extends GuardFacadeLogic
{

    /**
     * @param metaObject
     * @param context
     */
    public GuardFacadeLogicImpl(Guard metaObject,
                                String context)
    {
        super(metaObject, context);
    }

    protected Transition handleGetTransition()
    {
        return metaObject.getTransition();
    }

    protected String handleGetBody()
    {
        final BooleanExpression expression = metaObject.getExpression();
        return (expression == null) ? null : expression.getBody();
    }

    /**
     * @see org.andromda.core.metafacade.MetafacadeBase#getValidationOwner()
     */
    public Object getValidationOwner()
    {
        return getTransition();
    }
}