package org.andromda.cartridges.jbpm.metafacades;

import org.andromda.core.common.StringUtilsHelper;
import org.andromda.metafacades.uml.ActivityGraphFacade;
import org.andromda.metafacades.uml.EventFacade;
import org.andromda.metafacades.uml.StateFacade;
import org.andromda.metafacades.uml.StateMachineFacade;
import org.andromda.metafacades.uml.TransitionFacade;
import org.apache.commons.lang.StringUtils;

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

    protected boolean handleIsContainedInBusinessProcess()
    {
        return this.getOwner().getStateMachineContext() instanceof ActivityGraphFacade &&
                ((ActivityGraphFacade)this.getOwner().getStateMachineContext()).getUseCase()
                        instanceof JBpmProcessDefinition;
    }

    protected boolean handleIsAssignmentHandler()
    {
        boolean assignmentHandler = false;

        final List actions = internalJBpmActions();
        for (int i = 0; i < actions.size() && !assignmentHandler; i++)
        {
            final JBpmAction action = (JBpmAction)actions.get(i);
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
            final JBpmAction action = (JBpmAction)actions.get(i);
            actionHandler =
                    action.isAfterSignal() || action.isBeforeSignal() || action.isNodeEnter() || action.isNodeLeave() ||
                            action.isTimer();
        }

        return actionHandler;
    }

    private List internalActions = null;

    private List internalJBpmActions()
    {
        if (this.internalActions == null)
        {
            internalActions = new ArrayList();

            final StateMachineFacade stateMachine = getOwner().getStateMachineContext();
            if (stateMachine instanceof ActivityGraphFacade)
            {
                final ActivityGraphFacade graph = (ActivityGraphFacade)stateMachine;
                final Collection states = graph.getStates();
                for (final Iterator stateIterator = states.iterator(); stateIterator.hasNext();)
                {
                    final StateFacade state = (StateFacade)stateIterator.next();
                    final Collection events = state.getDeferrableEvents();
                    for (final Iterator eventIterator = events.iterator(); eventIterator.hasNext();)
                    {
                        final EventFacade event = (EventFacade)eventIterator.next();
                        if (event instanceof JBpmAction)
                        {
                            final JBpmAction action = (JBpmAction)event;
                            if (this.equals(action.getOperation()))
                            {
                                internalActions.add(event);
                            }
                        }
                    }
                }

                final Collection transitions = graph.getTransitions();
                for (final Iterator transitionIterator = transitions.iterator(); transitionIterator.hasNext();)
                {
                    final TransitionFacade transition = (TransitionFacade)transitionIterator.next();
                    final EventFacade event = transition.getTrigger();
                    if (event != null)
                    {
                        if (event instanceof JBpmAction)
                        {
                            final JBpmAction action = (JBpmAction)event;
                            if (this.equals(action.getOperation()))
                            {
                                internalActions.add(event);
                            }
                        }
                    }
                }
            }
        }

        return internalActions;
    }

    protected String handleGetHandlerPackageName()
    {
        return this.getOwner().getPackageName();
    }

    protected String handleGetHandlerFullPath()
    {
        return StringUtils.replace(this.getClazz(), ".", "/");
    }

    protected String handleGetHandlerClassName()
    {
        return StringUtilsHelper.upperCamelCaseName(this.getName());
    }

    protected String handleGetClazz()
    {
        String handlerClass = null;

        final StringBuffer clazzBuffer = new StringBuffer();
        if (StringUtils.isNotBlank(this.getHandlerPackageName()))
        {
            clazzBuffer.append(this.getHandlerPackageName());
            clazzBuffer.append('.');
        }
        clazzBuffer.append(this.getHandlerClassName());
        handlerClass = clazzBuffer.toString();

        return handlerClass;
    }
}