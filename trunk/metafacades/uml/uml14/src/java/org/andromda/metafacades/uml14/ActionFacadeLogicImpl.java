package org.andromda.metafacades.uml14;

import java.util.Collection;
import java.util.Iterator;
import org.omg.uml.behavioralelements.activitygraphs.ActionState;
import org.omg.uml.behavioralelements.statemachines.Transition;
import org.omg.uml.behavioralelements.commonbehavior.Action;

/**
 * MetafacadeLogic implementation.
 *
 * @see org.andromda.metafacades.uml.ActionFacade
 * @author Bob Fields
 */
public class ActionFacadeLogicImpl
        extends ActionFacadeLogic
{
    /**
     * @param metaObject
     * @param context
     */
    public ActionFacadeLogicImpl(Action metaObject,
                                 String context)
    {
        super(metaObject, context);
    }

    protected Transition handleGetTransition()
    {
        Transition effectTransition = null;

        final Collection allTransitions = UML14MetafacadeUtils.getModel().getStateMachines().getTransition().refAllOfType();
        for (final Iterator iterator = allTransitions.iterator(); iterator.hasNext() && effectTransition == null;)
        {
            Transition transition = (Transition)iterator.next();
            if (metaObject.equals(transition.getEffect()))
            {
                effectTransition = transition;
            }
        }

        return effectTransition;
    }

    protected ActionState handleGetActionState()
    {
        ActionState entryState = null;

        final Collection allActionStates = UML14MetafacadeUtils.getModel().getActivityGraphs().getActionState().refAllOfType();
        for (final Iterator iterator = allActionStates.iterator(); iterator.hasNext() && entryState == null;)
        {
            final ActionState actionState = (ActionState)iterator.next();
            if (metaObject.equals(actionState.getEntry()))
            {
                entryState = actionState;
            }
        }

        return entryState;
    }

    /**
     * @see org.andromda.core.metafacade.MetafacadeBase#getValidationOwner()
     */
    public Object getValidationOwner()
    {
        Object validationOwner = getTransition();

        if (validationOwner == null)
        {
            validationOwner = getActionState();
        }

        return validationOwner;
    }
}