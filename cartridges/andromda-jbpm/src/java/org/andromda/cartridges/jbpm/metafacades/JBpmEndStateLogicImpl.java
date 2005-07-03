package org.andromda.cartridges.jbpm.metafacades;

import org.andromda.metafacades.uml.EventFacade;
import org.andromda.metafacades.uml.ActivityGraphFacade;

import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.jbpm.metafacades.JBpmEndState.
 *
 * @see org.andromda.cartridges.jbpm.metafacades.JBpmEndState
 */
public class JBpmEndStateLogicImpl
    extends JBpmEndStateLogic
{

    public JBpmEndStateLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    protected boolean handleIsContainedInBusinessProcess()
    {
        return this.getStateMachine() instanceof ActivityGraphFacade
                && ((ActivityGraphFacade)this.getStateMachine()).getUseCase() instanceof JBpmProcessDefinition;
    }

    /**
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmEndState#getBeforeSignal()
     */
    protected java.util.List handleGetBeforeSignal()
    {
        final List beforeSignal = new ArrayList();

        final Collection events = getDeferrableEvents();
        for (final Iterator eventIterator = events.iterator(); eventIterator.hasNext();)
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
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmEndState#getNodeLeave()
     */
    protected java.util.List handleGetNodeLeave()
    {
        final List nodeLeave = new ArrayList();

        final Collection events = getDeferrableEvents();
        for (final Iterator eventIterator = events.iterator(); eventIterator.hasNext();)
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
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmEndState#getNodeEnter()
     */
    protected java.util.List handleGetNodeEnter()
    {
        final List nodeEnter = new ArrayList();

        final Collection events = getDeferrableEvents();
        for (final Iterator eventIterator = events.iterator(); eventIterator.hasNext();)
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
     * @see org.andromda.cartridges.jbpm.metafacades.JBpmEndState#getAfterSignal()
     */
    protected java.util.List handleGetAfterSignal()
    {
        final List afterSignals = new ArrayList();

        final Collection events = getDeferrableEvents();
        for (final Iterator eventIterator = events.iterator(); eventIterator.hasNext();)
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

}