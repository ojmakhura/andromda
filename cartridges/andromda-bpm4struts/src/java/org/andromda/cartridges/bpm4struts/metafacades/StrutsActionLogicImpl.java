package org.andromda.cartridges.bpm4struts.metafacades;

import org.andromda.cartridges.bpm4struts.Bpm4StrutsProfile;
import org.andromda.core.common.StringUtilsHelper;
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
        }
        else if ((target instanceof PseudostateFacade) && ((PseudostateFacade) target).isDecisionPoint())
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
            StrutsForward forward = ((StrutsActionState) target).getForward();
            if (forward != null)
            {
                collectTransitions(forward, processedTransitions);
            }
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

    public String handleGetActionName()
    {
        return getFormBeanName();
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

    public boolean handleIsTableLinkPresent()
    {
        final Collection parameters = getActionParameters();
        for (Iterator iterator = parameters.iterator(); iterator.hasNext();)
        {
            StrutsParameter parameter = (StrutsParameter) iterator.next();
            if (parameter.isTableLink())
            {
                return true;
            }
        }
        return false;
    }

    public boolean handleIsHyperlink()
    {
        Object value = findTaggedValue(Bpm4StrutsProfile.TAGGED_VALUE_ACTION_TYPE);
        return Bpm4StrutsProfile.TAGGED_VALUE_ACTION_TYPE_HYPERLINK.equalsIgnoreCase(value == null ? null : value.toString());
    }

    public boolean handleIsSuccessMessagePresent()
    {
        Object value = findTaggedValue(Bpm4StrutsProfile.TAGGED_VALUE_ACTION_SUCCES_MESSAGE);
        return isTrue(value == null ? null : value.toString());
    }

    public java.lang.String handleGetActionPath()
    {
        return getActionPathRoot() + '/' + getActionClassName();
    }

    public String handleGetActionPathRoot()
    {
        return '/' + StringUtilsHelper.upperCamelCaseName(getActivityGraph().getUseCase().getName());
    }

    public String handleGetActionScope()
    {
        return "request";
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
                StrutsUseCase useCase = ((StrutsFinalState) transition.getTarget()).getTargetUseCase();
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
        }
        else
        {
            final EventFacade trigger = getTrigger();
            final String suffix = (trigger == null) ? getTarget().getName() : trigger.getName();
            name = getSource().getName() + ' ' + suffix;
        }
        return StringUtilsHelper.upperCamelCaseName(name);
    }

    public String handleGetFormBeanClassName()
    {
        return getActionClassName() + "ActionForm";
    }

    public String handleGetFormBeanName()
    {
        final String useCaseName = getActivityGraph().getUseCase().getName();
        return StringUtilsHelper.lowerCamelCaseName(useCaseName) + getFormBeanClassName();
    }

    public String handleGetFormValidationMethodName()
    {
        return "validate" + getFormBeanClassName();
    }

    public String handleGetMessageKey()
    {
        String messageKey = getActivityGraph().getUseCase().getName() + ' ';
        messageKey += (isExitingPage()) ? getInput().getName() : messageKey;
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
        return getActivityGraph().getUseCase().getPackageName();
    }

    public boolean handleIsResettable()
    {
        Object value = findTaggedValue(Bpm4StrutsProfile.TAGGED_VALUE_ACTION_RESETTABLE);
        return isTrue(value == null ? null : value.toString());
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
        return '/' + getActivityGraph().getUseCase().getPackagePath();
    }

    public String handleGetFullFormBeanPath()
    {
        return '/' + (getPackageName() + '/' + getFormBeanClassName()).replace('.', '/');
    }

    public boolean handleIsValidationRequired()
    {
        final Collection actionParameters = getActionParameters();
        for (Iterator iterator = actionParameters.iterator(); iterator.hasNext();)
        {
            StrutsParameter parameter = (StrutsParameter) iterator.next();
            if (parameter.isValidationRequired())
            {
                return true;
            }
        }
        return false;
    }

    public boolean handleIsDateFieldPresent()
    {
        final Collection actionParameters = getActionParameters();
        for (Iterator iterator = actionParameters.iterator(); iterator.hasNext();)
        {
            StrutsParameter parameter = (StrutsParameter) iterator.next();
            if (parameter.isDate())
            {
                return true;
            }
        }
        return false;
    }

    public boolean handleIsCalendarRequired()
    {
        final Collection actionParameters = getActionParameters();
        for (Iterator iterator = actionParameters.iterator(); iterator.hasNext();)
        {
            StrutsParameter parameter = (StrutsParameter) iterator.next();
            if (parameter.isCalendarRequired())
            {
                return true;
            }
        }
        return false;
    }

    public String handleGetFormBeanPackageName()
    {
        return getPackageName();
    }

    public String handleGetFormBeanType()
    {
        return getFormBeanPackageName() + '.' + getFormBeanClassName();
    }

    public String handleGetDocumentationKey()
    {
        StrutsTrigger trigger = getActionTrigger();
        return ((trigger == null)
                ? getMessageKey() + ".is.an.action.without.trigger"
                : getActionTrigger().getTriggerKey()) + ".documentation";
    }

    public String handleGetDocumentationValue()
    {
        final String value = StringUtilsHelper.toResourceMessage(getDocumentation("", 64, false));
        return (value == null) ? "" : value;
    }

    public String handleGetOnlineHelpKey()
    {
        StrutsTrigger trigger = getActionTrigger();
        return ((trigger == null)
                ? getMessageKey() + ".is.an.action.without.trigger"
                : getActionTrigger().getTriggerKey()) + ".online.help";
    }

    public String handleGetOnlineHelpValue()
    {
        final String crlf = "<br/>";
        StringBuffer buffer = new StringBuffer();

        String value = StringUtilsHelper.toResourceMessage(getDocumentation("", 64, false));
        buffer.append((value == null) ? "No action documentation has been specified" : value);
        buffer.append(crlf);

        return StringUtilsHelper.toResourceMessage(buffer.toString());
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsAction#getActionForwards()
     */
    protected Collection handleGetActionForwards()
    {
        if (actionForwards == null) initializeCollections();
        return actionForwards;
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsAction#getDecisionTransitions()
     */
    protected Collection handleGetDecisionTransitions()
    {
        if (decisionTransitions == null) initializeCollections();
        return decisionTransitions;
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsAction#getActionStates()
     */
    protected Collection handleGetActionStates()
    {
        if (actionStates == null) initializeCollections();
        return actionStates;
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsAction#getActionExceptions()
     */
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

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsAction#getInput()
     */
    protected java.lang.Object handleGetInput()
    {
        Object input = null;
        ModelElementFacade source = getSource();
        if (source instanceof PseudostateFacade)
        {
            PseudostateFacade pseudostate = (PseudostateFacade) source;
            if (pseudostate.isInitialState())
            {
                input = source;
            }
        }
        else
        {
            if (source.hasStereotype(Bpm4StrutsProfile.STEREOTYPE_VIEW))
            {
                input = source;
            }
        }
        return input;
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsAction#getActivityGraph()
     */
    protected Object handleGetActivityGraph()
    {
        return getSource().getActivityGraph();
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsAction#getController()
     */
    protected Object handleGetController()
    {
        return getActivityGraph().getController();
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsAction#getActionTrigger()
     */
    protected Object handleGetActionTrigger()
    {
        return this.getTrigger();
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsAction#getActionFormFields()
     */
    protected Collection handleGetActionFormFields()
    {
        Collection formFields = new HashSet();

        // add all action parameters
        formFields.addAll(getActionParameters());

        // add page variables for all pages targetted
        Collection forwards = getActionForwards();
        for (Iterator iterator = forwards.iterator(); iterator.hasNext();)
        {
            StrutsForward forward = (StrutsForward) iterator.next();
            StateVertexFacade target = forward.getTarget();
            if (target instanceof StrutsJsp)
            {
                formFields.addAll(((StrutsJsp) target).getPageVariables());
            }
        }

        return formFields;
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsAction#getActionParameters()
     */
    protected Collection handleGetActionParameters()
    {
        final StrutsTrigger trigger = getActionTrigger();
        return (trigger == null) ? Collections.EMPTY_LIST : trigger.getParameters();
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsAction#getNonTabbedActionParameters()
     */
    protected Collection handleGetNonTabbedActionParameters()
    {
        Collection nonTabbedParameters = new ArrayList();
        Collection actionParameters = getActionParameters();

        for (Iterator iterator = actionParameters.iterator(); iterator.hasNext();)
        {
            StrutsParameter parameter = (StrutsParameter) iterator.next();
            if (parameter.getTabIndex() < 0)
            {
                nonTabbedParameters.add(parameter);
            }
        }

        return nonTabbedParameters;
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsAction#isTabbed()
     */
    public boolean handleIsTabbed()
    {
        Collection actionParameters = getActionParameters();
        for (Iterator iterator = actionParameters.iterator(); iterator.hasNext();)
        {
            StrutsParameter parameter = (StrutsParameter) iterator.next();
            if (parameter.getTabIndex() >= 0)
            {
                return true;
            }
        }
        return false;
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsAction#getTabCount()
     */
    public int handleGetTabCount()
    {
        return (isTabbed()) ? getTabMap().keySet().size() : 0;
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsAction#getTabName(int)
     */
    public String handleGetTabName(int tabIndex)
    {
        return String.valueOf(tabIndex + 1);
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsAction#getTabIndex()
     */
    public int handleGetTabIndex()
    {
        final String tabIndex = String.valueOf(this.findTaggedValue(Bpm4StrutsProfile.TAGGED_VALUE_ACTION_TABINDEX));

        try
        {
            return (tabIndex == null) ? -1 : Integer.parseInt(tabIndex);
        }
        catch (NumberFormatException e)
        {
            return -1;
        }
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsAction#getTabMap()
     */
    public Map handleGetTabMap()
    {
        Map tabMap = new LinkedHashMap();
        Collection actionParameters = getActionParameters();

        for (Iterator iterator = actionParameters.iterator(); iterator.hasNext();)
        {
            StrutsParameter parameter = (StrutsParameter) iterator.next();
            int tabIndex = parameter.getTabIndex();

            if (tabIndex >= 0)
            {
                String tabKey = String.valueOf(tabIndex);
                Collection tabFields = (Collection) tabMap.get(tabKey);

                if (tabFields == null)
                {
                    tabFields = new ArrayList();
                    tabMap.put(tabKey, tabFields);
                }

                tabFields.add(parameter);
            }
        }
        return tabMap;
    }

    protected Collection handleGetTargetPages()
    {
        Collection targetPages = new HashSet();

        Collection forwards = getActionForwards();
        for (Iterator forwardIterator = forwards.iterator(); forwardIterator.hasNext();)
        {
            StrutsForward forward = (StrutsForward) forwardIterator.next();
            if (forward.isTargettingPage())
            {
                targetPages.add(forward.getTarget());
            }
        }

        return targetPages;
    }

    protected Collection handleGetPreloadableFormFields()
    {
        Collection preloadFields = new HashSet();

        Collection pages = getTargetPages();
        for (Iterator pageIterator = pages.iterator(); pageIterator.hasNext();)
        {
            StrutsJsp jsp = (StrutsJsp) pageIterator.next();
            Collection actionParameters = jsp.getAllActionParameters();
            preloadFields.addAll(actionParameters);
        }

        return preloadFields;
    }

    protected Collection handleGetAllFormFields()
    {
        Collection fields = new HashSet();
        fields.addAll(getPreloadableFormFields());
        fields.addAll(getActionFormFields());
        return fields;
    }

    public Collection handleGetControllerCallInterfaceNames()
    {
        Collection interfaceNames = new HashSet();

        Collection actionStates = getActionStates();
        for (Iterator actionStateIterator = actionStates.iterator(); actionStateIterator.hasNext();)
        {
            StrutsActionState actionState = (StrutsActionState) actionStateIterator.next();
            Collection calls = actionState.getControllerCalls();
            for (Iterator callIterator = calls.iterator(); callIterator.hasNext();)
            {
                StrutsControllerOperation operation = (StrutsControllerOperation) callIterator.next();
                interfaceNames.add(operation.getInterfaceName());
            }
        }

        Collection decisionTransitions = getDecisionTransitions();
        for (Iterator transitionIterator = decisionTransitions.iterator(); transitionIterator.hasNext();)
        {
            StrutsForward forward = (StrutsForward) transitionIterator.next();
            EventFacade event = forward.getTrigger();
            if (event instanceof StrutsTrigger)
            {
                interfaceNames.add(((StrutsTrigger) event).getControllerCall().getInterfaceName());
            }
        }

        return interfaceNames;
    }
}
