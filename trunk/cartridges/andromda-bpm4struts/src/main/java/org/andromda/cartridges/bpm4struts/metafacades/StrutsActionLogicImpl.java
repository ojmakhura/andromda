package org.andromda.cartridges.bpm4struts.metafacades;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import org.andromda.cartridges.bpm4struts.Bpm4StrutsGlobals;
import org.andromda.cartridges.bpm4struts.Bpm4StrutsProfile;
import org.andromda.cartridges.bpm4struts.Bpm4StrutsUtils;
import org.andromda.metafacades.uml.EventFacade;
import org.andromda.metafacades.uml.FilteredCollection;
import org.andromda.metafacades.uml.FrontEndEvent;
import org.andromda.metafacades.uml.FrontEndExceptionHandler;
import org.andromda.metafacades.uml.FrontEndFinalState;
import org.andromda.metafacades.uml.FrontEndForward;
import org.andromda.metafacades.uml.FrontEndParameter;
import org.andromda.metafacades.uml.FrontEndUseCase;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.ParameterFacade;
import org.andromda.metafacades.uml.PseudostateFacade;
import org.andromda.metafacades.uml.Role;
import org.andromda.metafacades.uml.StateVertexFacade;
import org.andromda.metafacades.uml.TransitionFacade;
import org.andromda.metafacades.uml.UMLProfile;
import org.andromda.metafacades.uml.UseCaseFacade;
import org.andromda.utils.StringUtilsHelper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

/**
 * MetafacadeLogic implementation.
 *
 * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsAction
 */
