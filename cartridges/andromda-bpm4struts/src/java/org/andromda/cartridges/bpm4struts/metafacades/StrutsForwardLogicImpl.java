package org.andromda.cartridges.bpm4struts.metafacades;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.andromda.cartridges.bpm4struts.Bpm4StrutsProfile;
import org.andromda.core.common.StringUtilsHelper;
import org.andromda.metafacades.uml.*;


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

    protected String handleGetGuardName()
    {
        final GuardFacade guard = getGuard();
        return (guard == null) ? null : guard.getName();
    }

    protected boolean handleIsTargettingActionState()
    {
        return getTarget() instanceof StrutsActionState;
    }

    protected boolean handleIsTargettingFinalState()
    {
        return getTarget() instanceof StrutsFinalState;
    }

    protected boolean handleIsTargettingDecisionPoint()
    {
        final StateVertexFacade target = getTarget();
        return target instanceof PseudostateFacade && ((PseudostateFacade) target).isDecisionPoint();
    }

    protected boolean handleIsTargettingPage()
    {
        return getTarget() instanceof StrutsJsp;
    }

    protected java.lang.String handleGetForwardName()
    {
        return StringUtilsHelper.toResourceMessageKey(resolveName());
    }

    protected java.lang.String handleGetForwardPath()
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

    protected String handleGetActionMethodName()
    {
        return StringUtilsHelper.lowerCamelCaseName(resolveName());
    }

    protected String handleGetTargetNameKey()
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

    protected boolean handleIsExitingPage()
    {
        return getSource() instanceof StrutsJsp;
    }

    protected boolean handleIsSuccessMessagesPresent()
    {
        return getSuccessMessages().isEmpty() == false;
    }

    protected boolean handleIsWarningMessagesPresent()
    {
        return getWarningMessages().isEmpty() == false;
    }

    protected String handleGetMessageKey()
    {
        String messageKey = null;

        StrutsUseCase useCase = getUseCase();
        if (useCase != null)
        {
            messageKey = useCase.getName();
        }
        return messageKey;
    }

    private Map getMessages(String messageType, String messageKey, String taggedValue)
    {
        Map messages = null;

        Collection taggedValues = findTaggedValues(taggedValue);
        if (taggedValues.isEmpty())
        {
            messages = Collections.EMPTY_MAP;
        }
        else
        {
            messages = new LinkedHashMap(); // we want to keep the order

            StringBuffer buffer = new StringBuffer();
            buffer.append(StringUtilsHelper.toResourceMessageKey(messageKey));
            buffer.append('.');
            buffer.append(messageType);
            buffer.append('.');

            String prefix = buffer.toString();
            for (Iterator iterator = taggedValues.iterator(); iterator.hasNext();)
            {
                String value = (String) iterator.next();
                messages.put(prefix + value.hashCode(), value);
            }
        }

        return messages;
    }

    protected Map handleGetSuccessMessages()
    {
        Map messages = null;

        String messageKey = getMessageKey();
        if (messageKey == null)
        {
            messages = Collections.EMPTY_MAP;
        }
        else
        {
            messages = getMessages("success", messageKey, Bpm4StrutsProfile.TAGGEDVALUE_ACTION_SUCCES_MESSAGE);
        }

        return messages;
    }

    protected Map handleGetWarningMessages()
    {
        Map messages = null;

        String messageKey = getMessageKey();
        if (messageKey == null)
        {
            messages = Collections.EMPTY_MAP;
        }
        else
        {
            messages = getMessages("warning", messageKey, Bpm4StrutsProfile.TAGGEDVALUE_ACTION_WARNING_MESSAGE);
        }
        return messages;
    }

    protected java.util.Collection handleGetForwardParameters()
    {
        final EventFacade trigger = getTrigger();
        return (trigger == null) ? Collections.EMPTY_LIST : trigger.getParameters();
    }

    protected Object handleGetDecisionTrigger()
    {
        return (isTargettingDecisionPoint()) ? getTrigger() : null;
    }

    protected Object handleGetStrutsActivityGraph()
    {
        Object graph = getSource().getActivityGraph();
        return (graph instanceof StrutsActivityGraph) ? graph : null;
    }

    protected Collection handleGetActions()
    {
        Set actions = new HashSet();
        findActions(actions, new HashSet());
        return actions;
    }

    private void findActions(Set actions, Set handledForwards)
    {
        if (!handledForwards.contains(this))
        {
            handledForwards.add(this);
            
            if (this instanceof StrutsAction) // @todo this is not so nice because StrutsAction extends StrutsForward, solution would be to override in StrutsAction
            {
                actions.add(this);
            }
            else
            {
                StateVertexFacade vertex = getSource();
                if (vertex instanceof StrutsJsp)
                {
                    StrutsJsp jsp = (StrutsJsp)vertex;
                    actions.addAll(jsp.getActions());
                }
                else if (vertex instanceof StrutsActionState)
                {
                    StrutsActionState actionState = (StrutsActionState)vertex;
                    actions.addAll(actionState.getContainerActions());
                }
                else if (vertex instanceof PseudostateFacade)
                {
                    PseudostateFacade pseudostate = (PseudostateFacade)vertex;
                    if (!pseudostate.isInitialState())
                    {
                        Collection incomingForwards = pseudostate.getIncoming();
                        for (Iterator forwardIterator = incomingForwards.iterator(); forwardIterator.hasNext();)
                        {
                            StrutsForward forward = (StrutsForward) forwardIterator.next();
                            actions.addAll(forward.getActions());
                        }
                    }
                }
            }
        }
    }

    protected Object handleGetUseCase()
    {
        StrutsUseCase useCase = null;

        StrutsActivityGraph graph = getStrutsActivityGraph();
        if (graph != null)
        {
            UseCaseFacade graphUseCase = graph.getUseCase();
            if (graphUseCase instanceof StrutsUseCase)
            {
                useCase = (StrutsUseCase)graphUseCase;
            }
        }

        return useCase;
    }
}
