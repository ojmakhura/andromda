package org.andromda.cartridges.bpm4struts.metafacades;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.andromda.cartridges.bpm4struts.Bpm4StrutsProfile;
import org.andromda.metafacades.uml.EventFacade;
import org.andromda.metafacades.uml.GuardFacade;
import org.andromda.metafacades.uml.PseudostateFacade;
import org.andromda.metafacades.uml.StateVertexFacade;
import org.andromda.utils.StringUtilsHelper;
import org.apache.commons.lang.StringUtils;
/**
 * MetafacadeLogic implementation.
 *
 * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsForward
 * @author Bob Fields
 */
public class StrutsForwardLogicImpl
    extends StrutsForwardLogic
{
    /**
     * @param metaObject
     * @param context
     */
    public StrutsForwardLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    protected String handleGetGuardName()
    {
        final GuardFacade guard = this.getGuard();
        return (guard == null) ? null : guard.getName();
    }

    protected boolean handleIsEnteringPage()
    {
        return this.isEnteringView();
    }

    protected String handleGetForwardName()
    {
        return StringUtilsHelper.toResourceMessageKey(this.resolveName());
    }

    protected String handleGetForwardPath()
    {
        String forwardPath = null;

        final StateVertexFacade target = this.getTarget();
        if (isEnteringPage())
        {
            forwardPath = ((StrutsJsp)target).getFullPath() + ".jsp";
        }
        else if (isEnteringFinalState())
        {
            forwardPath = ((StrutsFinalState)target).getFullPath();
        }

        return forwardPath;
    }

    protected String handleGetActionMethodName()
    {
        return StringUtilsHelper.lowerCamelCaseName(this.resolveName());
    }

    protected String handleGetTargetNameKey()
    {
        if (this.isEnteringPage())
        {
            return ((StrutsJsp)this.getTarget()).getTitleKey();
        }
        else if (this.isEnteringFinalState())
        {
            return ((StrutsUseCase)((StrutsFinalState)this.getTarget()).getTargetUseCase()).getTitleKey();
        }
        return null;
    }

    /**
     * If this forward has a trigger this method returns that trigger's name, otherwise if this forward
     * has a name this method returns that name, otherwise if this forward's target has a name this
     * method returns that name, otherwise simply returns <code>"unknown"</code>
     */
    private String resolveName()
    {
        String forwardName = null;
        //trigger
        final EventFacade trigger = this.getTrigger();
        if (trigger != null) forwardName = trigger.getName();
        //name
        if (StringUtils.isEmpty(forwardName)) forwardName = this.getName();
        //target
        if (StringUtils.isEmpty(forwardName)) forwardName = this.getTarget().getName();
        // else
        if (StringUtils.isEmpty(forwardName)) forwardName = "unknown";
        // return
        return forwardName;
    }

    protected boolean handleIsExitingPage()
    {
        return this.isExitingView();
    }

    protected boolean handleIsSuccessMessagesPresent()
    {
        return !this.getSuccessMessages().isEmpty();
    }

    protected boolean handleIsWarningMessagesPresent()
    {
        return !this.getWarningMessages().isEmpty();
    }

    /**
     * Collects specific messages in a map.
     *
     * @param taggedValue the tagged value from which to read the message
     * @return maps message keys to message values, but only those that match the arguments
     *         will have been recorded
     */
    private Map getMessages(String taggedValue)
    {
        Map messages;

        final Collection taggedValues = this.findTaggedValues(taggedValue);
        if (taggedValues.isEmpty())
        {
            messages = Collections.emptyMap();
        }
        else
        {
            messages = new LinkedHashMap(); // we want to keep the order

            for (final Iterator iterator = taggedValues.iterator(); iterator.hasNext();)
            {
                final String value = (String)iterator.next();
                messages.put(StringUtilsHelper.toResourceMessageKey(value), value);
            }
        }

        return messages;
    }

    protected Map handleGetSuccessMessages()
    {
        return this.getMessages(Bpm4StrutsProfile.TAGGEDVALUE_ACTION_SUCCESS_MESSAGE);
    }

    protected Map handleGetWarningMessages()
    {
        return this.getMessages(Bpm4StrutsProfile.TAGGEDVALUE_ACTION_WARNING_MESSAGE);
    }

    protected Object handleGetStrutsActivityGraph()
    {
        return this.getFrontEndActivityGraph();
    }

    /**
     * Overridden since StrutsAction doesn't extend FrontEndAction.
     *
     * @see org.andromda.metafacades.uml.FrontEndForward#getActions()
     */
    public List getActions()
    {
        final Set actions = new LinkedHashSet();
        this.findActions(actions, new LinkedHashSet());
        return new ArrayList(actions);
    }

    /**
     * Recursively finds all actions for this forward, what this means depends on the context in which
     * this forward is used: if the source is a page action state it will collect all actions going out
     * of this page, if the source is a regular action state it will collect all actions that might traverse
     * this action state, if the source is the initial state it will collect all actions forwarding to this
     * forward's use-case (please not that those actions most likely are defined in other use-cases).
     *
     * @param actions         the default set of actions, duplicates will not be recorded
     * @param handledForwards the forwards already processed
     */
    private final void findActions(
        final Set actions,
        final Set handledForwards)
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
                final StateVertexFacade vertex = getSource();
                if (vertex instanceof StrutsJsp)
                {
                    final StrutsJsp jsp = (StrutsJsp)vertex;
                    actions.addAll(jsp.getActions());
                }
                else if (vertex instanceof StrutsActionState)
                {
                    final StrutsActionState actionState = (StrutsActionState)vertex;
                    actions.addAll(actionState.getContainerActions());
                }
                else if (vertex instanceof PseudostateFacade)
                {
                    final PseudostateFacade pseudostate = (PseudostateFacade)vertex;
                    if (!pseudostate.isInitialState())
                    {
                        final Collection incomingForwards = pseudostate.getIncomings();
                        for (final Iterator forwardIterator = incomingForwards.iterator(); forwardIterator.hasNext();)
                        {
                            final StrutsForward forward = (StrutsForward)forwardIterator.next();
                            actions.addAll(forward.getActions());
                        }
                    }
                }
            }
        }
    }
}
