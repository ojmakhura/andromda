package org.andromda.metafacades.emf.uml22;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.andromda.metafacades.uml.EventFacade;
import org.andromda.metafacades.uml.FrontEndActionState;
import org.andromda.metafacades.uml.FrontEndActivityGraph;
import org.andromda.metafacades.uml.FrontEndAttribute;
import org.andromda.metafacades.uml.FrontEndController;
import org.andromda.metafacades.uml.FrontEndControllerOperation;
import org.andromda.metafacades.uml.FrontEndEvent;
import org.andromda.metafacades.uml.FrontEndFinalState;
import org.andromda.metafacades.uml.FrontEndForward;
import org.andromda.metafacades.uml.FrontEndParameter;
import org.andromda.metafacades.uml.FrontEndUseCase;
import org.andromda.metafacades.uml.FrontEndView;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.OperationFacade;
import org.andromda.metafacades.uml.ParameterFacade;
import org.andromda.metafacades.uml.PseudostateFacade;
import org.andromda.metafacades.uml.StateVertexFacade;
import org.andromda.metafacades.uml.TransitionFacade;
import org.andromda.metafacades.uml.UMLMetafacadeUtils;
import org.andromda.metafacades.uml.UMLProfile;
import org.andromda.metafacades.uml.UseCaseFacade;
import org.andromda.metafacades.uml.web.MetafacadeWebGlobals;
import org.andromda.metafacades.uml.web.MetafacadeWebProfile;
import org.andromda.metafacades.uml.web.MetafacadeWebUtils;
import org.andromda.utils.StringUtilsHelper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang3.StringUtils;

/**
 * MetafacadeLogic implementation for
 * org.andromda.metafacades.uml.FrontEndAction.
 *
 * @see org.andromda.metafacades.uml.FrontEndAction
 */
