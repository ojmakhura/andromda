package org.andromda.cartridges.bpm4struts.metafacades;

import org.andromda.cartridges.bpm4struts.Bpm4StrutsProfile;
import org.andromda.core.common.StringUtilsHelper;
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
    private String actionMethodName = null;
    private Collection controllerCalls = null;
    private Object forward = null;
    private Collection exceptions = null;

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
    public java.lang.String getActionMethodName()
    {
        if (Bpm4StrutsProfile.ENABLE_CACHE && actionMethodName != null) return actionMethodName;
        return actionMethodName = StringUtilsHelper.toJavaMethodName(getName());
    }

    // ------------- relations ------------------

    protected Collection handleGetControllerCalls()
    {
        if (Bpm4StrutsProfile.ENABLE_CACHE && controllerCalls != null) return controllerCalls;

        final Collection controllerCallsList = new LinkedList();
        final Collection deferrableEvents = getDeferrableEvents();
        for (Iterator iterator = deferrableEvents.iterator(); iterator.hasNext();)
        {
            EventFacade event = (EventFacade) iterator.next();
            if (event instanceof CallEventFacade)
            {
                controllerCallsList.add(((CallEventFacade) event).getOperation());
            } else if (event instanceof StrutsTrigger)
            {
                controllerCallsList.add(((StrutsTrigger) event).getControllerCall());
            }
        }
        return controllerCalls = controllerCallsList;
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsActionState#getForward()
     */
    protected java.lang.Object handleGetForward()
    {
        if (Bpm4StrutsProfile.ENABLE_CACHE && forward != null) return forward;

        final Collection outgoing = getOutgoing();
        for (Iterator iterator = outgoing.iterator(); iterator.hasNext();)
        {
            TransitionFacade transition = (TransitionFacade) iterator.next();
            if (!(transition instanceof StrutsExceptionHandler))
                return forward = transition;
        }
        return null;
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsActionState#getExceptions()
     */
    protected java.util.Collection handleGetExceptions()
    {
        if (Bpm4StrutsProfile.ENABLE_CACHE && exceptions != null) return exceptions;

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
        return exceptions = exceptionsMap.values();
    }
}
