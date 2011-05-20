package org.andromda.cartridges.jbpm.metafacades;

import java.util.ArrayList;
import java.util.List;
import org.andromda.metafacades.uml.EventFacade;

/**
 * MetafacadeLogic implementation for org.andromda.cartridges.jbpm.metafacades.JBpmEventState.
 *
 * @see org.andromda.cartridges.jbpm.metafacades.JBpmEventState
 * @author Bob Fields
 */
public class JBpmEventStateLogicImpl
    extends JBpmEventStateLogic
{
    private static final long serialVersionUID = 34L;
    /**
     * @param metaObject
     * @param context
     */
    public JBpmEventStateLogicImpl(Object metaObject, String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmEventStateLogic#handleGetBeforeSignals()
     */
    protected List<JBpmAction> handleGetBeforeSignals()
    {
        final List<JBpmAction> beforeSignals = new ArrayList<JBpmAction>();
        for (final EventFacade eventObject : this.getDeferrableEvents())
        {
            if (eventObject instanceof JBpmAction && ((JBpmAction)eventObject).isBeforeSignal())
            {
                beforeSignals.add((JBpmAction)eventObject);
            }
        }
        return beforeSignals;
    }

    /**
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmEventStateLogic#handleGetAfterSignals()
     */
    protected List<JBpmAction> handleGetAfterSignals()
    {
        final List<JBpmAction> afterSignals = new ArrayList<JBpmAction>();
        for (final EventFacade eventObject : this.getDeferrableEvents())
        {
            if (eventObject instanceof JBpmAction && ((JBpmAction)eventObject).isAfterSignal())
            {
                afterSignals.add((JBpmAction)eventObject);
            }
        }
        return afterSignals;
    }

    /**
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmEventStateLogic#handleGetNodeEnters()
     */
    protected List<JBpmAction> handleGetNodeEnters()
    {
        final List<JBpmAction> nodeEnters = new ArrayList<JBpmAction>();
        for (final EventFacade eventObject : this.getDeferrableEvents())
        {
            if (eventObject instanceof JBpmAction && ((JBpmAction)eventObject).isNodeEnter())
            {
                nodeEnters.add((JBpmAction)eventObject);
            }
        }
        return nodeEnters;
    }

    /**
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmEventStateLogic#handleGetNodeLeaves()
     */
    protected List<JBpmAction> handleGetNodeLeaves()
    {
        final List<JBpmAction> nodeLeaves = new ArrayList<JBpmAction>();
        for (final EventFacade eventObject : this.getDeferrableEvents())
        {
            if (eventObject instanceof JBpmAction && ((JBpmAction)eventObject).isNodeLeave())
            {
                nodeLeaves.add((JBpmAction)eventObject);
            }
        }
        return nodeLeaves;
    }

    /**
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmEventStateLogic#handleGetTasks()
     */
    protected List<JBpmAction> handleGetTasks()
    {
        final List<JBpmAction> tasks = new ArrayList<JBpmAction>();
        for (final EventFacade eventObject : this.getDeferrableEvents())
        {
            if (eventObject instanceof JBpmAction && ((JBpmAction)eventObject).isTask())
            {
                tasks.add((JBpmAction)eventObject);
            }
        }
        return tasks;
    }

    /**
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmEventStateLogic#handleGetTimers()
     */
    protected List<JBpmAction> handleGetTimers()
    {
        final List<JBpmAction> timers = new ArrayList<JBpmAction>();
        for (final EventFacade eventObject : this.getDeferrableEvents())
        {
            if (eventObject instanceof JBpmAction && ((JBpmAction)eventObject).isTimer())
            {
                timers.add((JBpmAction)eventObject);
            }
        }
        return timers;
    }
}