public class FrontEndActionLogicImpl
        extends FrontEndActionLogic {
    private static final long serialVersionUID = -501340062188534083L;

    /**
     * @param metaObject
     * @param context
     */
    public FrontEndActionLogicImpl(
            final Object metaObject,
            final String context) {
        super(metaObject, context);
    }

    /*
     * The logger instance.
     * private static final Logger LOGGER =
     * Logger.getLogger(FrontEndActionLogicImpl.class);
     */

    /**
     * @see org.andromda.metafacades.uml.FrontEndAction#getInput()
     */
    @Override
    protected Object handleGetInput() {
        Object input = null;
        final ModelElementFacade source = this.getSource();
        if (source instanceof PseudostateFacade) {
            final PseudostateFacade pseudostate = (PseudostateFacade) source;
            if (pseudostate.isInitialState()) {
                input = source;
            }
        } else {
            if (source instanceof FrontEndView) {
                input = source;
            }
        }
        return input;
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndAction#getParameters()
     */
    @Override
    protected List<ParameterFacade> handleGetParameters() {
        final EventFacade trigger = this.getTrigger();
        return trigger == null ? Collections.<ParameterFacade>emptyList()
                : new ArrayList<ParameterFacade>(trigger.getParameters());
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndAction#findParameter(String)
     */
    @Override
    protected ParameterFacade handleFindParameter(final String name) {
        return (ParameterFacade) CollectionUtils.find(
                this.getParameters(),
                new Predicate() {
                    public boolean evaluate(final Object object) {
                        final ParameterFacade parameter = (ParameterFacade) object;
                        return StringUtils.trimToEmpty(parameter.getName()).equals(name);
                    }
                });
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndAction#getDeferredOperations()
     */
    @Override
    protected List<OperationFacade> handleGetDeferredOperations() {
        final Collection<OperationFacade> deferredOperations = new LinkedHashSet<OperationFacade>();
        final FrontEndController controller = this.getController();
        if (controller != null) {
            final List<FrontEndActionState> actionStates = this.getActionStates();
            for (final FrontEndActionState actionState : actionStates) {
                deferredOperations.addAll(actionState.getControllerCalls());
            }

            final List<FrontEndForward> transitions = this.getDecisionTransitions();
            for (final FrontEndForward forward : transitions) {
                final FrontEndEvent trigger = forward.getDecisionTrigger();
                if (trigger != null) {
                    deferredOperations.add(trigger.getControllerCall());
                }
            }
        }
        return new ArrayList<OperationFacade>(deferredOperations);
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndAction#getDecisionTransitions()
     */
    @Override
    protected List<TransitionFacade> handleGetDecisionTransitions() {
        if (this.decisionTransitions == null) {
            this.initializeCollections();
        }
        return new ArrayList<TransitionFacade>(this.decisionTransitions);
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndAction#getTargetViews()
     */
    @Override
    protected List<StateVertexFacade> handleGetTargetViews() {
        final Collection<StateVertexFacade> targetViews = new LinkedHashSet<StateVertexFacade>();
        final Collection<FrontEndForward> forwards = this.getActionForwards();
        for (final FrontEndForward forward : forwards) {
            if (forward.isEnteringView()) {
                targetViews.add(forward.getTarget());
            }
        }
        return new ArrayList<StateVertexFacade>(targetViews);
    }

    /**
     * All action states that make up this action, this includes all possible
     * action states traversed after a decision point too.
     */
    private Collection<FrontEndActionState> actionStates = null;

    /**
     * All transitions leading into either a page or final state that originated
     * from a call to this action.
     */
    private Map<StateVertexFacade, TransitionFacade> actionForwards = null;

    /**
     * All transitions leading into a decision point that originated from a call
     * to this action.
     */
    private Collection<TransitionFacade> decisionTransitions = null;

    /**
     * All transitions that can be traversed when calling this action.
     */
    private Collection<TransitionFacade> transitions = null;

    /**
     * Initializes all action states, action forwards, decision transitions and
     * transitions in one shot, so that they can be queried more efficiently
     * later on.
     */
    private void initializeCollections() {
        this.actionStates = new LinkedHashSet<FrontEndActionState>();
        this.actionForwards = new LinkedHashMap<StateVertexFacade, TransitionFacade>();
        this.decisionTransitions = new LinkedHashSet<TransitionFacade>();
        this.transitions = new LinkedHashSet<TransitionFacade>();
        this.collectTransitions(
                (TransitionFacade) this.THIS(),
                this.transitions);
    }

    /**
     * Recursively collects all action states, action forwards, decision
     * transitions and transitions.
     *
     * @param transition
     *                             the current transition that is being processed
     * @param processedTransitions
     *                             the set of transitions already processed
     */
    private void collectTransitions(
            final TransitionFacade transition,
            final Collection<TransitionFacade> processedTransitions) {
        if (processedTransitions.contains(transition)) {
            return;
        }
        processedTransitions.add(transition);
        final StateVertexFacade target = transition.getTarget();
        if (target instanceof FrontEndView || target instanceof FrontEndFinalState) {
            if (!this.actionForwards.containsKey(transition.getTarget())) {
                this.actionForwards.put(
                        transition.getTarget(),
                        transition);
            }
        } else if (target instanceof PseudostateFacade && ((PseudostateFacade) target).isDecisionPoint()) {
            this.decisionTransitions.add(transition);
            final Collection<TransitionFacade> outcomes = target.getOutgoings();
            for (final TransitionFacade outcome : outcomes) {
                this.collectTransitions(
                        outcome,
                        processedTransitions);
            }
        } else if (target instanceof FrontEndActionState) {
            this.actionStates.add((FrontEndActionState) target);
            final FrontEndForward forward = ((FrontEndActionState) target).getForward();
            if (forward != null) {
                this.collectTransitions(
                        forward,
                        processedTransitions);
            }
        } else// all the rest is ignored but outgoing transitions are further
        // processed
        {
            final Collection<TransitionFacade> outcomes = target.getOutgoings();
            for (final TransitionFacade outcome : outcomes) {
                this.collectTransitions(
                        outcome,
                        processedTransitions);
            }
        }
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndAction#getActionStates()
     */
    @Override
    protected List<FrontEndActionState> handleGetActionStates() {
        if (this.actionStates == null) {
            this.initializeCollections();
        }
        return new ArrayList<FrontEndActionState>(this.actionStates);
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndAction#getTransitions()
     */
    @Override
    protected List<TransitionFacade> handleGetTransitions() {
        if (this.transitions == null) {
            this.initializeCollections();
        }
        return new ArrayList<TransitionFacade>(this.transitions);
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndAction#getActionForwards()
     */
    @Override
    protected List<TransitionFacade> handleGetActionForwards() {
        if (this.actionForwards == null) {
            this.initializeCollections();
        }
        return new ArrayList<TransitionFacade>(this.actionForwards.values());
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndAction#getController()
     */
    @Override
    protected Object handleGetController() {
        final FrontEndActivityGraph graph = this.getFrontEndActivityGraph();
        return graph == null ? null : graph.getController();
    }

    /**
     * Overridden because actions (transitions) are not directly contained in a
     * UML namespace.
     *
     * @see org.andromda.metafacades.uml.ModelElementFacade#getPackageName()
     */
    @Override
    public String handleGetPackageName() {
        String packageName = null;

        final UseCaseFacade useCase = this.getUseCase();
        if (useCase != null) {
            packageName = useCase.getPackageName();
        }
        return packageName;
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndAction#isUseCaseStart()
     */
    @Override
    protected boolean handleIsUseCaseStart() {
        final StateVertexFacade source = this.getSource();
        return source instanceof PseudostateFacade && ((PseudostateFacade) source).isInitialState();
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndAction#getFormFields()
     */
    @Override
    protected List<ParameterFacade> handleGetFormFields() {
        final Map<String, ParameterFacade> formFieldMap = new LinkedHashMap<String, ParameterFacade>();

        // - For an action that starts the use case, we need to detect all
        // usecases forwarding to the one
        // belonging to this action if there are any parameters in those
        // transitions we need to have
        // them included in this action's form
        if (this.isUseCaseStart()) {
            final FrontEndUseCase useCase = this.getUseCase();
            if (useCase != null) {
                final Collection finalStates = useCase.getReferencingFinalStates();
                for (final Iterator finalStateIterator = finalStates.iterator(); finalStateIterator.hasNext();) {
                    final Object finalStateObject = finalStateIterator.next();

                    // we need to test for the type because a non
                    // struts-use-case final state might accidentally
                    // link to this use-case (for example: the user
                    // temporarily wants to disable code generation
                    // for a specific use-case and is not removing the
                    // final-state to use-case link(s))
                    if (finalStateObject instanceof FrontEndFinalState) {
                        final FrontEndFinalState finalState = (FrontEndFinalState) finalStateObject;
                        final Collection<FrontEndParameter> parameters = finalState.getInterUseCaseParameters();
                        for (final FrontEndParameter parameter : parameters) {
                            formFieldMap.put(
                                    parameter.getName(),
                                    parameter);
                        }
                    }
                }
            }
        }

        // if any action encountered by the execution of the complete
        // action-graph path emits a forward
        // containing one or more parameters they need to be included as a form
        // field too
        final Collection<FrontEndActionState> actionStates = this.getActionStates();
        for (final FrontEndActionState actionState : actionStates) {
            final FrontEndForward forward = actionState.getForward();
            if (forward != null) {
                final Collection<FrontEndParameter> forwardParameters = forward.getForwardParameters();
                for (final FrontEndParameter forwardParameter : forwardParameters) {
                    formFieldMap.put(
                            forwardParameter.getName(),
                            forwardParameter);
                }
            }
        }

        // add page variables for all pages/final-states targeted
        // also add the fields of the target page's actions (for pre-loading)
        final Collection<FrontEndForward> forwards = this.getActionForwards();
        for (final FrontEndForward forward : forwards) {
            final StateVertexFacade target = forward.getTarget();
            if (target instanceof FrontEndView) {
                final FrontEndView view = (FrontEndView) target;
                final Collection<FrontEndParameter> viewVariables = view.getVariables();
                for (final FrontEndParameter facade : viewVariables) {
                    formFieldMap.put(
                            facade.getName(),
                            facade);
                }
                final Collection<FrontEndParameter> allActionParameters = view.getAllFormFields();
                for (final Iterator<FrontEndParameter> actionParameterIterator = allActionParameters
                        .iterator(); actionParameterIterator.hasNext();) {
                    // - don't allow existing parameters that are tables to be
                    // overwritten (since they take precedence
                    final Object parameter = actionParameterIterator.next();
                    if (parameter instanceof FrontEndParameter) {
                        FrontEndParameter variable = (FrontEndParameter) parameter;
                        final String name = variable.getName();
                        final Object existingParameter = formFieldMap.get(name);
                        if (existingParameter instanceof FrontEndParameter) {
                            final FrontEndParameter existingVariable = (FrontEndParameter) existingParameter;
                            if (existingVariable.isTable()) {
                                variable = existingVariable;
                            }
                        }
                        formFieldMap.put(
                                name,
                                variable);
                    }
                }
            } else if (target instanceof FrontEndFinalState) {
                // only add these if there is no parameter recorded yet with the
                // same name
                final Collection<FrontEndParameter> forwardParameters = forward.getForwardParameters();
                for (final FrontEndParameter facade : forwardParameters) {
                    if (!formFieldMap.containsKey(facade.getName())) {
                        formFieldMap.put(
                                facade.getName(),
                                facade);
                    }
                }
            }
        }

        // we do the action parameters in the end because they are allowed to
        // overwrite existing properties
        final Collection<FrontEndParameter> actionParameters = this.getParameters();
        for (final Iterator<FrontEndParameter> parameterIterator = actionParameters.iterator(); parameterIterator
                .hasNext();) {
            final Object parameter = parameterIterator.next();
            if (parameter instanceof FrontEndParameter) {
                final FrontEndParameter variable = (FrontEndParameter) parameter;
                formFieldMap.put(
                        variable.getName(),
                        variable);
            }
        }

        // - if we don't have any fields defined on this action and there are no
        // action forwards,
        // take the parameters from the deferred operations (since we would want
        // to stay on the same view)
        if (formFieldMap.isEmpty() && this.getActionForwards().isEmpty()) {
            for (final FrontEndControllerOperation operation : this.getDeferredOperations()) {
                if (operation != null) {
                    for (ParameterFacade parameter : operation.getArguments()) {
                        formFieldMap.put(
                                parameter.getName(),
                                parameter);
                    }
                }
            }
        }
        return new ArrayList<ParameterFacade>(formFieldMap.values());
    }

    // @Override
    // protected String handleGetRestPath() {
    //     String path = (String)this.findTaggedValue(JakartaGlobals.WEBSERVICE_PATH);
        
    //     if(path != null) {

    //         if(path.trim().length() > 0 && !path.startsWith("/")) {
    //             path = "/" + path;
    //         }

    //         return path;
    //     }

    //     return "/" + Metafacade.toWebResourceName(this.getTriggerName());
    // }
    @Override
    protected boolean handleIsFinalStateTarget() {
        return this.getTarget() instanceof FrontEndFinalState;
    }

    @Override
    protected String handleGetMessageKey() {
        String messageKey = null;

        final Object trigger = this.getTrigger();
        if (trigger instanceof FrontEndEvent) {
            final FrontEndEvent actionTrigger = (FrontEndEvent) trigger;
            messageKey = actionTrigger.getMessageKey();
        }
        return messageKey;
    }

    @Override
    protected boolean handleIsSuccessMessagesPresent() {
        return !this.getSuccessMessages().isEmpty();
    }

    @Override
    protected String handleGetTriggerName() {
        String name = null;
        if (this.isExitingInitialState()) {
            final FrontEndUseCase useCase = this.getUseCase();
            if (useCase != null) {
                name = useCase.getName();
            }
        } else {
            final EventFacade trigger = this.getTrigger();
            final String suffix = trigger == null ? this.getTarget().getName() : trigger.getName();
            name = this.getSource().getName() + ' ' + suffix;
        }
        return StringUtilsHelper.lowerCamelCaseName(name);
    }

    @Override
    protected boolean handleIsResettable() {
        final Object value = findTaggedValue(MetafacadeWebProfile.TAGGEDVALUE_ACTION_RESETTABLE);
        return this.isTrue(value == null ? null : value.toString());
    }

    /**
     * Convenient method to detect whether or not a String instance represents a
     * boolean <code>true</code> value.
     */
    private boolean isTrue(String string) {
        return "yes".equalsIgnoreCase(string) || "true".equalsIgnoreCase(string) || "on".equalsIgnoreCase(string) ||
                "1".equalsIgnoreCase(string);
    }

    @Override
    protected boolean handleIsTableLink() {
        return this.getTableLinkParameter() != null;
    }

    @Override
    protected String handleGetViewFragmentPath() {
        return '/' + this.getPackageName().replace(
                '.',
                '/') + '/' + MetafacadeWebUtils.toWebResourceName(this.getTriggerName());
    }

    /**
     * Collects specific messages in a map.
     *
     * @param taggedValue the tagged value from which to read the message
     * @return maps message keys to message values, but only those that match the
     *         arguments
     *         will have been recorded
     */
    private Map<String, String> getMessages(String taggedValue) {
        Map<String, String> messages;

        final Collection taggedValues = this.findTaggedValues(taggedValue);
        if (taggedValues.isEmpty()) {
            messages = Collections.EMPTY_MAP;
        } else {
            messages = new LinkedHashMap<String, String>(); // we want to keep the order
            for (final Object tag : taggedValues) {
                final String value = (String) tag;
                messages.put(StringUtilsHelper.toResourceMessageKey(value), value);
            }
        }

        return messages;
    }

    @Override
    protected Map handleGetSuccessMessages() {
        return this
                .getMessages(org.andromda.metafacades.uml.web.MetafacadeWebProfile.TAGGEDVALUE_ACTION_SUCCESS_MESSAGE);
    }

    @Override
    protected List<FrontEndParameter> handleGetHiddenParameters() {
        final List<FrontEndParameter> hiddenParameters = new ArrayList<FrontEndParameter>(this.getParameters());
        CollectionUtils.filter(
                hiddenParameters,
                new Predicate() {
                    public boolean evaluate(final Object object) {
                        boolean valid = false;
                        if (object instanceof FrontEndParameter) {
                            final FrontEndParameter parameter = (FrontEndParameter) object;
                            valid = parameter.isInputHidden();
                            if (!valid) {
                                for (final Iterator iterator = parameter.getAttributes().iterator(); iterator
                                        .hasNext();) {
                                    FrontEndAttribute attribute = (FrontEndAttribute) iterator.next();
                                    valid = attribute.isInputHidden();
                                    if (valid) {
                                        break;
                                    }
                                }
                            }
                        }
                        return valid;
                    }
                });
        return hiddenParameters;
    }

    @Override
    protected boolean handleIsHyperlink() {
        final Object value = findTaggedValue(MetafacadeWebProfile.TAGGEDVALUE_ACTION_TYPE);
        return MetafacadeWebGlobals.ACTION_TYPE_HYPERLINK.equalsIgnoreCase(value == null ? null : value.toString());
    }

    @Override
    protected boolean handleIsTableAction() {
        return MetafacadeWebGlobals.ACTION_TYPE_TABLE
                .equals(this.findTaggedValue(MetafacadeWebProfile.TAGGEDVALUE_ACTION_TYPE));
    }

    @Override
    protected String handleGetFullyQualifiedActionClassName() {
        final StringBuilder path = new StringBuilder();
        final FrontEndUseCase useCase = this.getUseCase();
        if (useCase != null) {
            final String packageName = useCase.getPackageName();
            if (StringUtils.isNotBlank(packageName)) {
                path.append(packageName);
                path.append('.');
            }
        }
        path.append(this.getActionClassName());
        return path.toString();
    }

    @Override
    protected String handleGetTriggerMethodName() {
        final StringBuilder methodName = new StringBuilder();
        if (this.isExitingInitialState()) {
            final FrontEndUseCase useCase = this.getUseCase();
            methodName.append(StringUtilsHelper.lowerCamelCaseName(useCase.getName()) + "_started");
        } else {
            methodName.append(StringUtilsHelper.lowerCamelCaseName(this.getSource().getName()));
            methodName.append('_');
            final EventFacade trigger = this.getTrigger();
            final String suffix = trigger == null ? this.getTarget().getName() : trigger.getName();
            methodName.append(StringUtilsHelper.lowerCamelCaseName(suffix));
        }
        return "_" + methodName.toString();
    }

    @Override
    protected String handleGetPath() {
        String path = this.getPathRoot() + '/' + MetafacadeWebUtils.toWebResourceName(this.getTriggerName());
        if (this.isExitingInitialState()) {
            final FrontEndUseCase useCase = this.getUseCase();
            if (useCase != null && useCase.isViewHasNameOfUseCase()) {
                // - add the uc prefix to make the trigger name unique
                // when a view contained within the use case has the same name
                // as the use case
                path = path + "uc";
            }
        }
        return path;
    }

    @Override
    protected boolean handleIsNeedsFileUpload() {
        if (this.getParameters().size() == 0) {
            return false;
        }

        for (final FrontEndParameter parameter : this.getParameters()) {
            if (parameter.isInputFile()) {
                return true;
            }
            if (parameter.isComplex()) {
                for (final Iterator attributes = parameter.getAttributes().iterator(); attributes.hasNext();)
                    if (((FrontEndAttribute) attributes.next()).isInputFile())
                        return true;
            }
        }
        return false;
    }

    @Override
    protected FrontEndParameter handleGetTableLinkParameter() {
        FrontEndParameter tableLinkParameter = null;
        final String tableLinkName = this.getTableLinkName();
        if (tableLinkName != null) {
            final FrontEndView view = (FrontEndView) this.getInput();
            if (view != null) {
                final List<FrontEndParameter> tables = view.getTables();
                for (int ctr = 0; ctr < tables.size() && tableLinkParameter == null; ctr++) {
                    final FrontEndParameter table = tables.get(ctr);
                    if (tableLinkName.equals(table.getName())) {
                        tableLinkParameter = table;
                    }
                }
            }
        }
        return tableLinkParameter;
    }

    @Override
    protected boolean handleIsFormReset() {
        boolean resetRequired = this.isFormReset();
        if (!resetRequired) {
            for (final FrontEndParameter parameter : this.getParameters()) {
                resetRequired = parameter.isReset();
                if (resetRequired) {
                    break;
                }
            }
        }
        return resetRequired;
    }

    @Override
    protected Map handleGetWarningMessages() {
        return this.getMessages(MetafacadeWebProfile.TAGGEDVALUE_ACTION_WARNING_MESSAGE);
    }

    @Override
    protected String handleGetFormKey() {
        final Object formKeyValue = this.findTaggedValue(MetafacadeWebProfile.TAGGEDVALUE_ACTION_FORM_KEY);
        return formKeyValue == null ? Objects.toString(this.getConfiguredProperty(MetafacadeWebGlobals.ACTION_FORM_KEY))
                : String.valueOf(formKeyValue);
    }

    @Override
    protected String handleGetFromOutcome() {
        return this.getName();
    }

    @Override
    protected boolean handleIsWarningMessagesPresent() {
        return !this.getWarningMessages().isEmpty();
    }

    @Override
    protected String handleGetPathRoot() {
        final StringBuilder pathRoot = new StringBuilder();
        final FrontEndUseCase useCase = this.getUseCase();
        if (useCase != null) {
            pathRoot.append(useCase.getPathRoot());
        }
        return pathRoot.toString();
    }

    @Override
    protected String handleGetTableLinkColumnName() {
        String tableLink = null;
        final Object value = findTaggedValue(MetafacadeWebProfile.TAGGEDVALUE_ACTION_TABLELINK);
        if (value != null) {
            tableLink = StringUtils.trimToNull(value.toString());

            if (tableLink != null) {
                final int columnOffset = tableLink.indexOf('.');
                tableLink = (columnOffset == -1 || columnOffset == tableLink.length() - 1)
                        ? null
                        : tableLink.substring(columnOffset + 1);
            }
        }
        return tableLink;
    }

    @Override
    protected boolean handleIsPopup() {
        boolean popup = Objects.toString(this.findTaggedValue(MetafacadeWebProfile.TAGGEDVALUE_ACTION_TYPE))
                .equalsIgnoreCase(
                        MetafacadeWebGlobals.ACTION_TYPE_POPUP);
        return popup;
    }

    @Override
    protected boolean handleIsFormResetRequired() {
        boolean resetRequired = this.isFormReset();
        if (!resetRequired) {
            for (final FrontEndParameter parameter : this.getParameters()) {
                resetRequired = parameter.isReset();
                if (resetRequired) {
                    break;
                }
            }
        }
        return resetRequired;
    }

    @Override
    protected String handleGetTableLinkName() {
        String tableLink = null;

        final Object value = findTaggedValue(MetafacadeWebProfile.TAGGEDVALUE_ACTION_TABLELINK);
        if (value != null)
        {
            tableLink = StringUtils.trimToNull(value.toString());

            if (tableLink != null)
            {
                final int columnOffset = tableLink.indexOf('.');
                tableLink = columnOffset == -1 ? tableLink : tableLink.substring(
                        0,
                        columnOffset);
            }
        }

        return tableLink;
    }

    @Override
    protected boolean handleIsDialog() {
        return this.isPopup();
    }

    @Override
    protected boolean handleIsValidationRequired() {
        boolean required = false;
        for (final FrontEndParameter parameter : this.getParameters())
        {
                if (parameter.isValidationRequired())
                {
                    required = true;
                    break;
                }
        }
        return required;
    }

    @Override
    protected String handleGetFullyQualifiedActionClassPath() {
        return this.getFullyQualifiedActionClassName().replace(
            '.',
            '/');
    }

    @Override
    protected String handleGetControllerAction() {
        return this.getTriggerName();
    }

    @Override
    protected String handleGetActionClassName() {
        return StringUtilsHelper.upperCamelCaseName(this.getTriggerName());
    }

    @Override
    protected String handleGetDocumentationValue() {
        final String value = StringUtilsHelper.toResourceMessage(getDocumentation(
                    "",
                    64,
                    false));
        return value == null ? "" : value;
    }

    @Override
    protected String handleGetDocumentationKey() {
        final Object trigger = this.getTrigger();
        FrontEndEvent event = null;
        if (trigger instanceof FrontEndEvent)
        {
            event = (FrontEndEvent)trigger;
        }
        return (event == null ? this.getMessageKey() + ".is.an.action.without.trigger" : event.getMessageKey()) +
            '.' + MetafacadeWebGlobals.DOCUMENTATION_MESSAGE_KEY_SUFFIX;
    }

    @Override
    protected String handleGetFormImplementationName() {
        final String pattern =
            Objects.toString(this.getConfiguredProperty(MetafacadeWebGlobals.FORM_IMPLEMENTATION_PATTERN));
        return pattern.replaceFirst(
            "\\{0\\}",
            StringUtils.capitalize(this.getTriggerName()));
    }

    @Override
    protected String handleGetFormBeanName() {
        return this.getFormBeanName(true);
    }

    /**
     * Constructs the form bean name, with our without prefixing the use case name.
     *
     * @param withUseCaseName whether or not to prefix the use case name.
     * @return the constructed form bean name.
     */
    private String getFormBeanName(boolean withUseCaseName)
    {
        final String pattern = Objects.toString(this.getConfiguredProperty(MetafacadeWebGlobals.FORM_BEAN_PATTERN));
        final ModelElementFacade useCase = this.getUseCase();
        final String useCaseName = withUseCaseName && useCase != null
            ? StringUtilsHelper.lowerCamelCaseName(useCase.getName()) : "";
        final String formBeanName = pattern.replaceFirst("\\{0\\}", useCaseName);
        final String triggerName = !pattern.equals(formBeanName)
            ? StringUtils.capitalize(this.getTriggerName()) : this.getTriggerName();
        return formBeanName.replaceFirst(
            "\\{1\\}",
            triggerName);
    }

    @Override
    protected String handleGetFullyQualifiedFormImplementationName() {
        final StringBuilder fullyQualifiedName = new StringBuilder();
        final String packageName = this.getPackageName();
        if (StringUtils.isNotBlank(packageName))
        {
            fullyQualifiedName.append(packageName + '.');
        }
        return fullyQualifiedName.append(this.getFormImplementationName()).toString();
    }

    @Override
    protected String handleGetFullyQualifiedFormImplementationPath() {
        return this.getFullyQualifiedFormImplementationName().replace(
            '.',
            '/');
    }

    @Override
    protected String handleGetFormScope() {
        String scope = Objects.toString(this.findTaggedValue(MetafacadeWebProfile.TAGGEDVALUE_ACTION_FORM_SCOPE));
        if (StringUtils.isEmpty(scope))
        {
            scope = Objects.toString(this.getConfiguredProperty(MetafacadeWebGlobals.FORM_SCOPE));
        }
        return scope;
    }

    private static final String EMPTY_STRING = "";
    private static final String DEFAULT = "default";
    private static final String SLASH = "/";
    private static final String QUOTE = "\"";

    @Override
    protected String handleGetRestPath() {
                
        return UMLMetafacadeUtils.getRestPath(this, this.getTriggerName());
    }

    @Override
    protected Collection handleGetAllowedRoles() {
        return UMLMetafacadeUtils.getAllowedRoles(this);
    }
}
