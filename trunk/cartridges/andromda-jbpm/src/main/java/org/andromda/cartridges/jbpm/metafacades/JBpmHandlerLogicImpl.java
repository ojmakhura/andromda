package org.andromda.cartridges.jbpm.metafacades;

import java.util.ArrayList;
import java.util.List;
import org.andromda.metafacades.uml.ActivityGraphFacade;
import org.andromda.metafacades.uml.EventFacade;
import org.andromda.metafacades.uml.StateFacade;
import org.andromda.metafacades.uml.StateMachineFacade;
import org.andromda.metafacades.uml.TransitionFacade;
import org.andromda.utils.StringUtilsHelper;
import org.apache.commons.lang.StringUtils;

/**
 * MetafacadeLogic implementation for org.andromda.cartridges.jbpm.metafacades.JBpmHandler.
 *
 * @see org.andromda.cartridges.jbpm.metafacades.JBpmHandler
 */
public class JBpmHandlerLogicImpl
        extends JBpmHandlerLogic
{
    private static final long serialVersionUID = 34L;
    /**
     * @param metaObject
     * @param context
     */
    public JBpmHandlerLogicImpl(Object metaObject, String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmHandlerLogic#handleIsContainedInBusinessProcess()
     */
    protected boolean handleIsContainedInBusinessProcess()
    {
        return this.getOwner().getStateMachineContext() instanceof ActivityGraphFacade &&
                ((ActivityGraphFacade)this.getOwner().getStateMachineContext()).getUseCase()
                        instanceof JBpmProcessDefinition;
    }

    /**
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmHandlerLogic#handleIsAssignmentHandler()
     */
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

    /**
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmHandlerLogic#handleIsActionHandler()
     */
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

    private List<EventFacade> internalActions = null;

    private List<EventFacade> internalJBpmActions()
    {
        if (this.internalActions == null)
        {
            internalActions = new ArrayList<EventFacade>();
            final StateMachineFacade stateMachine = getOwner().getStateMachineContext();
            if (stateMachine instanceof ActivityGraphFacade)
            {
                final ActivityGraphFacade graph = (ActivityGraphFacade)stateMachine;
                for (final StateFacade state : graph.getStates())
                {
                    for (final EventFacade event : state.getDeferrableEvents())
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

                for (final TransitionFacade transition : graph.getTransitions())
                {
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

    /**
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmHandlerLogic#handleGetHandlerPackageName()
     */
    protected String handleGetHandlerPackageName()
    {
        return this.getOwner().getPackageName();
    }

    /**
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmHandlerLogic#handleGetHandlerFullPath()
     */
    protected String handleGetHandlerFullPath()
    {
        return StringUtils.replace(this.getClazz(), ".", "/");
    }

    /**
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmHandlerLogic#handleGetHandlerClassName()
     */
    protected String handleGetHandlerClassName()
    {
        return StringUtilsHelper.upperCamelCaseName(this.getName());
    }

    /**
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmHandlerLogic#handleGetClazz()
     */
    protected String handleGetClazz()
    {
        String handlerClass = null;

        final StringBuilder clazzBuffer = new StringBuilder();
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
