package org.andromda.metafacades.uml14;

import java.util.Collection;
import java.util.Iterator;
import org.omg.uml.behavioralelements.statemachines.State;
import org.omg.uml.behavioralelements.statemachines.Transition;
import org.omg.uml.behavioralelements.statemachines.Event;

/**
 * MetafacadeLogic implementation.
 *
 * @see org.andromda.metafacades.uml.EventFacade
 * @author Bob Fields
 */
public class EventFacadeLogicImpl
    extends EventFacadeLogic
{
    /**
     * @param metaObject
     * @param context
     */
    public EventFacadeLogicImpl(
        Event metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.EventFacade#getParameters()
     */
    @Override
    protected Collection handleGetParameters()
    {
        return metaObject.getParameter();
    }

    /**
     * @see org.andromda.metafacades.uml.EventFacade#getTransition()
     */
    @Override
    protected Transition handleGetTransition()
    {
        Transition eventTransition = null;

        final Collection<Transition> allTransitions =
            UML14MetafacadeUtils.getModel().getStateMachines().getTransition().refAllOfType();
        for (final Iterator<Transition> iterator = allTransitions.iterator(); iterator.hasNext() && eventTransition == null;)
        {
            final Transition transition = (Transition)iterator.next();
            if (metaObject.equals(transition.getTrigger()))
            {
                eventTransition = transition;
            }
        }

        return eventTransition;
    }

    /**
     * @see org.andromda.metafacades.uml.EventFacade#getState()
     */
    @Override
    protected State handleGetState()
    {
        State eventState = null;

        final Collection<State> allStates = UML14MetafacadeUtils.getModel().getStateMachines().getState().refAllOfType();
        for (final Iterator<State> stateIterator = allStates.iterator(); stateIterator.hasNext() && eventState == null;)
        {
            final State state = (State)stateIterator.next();
            if (state.getDeferrableEvent().contains(metaObject))
            {
                eventState = state;
            }
        }

        return eventState;
    }

    /**
     * @see org.andromda.core.metafacade.MetafacadeBase#getValidationOwner()
     */
    public Object getValidationOwner()
    {
        Object validationOwner = this.getTransition();
        if (validationOwner == null)
        {
            validationOwner = this.getState();
        }
        return validationOwner;
    }
}