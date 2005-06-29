package org.andromda.cartridges.jbpm.metafacades;

import org.andromda.cartridges.jbpm.JBpmProfile;
import org.andromda.metafacades.uml.EventFacade;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.jbpm.metafacades.JBpmState.
 *
 * @see org.andromda.cartridges.jbpm.metafacades.JBpmState
 */
public class JBpmStateLogicImpl
    extends JBpmStateLogic
{

    public JBpmStateLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    protected boolean handleIsTaskNode()
    {
        return hasStereotype(JBpmProfile.STEREOTYPE_TASK);
    }

    /**
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmState#getBeforeSignal()
     */
    protected java.util.List handleGetBeforeSignal()
    {
        final List beforeSignal = new ArrayList();

        final Collection events = getDeferrableEvents();
        for (Iterator eventIterator = events.iterator(); eventIterator.hasNext();)
        {
            final EventFacade event = (EventFacade)eventIterator.next();
            if (event instanceof JBpmAction)
            {
                final JBpmAction action = (JBpmAction)event;
                if (action.isBeforeSignal())
                {
                    beforeSignal.add(action);
                }
            }
        }

        return beforeSignal;
    }

    /**
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmState#getAfterSignal()
     */
    protected java.util.List handleGetAfterSignal()
    {
        final List afterSignals = new ArrayList();

        final Collection events = getDeferrableEvents();
        for (Iterator eventIterator = events.iterator(); eventIterator.hasNext();)
        {
            final EventFacade event = (EventFacade)eventIterator.next();
            if (event instanceof JBpmAction)
            {
                final JBpmAction action = (JBpmAction)event;
                if (action.isAfterSignal())
                {
                    afterSignals.add(action);
                }
            }
        }

        return afterSignals;
    }

    /**
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmState#getNodeEnter()
     */
    protected java.util.List handleGetNodeEnter()
    {
        final List nodeEnter = new ArrayList();

        final Collection events = getDeferrableEvents();
        for (Iterator eventIterator = events.iterator(); eventIterator.hasNext();)
        {
            final EventFacade event = (EventFacade)eventIterator.next();
            if (event instanceof JBpmAction)
            {
                final JBpmAction action = (JBpmAction)event;
                if (action.isNodeEnter())
                {
                    nodeEnter.add(action);
                }
            }
        }

        return nodeEnter;
    }

    /**
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmState#getNodeLeave()
     */
    protected java.util.List handleGetNodeLeave()
    {
        final List nodeLeave = new ArrayList();

        final Collection events = getDeferrableEvents();
        for (Iterator eventIterator = events.iterator(); eventIterator.hasNext();)
        {
            final EventFacade event = (EventFacade)eventIterator.next();
            if (event instanceof JBpmAction)
            {
                final JBpmAction action = (JBpmAction)event;
                if (action.isNodeLeave())
                {
                    nodeLeave.add(action);
                }
            }
        }

        return nodeLeave;
    }

    /**
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmState#getTasks()
     */
    protected java.util.List handleGetTasks()
    {
        final List tasks = new ArrayList();

        final Collection events = getDeferrableEvents();
        for (Iterator eventIterator = events.iterator(); eventIterator.hasNext();)
        {
            final EventFacade event = (EventFacade)eventIterator.next();
            if (event instanceof JBpmAction)
            {
                final JBpmAction action = (JBpmAction)event;
                if (action.isTask())
                {
                    tasks.add(action);
                }
            }
        }

        return tasks;
    }

    protected Object handleGetSwimlane()
    {
        return this.getPartition();
    }
}