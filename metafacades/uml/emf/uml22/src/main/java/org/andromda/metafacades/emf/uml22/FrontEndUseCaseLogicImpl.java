package org.andromda.metafacades.emf.uml22;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;

import org.andromda.metafacades.uml.ActionStateFacade;
import org.andromda.metafacades.uml.ActivityGraphFacade;
import org.andromda.metafacades.uml.ActorFacade;
import org.andromda.metafacades.uml.AssociationEndFacade;
import org.andromda.metafacades.uml.AttributeFacade;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.FinalStateFacade;
import org.andromda.metafacades.uml.FrontEndAction;
import org.andromda.metafacades.uml.FrontEndActivityGraph;
import org.andromda.metafacades.uml.FrontEndAttribute;
import org.andromda.metafacades.uml.FrontEndController;
import org.andromda.metafacades.uml.FrontEndEvent;
import org.andromda.metafacades.uml.FrontEndFinalState;
import org.andromda.metafacades.uml.FrontEndForward;
import org.andromda.metafacades.uml.FrontEndParameter;
import org.andromda.metafacades.uml.FrontEndUseCase;
import org.andromda.metafacades.uml.FrontEndView;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.Role;
import org.andromda.metafacades.uml.UMLMetafacadeUtils;
import org.andromda.metafacades.uml.UMLProfile;
import org.andromda.metafacades.uml.UseCaseFacade;
import org.andromda.metafacades.uml.web.MetafacadeWebGlobals;
import org.andromda.metafacades.uml.web.MetafacadeWebProfile;
import org.andromda.metafacades.uml.web.MetafacadeWebUtils;
import org.andromda.utils.StringUtilsHelper;
import org.apache.commons.lang3.StringUtils;

/**
 * MetafacadeLogic implementation for
 * org.andromda.metafacades.uml.FrontEndUseCase.
 *
 * @see org.andromda.metafacades.uml.FrontEndUseCase
 */
