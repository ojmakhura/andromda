package org.andromda.cartridges.jbpm.metafacades;

import org.andromda.core.common.StringUtilsHelper;
import org.andromda.metafacades.uml.ActivityGraphFacade;
import org.andromda.metafacades.uml.EventFacade;
import org.andromda.metafacades.uml.StateFacade;
import org.andromda.metafacades.uml.TransitionFacade;
import org.andromda.metafacades.uml.StateMachineFacade;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.jbpm.metafacades.JBpmHandler.
 *
 * @see org.andromda.cartridges.jbpm.metafacades.JBpmHandler
 */
public class JBpmHandlerLogicImpl
        extends JBpmHandlerLogic
{
    public JBpmHandlerLogicImpl(Object metaObject, String context)
    {
        super(metaObject, context);
    }

    protected boolean handleIsAssignmentHandler()
    {
        boolean assignmentHandler = false;

        final List actions = internalJBpmActions();
        for (int i = 0; i < actions.size() && !assignmentHandler; i++)
        {
            final JBpmAction action = (JBpmAction) actions.get(i);
            assignmentHandler = action.isTask();
        }

        return assignmentHandler;
    }

    protected boolean handleIsActionHandler()
    {
        boolean actionHandler = false;

        final List actions = internalJBpmActions();
        for (int i = 0; i < actions.size() && !actionHandler; i++)
        {
            final JBpmAction action = (JBpmAction) actions.get(i);
            actionHandler =
                    action.isAfterSignal() || action.isBeforeSignal() || action.isNodeEnter() || action.isNodeLeave();
        }

        return actionHandler;
    }

    protected String handleGetClassName()
    {
        return StringUtilsHelper.upperCamelCaseName(getName());
    }

    private List internalJBpmActions()
    {
        final List deferringEvents = new ArrayList();

        final StateMachineFacade stateMachine = getOwner().getStateMachineContext();
        if (stateMachine instanceof ActivityGraphFacade)
        {
            final ActivityGraphFacade graph = (ActivityGraphFacade)stateMachine;
            final Collection actionStates = graph.getActionStates();
            for (Iterator actionStateIterator = actionStates.iterator(); actionStateIterator.hasNext();)
            {
                final StateFacade actionState = (StateFacade) actionStateIterator.next();
                final Collection events = actionState.getDeferrableEvents();
                for (Iterator eventIterator = events.iterator(); eventIterator.hasNext();)
                {
                    final EventFacade event = (EventFacade) eventIterator.next();
                    if (event instanceof JBpmAction)
                    {
                        final JBpmAction action = (JBpmAction) event;
                        if (this.equals(action.getOperation()))
                        {
                            deferringEvents.add(event);
                        }
                    }
                }
            }

            final Collection transitions = graph.getTransitions();
            for (Iterator transitionIterator = transitions.iterator(); transitionIterator.hasNext();)
            {
                final TransitionFacade transition = (TransitionFacade) transitionIterator.next();
                final EventFacade event = transition.getTrigger();
                if (event != null)
                {
                    if (event instanceof JBpmAction)
                    {
                        final JBpmAction action = (JBpmAction) event;
                        if (this.equals(action.getOperation()))
                        {
                            deferringEvents.add(event);
                        }
                    }
                }
            }
        }

        return deferringEvents;
    }
}