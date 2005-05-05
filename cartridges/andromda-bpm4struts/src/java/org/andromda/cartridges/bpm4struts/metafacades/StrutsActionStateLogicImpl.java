package org.andromda.cartridges.bpm4struts.metafacades;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.andromda.core.common.StringUtilsHelper;
import org.andromda.metafacades.uml.ActivityGraphFacade;
import org.andromda.metafacades.uml.CallEventFacade;
import org.andromda.metafacades.uml.EventFacade;
import org.andromda.metafacades.uml.TransitionFacade;
import org.andromda.metafacades.uml.UseCaseFacade;


/**
 * MetafacadeLogic implementation.
 *
 * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsActionState
 */
public class StrutsActionStateLogicImpl
        extends StrutsActionStateLogic
{
    // ---------------- constructor -------------------------------

    public StrutsActionStateLogicImpl(java.lang.Object metaObject, java.lang.String context)
    {
        super(metaObject, context);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class StrutsActionState ...

    protected java.lang.String handleGetActionMethodName()
    {
        return '_' + StringUtilsHelper.lowerCamelCaseName(getName());
    }

    // ------------- relations ------------------

    protected List handleGetContainerActions()
    {
        Collection actionSet = new HashSet();
        ActivityGraphFacade activityGraphFacade = this.getActivityGraph();

        if (activityGraphFacade instanceof StrutsActivityGraph)
        {
            StrutsActivityGraph activityGraph = (StrutsActivityGraph)activityGraphFacade;
            UseCaseFacade useCase = activityGraph.getUseCase();

            if (useCase instanceof StrutsUseCase)
            {
                Collection actions = ((StrutsUseCase)useCase).getActions();
                for (Iterator actionIterator = actions.iterator(); actionIterator.hasNext();)
                {
                    StrutsAction action = (StrutsAction)actionIterator.next();
                    if (action.getActionStates().contains(this))
                    {
                        actionSet.add(action);
                    }
                }
            }
        }
        return new ArrayList(actionSet);
    }


    protected List handleGetControllerCalls()
    {
        final List controllerCallsList = new ArrayList();
        final Collection deferrableEvents = getDeferrableEvents();
        for (Iterator iterator = deferrableEvents.iterator(); iterator.hasNext();)
        {
            EventFacade event = (EventFacade)iterator.next();
            if (event instanceof CallEventFacade)
            {
                Object operationObject = ((CallEventFacade)event).getOperation();
                if (operationObject != null)
                {
                    controllerCallsList.add(operationObject);
                }
            }
            else if (event instanceof StrutsTrigger)
            {
                Object callObject = ((StrutsTrigger)event).getControllerCall();
                if (callObject != null)
                {
                    controllerCallsList.add(callObject);
                }
            }
        }
        return controllerCallsList;
    }

    protected java.lang.Object handleGetForward()
    {
        Object forward = null;

        for (Iterator iterator = getOutgoing().iterator(); iterator.hasNext() && forward == null;)
        {
            final TransitionFacade transition = (TransitionFacade)iterator.next();
            if (!(transition instanceof StrutsExceptionHandler))
            {
                forward = transition;
            }
        }
        return forward;
    }

    protected java.util.List handleGetExceptions()
    {
        final Map exceptionsMap = new HashMap();
        final Collection outgoing = getOutgoing();
        for (Iterator iterator = outgoing.iterator(); iterator.hasNext();)
        {
            TransitionFacade transition = (TransitionFacade)iterator.next();
            if (transition instanceof StrutsExceptionHandler)
            {
                exceptionsMap.put(((StrutsExceptionHandler)transition).getExceptionKey(), transition);
            }
        }
        return new ArrayList(exceptionsMap.values());
    }

    protected boolean handleIsServerSide()
    {
        return !(this instanceof StrutsJsp);
    }
}
