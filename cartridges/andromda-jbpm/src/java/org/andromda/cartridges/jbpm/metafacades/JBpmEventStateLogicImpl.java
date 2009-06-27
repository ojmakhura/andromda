package org.andromda.cartridges.jbpm.metafacades;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.jbpm.metafacades.JBpmEventState.
 *
 * @see org.andromda.cartridges.jbpm.metafacades.JBpmEventState
 * @author Bob Fields
 */
public class JBpmEventStateLogicImpl
    extends JBpmEventStateLogic
{

    public JBpmEventStateLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    protected java.util.List handleGetBeforeSignals()
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

    protected java.util.List handleGetAfterSignals()
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

    protected java.util.List handleGetNodeEnters()
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

    protected java.util.List handleGetNodeLeaves()
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
}