package org.andromda.cartridges.bpm4struts.metafacades;

import org.andromda.cartridges.bpm4struts.Bpm4StrutsProfile;
import org.andromda.core.common.StringUtilsHelper;
import org.andromda.metafacades.uml.EventFacade;
import org.andromda.metafacades.uml.GuardFacade;
import org.andromda.metafacades.uml.PseudostateFacade;
import org.andromda.metafacades.uml.StateVertexFacade;

import java.util.*;


/**
 * MetafacadeLogic implementation.
 *
 * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsForward
 */
public class StrutsForwardLogicImpl
        extends StrutsForwardLogic
        implements org.andromda.cartridges.bpm4struts.metafacades.StrutsForward
{
    // ---------------- constructor -------------------------------

    public StrutsForwardLogicImpl(java.lang.Object metaObject, java.lang.String context)
    {
        super(metaObject, context);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class StrutsForward ...

    public String handleGetGuardName()
    {
        final GuardFacade guard = getGuard();
        return (guard == null) ? null : guard.getName();
    }

    public boolean handleIsTargettingActionState()
    {
        return getTarget() instanceof StrutsActionState;
    }

    public boolean handleIsTargettingFinalState()
    {
        return getTarget() instanceof StrutsFinalState;
    }

    public boolean handleIsTargettingDecisionPoint()
    {
        final StateVertexFacade target = getTarget();
        return target instanceof PseudostateFacade && ((PseudostateFacade) target).isDecisionPoint();
    }

    public boolean handleIsTargettingPage()
    {
        return getTarget() instanceof StrutsJsp;
    }

    public java.lang.String handleGetForwardName()
    {
        return StringUtilsHelper.toResourceMessageKey(resolveName());
    }

    public java.lang.String handleGetForwardPath()
    {
        final StateVertexFacade target = getTarget();
        if (isTargettingPage())
        {
            return ((StrutsJsp) target).getFullPath() + ".jsp";
        }
        else if (isTargettingFinalState())
        {
            return ((StrutsFinalState) target).getFullPath() + ".do";
        }
        else
            return null;
    }

    public String handleGetActionMethodName()
    {
        return StringUtilsHelper.lowerCamelCaseName(resolveName());
    }

    public String handleGetTargetNameKey()
    {
        if (isTargettingPage())
        {
            return ((StrutsJsp) getTarget()).getTitleKey();
        }
        else if (isTargettingFinalState())
        {
            return ((StrutsFinalState) getTarget()).getTargetUseCase().getTitleKey();
        }
        return null;
    }

    private String resolveName()
    {
        String forwardName = null;
        //trigger
        final EventFacade trigger = getTrigger();
        if (trigger != null) forwardName = trigger.getName();
        //name
        if (forwardName == null) forwardName = getName();
        //target
        if (forwardName == null) forwardName = getTarget().getName();
        // else
        if (forwardName == null) forwardName = "unknown";
        // return
        return forwardName;
    }

    public boolean handleIsExitingPage()
    {
        return getSource() instanceof StrutsJsp;
    }

    public boolean handleIsSuccessMessagesPresent()
    {
        return getSuccessMessages().isEmpty() == false;
    }

    public boolean handleIsWarningMessagesPresent()
    {
        return getWarningMessages().isEmpty() == false;
    }

    public String handleGetMessageKey()
    {
        String messageKey = getStrutsActivityGraph().getUseCase().getName();
        return StringUtilsHelper.toResourceMessageKey(messageKey);
    }

    private Map getMessages(String messageKey, String taggedValue)
    {
        Collection taggedValues = findTaggedValues(taggedValue);

        Map messages = new LinkedHashMap();

        for (Iterator iterator = taggedValues.iterator(); iterator.hasNext();)
        {
            String value = (String) iterator.next();
            messages.put(messageKey + value.hashCode(), value);
        }

        return messages;
    }

    public Map handleGetSuccessMessages()
    {
        return getMessages(getMessageKey() + ".success.", Bpm4StrutsProfile.TAGGED_VALUE_ACTION_SUCCES_MESSAGE);
    }

    public Map handleGetWarningMessages()
    {
        return getMessages(getMessageKey() + ".warning.", Bpm4StrutsProfile.TAGGED_VALUE_ACTION_WARNING_MESSAGE);
    }

    // ------------- relations ------------------

    protected java.util.Collection handleGetForwardParameters()
    {
        final EventFacade trigger = getTrigger();
        return (trigger == null) ? Collections.EMPTY_LIST : trigger.getParameters();
    }

    protected Object handleGetDecisionTrigger()
    {
        return getTrigger();
    }

    protected Object handleGetStrutsActivityGraph()
    {
        Object graph = getSource().getActivityGraph();
        return (graph instanceof StrutsActivityGraph) ? graph : null;
    }
}
