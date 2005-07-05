package org.andromda.cartridges.jbpm.metafacades;

import org.andromda.metafacades.uml.ActivityGraphFacade;
import org.andromda.metafacades.uml.StateMachineFacade;
import org.andromda.core.common.StringUtilsHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.jbpm.metafacades.JBpmNode.
 *
 * @see org.andromda.cartridges.jbpm.metafacades.JBpmNode
 */
public class JBpmNodeLogicImpl
    extends JBpmNodeLogic
{

    public JBpmNodeLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    protected boolean handleIsContainedInBusinessProcess()
    {
        return this.getStateMachine() instanceof ActivityGraphFacade
                && ((ActivityGraphFacade)this.getStateMachine()).getUseCase() instanceof JBpmProcessDefinition;
    }

    /**
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmNode#isTaskNode()
     */
    protected boolean handleIsTaskNode()
    {
        return !getTasks().isEmpty();
    }

    /**
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmNode#getBeforeSignal()
     */
    protected java.util.List handleGetBeforeSignal()
    {
        final List beforeSignals = new ArrayList();

        final Collection events = this.getDeferrableEvents();
        for (final Iterator eventIterator = events.iterator(); eventIterator.hasNext();)
        {
            final Object eventObject = eventIterator.next();
            if (eventObject instanceof JBpmAction && ((JBpmAction)eventObject).isBeforeSignal())
            {
                beforeSignals.add(eventObject);
            }
        }

        return beforeSignals;
    }

    /**
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmNode#getAfterSignal()
     */
    protected java.util.List handleGetAfterSignal()
    {
        final List afterSignals = new ArrayList();

        final Collection events = this.getDeferrableEvents();
        for (final Iterator eventIterator = events.iterator(); eventIterator.hasNext();)
        {
            final Object eventObject = eventIterator.next();
            if (eventObject instanceof JBpmAction && ((JBpmAction)eventObject).isAfterSignal())
            {
                afterSignals.add(eventObject);
            }
        }

        return afterSignals;
    }

    /**
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmNode#getNodeEnter()
     */
    protected java.util.List handleGetNodeEnter()
    {
        final List nodeEnters = new ArrayList();

        final Collection events = this.getDeferrableEvents();
        for (final Iterator eventIterator = events.iterator(); eventIterator.hasNext();)
        {
            final Object eventObject = eventIterator.next();
            if (eventObject instanceof JBpmAction && ((JBpmAction)eventObject).isNodeEnter())
            {
                nodeEnters.add(eventObject);
            }
        }

        return nodeEnters;
    }

    /**
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmNode#getNodeLeave()
     */
    protected java.util.List handleGetNodeLeave()
    {
        final List nodeLeaves = new ArrayList();

        final Collection events = this.getDeferrableEvents();
        for (final Iterator eventIterator = events.iterator(); eventIterator.hasNext();)
        {
            final Object eventObject = eventIterator.next();
            if (eventObject instanceof JBpmAction && ((JBpmAction)eventObject).isNodeLeave())
            {
                nodeLeaves.add(eventObject);
            }
        }

        return nodeLeaves;
    }

    /**
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmNode#getTasks()
     */
    protected java.util.List handleGetTasks()
    {
        final List tasks = new ArrayList();

        final Collection events = this.getDeferrableEvents();
        for (final Iterator eventIterator = events.iterator(); eventIterator.hasNext();)
        {
            final Object eventObject = eventIterator.next();
            if (eventObject instanceof JBpmAction && ((JBpmAction)eventObject).isTask())
            {
                tasks.add(eventObject);
            }
        }

        return tasks;
    }

    protected List handleGetTimers()
    {
        final List timers = new ArrayList();

        final Collection events = this.getDeferrableEvents();
        for (final Iterator eventIterator = events.iterator(); eventIterator.hasNext();)
        {
            final Object eventObject = eventIterator.next();
            if (eventObject instanceof JBpmAction && ((JBpmAction)eventObject).isTimer())
            {
                timers.add(eventObject);
            }
        }

        return timers;
    }

    /**
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmNode#getSwimlane()
     */
    protected java.lang.Object handleGetSwimlane()
    {
        return this.getPartition();
    }

    protected String handleGetNodeClassName()
    {
        return StringUtilsHelper.upperCamelCaseName(this.getName()) + "Node";
    }

    protected String handleGetNodePackageName()
    {
        return (this.getProcessDefinition() == null) ? null : this.getProcessDefinition().getPackageName();
    }

    protected Object handleGetProcessDefinition()
    {
        Object processDefinition = null;

        final StateMachineFacade stateMachine = this.getStateMachine();
        if (stateMachine instanceof ActivityGraphFacade)
        {
            processDefinition = ((ActivityGraphFacade)stateMachine).getUseCase();
        }

        return (processDefinition instanceof JBpmProcessDefinition) ? processDefinition : null;
    }
}