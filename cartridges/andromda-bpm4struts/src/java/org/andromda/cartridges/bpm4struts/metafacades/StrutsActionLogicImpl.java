package org.andromda.cartridges.bpm4struts.metafacades;

import org.andromda.cartridges.bpm4struts.Bpm4StrutsProfile;
import org.andromda.core.common.StringUtilsHelper;
import org.andromda.core.metafacade.ModelValidationException;
import org.andromda.metafacades.uml.*;

import java.util.*;


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

    private String actionName = null;
    private String actionInput = null;
    private String actionPath = null;
    private String actionClassName = null;
    private String actionPathRoot = null;
    private String actionRoles = null;
    private Collection actionExceptions = null;
    private String formBeanClassName = null;
    private String formBeanName = null;
    private String formBeanType = null;
    private String formValidationMethodName = null;
    private String fullActionPath = null;
    private String fullFormBeanPath = null;
    private String messageKey = null;
    private String successMessageKey = null;
    private String successMessageValue = null;
    private String packageName = null;
    private Boolean hasSuccessMessage = null;
    private Boolean isHyperlink = null;
    private Boolean isResettable = null;
    private Boolean isUseCaseStart = null;
    private Boolean requiresValidation = null;
    private String documentationKey = null;
    private String documentationValue = null;

    private Object input = null;
    private Object activityGraph = null;
    private Object actionTrigger = null;
    private Object controller = null;
    private Collection actionParameters = null;
    private Collection actionFormFields = null;
    private String fullTilePath = null;

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
        } else
        {
            processedTransitions.add(transition);
        }

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

    public String getActionName()
    {
        if (Bpm4StrutsProfile.ENABLE_CACHE && actionName != null) return actionName;
        return (actionName = getActivityGraph().getUseCase().getFormBeanName());
    }

    public String getActionInput()
    {
        if (Bpm4StrutsProfile.ENABLE_CACHE && actionInput != null) return actionInput;
        final StateVertexFacade source = getSource();
        return (actionInput = (source instanceof StrutsJsp) ? ((StrutsJsp) source).getFullPath() : "");
    }

    public boolean isFormPost()
    {
        return !isHyperlink();
    }

    public boolean isHyperlink()
    {
        if (Bpm4StrutsProfile.ENABLE_CACHE && isHyperlink != null) return isHyperlink.booleanValue();
        boolean condition = Bpm4StrutsProfile.TAGGED_VALUE_ACTION_TYPE_HYPERLINK.equalsIgnoreCase(findTaggedValue(Bpm4StrutsProfile.TAGGED_VALUE_ACTION_TYPE));
        return (isHyperlink = (condition) ? Boolean.TRUE : Boolean.FALSE).booleanValue();
    }

    public boolean hasSuccessMessage()
    {
        if (Bpm4StrutsProfile.ENABLE_CACHE && hasSuccessMessage != null) return hasSuccessMessage.booleanValue();
        boolean condition = isTrue(findTaggedValue(Bpm4StrutsProfile.TAGGED_VALUE_ACTION_SUCCES_MESSAGE));
        return (hasSuccessMessage = (condition) ? Boolean.TRUE : Boolean.FALSE).booleanValue();
    }

    public java.lang.String getActionPath()
    {
        if (Bpm4StrutsProfile.ENABLE_CACHE && actionPath != null) return actionPath;
        return (actionPath = getActionPathRoot() + '/' + getActionClassName());
    }

    public String getActionPathRoot()
    {
        if (Bpm4StrutsProfile.ENABLE_CACHE && actionPathRoot != null) return actionPathRoot;
        return (actionPathRoot = '/' + StringUtilsHelper.toJavaClassName(getActivityGraph().getUseCase().getName()));
    }

    public java.lang.String getActionRoles()
    {
        if (Bpm4StrutsProfile.ENABLE_CACHE && actionRoles != null) return actionRoles;

        final Collection users = getRoleUsers();
        StringBuffer rolesBuffer = new StringBuffer();
        for (Iterator userIterator = users.iterator(); userIterator.hasNext();)
        {
            StrutsUser strutsUser = (StrutsUser) userIterator.next();
            rolesBuffer.append(strutsUser.getRole() + ' ');
        }
        return (actionRoles = StringUtilsHelper.separate(rolesBuffer.toString(), ","));
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

    public String getActionClassName()
    {
        if (Bpm4StrutsProfile.ENABLE_CACHE && actionClassName != null) return actionClassName;

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
        return (actionClassName = StringUtilsHelper.toJavaClassName(name));
    }

    public String getFormBeanClassName()
    {
        if (Bpm4StrutsProfile.ENABLE_CACHE && formBeanClassName != null) return formBeanClassName;
        return (formBeanClassName = getActionClassName() + "ActionForm");
    }

    public String getFormBeanName()
    {
        if (Bpm4StrutsProfile.ENABLE_CACHE && formBeanName != null) return formBeanName;
        return (formBeanName = StringUtilsHelper.lowerCaseFirstLetter(getFormBeanClassName()));
    }

    public String getFormValidationMethodName()
    {
        if (Bpm4StrutsProfile.ENABLE_CACHE && formValidationMethodName != null) return formValidationMethodName;
        return (formValidationMethodName = "validate" + getFormBeanClassName());
    }

    public String getMessageKey()
    {
        if (Bpm4StrutsProfile.ENABLE_CACHE && messageKey != null) return messageKey;

        String messageKey = getActivityGraph().getUseCase().getName() + ' ';
        messageKey += (isUseCaseStart()) ? messageKey : getInput().getName();
        return (messageKey = StringUtilsHelper.toResourceMessageKey(messageKey));
    }

    public String getSuccessMessageKey()
    {
        if (Bpm4StrutsProfile.ENABLE_CACHE && successMessageKey != null) return successMessageKey;
        return (successMessageKey = getMessageKey() + ".success");
    }

    public String getSuccessMessageValue()
    {
        if (Bpm4StrutsProfile.ENABLE_CACHE && successMessageValue != null) return successMessageValue;
        return (successMessageValue = '[' + getTrigger().getName() + "] succesfully executed on " + getInput().getTitleValue());
    }

    public String getPackageName()
    {
        if (Bpm4StrutsProfile.ENABLE_CACHE && packageName != null) return packageName;
        return (packageName = getActivityGraph().getController().getPackageName());
    }

    public boolean isResettable()
    {
        if (Bpm4StrutsProfile.ENABLE_CACHE && isResettable != null) return isResettable.booleanValue();
        boolean condition = isTrue(findTaggedValue(Bpm4StrutsProfile.TAGGED_VALUE_ACTION_RESETTABLE));
        return (isResettable = (condition) ? Boolean.TRUE : Boolean.FALSE).booleanValue();
    }

    private boolean isTrue(String string)
    {
        return "yes".equalsIgnoreCase(string) || "true".equalsIgnoreCase(string) ||
                "on".equalsIgnoreCase(string) || "1".equalsIgnoreCase(string);
    }

    public boolean isUseCaseStart()
    {
        if (Bpm4StrutsProfile.ENABLE_CACHE && isResettable != null) return isUseCaseStart.booleanValue();

        StateVertexFacade source = getSource();
        boolean condition = source instanceof PseudostateFacade && ((PseudostateFacade) source).isInitialState();
        return (isUseCaseStart = (condition) ? Boolean.TRUE : Boolean.FALSE).booleanValue();
    }

    public String getFullActionPath()
    {
        if (Bpm4StrutsProfile.ENABLE_CACHE && fullActionPath != null) return fullActionPath;
        return fullActionPath = getPackagePath() + '/' + getActionClassName();
    }

    public String getFullTilePath()
    {
        if (Bpm4StrutsProfile.ENABLE_CACHE && fullTilePath != null) return fullTilePath;
        return fullTilePath = isUseCaseStart()
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

    public String getFullFormBeanPath()
    {
        if (Bpm4StrutsProfile.ENABLE_CACHE && fullFormBeanPath != null) return fullFormBeanPath;
        return (fullFormBeanPath = '/' + (getPackageName() + '/' + getFormBeanClassName()).replace('.', '/'));
    }

    public boolean requiresValidation()
    {
        if (Bpm4StrutsProfile.ENABLE_CACHE && requiresValidation != null) return requiresValidation.booleanValue();

        final Collection actionParameters = getActionParameters();
        for (Iterator iterator = actionParameters.iterator(); iterator.hasNext();)
        {
            StrutsParameter parameter = (StrutsParameter) iterator.next();
            if (!parameter.getValidatorTypes().isEmpty())
                return (requiresValidation = Boolean.TRUE).booleanValue();
        }
        return (requiresValidation = Boolean.FALSE).booleanValue();
    }

    public String getFormBeanType()
    {
        if (Bpm4StrutsProfile.ENABLE_CACHE && formBeanType != null) return formBeanType;
        return getPackageName() + '.' + getFormBeanClassName();
    }

    public String getDocumentationKey()
    {
        if (Bpm4StrutsProfile.ENABLE_CACHE && documentationKey != null) return documentationKey;
        return documentationKey = getMessageKey() + '.' + getActionTrigger().getTriggerKey() + ".documentation";
    }

    public String getDocumentationValue()
    {
        if (Bpm4StrutsProfile.ENABLE_CACHE && documentationValue != null) return documentationValue;
        return documentationValue = StringUtilsHelper.toResourceMessage(getDocumentation(""));
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
        if (Bpm4StrutsProfile.ENABLE_CACHE && actionExceptions != null) return actionExceptions;

        final Collection exceptions = new HashSet();
        final Collection actionStates = getActionStates();
        for (Iterator iterator = actionStates.iterator(); iterator.hasNext();)
        {
            StrutsActionState actionState = (StrutsActionState) iterator.next();
            exceptions.addAll(actionState.getExceptions());
        }

        return actionExceptions = exceptions;
    }

    protected java.lang.Object handleGetInput()
    {
        if (Bpm4StrutsProfile.ENABLE_CACHE && input != null) return input;
        return (input = getSource());
    }

    protected Object handleGetActivityGraph()
    {
        if (Bpm4StrutsProfile.ENABLE_CACHE && activityGraph != null) return activityGraph;
        return (activityGraph = getSource().getActivityGraph());
    }

    protected Object handleGetController()
    {
        if (Bpm4StrutsProfile.ENABLE_CACHE && controller != null) return controller;
        return (controller = getActivityGraph().getController());
    }

    protected Object handleGetActionTrigger()
    {
        if (Bpm4StrutsProfile.ENABLE_CACHE && actionTrigger != null) return actionTrigger;
        return (actionTrigger = getTrigger());
    }

    protected Collection handleGetActionFormFields()
    {
        if (Bpm4StrutsProfile.ENABLE_CACHE && actionFormFields != null) return actionFormFields;

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
                collectFields(operation.getParameters(), fieldMap);
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

        return (actionFormFields = fieldMap.values());
    }

    protected Collection handleGetActionParameters()
    {
        if (Bpm4StrutsProfile.ENABLE_CACHE && actionParameters != null) return actionParameters;
        final StrutsTrigger trigger = getActionTrigger();
        return actionParameters = (trigger == null) ? Collections.EMPTY_LIST : trigger.getParameters();
    }

    private void collectFields(Collection fields, Map fieldMap)
    {
        for (Iterator iterator = fields.iterator(); iterator.hasNext();)
        {
            ParameterFacade parameter = (ParameterFacade) iterator.next();
            fieldMap.put(parameter.getName(), parameter);
        }
    }

    protected void performValidation() throws ModelValidationException
    {
        super.performValidation();
/*

        final StrutsTrigger actionTrigger = getActionTrigger();
        if (actionTrigger == null)
            validationError("Each action must have a trigger");
*/
    }
}
