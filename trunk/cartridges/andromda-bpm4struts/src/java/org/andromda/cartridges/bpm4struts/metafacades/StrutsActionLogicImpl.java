package org.andromda.cartridges.bpm4struts.metafacades;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.andromda.cartridges.bpm4struts.Bpm4StrutsGlobals;
import org.andromda.cartridges.bpm4struts.Bpm4StrutsProfile;
import org.andromda.core.common.StringUtilsHelper;
import org.andromda.metafacades.uml.*;
import org.apache.commons.lang.StringUtils;


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
    private Collection transitions = null;

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
        transitions = new HashSet();
        collectTransitions(this, transitions);
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

    protected String handleGetActionName()
    {
        return getFormBeanName();
    }

    protected String handleGetActionInput()
    {
        final StateVertexFacade source = getSource();
        return (source instanceof StrutsJsp) ? ((StrutsJsp) source).getFullPath() : "";
    }

    protected boolean handleIsFormPost()
    {
        return !isHyperlink();
    }

    protected boolean handleIsTableLinkPresent()
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

    protected boolean handleIsHyperlink()
    {
        Object value = findTaggedValue(Bpm4StrutsProfile.TAGGEDVALUE_ACTION_TYPE);
        return Bpm4StrutsProfile.TAGGEDVALUE_ACTION_TYPE_HYPERLINK.equalsIgnoreCase(value == null ? null : value.toString());
    }

    protected java.lang.String handleGetActionPath()
    {
        return getActionPathRoot() + '/' + getActionClassName();
    }

    protected String handleGetActionPathRoot()
    {
        String actionPathRoot = null;

        StrutsUseCase useCase = getUseCase();
        if (useCase != null)
        {
            actionPathRoot = '/' + StringUtilsHelper.upperCamelCaseName(useCase.getName());
        }
        return actionPathRoot;
    }

    protected String handleGetActionScope()
    {
        return "request";
    }

    protected java.lang.String handleGetActionRoles()
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
        Collection roleUsers = new ArrayList();

        if (isUseCaseStart())
        {
            StrutsUseCase useCase = getUseCase();
            if (useCase != null)
            {
                roleUsers.addAll(useCase.getUsers());
            }
        }
        else
        {
            for (Iterator iterator = getActionForwards().iterator(); iterator.hasNext();)
            {
                TransitionFacade transition = (TransitionFacade) iterator.next();
                if (transition.getTarget() instanceof StrutsFinalState)
                {
                    StrutsUseCase useCase = ((StrutsFinalState) transition.getTarget()).getTargetUseCase();
                    if (useCase != null)
                    {
                        roleUsers.addAll( useCase.getUsers() );
                    }
                }
            }
        }
        return roleUsers;
    }

    protected String handleGetActionClassName()
    {
        String name = null;
        final StateVertexFacade source = getSource();

        if (source instanceof PseudostateFacade)
        {
            PseudostateFacade pseudostate = (PseudostateFacade) source;
            if (pseudostate.isInitialState())
            {
                StrutsUseCase useCase = getUseCase();
                if (useCase != null)
                {
                    name = useCase.getName();
                }
            }
        }
        else
        {
            final EventFacade trigger = getTrigger();
            final String suffix = (trigger == null) ? getTarget().getName() : trigger.getName();
            name = getSource().getName() + ' ' + suffix;
        }
        return StringUtilsHelper.upperCamelCaseName(name);
    }

    protected String handleGetActionType()
    {
        return getPackageName() + '.' + getActionClassName();    
    }

    protected String handleGetFormBeanClassName()
    {
        return getActionClassName() + "Form";
    }

    protected String handleGetFormBeanName()
    {
        String formBeanName = null;

        StrutsUseCase useCase = getUseCase();
        if (useCase != null)
        {
            String useCaseName = useCase.getName();
            formBeanName = StringUtilsHelper.lowerCamelCaseName(useCaseName) + getFormBeanClassName();
        }
        return formBeanName;
    }

    protected String handleGetFormValidationMethodName()
    {
        return "validate" + getFormBeanClassName();
    }

    /**
     * Overrides the one from StrutsForward. This one incorporates the name of the originating page to avoid conflicts.
     */
    public String getMessageKey()
    {
        String messageKey = super.getMessageKey() + ' ';
        messageKey += (isExitingPage()) ? getInput().getName() : messageKey;
        return StringUtilsHelper.toResourceMessageKey(messageKey);
    }

    /**
     * Overrides the method defined in the facade parent of StrutsAction, this is done
     * because actions (transitions) are not directly contained in a UML namespace.
     */
    public String getPackageName()
    {
        String packageName = null;

        StrutsUseCase useCase = getUseCase();
        if (useCase != null)
        {
            packageName = useCase.getPackageName();
        }
        return packageName;
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsAction#isResettable()
     */
    protected boolean handleIsResettable()
    {
        Object value = findTaggedValue(Bpm4StrutsProfile.TAGGEDVALUE_ACTION_RESETTABLE);
        return isTrue(value == null ? null : value.toString());
    }

    private boolean isTrue(String string)
    {
        return "yes".equalsIgnoreCase(string) || "true".equalsIgnoreCase(string) ||
                "on".equalsIgnoreCase(string) || "1".equalsIgnoreCase(string);
    }

    protected boolean handleIsUseCaseStart()
    {
        StateVertexFacade source = getSource();
        return source instanceof PseudostateFacade && ((PseudostateFacade) source).isInitialState();
    }

    protected String handleGetFullActionPath()
    {
        return getPackagePath() + '/' + getActionClassName();
    }

    protected String handleGetFullTilePath()
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
        String packagePath = null;

        StrutsUseCase useCase = getUseCase();
        if (useCase != null)
        {
            packagePath = '/' + useCase.getPackagePath();
        }
        return packagePath;
    }

    protected String handleGetFullFormBeanPath()
    {
        return '/' + (getPackageName() + '/' + getFormBeanClassName()).replace('.', '/');
    }

    protected boolean handleIsValidationRequired()
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

    protected boolean handleIsDateFieldPresent()
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

    protected boolean handleIsCalendarRequired()
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

    protected String handleGetFormBeanPackageName()
    {
        return getPackageName();
    }

    protected String handleGetFormBeanType()
    {
        return getFormBeanPackageName() + '.' + getFormBeanClassName();
    }

    protected String handleGetDocumentationKey()
    {
        StrutsTrigger trigger = getActionTrigger();
        return ((trigger == null)
                ? getMessageKey() + ".is.an.action.without.trigger"
                : getActionTrigger().getTriggerKey()) + ".documentation";
    }

    protected String handleGetDocumentationValue()
    {
        final String value = StringUtilsHelper.toResourceMessage(getDocumentation("", 64, false));
        return (value == null) ? "" : value;
    }

    protected String handleGetOnlineHelpKey()
    {
        StrutsTrigger trigger = getActionTrigger();
        return ((trigger == null)
                ? getMessageKey() + ".is.an.action.without.trigger"
                : getActionTrigger().getTriggerKey()) + ".online.help";
    }

    protected String handleGetOnlineHelpValue()
    {
        final String crlf = "<br/>";
        StringBuffer buffer = new StringBuffer();

        String value = StringUtilsHelper.toResourceMessage(getDocumentation("", 64, false));
        buffer.append((value == null) ? "No action documentation has been specified" : value);
        buffer.append(crlf);

        return StringUtilsHelper.toResourceMessage(buffer.toString());
    }

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

    protected Object handleGetActivityGraph()
    {
        return getSource().getActivityGraph();
    }

    protected Object handleGetController()
    {
        return getStrutsActivityGraph().getController();
    }

    protected Object handleGetActionTrigger()
    {
        return this.getTrigger();
    }

    protected Collection handleGetActionFormFields()
    {
        Map formFieldMap = new HashMap();

        /**
         * for useCaseStart actions we need to detect all usecases forwarding to the one belonging to this action
         * if there are any parameters in those requests we need to have them included in this action's form
         */
        if (isUseCaseStart())
        {
            StrutsUseCase useCase = getUseCase();
            if (useCase != null)
            {
                Collection finalStates = useCase.getReferencingFinalStates();
                for (Iterator finalStateIterator = finalStates.iterator(); finalStateIterator.hasNext();)
                {
                    StrutsFinalState finalState = (StrutsFinalState) finalStateIterator.next();
                    Collection actions = finalState.getActions();
                    for (Iterator actionIterator = actions.iterator(); actionIterator.hasNext();)
                    {
                        StrutsAction action = (StrutsAction) actionIterator.next();
                        Collection parameters = action.getActionParameters();
                        for (Iterator parameterIterator = parameters.iterator(); parameterIterator.hasNext();)
                        {
                            StrutsParameter parameter = (StrutsParameter) parameterIterator.next();
                            formFieldMap.put(parameter.getName(), parameter);
                        }
                    }
                }
            }
        }

        // add page variables for all pages/final-states targetted
        // also add the fields of the target page's actions (for preloading)
        Collection forwards = getActionForwards();
        for (Iterator iterator = forwards.iterator(); iterator.hasNext();)
        {
            StrutsForward forward = (StrutsForward) iterator.next();
            StateVertexFacade target = forward.getTarget();
            if (target instanceof StrutsJsp)
            {
                StrutsJsp jsp = (StrutsJsp) target;
                Collection pageVariables = jsp.getPageVariables();
                for (Iterator pageVariableIterator = pageVariables.iterator(); pageVariableIterator.hasNext();)
                {
                    ModelElementFacade facade = (ModelElementFacade) pageVariableIterator.next();
                    formFieldMap.put(facade.getName(), facade);
                }
                Collection allActionParameters = jsp.getAllActionParameters();
                for (Iterator actionParameterIterator = allActionParameters.iterator(); actionParameterIterator.hasNext();)
                {
                    ModelElementFacade facade = (ModelElementFacade) actionParameterIterator.next();
                    formFieldMap.put(facade.getName(), facade);
                }
            }
            else if (target instanceof StrutsFinalState)
            {
                // only add these if there is no parameter recorded yet with the same name
                Collection forwardParameters = forward.getForwardParameters();
                for (Iterator forwardParameterIterator = forwardParameters.iterator(); forwardParameterIterator.hasNext();)
                {
                    ModelElementFacade facade = (ModelElementFacade) forwardParameterIterator.next();
                    if (formFieldMap.containsKey(facade.getName()) == false)
                    {
                        formFieldMap.put(facade.getName(), facade);
                    }
                }
            }
        }

        // we do the action parameters in the end because they are allowed to overwrite existing properties
        Collection actionParameters = getActionParameters();
        for (Iterator actionParameterIterator = actionParameters.iterator(); actionParameterIterator.hasNext();)
        {
            ModelElementFacade facade = (ModelElementFacade) actionParameterIterator.next();
            formFieldMap.put(facade.getName(), facade);
        }

        return formFieldMap.values();
    }

    protected Collection handleGetDeferredOperations()
    {
        Collection deferredOperations = new ArrayList();
        StrutsController controller = getController();
        if (controller != null)
        {
            Collection operations = controller.getOperations();
            for (Iterator operationIterator = operations.iterator(); operationIterator.hasNext();)
            {
                StrutsControllerOperation operation = (StrutsControllerOperation) operationIterator.next();
                if (operation.getDeferringActions().contains(this))
                {
                    deferredOperations.add(operation);
                }
            }
        }
        return deferredOperations;
    }

    protected Collection handleGetActionParameters()
    {
        final StrutsTrigger trigger = getActionTrigger();
        return (trigger == null) ? Collections.EMPTY_LIST : trigger.getParameters();
    }

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

    protected boolean handleIsTabbed()
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
    protected int handleGetTabCount()
    {
        return (isTabbed()) ? getTabMap().keySet().size() : 0;
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsAction#getTabName(int)
     */
    protected String handleGetTabName(int tabIndex)
    {
        return String.valueOf(tabIndex + 1);
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsAction#getTabIndex()
     */
    protected int handleGetTabIndex()
    {
        final String tabIndex = String.valueOf(this.findTaggedValue(Bpm4StrutsProfile.TAGGEDVALUE_ACTION_TABINDEX));

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
    protected Map handleGetTabMap()
    {
        Map tabMap = new LinkedHashMap();
        Collection actionParameters = getActionParameters();

        for (Iterator iterator = actionParameters.iterator(); iterator.hasNext();)
        {
            StrutsParameter parameter = (StrutsParameter) iterator.next();
            int tabIndex = parameter.getTabIndex();

            if (!parameter.isTableLink() && tabIndex >= 0)
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

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsAction#getTargetPages()
     */
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

    protected Collection handleGetPageVariables()
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

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsActionLogic#getTransitions()
     */
    protected Collection handleGetTransitions()
    {
        if (transitions == null)
        {
            initializeCollections();
        }
        return transitions;
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsAction#getTabs()
     */
    protected Collection handleGetTabs()
    {
        // @todo: implement
        return Collections.EMPTY_LIST;
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsAction#getStyleId()
     */
    protected String handleGetStyleId()
    {
        String styleId = null;

        StrutsTrigger trigger = getActionTrigger();
        if (trigger != null)
        {
            String triggerName = trigger.getName();
            triggerName = StringUtilsHelper.lowerCamelCaseName(triggerName);
            styleId = StringUtilsHelper.lowerCamelCaseName(triggerName);
        }
        return styleId;
    }
    
    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsAction#isRedirect()
     */
    public boolean handleIsRedirect()
    {
        String redirect = (String)this.getConfiguredProperty(
            Bpm4StrutsGlobals.PROPERTY_DEFAULT_ACTION_REDIRECT);
        Object value = this
            .findTaggedValue(Bpm4StrutsProfile.TAGGEDVALUE_ACTION_REDIRECT);
        if (value != null)
        {
            redirect = (String)value;
        }
        return Boolean.valueOf(StringUtils.trimToEmpty(redirect)).booleanValue();
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsAction#getResettableActionParameters()
     */
    protected Collection handleGetResettableActionParameters()
    {
        return new FilteredCollection(this.getActionParameters())
        {
            public boolean evaluate(Object object)
            {
                return object != null && ((StrutsParameter)object).isShouldReset();
            }
        };
    }
}
