package org.andromda.cartridges.bpm4struts.metafacades;

import org.andromda.cartridges.bpm4struts.Bpm4StrutsProfile;
import org.andromda.core.common.StringUtilsHelper;
import org.andromda.metafacades.uml.PseudostateFacade;
import org.andromda.metafacades.uml.StateVertexFacade;
import org.andromda.metafacades.uml.TransitionFacade;
import org.andromda.metafacades.uml.EventFacade;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Collections;


/**
 * MetafacadeLogic implementation.
 *
 * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsAction
 */
public class StrutsActionLogicImpl
        extends StrutsActionLogic
        implements org.andromda.cartridges.bpm4struts.metafacades.StrutsAction
{
    private Collection actionStates = null;
    private Collection actionForwards = null;
    private Collection decisionTransitions = null;

    // ---------------- constructor -------------------------------
    
    public StrutsActionLogicImpl(Object metaObject, String context)
    {
        super(metaObject, context);
    }

    private void initializeCollections()
    {
        actionStates = new HashSet();
        actionForwards = new HashSet();
        decisionTransitions = new HashSet();
        collectTransitions(this, new HashSet());
    }

    private void collectTransitions(TransitionFacade transition, Collection processedTransitions)
    {
        if (processedTransitions.contains(transition))
        {
            return;
        }
        else
        {
            processedTransitions.add(transition);
        }

        final StateVertexFacade target = transition.getTarget();
        if ( (target instanceof StrutsJsp) || (target instanceof StrutsFinalState) )
        {
            actionForwards.add(transition);
        }
        else if ( (target instanceof PseudostateFacade) && ((PseudostateFacade)target).isDecisionPoint() )
        {
            decisionTransitions.add(transition);
            Collection outcomes = target.getOutgoing();
            for (Iterator iterator = outcomes.iterator(); iterator.hasNext();)
            {
                TransitionFacade outcome = (TransitionFacade) iterator.next();
                collectTransitions(outcome, processedTransitions);
            }
        }
        else if (target instanceof StrutsActionState)
        {
            actionStates.add(target);
            collectTransitions( ((StrutsActionState)target).getForward(), processedTransitions );
        }
        else    // all the rest is ignored but outgoing transitions are further processed
        {
            Collection outcomes = target.getOutgoing();
            for (Iterator iterator = outcomes.iterator(); iterator.hasNext();)
            {
                TransitionFacade outcome = (TransitionFacade) iterator.next();
                collectTransitions(outcome, processedTransitions);
            }
        }
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class StrutsAction ...

    public String getActionName()
    {
        return getActivityGraph().getUseCase().getFormBeanName();
    }

    public String getActionInput()
    {
        final StateVertexFacade source = getSource();
        return (source instanceof StrutsJsp) ? ((StrutsJsp)source).getFullPath() : "";
    }

    public boolean isFormPost()
    {
        return !isHyperlink();
    }

    public boolean isHyperlink()
    {
       return Bpm4StrutsProfile.TAGGED_VALUE_ACTION_TYPE_HYPERLINK.equalsIgnoreCase(
               findTaggedValue(Bpm4StrutsProfile.TAGGED_VALUE_ACTION_TYPE));
    }

    public boolean hasSuccessMessage()
    {
        return null != findTaggedValue(Bpm4StrutsProfile.TAGGED_VALUE_ACTION_SUCCES_MESSAGE);
    }

    public java.lang.String getActionPath()
    {
        return getActionPathRoot() + '/' + getActionClassName();
    }

    public String getActionPathRoot()
    {
        return '/' + StringUtilsHelper.toJavaClassName(getActivityGraph().getUseCase().getName());
    }


    public java.lang.String getActionRoles()
    {
        final Collection users = getActivityGraph().getUseCase().getAllUsers();
        StringBuffer rolesBuffer = new StringBuffer();
        for (Iterator userIterator = users.iterator(); userIterator.hasNext();)
        {
            StrutsUser strutsUser = (StrutsUser) userIterator.next();
            rolesBuffer.append(strutsUser.getRole() + ' ');
        }
        return StringUtilsHelper.separate(rolesBuffer.toString(), ",");
    }

    public String getActionClassName()
    {
        String name = null;
        final StateVertexFacade source = getSource();

        if (source instanceof PseudostateFacade)
        {
            PseudostateFacade pseudostate = (PseudostateFacade)source;
            if (pseudostate.isInitialState())
                name = getActivityGraph().getUseCase().getName();
        }
        else
        {
            final EventFacade trigger = getTrigger();
            final String suffix = (trigger == null) ? getTarget().getName() : trigger.getName();
            name = getSource().getName() + ' ' + suffix;
        }
        return StringUtilsHelper.toJavaClassName(name);
    }

    public String getFormBeanClassName()
    {
        return getActionClassName() + "ActionForm";
    }

    public String getFormBeanName()
    {
        return StringUtilsHelper.lowerCaseFirstLetter(getFormBeanClassName());
    }

    public String getFormValidationMethodName()
    {
        return "validate" + getFormBeanClassName();
    }

    public String getMessageKey()
    {
        String messageKey = getActivityGraph().getUseCase().getName() + ' ';
        messageKey += (isUseCaseStart()) ? messageKey : getInput().getName();
        return StringUtilsHelper.toResourceMessageKey(messageKey);
    }

    public String getSuccessMessageKey()
    {
        return getMessageKey() + ".success";
    }

    public String getSuccessMessageValue()
    {
        return '[' + getMessageKey() + "] succesfully executed on " + getInput().getTitleValue();
    }

    public String getPackageName()
    {
        return getActivityGraph().getController().getPackageName();
    }

    public boolean isResettable()
    {
        return null != findTaggedValue(Bpm4StrutsProfile.TAGGED_VALUE_ACTION_RESETTABLE);
    }

    public boolean isUseCaseStart()
    {
        StateVertexFacade source = getSource();
        return source instanceof PseudostateFacade && ((PseudostateFacade)source).isInitialState();
    }

    public String getFullActionPath()
    {
        return '/' + (getPackageName() + '/' + getActionClassName()).replace('.','/');
    }

    public String getFullFormBeanPath()
    {
        return '/' + (getPackageName() + '/' + getFormBeanClassName()).replace('.','/');
    }

    // ------------- relations ------------------

    protected Collection handleGetActionForwards()
    {
        if (actionForwards == null) initializeCollections();
        return actionForwards;
    }

    protected Collection handleGetDecisionTransitions()
    {
        if (decisionTransitions == null) initializeCollections();
        return decisionTransitions;
    }

    protected Collection handleGetActionStates()
    {
        if (actionStates == null) initializeCollections();
        return actionStates;
    }

    protected Collection handleGetActionExceptions()
    {
        final Collection exceptions = new HashSet();

        final Collection actionStates = getActionStates();
        for (Iterator iterator = actionStates.iterator(); iterator.hasNext();)
        {
            StrutsActionState actionState = (StrutsActionState)iterator.next();
            exceptions.addAll(actionState.getExceptions());
        }

        return exceptions;
    }

    protected java.lang.Object handleGetInput()
    {
        return getSource();
    }

    protected Object handleGetActivityGraph()
    {
        return getSource().getActivityGraph();
    }

    protected Object handleGetController()
    {
        return getActivityGraph().getController();
    }

    protected Object handleGetActionTrigger()
    {
        return getTrigger();
    }

    protected Collection handleGetActionParameters()
    {
        StrutsTrigger trigger = getActionTrigger();
        return (trigger == null) ? Collections.EMPTY_LIST : trigger.getParameters();
    }
}
