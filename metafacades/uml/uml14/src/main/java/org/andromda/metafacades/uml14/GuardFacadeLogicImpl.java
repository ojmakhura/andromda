package org.andromda.metafacades.uml14;

import org.omg.uml.behavioralelements.statemachines.Guard;
import org.omg.uml.behavioralelements.statemachines.Transition;
import org.omg.uml.foundation.datatypes.BooleanExpression;

/**
 * MetafacadeLogic implementation.
 *
 * @see org.andromda.metafacades.uml.GuardFacade
 * @author Bob Fields
 */
public class GuardFacadeLogicImpl
        extends GuardFacadeLogic
{
    private static final long serialVersionUID = -6392947981148041940L;

    /**
     * @param metaObject
     * @param context
     */
    public GuardFacadeLogicImpl(Guard metaObject,
                                String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml14.GuardFacadeLogic#handleGetTransition()
     */
    protected Transition handleGetTransition()
    {
        return metaObject.getTransition();
    }

    /**
     * @see org.andromda.metafacades.uml14.GuardFacadeLogic#handleGetBody()
     */
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