public class StrutsActionLogicImpl
    extends StrutsActionLogic
{
    private static final long serialVersionUID = 34L;
    /**
     * All action states that make up this action, this includes all possible action states traversed
     * after a decision point too.
     */
    private Collection<StrutsActionState> actionStates = null;

    /**
     * All transitions leading into either a page or final state that originated from a call to this action.
     */
    private Map<StateVertexFacade, TransitionFacade> actionForwards = null;

    /**
     * All transitions leading into a decision point that originated from a call to this action.
     */
    private Collection<TransitionFacade> decisionTransitions = null;

    /**
     * All transitions that can be traversed when calling this action.
     */
    private Collection<TransitionFacade> transitions = null;

    /**
     * @param metaObject
     * @param context
     */
    public StrutsActionLogicImpl(Object metaObject,
                                 String context)
    {
        super(metaObject, context);
    }

    /**
     * Initializes all action states, action forwards, decision transitions and transitions in one shot, so that they
     * can be queried more efficiently later on.
     */
    private void initializeCollections()
    {
        actionStates = new LinkedHashSet<StrutsActionState>();
        actionForwards = new HashMap<StateVertexFacade, TransitionFacade>();
        decisionTransitions = new LinkedHashSet<TransitionFacade>();
        transitions = new LinkedHashSet<TransitionFacade>();
        collectTransitions(this, transitions);
    }

    /**
     * Recursively collects all action states, action forwards, decision transitions and transitions.
     *
     * @param transition           the current transition that is being processed
     * @param processedTransitions the set of transitions already processed
     */
    private void collectTransitions(TransitionFacade transition,
                                    Collection processedTransitions)
    {
        if (processedTransitions.contains(transition))
        {
            return;
        }
        processedTransitions.add(transition);

        final StateVertexFacade target = transition.getTarget();
        if ((target instanceof StrutsJsp) || (target instanceof StrutsFinalState))
        {
            if (!actionForwards.containsKey(transition.getTarget()))
            {
                actionForwards.put(transition.getTarget(), transition);
            }
        }
        else if ((target instanceof PseudostateFacade) && ((PseudostateFacade) target).isDecisionPoint())
        {
            decisionTransitions.add(transition);
            for (final TransitionFacade outcome : target.getOutgoings())
            {
                collectTransitions(outcome, processedTransitions);
            }
        }
        else if (target instanceof StrutsActionState)
        {
            actionStates.add((StrutsActionState)target);
            final FrontEndForward forward = ((StrutsActionState) target).getForward();
            if (forward != null)
            {
                collectTransitions(forward, processedTransitions);
            }
        }
        else // all the rest is ignored but outgoing transitions are further processed
        {
            for (final TransitionFacade outcome : target.getOutgoings())
            {
                collectTransitions(outcome, processedTransitions);
            }
        }
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsActionLogic#handleGetActionName()
     */
    protected String handleGetActionName()
    {
        return getFormBeanName();
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsActionLogic#handleGetActionInput()
     */
    protected String handleGetActionInput()
    {
        final StateVertexFacade source = getSource();
        return (source instanceof StrutsJsp) ? ((StrutsJsp) source).getFullPath() : "";
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsActionLogic#handleIsMultipartFormData()
     */
    protected boolean handleIsMultipartFormData()
    {
        boolean multipartFormPost = false;

        for (StrutsParameter field : this.getActionFormFields())
        {
            if (field.isFile())
            {
                multipartFormPost = true;
                break;
            }
        }

        return multipartFormPost;
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsActionLogic#handleIsFormPost()
     */
    protected boolean handleIsFormPost()
    {
        final Object value = this.findTaggedValue(Bpm4StrutsProfile.TAGGEDVALUE_ACTION_TYPE);
        return value == null || Bpm4StrutsProfile.TAGGEDVALUE_ACTION_TYPE_FORM.equals(value);
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsActionLogic#handleIsHyperlink()
     */
    protected boolean handleIsHyperlink()
    {
        final Object value = findTaggedValue(Bpm4StrutsProfile.TAGGEDVALUE_ACTION_TYPE);
        return Bpm4StrutsProfile.TAGGEDVALUE_ACTION_TYPE_HYPERLINK
            .equalsIgnoreCase(value == null ? null : value.toString());
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsActionLogic#handleIsImageLink()
     */
    protected boolean handleIsImageLink()
    {
        final Object value = findTaggedValue(Bpm4StrutsProfile.TAGGEDVALUE_ACTION_TYPE);
        return Bpm4StrutsProfile.TAGGEDVALUE_ACTION_TYPE_IMAGE
            .equalsIgnoreCase(value == null ? null : value.toString());
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsActionLogic#handleIsTableAction()
     */
    protected boolean handleIsTableAction()
    {
        return Bpm4StrutsProfile.TAGGEDVALUE_ACTION_TYPE_TABLE
            .equals(this.findTaggedValue(Bpm4StrutsProfile.TAGGEDVALUE_ACTION_TYPE));
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsActionLogic#handleIsTableRowAction()
     */
    protected boolean handleIsTableRowAction()
    {
        return this.isTableLink() && !this.isTableAction();
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsActionLogic#handleIsTableLink()
     */
    protected boolean handleIsTableLink()
    {
        return this.getTableLinkParameter() != null;
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsActionLogic#handleGetTableLinkParameter()
     */
    protected Object handleGetTableLinkParameter()
    {
        StrutsParameter tableLinkParameter = null;

        final String tableLinkName = getTableLinkName();
        if (tableLinkName != null)
        {
            final StrutsJsp page = this.getInput();
            if (page != null)
            {
                for (FrontEndParameter table : page.getTables())
                {
                    if (tableLinkName.equals(table.getName()))
                    {
                        tableLinkParameter = (StrutsParameter)table;
                    }
                }
            }
        }

        return tableLinkParameter;
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsActionLogic#handleGetTableNonColumnFormParameters()
     */
    protected List handleGetTableNonColumnFormParameters()
    {
        List tableNonColumnActionParameters = null;

        final StrutsParameter table = getTableLinkParameter();
        if (table != null)
        {
            final Map tableNonColumnActionParametersMap = new LinkedHashMap(4);
            final Collection<String> columnNames = table.getTableColumnNames();
            final List<StrutsAction> formActions = table.getTableFormActions();
            int formSize = formActions.size();
            for (int i = 0; i < formSize; i++)
            {
                final StrutsAction action = formActions.get(i);
                int size = action.getActionParameters().size();
                for (int j = 0; j < size; j++)
                {
                    final StrutsParameter parameter = action.getActionParameters().get(j);
                    if (!columnNames.contains(parameter.getName()))
                    {
                        tableNonColumnActionParametersMap.put(parameter.getName(), parameter);
                    }
                }
            }

            tableNonColumnActionParameters = new ArrayList(tableNonColumnActionParametersMap.values());
        }

        return tableNonColumnActionParameters;
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsActionLogic#handleGetTableLinkName()
     */
    protected String handleGetTableLinkName()
    {
        String tableLink = null;

        final Object value = findTaggedValue(Bpm4StrutsProfile.TAGGEDVALUE_ACTION_TABLELINK);
        if (value != null)
        {
            tableLink = StringUtils.trimToNull(value.toString());

            if (tableLink != null)
            {
                final int columnOffset = tableLink.indexOf('.');
                tableLink = (columnOffset == -1) ? tableLink : tableLink.substring(0, columnOffset);
            }
        }

        return tableLink;
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsActionLogic#handleGetTableLinkColumnName()
     */
    protected String handleGetTableLinkColumnName()
    {
        String tableLink = null;

        final Object value = findTaggedValue(Bpm4StrutsProfile.TAGGEDVALUE_ACTION_TABLELINK);
        if (value != null)
        {
            tableLink = StringUtils.trimToNull(value.toString());

            if (tableLink != null)
            {
                final int columnOffset = tableLink.indexOf('.');
                tableLink = (columnOffset == -1 || columnOffset == tableLink.length() - 1)
                    ? null
                    : tableLink.substring(columnOffset + 1);
            }
        }

        return tableLink;
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsActionLogic#handleGetImagePath()
     */
    protected String handleGetImagePath()
    {
        return getPackagePath() + '/' + Bpm4StrutsUtils.toWebFileName(getActionClassName()) + ".gif";
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsActionLogic#handleGetActionPath()
     */
    protected String handleGetActionPath()
    {
        return getActionPathRoot() + '/' + getActionClassName();
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsActionLogic#handleGetActionPathRoot()
     */
    protected String handleGetActionPathRoot()
    {
        String actionPathRoot = null;

        final FrontEndUseCase useCase = this.getUseCase();
        if (useCase != null)
        {
            final StringBuilder buffer = new StringBuilder();

            final String actionPathPrefix = Bpm4StrutsGlobals.PROPERTY_ACTION_PATH_PREFIX;
            String prefix = this.isConfiguredProperty(actionPathPrefix) ? ObjectUtils
                .toString(this.getConfiguredProperty(actionPathPrefix)) : "";

            final ModelElementFacade useCasePackage = useCase.getPackage();
            if (useCasePackage != null)
            {
                prefix = prefix.replaceAll("\\{0\\}", useCasePackage.getPackagePath());
            }

            buffer.append(prefix);
            buffer.append('/');
            buffer.append(StringUtilsHelper.upperCamelCaseName(useCase.getName()));

            actionPathRoot = buffer.toString();
        }
        return actionPathRoot;
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsActionLogic#handleGetActionScope()
     */
    protected String handleGetActionScope()
    {
        return "request";
    }

    /**
     * @return getRoleUsers()
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsAction#getActionRoles()
     */
    protected String handleGetActionRoles()
    {
        final Collection users = getRoleUsers();
        final StringBuilder roles = new StringBuilder();
        for (final Iterator userIterator = users.iterator(); userIterator.hasNext();)
        {
            roles.append(((ModelElementFacade) userIterator.next()).getName());
            if (userIterator.hasNext())
            {
                roles.append(',');
            }
        }
        return roles.toString();
    }

    /**
     * Returns a collection containing StrutsUser instances representing the roles
     * authorized to call this action. If this action starts the use-case that use-case's users
     * are returned, otherwise it will return the users associated to the use-cases targeted by this
     * action (which may be none at all)
     */
    private Collection<Role> getRoleUsers()
    {
        final Collection<Role> roleUsers = new ArrayList<Role>();

        if (this.isUseCaseStart())
        {
            final FrontEndUseCase useCase = getUseCase();
            if (useCase != null)
            {
                roleUsers.addAll(useCase.getRoles());
            }
        }
        else
        {
            for (final StrutsForward forward : getActionForwards())
            {
                if (forward.getTarget() instanceof StrutsFinalState)
                {
                    final FrontEndUseCase useCase = ((StrutsFinalState) forward.getTarget()).getTargetUseCase();
                    if (useCase != null)
                    {
                        roleUsers.addAll(useCase.getRoles());
                    }
                }
            }
        }

        return roleUsers;
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsActionLogic#handleGetActionClassName()
     */
    protected String handleGetActionClassName()
    {
        String name = null;

        if (this.isExitingInitialState())
        {
            final UseCaseFacade useCase = this.getUseCase();
            if (useCase != null)
            {
                name = useCase.getName();
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

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsActionLogic#handleGetActionType()
     */
    protected String handleGetActionType()
    {
        return getPackageName() + '.' + getActionClassName();
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsActionLogic#handleGetFormBeanClassName()
     */
    protected String handleGetFormBeanClassName()
    {
        return getActionClassName() + Bpm4StrutsGlobals.FORM_IMPLEMENTATION_SUFFIX;
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsActionLogic#handleGetFormBeanName()
     */
    protected String handleGetFormBeanName()
    {
        String formBeanName = null;

        final UseCaseFacade useCase = this.getUseCase();
        if (useCase != null)
        {
            final String useCaseName = useCase.getName();
            formBeanName = StringUtilsHelper.lowerCamelCaseName(useCaseName) + getActionClassName() + Bpm4StrutsGlobals
                .FORM_SUFFIX;
        }
        return formBeanName;
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsActionLogic#handleGetFormValidationMethodName()
     */
    protected String handleGetFormValidationMethodName()
    {
        return "validate" + this.getActionClassName() + Bpm4StrutsGlobals.FORM_SUFFIX;
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsActionLogic#handleGetMessageKey()
     */
    protected String handleGetMessageKey()
    {
        String messageKey = null;

        final StrutsTrigger actionTrigger = getActionTrigger();
        if (actionTrigger != null)
        {
            messageKey = actionTrigger.getTriggerKey();
        }

        return messageKey;
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsActionLogic#handleGetImageMessageKey()
     */
    protected String handleGetImageMessageKey()
    {
        return getMessageKey() + ".image";
    }

    /**
     * Overrides the method defined in the facade parent of StrutsAction, this is done because actions (transitions) are
     * not directly contained in a UML namespace.
     * @return useCase.getPackageName()
     */
    public String getPackageName()
    {
        String packageName = null;

        final UseCaseFacade useCase = this.getUseCase();
        if (useCase != null)
        {
            packageName = useCase.getPackageName();
        }
        return packageName;
    }

    /**
     * @return Bpm4StrutsProfile.TAGGEDVALUE_ACTION_RESETTABLE
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsAction#isResettable()
     */
    protected boolean handleIsResettable()
    {
        Object value = findTaggedValue(Bpm4StrutsProfile.TAGGEDVALUE_ACTION_RESETTABLE);
        return isTrue(value == null ? null : value.toString());
    }

    /**
     * Convenient method to detect whether or not a String instance represents a boolean <code>true</code> value.
     */
    private boolean isTrue(String string)
    {
        return "yes".equalsIgnoreCase(string) ||
            "true".equalsIgnoreCase(string) ||
            "on".equalsIgnoreCase(string) ||
            "1".equalsIgnoreCase(string);
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsActionLogic#handleIsUseCaseStart()
     */
    protected boolean handleIsUseCaseStart()
    {
        StateVertexFacade source = getSource();
        return source instanceof PseudostateFacade && ((PseudostateFacade) source).isInitialState();
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsActionLogic#handleGetFullActionPath()
     */
    protected String handleGetFullActionPath()
    {
        return getPackagePath() + '/' + getActionClassName();
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsActionLogic#handleGetFullTilePath()
     */
    protected String handleGetFullTilePath()
    {
        return isUseCaseStart()
            ? "empty-file"
            : getPackagePath() + '/' + Bpm4StrutsUtils.toWebFileName(getActionClassName());
    }

    /**
     * We override this method here to make sure the actions end-up in the same package as their use-case. A transition
     * (this class' parent type) does not have a real package as we need it here.
     * @return this.getUseCase() / useCase.getPackagePath()
     */
    public String getPackagePath()
    {
        String packagePath = null;

        final UseCaseFacade useCase = this.getUseCase();
        if (useCase != null)
        {
            packagePath = '/' + useCase.getPackagePath();
        }
        return packagePath;
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsActionLogic#handleGetFullFormBeanPath()
     */
    protected String handleGetFullFormBeanPath()
    {
        return '/' + (getPackageName() + '/' + getFormBeanClassName()).replace('.', '/');
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsActionLogic#handleIsValidationRequired()
     */
    protected boolean handleIsValidationRequired()
    {
        for (final StrutsParameter parameter : getActionParameters())
        {
            if (parameter.isValidationRequired())
            {
                return true;
            }
        }
        return false;
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsActionLogic#handleIsDateFieldPresent()
     */
    protected boolean handleIsDateFieldPresent()
    {
        for (final StrutsParameter parameter : getActionParameters())
        {
            if (parameter.isDate())
            {
                return true;
            }
        }
        return false;
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsActionLogic#handleIsCalendarRequired()
     */
    protected boolean handleIsCalendarRequired()
    {
        for (final StrutsParameter parameter : getActionParameters())
        {
            if (parameter.isCalendarRequired())
            {
                return true;
            }
        }
        return false;
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsActionLogic#handleGetFormBeanPackageName()
     */
    protected String handleGetFormBeanPackageName()
    {
        return getPackageName();
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsActionLogic#handleGetFormBeanType()
     */
    protected String handleGetFormBeanType()
    {
        return getFormBeanPackageName() + '.' + getFormBeanClassName();
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsActionLogic#handleGetDocumentationKey()
     */
    protected String handleGetDocumentationKey()
    {
        final StrutsTrigger trigger = getActionTrigger();
        return ((trigger == null) ? getMessageKey() + ".is.an.action.without.trigger" : trigger.getTriggerKey()) +
            ".documentation";
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsActionLogic#handleGetDocumentationValue()
     */
    protected String handleGetDocumentationValue()
    {
        final String value = StringUtilsHelper.toResourceMessage(getDocumentation("", 64, false));
        return (value == null) ? "" : value;
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsActionLogic#handleGetOnlineHelpKey()
     */
    protected String handleGetOnlineHelpKey()
    {
        final StrutsTrigger trigger = getActionTrigger();
        return ((trigger == null) ? getMessageKey() + ".is.an.action.without.trigger" : trigger.getTriggerKey()) +
            ".online.help";
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsActionLogic#handleGetOnlineHelpValue()
     */
    protected String handleGetOnlineHelpValue()
    {
        final String crlf = "<br/>";
        final StringBuilder buffer = new StringBuilder();

        final String value = StringUtilsHelper.toResourceMessage(getDocumentation("", 64, false));
        buffer.append((value == null) ? "No action documentation has been specified" : value);
        buffer.append(crlf);

        return StringUtilsHelper.toResourceMessage(buffer.toString());
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsActionLogic#handleGetActionForwards()
     */
    protected List handleGetActionForwards()
    {
        if (actionForwards == null) initializeCollections();
        return new ArrayList(actionForwards.values());
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsActionLogic#handleGetDecisionTransitions()
     */
    protected List handleGetDecisionTransitions()
    {
        if (decisionTransitions == null) initializeCollections();
        return new ArrayList(decisionTransitions);
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsActionLogic#handleGetActionStates()
     */
    protected List handleGetActionStates()
    {
        if (actionStates == null) initializeCollections();
        return new ArrayList(actionStates);
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsActionLogic#handleGetActionExceptions()
     */
    protected List<FrontEndExceptionHandler> handleGetActionExceptions()
    {
        final Collection<FrontEndExceptionHandler> exceptions = new LinkedHashSet();
        final Collection<StrutsActionState> actionStates = getActionStates();
        for (final Iterator<StrutsActionState> iterator = actionStates.iterator(); iterator.hasNext();)
        {
            StrutsActionState actionState = iterator.next();
            exceptions.addAll(actionState.getExceptions());
        }

        return new ArrayList<FrontEndExceptionHandler>(exceptions);
    }

    /**
     * @return PseudostateFacade or STEREOTYPE_FRONT_END_VIEW
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsAction#getInput()
     */
    protected Object handleGetInput()
    {
        Object input = null;
        final ModelElementFacade source = getSource();
        if (source instanceof PseudostateFacade)
        {
            final PseudostateFacade pseudostate = (PseudostateFacade) source;
            if (pseudostate.isInitialState())
            {
                input = source;
            }
        }
        else
        {
            if (source.hasStereotype(UMLProfile.STEREOTYPE_FRONT_END_VIEW))
            {
                input = source;
            }
        }
        return input;
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsActionLogic#handleGetController()
     */
    protected Object handleGetController()
    {
        final StrutsActivityGraph graph = this.getStrutsActivityGraph();
        return graph == null ? null : graph.getController();
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsActionLogic#handleGetActionTrigger()
     */
    protected Object handleGetActionTrigger()
    {
        return this.getTrigger();
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsActionLogic#handleGetActionFormFields()
     */
    protected List handleGetActionFormFields()
    {
        final Map formFieldMap = new HashMap();

        /**
         * for useCaseStart actions we need to detect all usecases forwarding to the one belonging to this action
         * if there are any parameters in those requests we need to have them included in this action's form
         */
        if (this.isUseCaseStart())
        {
            final FrontEndUseCase useCase = this.getUseCase();
            if (useCase != null)
            {
                final Collection<FrontEndFinalState> finalStates = useCase.getReferencingFinalStates();
                for (final Iterator finalStateIterator = finalStates.iterator(); finalStateIterator.hasNext();)
                {
                    final Object finalStateObject = finalStateIterator.next();
                    // we need to test for the type because a non struts-use-case final state might accidentally
                    // be linking to this use-case (for example: the user temporarily wants to disable code generation
                    // for a specific use-case and is not removing the final-state to use-case link(s))
                    if (finalStateObject instanceof StrutsFinalState)
                    {
                        final StrutsFinalState finalState = (StrutsFinalState) finalStateObject;
                        final Collection parameters = finalState.getInterUseCaseParameters();
                        for (final Iterator parameterIterator = parameters.iterator(); parameterIterator.hasNext();)
                        {
                            final ParameterFacade parameter = (ParameterFacade) parameterIterator.next();
                            formFieldMap.put(parameter.getName(), parameter);
                        }
                    }
                }
            }
        }

        // if any action encountered by the execution of the complete action-graph path emits a forward
        // containing one or more parameters they need to be included as a form field too
        for (final StrutsActionState actionState : getActionStates())
        {
            final StrutsForward forward = (StrutsForward) actionState.getForward();
            if (forward != null)
            {
                for (final FrontEndParameter forwardParameter : forward.getForwardParameters())
                {
                    formFieldMap.put(forwardParameter.getName(), forwardParameter);
                }
            }
        }

        // add page variables for all pages/final-states targeted
        // also add the fields of the target page's actions (for preloading)
        for (final StrutsForward forward : getActionForwards())
        {
            final StateVertexFacade target = forward.getTarget();
            if (target instanceof StrutsJsp)
            {
                final StrutsJsp jsp = (StrutsJsp) target;
                for (final StrutsParameter facade : jsp.getPageVariables())
                {
                    formFieldMap.put(facade.getName(), facade);
                }
                for (final FrontEndParameter facade : jsp.getAllActionParameters())
                {
                    formFieldMap.put(facade.getName(), facade);
                }
            }
            else if (target instanceof StrutsFinalState)
            {
                // only add these if there is no parameter recorded yet with the same name
                for (final FrontEndParameter facade : forward.getForwardParameters())
                {
                    if (!formFieldMap.containsKey(facade.getName()))
                    {
                        formFieldMap.put(facade.getName(), facade);
                    }
                }
            }
        }

        // we do the action parameters in the end because they are allowed to overwrite existing properties
        for (final StrutsParameter facade : getActionParameters())
        {
            formFieldMap.put(facade.getName(), facade);
        }

        return new ArrayList(formFieldMap.values());
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsActionLogic#handleGetDeferredOperations()
     */
    protected List handleGetDeferredOperations()
    {
        final Collection deferredOperations = new LinkedHashSet();

        final StrutsController controller = getController();
        if (controller != null)
        {
            final List<StrutsActionState> actionStates = getActionStates();
            int size = actionStates.size();
            for (int i = 0; i < size; i++)
            {
                final StrutsActionState actionState = actionStates.get(i);
                deferredOperations.addAll(actionState.getControllerCalls());
            }

            final List transitions = getDecisionTransitions();
            size = transitions.size();
            for (int i = 0; i < size; i++)
            {
                final StrutsForward forward = (StrutsForward) transitions.get(i);
                final FrontEndEvent trigger = forward.getDecisionTrigger();
                if (trigger != null)
                {
                    deferredOperations.add(trigger.getControllerCall());
                }
            }
        }
        return new ArrayList(deferredOperations);
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsActionLogic#handleGetActionParameters()
     */
    protected List handleGetActionParameters()
    {
        final StrutsTrigger trigger = getActionTrigger();
        return (trigger == null) ? Collections.emptyList() : new ArrayList(trigger.getParameters());
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsActionLogic#handleGetInterUseCaseParameters(org.andromda.cartridges.bpm4struts.metafacades.StrutsFinalState)
     */
    protected List handleGetInterUseCaseParameters(StrutsFinalState finalState)
    {
        List parameters;

        if (finalState == null)
        {
            parameters = Collections.emptyList();
        }
        else
        {
            // we don't want to list parameters with the same name to we use a hash map
            final Map parameterMap = new HashMap();

            final List transitions = getActionForwards();
            int size = transitions.size();
            for (int i = 0; i < size; i++)
            {
                final StrutsForward forward = (StrutsForward) transitions.get(i);
                // only return those parameters that belong to both this action and the argument final state
                if (finalState.equals(forward.getTarget()))
                {
                    final List forwardParameters = forward.getForwardParameters();
                    int paramSize = forwardParameters.size();
                    for (int j = 0; j < paramSize; j++)
                    {
                        final ModelElementFacade parameter = (ModelElementFacade) forwardParameters.get(j);
                        parameterMap.put(parameter.getName(), parameter);
                    }
                }
            }
            parameters = new ArrayList(parameterMap.values());
        }

        return parameters;
    }

    /**
     * @return getActionForwards().getTarget()
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsAction#getTargetPages()
     */
    protected List handleGetTargetPages()
    {
        Collection targetPages = new LinkedHashSet();

        for (final StrutsForward forward : getActionForwards())
        {
            if (forward.isEnteringPage())
            {
                targetPages.add(forward.getTarget());
            }
        }

        return new ArrayList(targetPages);
    }

    /**
     * @return new ArrayList(transitions)
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsActionLogic#getTransitions()
     */
    protected List handleGetTransitions()
    {
        if (transitions == null)
        {
            initializeCollections();
        }
        return new ArrayList(transitions);
    }

    /**
     * @return getActionTrigger().getName() lowerCamelCaseName
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsAction#getStyleId()
     */
    protected String handleGetStyleId()
    {
        String styleId = null;

        StrutsTrigger trigger = getActionTrigger();
        if (trigger != null)
        {
            String triggerName = trigger.getName();
            styleId = StringUtilsHelper.lowerCamelCaseName(triggerName);
        }
        return styleId;
    }

    /**
     * @return Bpm4StrutsProfile.TAGGEDVALUE_ACTION_REDIRECT
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsAction#isRedirect()
     */
    protected boolean handleIsRedirect()
    {
        String redirect = (String) this.getConfiguredProperty(Bpm4StrutsGlobals.PROPERTY_DEFAULT_ACTION_REDIRECT);
        Object value = this.findTaggedValue(Bpm4StrutsProfile.TAGGEDVALUE_ACTION_REDIRECT);
        if (value != null)
        {
            redirect = (String) value;
        }
        return Boolean.valueOf(StringUtils.trimToEmpty(redirect));
    }

    /**
     * @return getActionParameters().isShouldReset()
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsAction#getResettableActionParameters()
     */
    protected List handleGetResettableActionParameters()
    {
        return new ArrayList(new FilteredCollection(this.getActionParameters())
        {
            private static final long serialVersionUID = 34L;
            public boolean evaluate(Object object)
            {
                return object != null && ((StrutsParameter) object).isShouldReset();
            }
        });
    }

    /**
     * The "session" action form scope.
     */
    private static final String FORM_SCOPE_SESSION = "session";

    /**
     * The "request" action form scope.
     */
    private static final String FORM_SCOPE_REQUEST = "request";

    /**
     * The "none" action form scope.
     */
    private static final String FORM_SCOPE_NONE = "none";

    /**
     * @return Bpm4StrutsProfile.TAGGEDVALUE_ACTION_FORM_SCOPE
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsAction#getFormScope()
     */
    protected String handleGetFormScope()
    {
        String actionFormScope = String
            .valueOf(this.getConfiguredProperty(Bpm4StrutsGlobals.PROPERTY_ACTION_FORM_SCOPE));
        Object value = this.findTaggedValue(Bpm4StrutsProfile.TAGGEDVALUE_ACTION_FORM_SCOPE);
        if (value != null)
        {
            actionFormScope = String.valueOf(value);
        }
        return StringUtils.trimToEmpty(actionFormScope);
    }

    /**
     * @return getFormScope().equalsIgnoreCase(FORM_SCOPE_SESSION)
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsAction#isFormScopeSession()
     */
    protected boolean handleIsFormScopeSession()
    {
        return FORM_SCOPE_SESSION.equalsIgnoreCase(this.getFormScope());
    }

    /**
     * @return getFormScope().equalsIgnoreCase(FORM_SCOPE_REQUEST)
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsAction#isFormScopeRequest()
     */
    protected boolean handleIsFormScopeRequest()
    {
        return FORM_SCOPE_REQUEST.equalsIgnoreCase(this.getFormScope());
    }

    /**
     * @return getFormScope().equalsIgnoreCase(FORM_SCOPE_NONE)
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsAction#isFormScopeNone()
     */
    protected boolean handleIsFormScopeNone()
    {
        return this.getFormScope().equalsIgnoreCase(FORM_SCOPE_NONE);
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsActionLogic#handleGetHiddenActionParameters()
     */
    protected List handleGetHiddenActionParameters()
    {
        final List hiddenActionParameters = new ArrayList(this.getActionParameters());
        CollectionUtils.filter(hiddenActionParameters, new Predicate()
        {
            public boolean evaluate(final Object object)
            {
                return StrutsParameterLogicImpl.HIDDEN_INPUT_TYPE.equals(((StrutsParameter) object).getWidgetType());
            }
        });
        return hiddenActionParameters;
    }
}
