package org.andromda.metafacades.emf.uml22;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.andromda.core.metafacade.MetafacadeBase;
import org.andromda.metafacades.uml.EventFacade;
import org.andromda.metafacades.uml.FrontEndAction;
import org.andromda.metafacades.uml.FrontEndActionState;
import org.andromda.metafacades.uml.FrontEndActivityGraph;
import org.andromda.metafacades.uml.FrontEndControllerOperation;
import org.andromda.metafacades.uml.FrontEndEvent;
import org.andromda.metafacades.uml.FrontEndFinalState;
import org.andromda.metafacades.uml.FrontEndForward;
import org.andromda.metafacades.uml.FrontEndUseCase;
import org.andromda.metafacades.uml.FrontEndView;
import org.andromda.metafacades.uml.ParameterFacade;
import org.andromda.metafacades.uml.PseudostateFacade;
import org.andromda.metafacades.uml.StateVertexFacade;
import org.andromda.metafacades.uml.TransitionFacade;
import org.andromda.metafacades.uml.UseCaseFacade;
import org.andromda.metafacades.uml.web.MetafacadeWebProfile;
import org.andromda.utils.StringUtilsHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * MetafacadeLogic implementation for
 * org.andromda.metafacades.uml.FrontEndForward.
 *
 * @see org.andromda.metafacades.uml.FrontEndForward
 */
