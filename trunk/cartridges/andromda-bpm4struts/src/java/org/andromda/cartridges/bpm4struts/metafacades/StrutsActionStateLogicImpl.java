package org.andromda.cartridges.bpm4struts.metafacades;

import org.andromda.core.common.StringUtilsHelper;
import org.andromda.metafacades.uml.ActivityGraphFacade;
import org.andromda.metafacades.uml.CallEventFacade;
import org.andromda.metafacades.uml.EventFacade;
import org.andromda.metafacades.uml.TransitionFacade;

import java.util.*;


/**
 * MetafacadeLogic implementation.
 *
 * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsActionState
 */
public class StrutsActionStateLogicImpl
        extends StrutsActionStateLogic
        implements org.andromda.cartridges.bpm4struts.metafacades.StrutsActionState
{
    // ---------------- constructor -------------------------------

    public StrutsActionStateLogicImpl(java.lang.Object metaObject, java.lang.String context)
    {
        super(metaObject, context);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class StrutsActionState ...

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsActionState#getActionMethodName()()
     */
    public java.lang.String handleGetActionMethodName()
    {
        return StringUtilsHelper.lowerCamelCaseName(getName());
    }

    // ------------- relations ------------------

    private Collection containerActions = null;

    protected Collection handleGetContainerActions()
    {
        if (containerActions == null)
        {
            Collection actionSet = new HashSet();
            ActivityGraphFacade activityGraphFacade = this.getActivityGraph();

            if (activityGraphFacade instanceof StrutsActivityGraph)
            {
                StrutsActivityGraph activityGraph = (StrutsActivityGraph) activityGraphFacade;
                Collection pages = activityGraph.getUseCase().getPages();
                for (Iterator pageIterator = pages.iterator(); pageIterator.hasNext();)
                {
                    StrutsJsp jsp = (StrutsJsp) pageIterator.next();
                    Collection actions = jsp.getActions();
                    for (Iterator actionIterator = actions.iterator(); actionIterator.hasNext();)
                    {
                        StrutsAction action = (StrutsAction) actionIterator.next();
                        if (action.getActionStates().contains(this))
                        {
                            actionSet.add(action);
                        }
                    }
                }
            }
            this.containerActions = actionSet;
        }
        return this.containerActions;
    }


    protected Collection handleGetControllerCalls()
    {
        final Collection controllerCallsList = new ArrayList();
        final Collection deferrableEvents = getDeferrableEvents();
        for (Iterator iterator = deferrableEvents.iterator(); iterator.hasNext();)
        {
            EventFacade event = (EventFacade) iterator.next();
            if (event instanceof CallEventFacade)
            {
                controllerCallsList.add(((CallEventFacade) event).getOperation());
            }
            else if (event instanceof StrutsTrigger)
            {
                controllerCallsList.add(((StrutsTrigger) event).getControllerCall());
            }
        }
        return controllerCallsList;
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsActionState#getForward()
     */
    protected java.lang.Object handleGetForward()
    {
        final Collection outgoing = getOutgoing();
        for (Iterator iterator = outgoing.iterator(); iterator.hasNext();)
        {
            TransitionFacade transition = (TransitionFacade) iterator.next();
            if (!(transition instanceof StrutsExceptionHandler))
                return transition;
        }
        return null;
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsActionState#getExceptions()
     */
    protected java.util.Collection handleGetExceptions()
    {
        final Map exceptionsMap = new HashMap();
        final Collection outgoing = getOutgoing();
        for (Iterator iterator = outgoing.iterator(); iterator.hasNext();)
        {
            TransitionFacade transition = (TransitionFacade) iterator.next();
            if (transition instanceof StrutsExceptionHandler)
            {
                exceptionsMap.put(((StrutsExceptionHandler) transition).getExceptionKey(), transition);
            }
        }
        return exceptionsMap.values();
    }
}
