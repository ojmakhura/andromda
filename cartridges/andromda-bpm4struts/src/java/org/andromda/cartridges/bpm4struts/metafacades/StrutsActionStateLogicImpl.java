package org.andromda.cartridges.bpm4struts.metafacades;

import org.andromda.cartridges.bpm4struts.Bpm4StrutsProfile;

import org.andromda.core.common.StringUtilsHelper;
import org.andromda.metafacades.uml.ActivityGraphFacade;
import org.andromda.metafacades.uml.CallEventFacade;
import org.andromda.metafacades.uml.EventFacade;
import org.andromda.metafacades.uml.TransitionFacade;
import org.andromda.metafacades.uml.UseCaseFacade;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;


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

    protected java.lang.String handleGetActionMethodName()
    {
        return '_' + StringUtilsHelper.lowerCamelCaseName(getName());
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
                UseCaseFacade useCase = activityGraph.getUseCase();

                if (useCase instanceof StrutsUseCase)
                {
                    Collection actions = ((StrutsUseCase)useCase).getActions();
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
                Object operationObject = ((CallEventFacade) event).getOperation();
                if (operationObject != null)
                {
                    controllerCallsList.add(operationObject);
                }
            }
            else if (event instanceof StrutsTrigger)
            {
                Object callObject = ((StrutsTrigger) event).getControllerCall();
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
        final Collection outgoing = getOutgoing();
        for (Iterator iterator = outgoing.iterator(); iterator.hasNext();)
        {
            TransitionFacade transition = (TransitionFacade) iterator.next();
            if (!(transition instanceof StrutsExceptionHandler))
                return transition;
        }
        return null;
    }

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

    protected boolean handleIsServerSide()
    {
        // all except pages
        return hasStereotype(Bpm4StrutsProfile.STEREOTYPE_VIEW) == false;
    }
}