public class FrontEndForwardLogicImpl
    extends FrontEndForwardLogic
{
    private static final long serialVersionUID = 34L;

    /**
     * The logger instance.
     */
    private static final Logger LOGGER = Logger.getLogger(FrontEndForwardLogicImpl.class);

    /**
     * @param metaObject
     * @param context
     */
    public FrontEndForwardLogicImpl(
        final Object metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndForward#isContainedInFrontEndUseCase()
     */
    @Override
    protected boolean handleIsContainedInFrontEndUseCase()
    {
        return this.getFrontEndActivityGraph() != null;
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndForward#getFrontEndActivityGraph()
     */
    @Override
    protected FrontEndActivityGraph handleGetFrontEndActivityGraph()
    {
        final Object graph = this.getSource().getStateMachine();
        return (FrontEndActivityGraph)(graph instanceof FrontEndActivityGraph ? graph : null);
    }

    /**
     * If this forward has a trigger this method returns that trigger's name,
     * otherwise if this forward has a name this method returns that name,
     * otherwise if this forward's target has a name this method returns that
     * name, otherwise simply returns <code>"unknown"</code>
     *
     * @see org.andromda.metafacades.emf.uml22.ModelElementFacadeLogic#handleGetName()
     */
    @Override
    protected final String handleGetName()
    {
        String forwardName = null;

        // - trigger
        final EventFacade trigger = this.getTrigger();
        if (trigger != null)
        {
            forwardName = trigger.getName();
        }

        // - name
        if (StringUtils.isBlank(forwardName))
        {
            forwardName = super.handleGetName();
        }

        // - target
        if (StringUtils.isBlank(forwardName))
        {
            forwardName = this.getTarget().getName();
        }

        // - else
        if (StringUtils.isBlank(forwardName))
        {
            forwardName = "unknown";
        }
        return forwardName;
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndForward#getActionMethodName()
     */
    @Override
    protected String handleGetActionMethodName()
    {
        return StringUtilsHelper.lowerCamelCaseName(this.handleGetName());
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndForward#isEnteringView()
     */
    @Override
    protected boolean handleIsEnteringView()
    {
        return this.getTarget() instanceof FrontEndView;
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndForward#isExitingView()
     */
    @Override
    protected boolean handleIsExitingView()
    {
        return this.getSource() instanceof FrontEndView;
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndForward#getUseCase()
     */
    @Override
    protected FrontEndUseCase handleGetUseCase()
    {
        FrontEndUseCase useCase = null;
        final FrontEndActivityGraph graph = this.getFrontEndActivityGraph();
        if (graph != null)
        {
            final UseCaseFacade graphUseCase = graph.getUseCase();
            if (graphUseCase instanceof FrontEndUseCase)
            {
                useCase = (FrontEndUseCase)graphUseCase;
            }
        }
        return useCase;
    }

    /**
     * All action states that make up this action, this includes all possible
     * action states traversed after a decision point too.
     */
    private Collection<FrontEndActionState> actionStates = null;

    /**
     * @return new ArrayList<FrontEndActionState>(this.actionStates)
     * @see org.andromda.metafacades.uml.FrontEndAction#getActionStates()
     */
    protected List<FrontEndActionState> handleGetActionStates()
    {
        if (this.actionStates == null)
        {
            this.initializeCollections();
        }
        return new ArrayList<FrontEndActionState>(this.actionStates);
    }

    /**
     * Initializes all action states, action forwards, decision transitions and
     * transitions in one shot, so that they can be queried more efficiently
     * later on.
     */
    private void initializeCollections()
    {
        this.actionStates = new LinkedHashSet<FrontEndActionState>();
        this.collectTransitions(
            this,
            new LinkedHashSet<TransitionFacade>());
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
        final Set<TransitionFacade> processedTransitions)
    {
        if (processedTransitions.contains(transition))
        {
            return;
        }
        processedTransitions.add(transition);

        final StateVertexFacade target = transition.getTarget();
        if (target instanceof FrontEndActionState)
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
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndAction#getDecisionTrigger()
     */
    @Override
    protected FrontEndEvent handleGetDecisionTrigger()
    {
        return (FrontEndEvent)(this.isEnteringDecisionPoint() ? this.shieldedElement(this.getTrigger()) : null);
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndAction#getActions()
     */
    @Override
    protected List<FrontEndAction> handleGetActions()
    {
        final Set<FrontEndAction> actions = new LinkedHashSet<FrontEndAction>();
        this.findActions(
            actions,
            new LinkedHashSet<MetafacadeBase>());
        return new ArrayList<FrontEndAction>(actions);
    }

    /**
     * Recursively finds all actions for this forward, what this means depends
     * on the context in which this forward is used: if the source is a page
     * action state it will collect all actions going out of this page, if the
     * source is a regular action state it will collect all actions that might
     * traverse this action state, if the source is the initial state it will
     * collect all actions forwarding to this forward's use-case (please not
     * that those actions most likely are defined in other use-cases).
     *
     * @param actions
     *            the default set of actions, duplicates will not be recorded
     * @param handledForwards
     *            the forwards already processed
     */
    private void findActions(
        final Set<FrontEndAction> actions,
        final Set<MetafacadeBase> handledForwards)
    {
        if (!handledForwards.contains(this.THIS()))
        {
            handledForwards.add(this);

            // TODO this is not so nice because FrontEndAction extends FrontEndForward, solution would be to override in FrontEndAction
            if (this instanceof FrontEndAction)
            {
                actions.add((FrontEndAction)this.THIS());
            }
            else
            {
                final StateVertexFacade vertex = this.getSource();
                if (vertex instanceof FrontEndView)
                {
                    final FrontEndView view = (FrontEndView)vertex;
                    actions.addAll(view.getActions());
                }
                else if (vertex instanceof FrontEndActionState)
                {
                    final FrontEndActionState actionState = (FrontEndActionState)vertex;
                    actions.addAll(actionState.getContainerActions());
                }
                else if (vertex instanceof PseudostateFacade)
                {
                    final PseudostateFacade pseudostate = (PseudostateFacade)vertex;
                    if (!pseudostate.isInitialState())
                    {
                        final Collection<TransitionFacade> incomingForwards = pseudostate.getIncomings();
                        for (TransitionFacade incomingForward : incomingForwards)
                        {
                            final FrontEndForward forward = (FrontEndForward) incomingForward;
                            actions.addAll(forward.getActions());
                        }
                    }
                }
            }
        }
    }

    /**
     * Overridden since a transition doesn't exist in a package.
     *
     * @see org.andromda.metafacades.emf.uml22.ModelElementFacadeLogic#handleGetPackageName()
     */
    @Override
    protected String handleGetPackageName()
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
     * @see org.andromda.metafacades.uml.FrontEndAction#getForwardParameters()
     */
    @Override
    protected List<ParameterFacade> handleGetForwardParameters()
    {
        final EventFacade trigger = this.getTrigger();
        return trigger == null ? Collections.<ParameterFacade>emptyList() : new ArrayList<ParameterFacade>(trigger.getParameters());
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndAction#getOperationCall()
     */
    @Override
    protected FrontEndControllerOperation handleGetOperationCall()
    {
        FrontEndControllerOperation operation = null;
        final EventFacade trigger = this.getTrigger();
        if (trigger instanceof FrontEndEvent)
        {
            final FrontEndEvent triggerEvent = (FrontEndEvent)trigger;
            operation = triggerEvent.getControllerCall();
        }
        else
        {
            LOGGER.info("FrontEndForward has no FrontEndEvent trigger defined. forward=" + this.getFullyQualifiedName(false) + " trigger=" + trigger);
        }
        return operation;
    }

    @Override
    protected String handleGetPath() {
        String forwardPath = null;
        final StateVertexFacade target = getTarget();
        if (this.isEnteringView())
        {
            forwardPath = ((FrontEndView)target).getPath();
        }
        else if (this.isEnteringFinalState())
        {
            forwardPath = ((FrontEndFinalState)target).getPath();
        }

        return forwardPath;
    }

    @Override
    protected boolean handleIsFinalStateTarget() {
        return this.getTarget() instanceof FrontEndFinalState;
    }

    @Override
    protected String handleGetFromOutcome() {
        return this.getName();
    }

    /**
     * Collects specific messages in a map.
     *
     * @param taggedValue the tagged value from which to read the message
     * @return maps message keys to message values, but only those that match the arguments
     *         will have been recorded
     */
    @SuppressWarnings("unchecked")
    private Map<String, String> getMessages(String taggedValue)
    {
        Map<String, String> messages;

        final Collection taggedValues = this.findTaggedValues(taggedValue);
        if (taggedValues.isEmpty())
        {
            messages = Collections.EMPTY_MAP;
        }
        else
        {
            messages = new LinkedHashMap<String, String>(); // we want to keep the order

            for (final Iterator iterator = taggedValues.iterator(); iterator.hasNext();)
            {
                final String value = (String)iterator.next();
                messages.put(StringUtilsHelper.toResourceMessageKey(value), value);
            }
        }

        return messages;
    }

    @Override
    protected Map handleGetSuccessMessages() {
        return this.getMessages(MetafacadeWebProfile.TAGGEDVALUE_ACTION_SUCCESS_MESSAGE);
    }

    @Override
    protected boolean handleIsSuccessMessagesPresent() {
        return !this.getSuccessMessages().isEmpty();
    }

    @Override
    protected Map handleGetWarningMessages() {
        return this.getMessages(MetafacadeWebProfile.TAGGEDVALUE_ACTION_WARNING_MESSAGE);
    }

    @Override
    protected boolean handleIsWarningMessagesPresent() {
        return !this.getWarningMessages().isEmpty();
    }
}