public class FrontEndUseCaseLogicImpl
        extends FrontEndUseCaseLogic {
    private static final long serialVersionUID = 34L;

    /**
     * @param metaObject
     * @param context
     */
    public FrontEndUseCaseLogicImpl(
            final Object metaObject,
            final String context) {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndUseCase#isEntryUseCase()
     */
    @Override
    protected boolean handleIsEntryUseCase() {
        return this.hasStereotype(UMLProfile.STEREOTYPE_FRONT_END_APPLICATION);
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndUseCase#getController()
     */
    @Override
    protected FrontEndController handleGetController() {
        final FrontEndActivityGraph graph = this.getActivityGraph();
        return graph == null ? null : graph.getController();
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndUseCase#getActivityGraph()
     */
    @Override
    protected ActivityGraphFacade handleGetActivityGraph() {
        // There is a method in use case Facade.
        // We can use it because, for now, we don't support hyperlink neither
        // tag value way to define
        // which activity graph is modelized for this use case.
        return this.getFirstActivityGraph();
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndUseCase#getReferencingFinalStates()
     */
    @Override
    protected List<FinalStateFacade> handleGetReferencingFinalStates() {
        return new ArrayList<FinalStateFacade>(this.getModel().findFinalStatesWithNameOrHyperlink(this));
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndUseCase#getAllUseCases()
     */
    @Override
    protected List<FrontEndUseCase> handleGetAllUseCases() {
        final List<FrontEndUseCase> useCases = new ArrayList<FrontEndUseCase>();
        for (UseCaseFacade useCase : this.getModel().getAllUseCases()) {
            if (useCase instanceof FrontEndUseCase) {
                useCases.add((FrontEndUseCase) useCase);
            }
        }
        return useCases;
    }

    /**
     * Gets those roles directly associated to this use-case.
     */
    private Collection<Role> getAssociatedRoles() {
        final Collection<Role> usersList = new ArrayList<Role>();
        final Collection<AssociationEndFacade> associationEnds = this.getAssociationEnds();
        for (final Iterator<AssociationEndFacade> iterator = associationEnds.iterator(); iterator.hasNext();) {
            final AssociationEndFacade associationEnd = iterator.next();
            final ClassifierFacade classifier = associationEnd.getOtherEnd().getType();
            if (classifier instanceof Role) {
                usersList.add((Role) classifier);
            }
        }
        return usersList;
    }

    /**
     * Recursively collects all roles generalizing the argument user, in the
     * specified collection.
     */
    private void collectRoles(
            final Role role,
            final Collection<Role> roles) {
        if (!roles.contains(role)) {
            roles.add(role);
            final Collection<ActorFacade> childUsers = role.getGeneralizedByActors();
            for (final Iterator<ActorFacade> iterator = childUsers.iterator(); iterator.hasNext();) {
                final Role childUser = (Role) iterator.next();
                this.collectRoles(
                        childUser,
                        roles);
            }
        }
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndUseCase#getRoles()
     */
    @Override
    protected List<Role> handleGetRoles() {
        final Collection<Role> allRoles = new LinkedHashSet<Role>();
        final Collection<Role> associatedUsers = this.getAssociatedRoles();
        for (Role user : associatedUsers) {
            this.collectRoles(
                    user,
                    allRoles);
        }
        return new ArrayList<Role>(allRoles);
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndUseCase#getAllRoles()
     */
    @Override
    protected List<Role> handleGetAllRoles() {
        final Collection<Role> allRoles = new LinkedHashSet<Role>();
        for (final FrontEndUseCase useCase : this.getAllUseCases()) {
            allRoles.addAll(useCase.getRoles());
        }
        return new ArrayList<Role>(allRoles);
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndUseCase#isSecured()
     */
    @Override
    protected boolean handleIsSecured() {
        return !this.getRoles().isEmpty();
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndUseCase#getViews()
     */
    @Override
    protected List<ActionStateFacade> handleGetViews() {
        // TODO: Return type FrontEndView, instead of ActionStateFacade
        final List views = new ArrayList<ActionStateFacade>();
        final ActivityGraphFacade graph = this.getActivityGraph();
        if (graph != null) {
            views.addAll(this.getModel().getAllActionStatesWithStereotype(
                    graph,
                    UMLProfile.STEREOTYPE_FRONT_END_VIEW));
        }
        return views;
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndUseCase#getViews()
     */
    @Override
    protected List<FrontEndAction> handleGetActions() {
        final Collection<FrontEndAction> actions = new LinkedHashSet<FrontEndAction>();
        final Collection<FrontEndView> pages = this.getViews();
        for (final Iterator<FrontEndView> pageIterator = pages.iterator(); pageIterator.hasNext();) {
            final FrontEndView view = pageIterator.next();
            actions.addAll(view.getActions());
        }

        final FrontEndActivityGraph graph = this.getActivityGraph();
        if (graph != null) {
            final FrontEndAction action = graph.getInitialAction();
            if (action != null) {
                actions.add(action);
            }
        }
        return new ArrayList<FrontEndAction>(actions);
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndUseCase#getInitialView()
     */
    @Override
    protected FrontEndView handleGetInitialView() {
        FrontEndView view = null;
        final FrontEndActivityGraph graph = this.getActivityGraph();
        final FrontEndAction action = graph == null ? null : this.getActivityGraph().getInitialAction();
        final Collection<FrontEndForward> forwards = action == null ? null : action.getActionForwards();
        if (forwards != null) {
            for (final Iterator<FrontEndForward> iterator = forwards.iterator(); iterator.hasNext();) {
                final FrontEndForward forward = iterator.next();
                final Object target = forward.getTarget();
                if (target instanceof FrontEndView) {
                    view = (FrontEndView) target;
                } else if (target instanceof FrontEndFinalState) {
                    final FrontEndFinalState finalState = (FrontEndFinalState) target;
                    final FrontEndUseCase targetUseCase = finalState.getTargetUseCase();
                    if (targetUseCase != null && !targetUseCase.equals(this.THIS())) {
                        view = targetUseCase.getInitialView();
                    }
                }
            }
        }
        return view;
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndUseCase#getViewVariables()
     */
    @Override
    protected List<FrontEndParameter> handleGetViewVariables() {
        final Map<String, FrontEndParameter> pageVariableMap = new LinkedHashMap<String, FrontEndParameter>();

        // - page variables can occur twice or more in the usecase if their
        // names are the same for different forms, storing them in a map
        // solves this issue because those names do not have the action-name
        // prefix
        final Collection<FrontEndView> views = this.getViews();
        for (final Iterator pageIterator = views.iterator(); pageIterator.hasNext();) {
            final FrontEndView view = (FrontEndView) pageIterator.next();
            final Collection<FrontEndParameter> variables = view.getVariables();
            for (FrontEndParameter variable : variables) {
                final String name = variable.getName();
                if (StringUtils.isNotBlank(name)) {
                    final FrontEndParameter existingVariable = pageVariableMap.get(name);
                    if (existingVariable != null && existingVariable.isTable()) {
                        variable = existingVariable;
                    }
                    pageVariableMap.put(
                            name,
                            variable);
                }
            }
        }
        return new ArrayList<FrontEndParameter>(pageVariableMap.values());
    }

    @Override
    public String handleGetActionClassName() {
        return StringUtilsHelper.upperCamelCaseName(this.getName());
    }

    @Override
    public List<FrontEndAction> handleGetActionForwards() {
        final Set<FrontEndAction> actionForwards = new LinkedHashSet<FrontEndAction>();
        for (final FrontEndView view : this.getViews()) {
            for (FrontEndForward frontEndAction : view.getActionForwards()) {
                actionForwards.add((FrontEndAction) frontEndAction);
            }
        }
        return new ArrayList<FrontEndAction>(actionForwards);
    }

    @Override
    public List handleGetAllForwards() {
        final Map<String, ModelElementFacade> forwards = new LinkedHashMap<String, ModelElementFacade>();
        for (final FrontEndAction forward : this.getActionForwards()) {
            forwards.put(forward.getName(), forward);
        }
        for (final FrontEndForward forward : this.getForwards()) {
            forwards.put(forward.getName(), forward);
        }
        return new ArrayList<ModelElementFacade>(forwards.values());
    }

    /**
     * Collects all attribute messages into the given Map.
     *
     * @param messages       the Map in which messages are collected.
     * @param attributes     the attributes to collect the messages from.
     * @param resolvingTypes used to prevent endless recursion.
     */
    private void collectAttributeMessages(Map<String, String> messages, Collection attributes,
            final Collection<ClassifierFacade> resolvingTypes) {
        if (attributes != null && !attributes.isEmpty()) {
            for (final Iterator iterator = attributes.iterator(); iterator.hasNext();) {
                final FrontEndAttribute attribute = (FrontEndAttribute) iterator.next();
                messages.put(
                        attribute.getMessageKey(),
                        attribute.getMessageValue());
                // - lets go another level for nested attributes
                this.collectTypeMessages(messages, attribute.getType(), resolvingTypes);
            }
        }
    }

    /**
     * Collects all association end messages into the given Map.
     *
     * @param messages        the Map in which messages are collected.
     * @param associationEnds the association ends to collect the messages from.
     * @param resolvingTypes  used to prevent endless recursion.
     */
    private void collectAssociationEndMessages(Map<String, String> messages, Collection associationEnds,
            final Collection<ClassifierFacade> resolvingTypes) {
        if (associationEnds != null && !associationEnds.isEmpty()) {
            for (final Iterator iterator = associationEnds.iterator(); iterator.hasNext();) {
                final AssociationEndFacade end = (AssociationEndFacade) iterator.next();
                this.collectTypeMessages(messages, end.getType(), resolvingTypes);
            }
        }
    }

    private void collectTypeMessages(Map<String, String> messages, ClassifierFacade type,
            final Collection<ClassifierFacade> resolvingTypes) {
        if (type != null) {
            if (!resolvingTypes.contains(type)) {
                resolvingTypes.add(type);
                if (type.isArrayType()) {
                    type = type.getNonArray();
                }
                // check again, since the type can be changed
                if (!resolvingTypes.contains(type)) {
                    this.collectAttributeMessages(messages, type.getAttributes(), resolvingTypes);
                    this.collectAssociationEndMessages(messages, type.getNavigableConnectingEnds(), resolvingTypes);
                }
            }
            resolvingTypes.remove(type);
        }
    }

    /**
     * Indicates whether or not we should normalize messages.
     *
     * @return true/false
     */
    private boolean isNormalizeMessages() {
        final String normalizeMessages = (String) getConfiguredProperty(MetafacadeWebGlobals.NORMALIZE_MESSAGES);
        return Boolean.valueOf(normalizeMessages).booleanValue();
    }

    @Override
    public Map handleGetAllMessages() {
        final boolean normalize = this.isNormalizeMessages();
        final Map<String, String> messages = normalize ? new TreeMap<String, String>()
                : new LinkedHashMap<String, String>();

        // - only retrieve the messages for the entry use case (i.e. the use case
        // where the application begins)
        if (this.isEntryUseCase()) {
            final List<FrontEndUseCase> useCases = this.getAllUseCases();
            for (int ctr = 0; ctr < useCases.size(); ctr++) {
                // - usecase
                final FrontEndUseCase useCase = (FrontEndUseCase) useCases.get(ctr);
                messages.put(
                        useCase.getTitleKey(),
                        useCase.getTitleValue());

                final List<FrontEndView> views = useCase.getViews();
                for (int ctr2 = 0; ctr2 < views.size(); ctr2++) {
                    // - view
                    final FrontEndView view = (FrontEndView) views.get(ctr2);
                    messages.put(
                            view.getTitleKey(),
                            view.getTitleValue());
                    messages.put(
                            view.getMessageKey(),
                            view.getMessageValue());
                    messages.put(
                            view.getDocumentationKey(),
                            view.getDocumentationValue());

                    final List<FrontEndParameter> viewVariables = view.getVariables();
                    for (int ctr3 = 0; ctr3 < viewVariables.size(); ctr3++) {
                        // - page variables
                        final Object object = viewVariables.get(ctr3);
                        if (object instanceof FrontEndParameter) {
                            final FrontEndParameter parameter = (FrontEndParameter) object;

                            final Collection<ClassifierFacade> resolvingTypes = new ArrayList<ClassifierFacade>();
                            this.collectAttributeMessages(messages, parameter.getAttributes(), resolvingTypes);
                            this.collectAssociationEndMessages(messages,
                                    parameter.getNavigableAssociationEnds(), resolvingTypes);
                            messages.put(
                                    parameter.getMessageKey(),
                                    parameter.getMessageValue());

                            // - table
                            if (parameter.isTable()) {
                                for (String columnName : parameter.getTableColumnNames()) {
                                    messages.put(
                                            parameter.getTableColumnMessageKey(columnName),
                                            parameter.getTableColumnMessageValue(columnName));
                                }
                            }
                        }
                    }

                    final List<FrontEndAction> actions = useCase.getActions();
                    for (int ctr3 = 0; ctr3 < actions.size(); ctr3++) {
                        // - action
                        final FrontEndAction action = (FrontEndAction) actions.get(ctr3);

                        // - event/trigger
                        final Object trigger = action.getTrigger();
                        if (trigger != null && trigger instanceof FrontEndEvent) {
                            final FrontEndEvent event = (FrontEndEvent) trigger;
                            // only add these when a trigger is present, otherwise it's no use having them
                            messages.put(
                                    action.getDocumentationKey(),
                                    action.getDocumentationValue());

                            // the regular trigger messages
                            messages.put(
                                    event.getResetMessageKey(),
                                    event.getResetMessageValue());

                            // this one is the same as doing: action.getMessageKey()
                            messages.put(
                                    event.getMessageKey(),
                                    event.getMessageValue());

                            // - IMAGE LINK

                            /*
                             * if (action.isImageLink())
                             * {
                             * messages.put(
                             * action.getImageMessageKey(),
                             * action.getImagePath());
                             * }
                             */
                        }

                        // - forwards
                        for (final FrontEndForward forward : action.getTransitions()) {
                            if (forward instanceof FrontEndForward) {
                                final FrontEndForward forwardTransition = (FrontEndForward) forward;
                                messages.putAll(forwardTransition.getSuccessMessages());
                                messages.putAll(forwardTransition.getWarningMessages());
                            } else {
                                final FrontEndAction actionTransition = (FrontEndAction) forward;
                                messages.putAll(actionTransition.getSuccessMessages());
                                messages.putAll(actionTransition.getWarningMessages());
                            }

                        }

                        // - action parameters
                        final List<FrontEndParameter> parameters = action.getParameters();
                        for (int l = 0; l < parameters.size(); l++) {
                            final FrontEndParameter parameter = parameters.get(l);
                            final Collection attributes = parameter.getAttributes();
                            if (!attributes.isEmpty()) {
                                for (final Iterator iterator = attributes.iterator(); iterator.hasNext();) {
                                    final FrontEndAttribute attribute = (FrontEndAttribute) iterator.next();
                                    messages.put(
                                            attribute.getMessageKey(),
                                            attribute.getMessageValue());
                                }
                            }
                            final Collection associationEnds = parameter.getNavigableAssociationEnds();
                            if (!associationEnds.isEmpty()) {
                                for (final Iterator iterator = associationEnds.iterator(); iterator.hasNext();) {
                                    final AssociationEndFacade end = (AssociationEndFacade) iterator.next();
                                    final ClassifierFacade type = end.getType();
                                    if (type != null) {
                                        final Collection<AttributeFacade> typeAttributes = type.getAttributes();
                                        if (!attributes.isEmpty()) {
                                            for (final Iterator<AttributeFacade> attributeIterator = typeAttributes
                                                    .iterator(); attributeIterator.hasNext();) {
                                                final FrontEndAttribute attribute = (FrontEndAttribute) attributeIterator
                                                        .next();
                                                messages.put(
                                                        attribute.getMessageKey(),
                                                        attribute.getMessageValue());
                                            }
                                        }
                                    }
                                }
                            }
                            messages.put(
                                    parameter.getMessageKey(),
                                    parameter.getMessageValue());
                            messages.put(
                                    parameter.getDocumentationKey(),
                                    parameter.getDocumentationValue());

                            // - submittable input table
                            if (parameter.isInputTable()) {
                                final Collection<String> columnNames = parameter.getTableColumnNames();
                                for (final Iterator<String> columnNameIterator = columnNames
                                        .iterator(); columnNameIterator.hasNext();) {
                                    final String columnName = columnNameIterator.next();
                                    messages.put(
                                            parameter.getTableColumnMessageKey(columnName),
                                            parameter.getTableColumnMessageValue(columnName));
                                }
                            }
                            /*
                             * if (parameter.getValidWhen() != null)
                             * {
                             * // this key needs to be fully qualified since the valid when value can be
                             * different
                             * final String completeKeyPrefix =
                             * (normalize)
                             * ? useCase.getTitleKey() + '.' + view.getMessageKey() + '.' +
                             * action.getMessageKey() + '.' + parameter.getMessageKey() :
                             * parameter.getMessageKey();
                             * messages.put(
                             * completeKeyPrefix + "_validwhen",
                             * "{0} is only valid when " + parameter.getValidWhen());
                             * }
                             */
                            /*
                             * if (parameter.getOptionCount() > 0)
                             * {
                             * final List optionKeys = parameter.getOptionKeys();
                             * final List optionValues = parameter.getOptionValues();
                             * 
                             * for (int m = 0; m < optionKeys.size(); m++)
                             * {
                             * messages.put(
                             * optionKeys.get(m),
                             * optionValues.get(m));
                             * messages.put(
                             * optionKeys.get(m) + ".title",
                             * optionValues.get(m));
                             * }
                             * }
                             */
                        }

                        // - portlet preferences
                        // final AngularPortletPreferences preferences = useCase.getPreferences();
                        // if (preferences != null)
                        // {
                        // final Collection<AttributeFacade> attributes =
                        // preferences.getAttributes(true);
                        // if (!attributes.isEmpty())
                        // {
                        // for (final Iterator iterator = attributes.iterator(); iterator.hasNext();)
                        // {
                        // final AngularAttribute attribute = (AngularAttribute)iterator.next();
                        // messages.put(
                        // attribute.getMessageKey(),
                        // attribute.getMessageValue());
                        // }
                        // }
                        // }

                        // - exception forwards

                        /*
                         * final List exceptions = action.getActionExceptions();
                         * 
                         * if (normalize)
                         * {
                         * if (exceptions.isEmpty())
                         * {
                         * messages.put("exception.occurred", "{0}");
                         * }
                         * else
                         * {
                         * for (int l = 0; l < exceptions.size(); l++)
                         * {
                         * final FrontEndExceptionHandler exception =
                         * (FrontEndExceptionHandler)exceptions.get(l);
                         * messages.put(action.getMessageKey() + '.' + exception.getExceptionKey(),
                         * "{0}");
                         * }
                         * }
                         * }
                         * else
                         * {
                         * if (exceptions.isEmpty())
                         * {
                         * if (!action.isUseCaseStart())
                         * {
                         * messages.put(action.getMessageKey() + ".exception",
                         * "{0} (java.lang.Exception)");
                         * }
                         * }
                         * else
                         * {
                         * for (int l = 0; l < exceptions.size(); l++)
                         * {
                         * final FrontEndExceptionHandler exception =
                         * (FrontEndExceptionHandler)exceptions.get(l);
                         * 
                         * // we construct the key using the action message too because the exception
                         * can
                         * // belong to more than one action (therefore it cannot return the correct
                         * value
                         * // in .getExceptionKey())
                         * messages.put(
                         * action.getMessageKey() + '.' + exception.getExceptionKey(),
                         * "{0} (" + exception.getExceptionType() + ")");
                         * }
                         * }
                         * }
                         */
                    }
                }
            }
        }
        return messages;
    }

    // @Override
    // protected String handleGetPath() {
    //     String actionPath = null;
    //     final FrontEndActivityGraph graph = this.getActivityGraph();
    //     if (graph != null) {
    //         final FrontEndAction action = (FrontEndAction) graph.getInitialAction();
    //         if (action != null) {
    //             actionPath = action.getPath();
    //         }
    //     }
    //     return actionPath;
    // }

    @Override
    protected String handleGetPathRoot() {
        final StringBuilder pathRoot = new StringBuilder("/");
        final String packagePath = this.getPackagePath();
        final String prefix = packagePath != null ? packagePath.trim() : "";
        pathRoot.append(prefix);
        return pathRoot.toString();
    }

    @Override
    protected String handleGetForwardName() {
        return MetafacadeWebUtils.toWebResourceName(this.getName()) + MetafacadeWebGlobals.USECASE_FORWARD_NAME_SUFFIX;
    }

    @Override
    protected String handleGetTitleKey() {
        return StringUtilsHelper.toResourceMessageKey(
                this.isNormalizeMessages() ? this.getTitleValue() : this.getName()) + '.' +
                MetafacadeWebGlobals.TITLE_MESSAGE_KEY_SUFFIX;
    }

    @Override
    protected String handleGetTitleValue() {
        return StringUtilsHelper.toPhrase(getName());
    }

    @Override
    protected String handleGetFullyQualifiedActionClassPath() {
        return this.getFullyQualifiedActionClassName().replace(
                '.',
                '/');
    }

    @Override
    protected String handleGetControllerAction() {
        return StringUtilsHelper.lowerCamelCaseName(this.getName());
    }

    @Override
    protected String handleGetFullyQualifiedActionClassName() {
        final StringBuilder path = new StringBuilder();
        final String packageName = this.getPackageName();
        if (StringUtils.isNotBlank(packageName)) {
            path.append(packageName);
            path.append('.');
        }
        path.append(this.getActionClassName());
        return path.toString();
    }

    @Override
    protected String handleGetFormKey() {
        final Object formKeyValue = this.findTaggedValue(MetafacadeWebProfile.TAGGEDVALUE_ACTION_FORM_KEY);
        return formKeyValue == null ? Objects.toString(this.getConfiguredProperty(MetafacadeWebGlobals.ACTION_FORM_KEY))
                : String.valueOf(formKeyValue);
    }

    /**
     * Gets the initial target when this use case is entered.
     *
     * @return the initial target.
     */
    private Object getInitialTarget() {
        Object initialTarget = null;
        final FrontEndActivityGraph graph = this.getActivityGraph();
        final FrontEndAction action = graph != null ? this.getActivityGraph().getInitialAction() : null;
        final Collection<FrontEndForward> forwards = action != null ? action.getActionForwards() : null;
        if (forwards != null && !forwards.isEmpty()) {
            final Object target = forwards.iterator().next().getTarget();
            if (target instanceof FrontEndView) {
                initialTarget = target;
            } else if (target instanceof FrontEndFinalState) {
                final FrontEndFinalState finalState = (FrontEndFinalState) target;
                final FrontEndUseCase targetUseCase = finalState.getTargetUseCase();
                if (targetUseCase != null && !targetUseCase.equals(this.THIS())) {
                    initialTarget = targetUseCase;
                }
            }
        }
        return initialTarget;
    }

    @Override
    protected String handleGetInitialTargetPath() {
        String path = null;
        final Object target = this.getInitialTarget();
        if (target instanceof FrontEndView) {
            path = ((FrontEndView) target).getPath();
        } else if (target instanceof FrontEndUseCase) {
            path = ((FrontEndUseCase) target).getPath();
        }
        return path;
    }

    @Override
    protected boolean handleIsInitialTargetView() {
        return this.getInitialTarget() instanceof FrontEndView;
    }

    @Override
    protected boolean handleIsApplicationValidationRequired() {
        boolean required = false;
        for (final FrontEndUseCase useCase : this.getAllUseCases()) {
            if (useCase.isValidationRequired()) {
                required = true;
                break;
            }
        }
        return required;
    }

    @Override
    protected boolean handleIsValidationRequired() {
        boolean required = false;
        for (final FrontEndView view : this.getViews()) {
            if (view.isValidationRequired()) {
                required = true;
                break;
            }
        }
        return required;
    }

    @Override
    protected boolean handleIsViewHasNameOfUseCase() {
        boolean sameName = false;
        for (final FrontEndView view : this.getViews()) {
            sameName = view.isHasNameOfUseCase();
            if (sameName) {
                break;
            }
        }
        return sameName;
    }

    @Override
    protected boolean handleIsRegistrationUseCase() {
        return this.hasStereotype(MetafacadeWebProfile.STEREOTYPE_FRONT_END_REGISTRATION);
    }

    /**
     * The suffix for the forwards class name.
     */
    private static final String FORWARDS_CLASS_NAME_SUFFIX = "Forwards";

    @Override
    protected String handleGetForwardsClassName() {
        return StringUtilsHelper.upperCamelCaseName(this.getName()) + FORWARDS_CLASS_NAME_SUFFIX;
    }

    @Override
    protected List<FrontEndForward> handleGetForwards() {
        final Map<String, FrontEndForward> forwards = new LinkedHashMap<String, FrontEndForward>();
        for (final FrontEndAction action : this.getActions()) {
            for (final FrontEndForward forward : action.getActionForwards()) {
                forwards.put(forward.getName(), forward);
            }
        }
        return new ArrayList(forwards.values());
    }

    @Override
    protected Collection<FrontEndView> handleGetAllViews() {
        final Set<FrontEndView> allViews = new LinkedHashSet<FrontEndView>();
        for (final FrontEndUseCase useCase : this.getAllUseCases()) {
            allViews.addAll(useCase.getViews());
        }
        return allViews;
    }

    @Override
    protected List<FrontEndUseCase> handleGetRegistrationUseCases() {
        final List<FrontEndUseCase> useCases = new ArrayList<FrontEndUseCase>(this.getAllUseCases());
        for (final Iterator<FrontEndUseCase> iterator = useCases.iterator(); iterator.hasNext();) {
            final FrontEndUseCase useCase = iterator.next();
            if (useCase instanceof FrontEndUseCase) {
                if (!((FrontEndUseCase) useCase).isRegistrationUseCase()) {
                    iterator.remove();
                }
            } else {
                iterator.remove();
            }
        }
        return useCases;
    }

    private static final String EMPTY_STRING = "";
    private static final String DEFAULT = "default";
    private static final String SLASH = "/";
    private static final String QUOTE = "\"";

    @Override
    protected String handleGetPath() {
        String path = StringUtils.strip(((String) this.findTaggedValue(UMLProfile.TAGGEDVALUE_PRESENTATION_PATH)));
        if (StringUtils.isBlank(path)) {
            path = EMPTY_STRING;
        }

        if (StringUtils.isBlank(path) || path.equals(DEFAULT)) {
            path = StringUtils.replace(this.getPackageName(), ".", "/");
        } else {

            final FrontEndActivityGraph graph = this.getActivityGraph();
            if (graph != null) {
                final FrontEndAction action = (FrontEndAction) graph.getInitialAction();
                if (action != null) {
                    path = action.getPath();
                }
            }
        }

        if (!path.startsWith(SLASH)) {
            path = SLASH + path;
        }

        return path;
    }

    @Override
    protected String handleGetRestPath() {
        String path = StringUtils.strip(((String) this.findTaggedValue(UMLProfile.TAGGEDVALUE_PRESENTATION_REST_PATH)));
        if (StringUtils.isBlank(path)) {
            path = EMPTY_STRING;
        }

        if (StringUtils.isBlank(path) || path.equals(DEFAULT)) {
            path = MetafacadeWebUtils.toWebResourceName(this.getName());
        } else {
            if (!path.startsWith(QUOTE)) {
                path = path;
            }
            if (!path.endsWith(QUOTE) || path.length() < 2) {
                path = path;
            }

            if (path.endsWith(SLASH)) {
                path = path.substring(0, path.length() - 1);
            }
        }

        if (!path.startsWith(SLASH)) {
            path = SLASH + path;
        }

        return path;
    }

    @Override
    protected String handleGetFilename() {
        String path = StringUtils.strip(((String) this.findTaggedValue(UMLProfile.TAGGEDVALUE_PRESENTATION_FILENAME)));
        if (StringUtils.isBlank(path)) {
            path = EMPTY_STRING;
        }

        if (StringUtils.isBlank(path) || path.equals(DEFAULT)) {
            path = MetafacadeWebUtils.toWebResourceName(this.getName());
        } else {
            if (!path.startsWith(QUOTE)) {
                path = path;
            }
            if (!path.endsWith(QUOTE) || path.length() < 2) {
                path = path;
            }

            if (path.endsWith(SLASH)) {
                path = path.substring(0, path.length() - 1);
            }
        }

        return path;
    }

    @Override
    protected Collection handleGetAllowedRoles() {
        return UMLMetafacadeUtils.getAllowedRoles(this);
    }
}
