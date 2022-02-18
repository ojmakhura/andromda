package org.andromda.metafacades.emf.uml22;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import org.andromda.metafacades.uml.EventFacade;
import org.andromda.metafacades.uml.FrontEndActionState;
import org.andromda.metafacades.uml.FrontEndActivityGraph;
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
import org.andromda.metafacades.uml.UseCaseFacade;
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
    extends FrontEndActionLogic
{
    private static final long serialVersionUID = -501340062188534083L;

    /**
     * @param metaObject
     * @param context
     */
    public FrontEndActionLogicImpl(
        final Object metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /*
     * The logger instance.
    private static final Logger LOGGER = Logger.getLogger(FrontEndActionLogicImpl.class);
     */

    /**
     * @see org.andromda.metafacades.uml.FrontEndAction#getInput()
     */
    @Override
    protected Object handleGetInput()
    {
        Object input = null;
        final ModelElementFacade source = this.getSource();
        if (source instanceof PseudostateFacade)
        {
            final PseudostateFacade pseudostate = (PseudostateFacade)source;
            if (pseudostate.isInitialState())
            {
                input = source;
            }
        }
        else
        {
            if (source instanceof FrontEndView)
            {
                input = source;
            }
        }
        return input;
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndAction#getParameters()
     */
    @Override
    protected List<ParameterFacade> handleGetParameters()
    {
        final EventFacade trigger = this.getTrigger();
        return trigger == null ? Collections.<ParameterFacade>emptyList() : new ArrayList<ParameterFacade>(trigger.getParameters());
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndAction#findParameter(String)
     */
    @Override
    protected ParameterFacade handleFindParameter(final String name)
    {
        return (ParameterFacade)CollectionUtils.find(
            this.getParameters(),
            new Predicate()
            {
                public boolean evaluate(final Object object)
                {
                    final ParameterFacade parameter = (ParameterFacade)object;
                    return StringUtils.trimToEmpty(parameter.getName()).equals(name);
                }
            });
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndAction#getDeferredOperations()
     */
    @Override
    protected List<OperationFacade> handleGetDeferredOperations()
    {
        final Collection<OperationFacade> deferredOperations = new LinkedHashSet<OperationFacade>();
        final FrontEndController controller = this.getController();
        if (controller != null)
        {
            final List<FrontEndActionState> actionStates = this.getActionStates();
            for (final FrontEndActionState actionState : actionStates)
            {
                deferredOperations.addAll(actionState.getControllerCalls());
            }

            final List<FrontEndForward> transitions = this.getDecisionTransitions();
            for (final FrontEndForward forward : transitions)
            {
                final FrontEndEvent trigger = forward.getDecisionTrigger();
                if (trigger != null)
                {
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
    protected List<TransitionFacade> handleGetDecisionTransitions()
    {
        if (this.decisionTransitions == null)
        {
            this.initializeCollections();
        }
        return new ArrayList<TransitionFacade>(this.decisionTransitions);
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndAction#getTargetViews()
     */
    @Override
    protected List<StateVertexFacade> handleGetTargetViews()
    {
        final Collection<StateVertexFacade> targetViews = new LinkedHashSet<StateVertexFacade>();
        final Collection<FrontEndForward> forwards = this.getActionForwards();
        for (final FrontEndForward forward : forwards)
        {
            if (forward.isEnteringView())
            {
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
    private void initializeCollections()
    {
        this.actionStates = new LinkedHashSet<FrontEndActionState>();
        this.actionForwards = new LinkedHashMap<StateVertexFacade, TransitionFacade>();
        this.decisionTransitions = new LinkedHashSet<TransitionFacade>();
        this.transitions = new LinkedHashSet<TransitionFacade>();
        this.collectTransitions(
            (TransitionFacade)this.THIS(),
            this.transitions);
    }

    /**
     * Recursively collects all action states, action forwards, decision
     * transitions and transitions.
     *
     * @param transition
     *            the current transition that is being processed
     * @param processedTransitions
     *            the set of transitions already processed
     */
    private void collectTransitions(
        final TransitionFacade transition,
        final Collection<TransitionFacade> processedTransitions)
    {
        if (processedTransitions.contains(transition))
        {
            return;
        }
        processedTransitions.add(transition);
        final StateVertexFacade target = transition.getTarget();
        if (target instanceof FrontEndView || target instanceof FrontEndFinalState)
        {
            if (!this.actionForwards.containsKey(transition.getTarget()))
            {
                this.actionForwards.put(
                    transition.getTarget(),
                    transition);
            }
        }
        else if (target instanceof PseudostateFacade && ((PseudostateFacade)target).isDecisionPoint())
        {
            this.decisionTransitions.add(transition);
            final Collection<TransitionFacade> outcomes = target.getOutgoings();
            for (final TransitionFacade outcome : outcomes)
            {
                this.collectTransitions(
                        outcome,
                        processedTransitions);
            }
        }
        else if (target instanceof FrontEndActionState)
        {
            this.actionStates.add((FrontEndActionState)target);
            final FrontEndForward forward = ((FrontEndActionState)target).getForward();
            if (forward != null)
            {
                this.collectTransitions(
                    forward,
                    processedTransitions);
            }
        }
        else// all the rest is ignored but outgoing transitions are further
        // processed
        {
            final Collection<TransitionFacade> outcomes = target.getOutgoings();
            for (final TransitionFacade outcome : outcomes)
            {
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
    protected List<FrontEndActionState> handleGetActionStates()
    {
        if (this.actionStates == null)
        {
            this.initializeCollections();
        }
        return new ArrayList<FrontEndActionState>(this.actionStates);
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndAction#getTransitions()
     */
    @Override
    protected List<TransitionFacade> handleGetTransitions()
    {
        if (this.transitions == null)
        {
            this.initializeCollections();
        }
        return new ArrayList<TransitionFacade>(this.transitions);
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndAction#getActionForwards()
     */
    @Override
    protected List<TransitionFacade> handleGetActionForwards()
    {
        if (this.actionForwards == null)
        {
            this.initializeCollections();
        }
        return new ArrayList<TransitionFacade>(this.actionForwards.values());
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndAction#getController()
     */
    @Override
    protected Object handleGetController()
    {
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
    public String handleGetPackageName()
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
     * @see org.andromda.metafacades.uml.FrontEndAction#isUseCaseStart()
     */
    @Override
    protected boolean handleIsUseCaseStart()
    {
        final StateVertexFacade source = this.getSource();
        return source instanceof PseudostateFacade && ((PseudostateFacade)source).isInitialState();
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndAction#getFormFields()
     */
    @Override
    protected List<ParameterFacade> handleGetFormFields()
    {
        final Map<String, ParameterFacade> formFieldMap = new LinkedHashMap<String, ParameterFacade>();

        // - For an action that starts the use case, we need to detect all
        // usecases forwarding to the one
        // belonging to this action if there are any parameters in those
        // transitions we need to have
        // them included in this action's form
        if (this.isUseCaseStart())
        {
            final FrontEndUseCase useCase = this.getUseCase();
            if (useCase != null)
            {
                final Collection finalStates = useCase.getReferencingFinalStates();
                for (final Iterator finalStateIterator = finalStates.iterator(); finalStateIterator.hasNext();)
                {
                    final Object finalStateObject = finalStateIterator.next();

                    // we need to test for the type because a non
                    // struts-use-case final state might accidentally
                    // link to this use-case (for example: the user
                    // temporarily wants to disable code generation
                    // for a specific use-case and is not removing the
                    // final-state to use-case link(s))
                    if (finalStateObject instanceof FrontEndFinalState)
                    {
                        final FrontEndFinalState finalState = (FrontEndFinalState)finalStateObject;
                        final Collection<FrontEndParameter> parameters = finalState.getInterUseCaseParameters();
                        for (final FrontEndParameter parameter : parameters)
                        {
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
        for (final FrontEndActionState actionState : actionStates)
        {
            final FrontEndForward forward = actionState.getForward();
            if (forward != null)
            {
                final Collection<FrontEndParameter> forwardParameters = forward.getForwardParameters();
                for (final FrontEndParameter forwardParameter : forwardParameters)
                {
                    formFieldMap.put(
                            forwardParameter.getName(),
                            forwardParameter);
                }
            }
        }

        // add page variables for all pages/final-states targeted
        // also add the fields of the target page's actions (for pre-loading)
        final Collection<FrontEndForward> forwards = this.getActionForwards();
        for (final FrontEndForward forward : forwards)
        {
            final StateVertexFacade target = forward.getTarget();
            if (target instanceof FrontEndView)
            {
                final FrontEndView view = (FrontEndView) target;
                final Collection<FrontEndParameter> viewVariables = view.getVariables();
                for (final FrontEndParameter facade : viewVariables)
                {
                    formFieldMap.put(
                            facade.getName(),
                            facade);
                }
                final Collection<FrontEndParameter> allActionParameters = view.getAllFormFields();
                for (final Iterator<FrontEndParameter> actionParameterIterator = allActionParameters.iterator();
                     actionParameterIterator.hasNext();)
                {
                    // - don't allow existing parameters that are tables to be
                    // overwritten (since they take precedence
                    final Object parameter = actionParameterIterator.next();
                    if (parameter instanceof FrontEndParameter)
                    {
                        FrontEndParameter variable = (FrontEndParameter) parameter;
                        final String name = variable.getName();
                        final Object existingParameter = formFieldMap.get(name);
                        if (existingParameter instanceof FrontEndParameter)
                        {
                            final FrontEndParameter existingVariable = (FrontEndParameter) existingParameter;
                            if (existingVariable.isTable())
                            {
                                variable = existingVariable;
                            }
                        }
                        formFieldMap.put(
                                name,
                                variable);
                    }
                }
            }
            else if (target instanceof FrontEndFinalState)
            {
                // only add these if there is no parameter recorded yet with the
                // same name
                final Collection<FrontEndParameter> forwardParameters = forward.getForwardParameters();
                for (final FrontEndParameter facade : forwardParameters)
                {
                    if (!formFieldMap.containsKey(facade.getName()))
                    {
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
        for (final Iterator<FrontEndParameter> parameterIterator = actionParameters.iterator(); parameterIterator.hasNext();)
        {
            final Object parameter = parameterIterator.next();
            if (parameter instanceof FrontEndParameter)
            {
                final FrontEndParameter variable = (FrontEndParameter)parameter;
                formFieldMap.put(
                    variable.getName(),
                    variable);
            }
        }

        // - if we don't have any fields defined on this action and there are no
        // action forwards,
        // take the parameters from the deferred operations (since we would want
        // to stay on the same view)
        if (formFieldMap.isEmpty() && this.getActionForwards().isEmpty())
        {
            for (final FrontEndControllerOperation operation : this.getDeferredOperations())
            {
                if (operation != null)
                {
                    for (ParameterFacade parameter : operation.getArguments())
                    {
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
}
