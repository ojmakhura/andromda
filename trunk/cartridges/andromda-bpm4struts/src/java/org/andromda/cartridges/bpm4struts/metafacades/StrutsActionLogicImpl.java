package org.andromda.cartridges.bpm4struts.metafacades;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import org.andromda.cartridges.bpm4struts.Bpm4StrutsProfile;
import org.andromda.core.common.StringUtilsHelper;
import org.andromda.metafacades.uml.EventFacade;
import org.andromda.metafacades.uml.OperationFacade;
import org.andromda.metafacades.uml.ParameterFacade;
import org.andromda.metafacades.uml.PseudostateFacade;
import org.andromda.metafacades.uml.StateVertexFacade;
import org.andromda.metafacades.uml.TransitionFacade;


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
        processedTransitions.add(transition);

        final StateVertexFacade target = transition.getTarget();
        if ((target instanceof StrutsJsp) || (target instanceof StrutsFinalState))
        {
            actionForwards.add(transition);
        } else if ((target instanceof PseudostateFacade) && ((PseudostateFacade) target).isDecisionPoint())
        {
            decisionTransitions.add(transition);
            Collection outcomes = target.getOutgoing();
            for (Iterator iterator = outcomes.iterator(); iterator.hasNext();)
            {
                TransitionFacade outcome = (TransitionFacade) iterator.next();
                collectTransitions(outcome, processedTransitions);
            }
        } else if (target instanceof StrutsActionState)
        {
            actionStates.add(target);
            collectTransitions(((StrutsActionState) target).getForward(), processedTransitions);
        } else    // all the rest is ignored but outgoing transitions are further processed
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

    public String handleGetActionName()
    {
        return getActivityGraph().getUseCase().getFormBeanName();
    }

    public String handleGetActionInput()
    {
        final StateVertexFacade source = getSource();
        return (source instanceof StrutsJsp) ? ((StrutsJsp) source).getFullPath() : "";
    }

    public boolean handleIsFormPost()
    {
        return !isHyperlink();
    }

    public boolean handleIsHyperlink()
    {
        Object value = findTaggedValue(Bpm4StrutsProfile.TAGGED_VALUE_ACTION_TYPE);
        return Bpm4StrutsProfile.TAGGED_VALUE_ACTION_TYPE_HYPERLINK.equalsIgnoreCase(value==null?null:value.toString());
    }

    public boolean handleHasSuccessMessage()
    {
        Object value = findTaggedValue(Bpm4StrutsProfile.TAGGED_VALUE_ACTION_SUCCES_MESSAGE);
        return isTrue(value==null?null:value.toString());
    }

    public java.lang.String handleGetActionPath()
    {
        return getActionPathRoot() + '/' + getActionClassName();
    }

    public String handleGetActionPathRoot()
    {
        return '/' + StringUtilsHelper.toJavaClassName(getActivityGraph().getUseCase().getName());
    }

    public java.lang.String handleGetActionRoles()
    {
        final Collection users = getRoleUsers();
        StringBuffer rolesBuffer = new StringBuffer();
        for (Iterator userIterator = users.iterator(); userIterator.hasNext();)
        {
            StrutsUser strutsUser = (StrutsUser) userIterator.next();
            rolesBuffer.append(strutsUser.getRole() + ' ');
        }
        return StringUtilsHelper.separate(rolesBuffer.toString(), ",");
    }

    private Collection getRoleUsers()
    {
        for (Iterator iterator = getActionForwards().iterator(); iterator.hasNext();)
        {
            TransitionFacade transition = (TransitionFacade) iterator.next();
            if (transition.getTarget() instanceof StrutsFinalState)
            {
                StrutsUseCase useCase = ((StrutsFinalState)transition.getTarget()).getTargetUseCase();
                return (useCase != null) ? useCase.getAllUsers() : Collections.EMPTY_LIST;
            }
        }
        return getActivityGraph().getUseCase().getAllUsers();
    }

    public String handleGetActionClassName()
    {
        String name = null;
        final StateVertexFacade source = getSource();

        if (source instanceof PseudostateFacade)
        {
            PseudostateFacade pseudostate = (PseudostateFacade) source;
            if (pseudostate.isInitialState())
                name = getActivityGraph().getUseCase().getName();
        } else
        {
            final EventFacade trigger = getTrigger();
            final String suffix = (trigger == null) ? getTarget().getName() : trigger.getName();
            name = getSource().getName() + ' ' + suffix;
        }
        return StringUtilsHelper.toJavaClassName(name);
    }

    public String handleGetFormBeanClassName()
    {
        return getActionClassName() + "ActionForm";
    }

    public String handleGetFormBeanName()
    {
        final String useCaseName = getActivityGraph().getUseCase().getName();
        return StringUtilsHelper.lowerCaseFirstLetter(useCaseName + getFormBeanClassName());
    }

    public String handleGetFormValidationMethodName()
    {
        return "validate" + getFormBeanClassName();
    }

    public String handleGetMessageKey()
    {
        String messageKey = getActivityGraph().getUseCase().getName() + ' ';
        messageKey += (isUseCaseStart()) ? messageKey : getInput().getName();
        return StringUtilsHelper.toResourceMessageKey(messageKey);
    }

    public String handleGetSuccessMessageKey()
    {
        return getMessageKey() + ".success";
    }

    public String handleGetSuccessMessageValue()
    {
        return '[' + getTrigger().getName() + "] succesfully executed on " + getInput().getTitleValue();
    }

    /**
     * Overwrites the method defined in the facade parent of StrutsAction, this is done
     * because actions (transitions) are not directly contained in a UML namespace.
     */ 
    public String getPackageName()
    {
        return getActivityGraph().getController().getPackageName();
    }

    public boolean handleIsResettable()
    {
        Object value = findTaggedValue(Bpm4StrutsProfile.TAGGED_VALUE_ACTION_RESETTABLE);
        return isTrue(value==null?null:value.toString());
    }

    private boolean isTrue(String string)
    {
        return "yes".equalsIgnoreCase(string) || "true".equalsIgnoreCase(string) ||
                "on".equalsIgnoreCase(string) || "1".equalsIgnoreCase(string);
    }

    public boolean handleIsUseCaseStart()
    {
        StateVertexFacade source = getSource();
        return source instanceof PseudostateFacade && ((PseudostateFacade) source).isInitialState();
    }

    public String handleGetFullActionPath()
    {
        return getPackagePath() + '/' + getActionClassName();
    }

    public String handleGetFullTilePath()
    {
        return isUseCaseStart()
                ? "empty-file"
                : getPackagePath() + '/' + StringUtilsHelper.toWebFileName(getActionClassName());
    }

    /**
     * We override this method here to make sure the actions end-up in the same package
     * as their use-case. A transition (this class' parent type) does not have a real package
     * as we need it here.
     */
    public String getPackagePath()
    {
        return getActivityGraph().getUseCase().getPackagePath();
    }

    public String handleGetFullFormBeanPath()
    {
        return '/' + (getPackageName() + '/' + getFormBeanClassName()).replace('.', '/');
    }

    public boolean handleRequiresValidation()
    {
        final Collection actionParameters = getActionParameters();
        for (Iterator iterator = actionParameters.iterator(); iterator.hasNext();)
        {
            StrutsParameter parameter = (StrutsParameter) iterator.next();
            if (!parameter.getValidatorTypes().isEmpty())
                return true;
        }
        return false;
    }

    public String handleGetFormBeanType()
    {
        return getPackageName() + '.' + getFormBeanClassName();
    }

    public String handleGetDocumentationKey()
    {
        return getMessageKey() + '.' + getActionTrigger().getTriggerKey() + ".documentation";
    }

    public String handleGetDocumentationValue()
    {
        return StringUtilsHelper.toResourceMessage(getDocumentation(""));
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
            StrutsActionState actionState = (StrutsActionState) iterator.next();
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

    protected Collection handleGetActionFormFields()
    {
        /*
         * in order to avoid naming collisions when a field is passed
         * around more than once, we keep a map which maps names onto the
         * corresponding objects
         */
        final Map fieldMap = new HashMap();

        // first add the parameters on the trigger
        collectFields(getActionParameters(), fieldMap);

        final Collection actionStates = getActionStates();
        for (Iterator actionStateIterator = actionStates.iterator(); actionStateIterator.hasNext();)
        {
            // also add the parameters for any deferred controller operations
            StrutsActionState actionState = (StrutsActionState) actionStateIterator.next();
            Collection controllerCalls = actionState.getControllerCalls();
            for (Iterator controllerCallIterator = controllerCalls.iterator(); controllerCallIterator.hasNext();)
            {
                OperationFacade operation = (OperationFacade) controllerCallIterator.next();
                collectFields(operation.getArguments(), fieldMap);
            }

            // any parameters passed between 'internal' action states are also recorded
            Collection outgoing = actionState.getOutgoing();
            for (Iterator outgoingIterator = outgoing.iterator(); outgoingIterator.hasNext();)
            {
                TransitionFacade transitionFacade = (TransitionFacade) outgoingIterator.next();
                EventFacade transitionTrigger = transitionFacade.getTrigger();
                if (transitionTrigger != null)
                    collectFields(transitionTrigger.getParameters(), fieldMap);
            }
        }

        return fieldMap.values();
    }

    protected Collection handleGetActionParameters()
    {
        final StrutsTrigger trigger = getActionTrigger();
        return (trigger == null) ? Collections.EMPTY_LIST : trigger.getParameters();
    }

    private void collectFields(Collection fields, Map fieldMap)
    {
        for (Iterator iterator = fields.iterator(); iterator.hasNext();)
        {
            ParameterFacade parameter = (ParameterFacade) iterator.next();
            fieldMap.put(parameter.getName(), parameter);
        }
    }
}
