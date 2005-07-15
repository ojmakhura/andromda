package org.andromda.metafacades.uml14;

import org.omg.uml.foundation.datatypes.BooleanExpression;

/**
 * MetafacadeLogic implementation.
 *
 * @see org.andromda.metafacades.uml.GuardFacade
 */
public class GuardFacadeLogicImpl
        extends GuardFacadeLogic
{

    public GuardFacadeLogicImpl(org.omg.uml.behavioralelements.statemachines.Guard metaObject,
                                java.lang.String context)
    {
        super(metaObject, context);
    }

    protected Object handleGetTransition()
    {
        return metaObject.getTransition();
    }

    protected String handleGetBody()
    {
        final BooleanExpression expression = metaObject.getExpression();
        return (expression == null) ? null : expression.getBody();
    }

    public Object getValidationOwner()
    {
        return getTransition();
    }
}
