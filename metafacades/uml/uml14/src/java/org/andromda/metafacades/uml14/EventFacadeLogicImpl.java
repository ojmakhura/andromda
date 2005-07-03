package org.andromda.metafacades.uml14;

import org.omg.uml.behavioralelements.statemachines.State;
import org.omg.uml.behavioralelements.statemachines.Transition;

import java.util.Collection;
import java.util.Iterator;
import java.util.ArrayList;

/**
 * MetafacadeLogic implementation.
 *
 * @see org.andromda.metafacades.uml.EventFacade
 */
public class EventFacadeLogicImpl
        extends EventFacadeLogic
{
    
    public EventFacadeLogicImpl(org.omg.uml.behavioralelements.statemachines.Event metaObject,
                                java.lang.String context)
    {
        super(metaObject, context);
    }

    protected Collection handleGetParameters()
    {
        return metaObject.getParameter();
    }

    protected Object handleGetTransition()
    {
        Transition eventTransition = null;

        final Collection allTransitions = UML14MetafacadeUtils.getModel().getStateMachines().getTransition().refAllOfType();
        for (final Iterator iterator = allTransitions.iterator(); iterator.hasNext() && eventTransition == null;)
        {
            final Transition transition = (Transition)iterator.next();
            if (metaObject.equals(transition.getTrigger()))
            {
                eventTransition = transition;
            }
        }

        return eventTransition;
    }

    protected Object handleGetState()
    {
        State eventState = null;

        final Collection allStates = new ArrayList(UML14MetafacadeUtils.getModel().getActivityGraphs().getActionState().refAllOfType());
        allStates.addAll(UML14MetafacadeUtils.getModel().getActivityGraphs().getObjectFlowState().refAllOfType());

        for (final Iterator stateIterator = allStates.iterator(); stateIterator.hasNext() && eventState == null;)
        {
            final State state = (State)stateIterator.next();
            if (state.getDeferrableEvent().contains(metaObject))
            {
                eventState = state;
            }
        }

        return eventState;
    }

    public Object getValidationOwner()
    {
        Object validationOwner = getTransition();

        if (validationOwner == null)
        {
            validationOwner = getState();
        }

        return validationOwner;
    }

}
