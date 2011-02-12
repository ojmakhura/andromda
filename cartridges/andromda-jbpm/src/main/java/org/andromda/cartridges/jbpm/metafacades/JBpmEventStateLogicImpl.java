package org.andromda.cartridges.jbpm.metafacades;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

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
    public JBpmEventStateLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmEventStateLogic#handleGetBeforeSignals()
     */
    protected List handleGetBeforeSignals()
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
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmEventStateLogic#handleGetAfterSignals()
     */
    protected List handleGetAfterSignals()
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
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmEventStateLogic#handleGetNodeEnters()
     */
    protected List handleGetNodeEnters()
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
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmEventStateLogic#handleGetNodeLeaves()
     */
    protected List handleGetNodeLeaves()
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
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmEventStateLogic#handleGetTasks()
     */
    protected List handleGetTasks()
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

    /**
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmEventStateLogic#handleGetTimers()
     */
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
}