package org.andromda.cartridges.jbpm.metafacades;

import org.andromda.cartridges.jbpm.JBpmProfile;
import org.andromda.metafacades.uml.ActivityGraphFacade;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.OperationFacade;
import org.andromda.metafacades.uml.StateMachineFacade;
import org.apache.commons.lang.StringUtils;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.jbpm.metafacades.JBpmAction.
 *
 * @see org.andromda.cartridges.jbpm.metafacades.JBpmAction
 */
public class JBpmActionLogicImpl
        extends JBpmActionLogic
{
    public JBpmActionLogicImpl(Object metaObject, String context)
    {
        super(metaObject, context);
    }

    protected boolean handleIsContainedInBusinessProcess()
    {
        boolean containedInBusinessProcess = false;
        if (this.getState() != null)
        {
            final StateMachineFacade stateMachine = this.getState().getStateMachine();
            if (stateMachine instanceof ActivityGraphFacade)
            {
                final ActivityGraphFacade activityGraph = (ActivityGraphFacade)stateMachine;
                containedInBusinessProcess = activityGraph.getUseCase() instanceof JBpmProcessDefinition;
            }
        }
        else
        {
            containedInBusinessProcess = this.getTransition() instanceof JBpmTransition;
        }
        return containedInBusinessProcess;
    }

    /**
     * We override this method in order to be able to return the call-event's operation name
     * when the event's name itself has not been specified.
     */
    public String getName()
    {
        String name = super.getName();

        if (StringUtils.isBlank(name))
        {
            final ModelElementFacade operation = getOperation();
            if (operation != null)
            {
                name = operation.getName();
            }
        }

        return name;
    }

    /**
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmAction#isBeforeSignal()
     */
    protected boolean handleIsBeforeSignal()
    {
        return this.hasStereotype(JBpmProfile.STEREOTYPE_BEFORE_SIGNAL);
    }

    /**
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmAction#isAfterSignal()
     */
    protected boolean handleIsAfterSignal()
    {
        return this.hasStereotype(JBpmProfile.STEREOTYPE_AFTER_SIGNAL);
    }

    /**
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmAction#isNodeEnter()
     */
    protected boolean handleIsNodeEnter()
    {
        return this.hasStereotype(JBpmProfile.STEREOTYPE_NODE_ENTER);
    }

    /**
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmAction#isNodeLeave()
     */
    protected boolean handleIsNodeLeave()
    {
        return this.hasStereotype(JBpmProfile.STEREOTYPE_NODE_LEAVE);
    }

    /**
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmAction#isTask()
     */
    protected boolean handleIsTask()
    {
        // tasks may only be used on a node, not on a wait-state
        return this.hasStereotype(JBpmProfile.STEREOTYPE_TASK) && this.getState() instanceof JBpmNode;
    }

    /**
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmAction#isTimer()
     */
    protected boolean handleIsTimer()
    {
        return this.hasStereotype(JBpmProfile.STEREOTYPE_TIMER);
    }

    /**
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmAction#isBlocking()
     */
    protected boolean handleIsBlocking()
    {
        final String blocking = (String)this.findTaggedValue(JBpmProfile.TAGGEDVALUE_TASK_BLOCKING);
        return blocking == null || Boolean.valueOf(blocking);
    }

    /**
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmAction#getDueDate()
     */
    protected java.lang.String handleGetDueDate()
    {
        return isTimer() ? (String)findTaggedValue(JBpmProfile.TAGGEDVALUE_TIMER_DUEDATE) : null;
    }

    protected java.lang.String handleGetTimerRepeat()
    {
        return (String)findTaggedValue(JBpmProfile.TAGGEDVALUE_TIMER_REPEAT);
    }

    protected java.lang.String handleGetTimerTransition()
    {
        return (String)findTaggedValue(JBpmProfile.TAGGEDVALUE_TIMER_TRANSITION);
    }

    /**
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmAction#getClazz()
     */
    protected java.lang.String handleGetClazz()
    {
        String clazz = null;
        if (this.isAssignment() || this.isTimer())
        {
            final OperationFacade handler = this.getOperation();

            if (handler instanceof JBpmHandler)
            {
                final StringBuffer clazzBuffer = new StringBuffer();
                final String packageName = handler.getOwner().getPackageName();
                clazzBuffer.append(packageName);
                if (StringUtils.isNotBlank(packageName))
                {
                    clazzBuffer.append('.');
                }
                clazzBuffer.append(((JBpmHandler)handler).getHandlerClassName());
                clazz = clazzBuffer.toString();
            }
        }

        return clazz;
    }

    /**
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmAction#getConfigType()
     */
    protected java.lang.String handleGetConfigType()
    {
        // @todo
        return null;
    }

    /**
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmAction#isAssignment()
     */
    protected boolean handleIsAssignment()
    {
        return this.getOperation() != null;
    }
}