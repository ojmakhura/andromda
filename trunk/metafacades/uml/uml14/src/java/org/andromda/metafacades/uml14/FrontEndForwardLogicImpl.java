package org.andromda.metafacades.uml14;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.andromda.metafacades.uml.EventFacade;
import org.andromda.metafacades.uml.FrontEndAction;
import org.andromda.metafacades.uml.FrontEndActionState;
import org.andromda.metafacades.uml.FrontEndActivityGraph;
import org.andromda.metafacades.uml.FrontEndControllerOperation;
import org.andromda.metafacades.uml.FrontEndEvent;
import org.andromda.metafacades.uml.FrontEndForward;
import org.andromda.metafacades.uml.FrontEndUseCase;
import org.andromda.metafacades.uml.FrontEndView;
import org.andromda.metafacades.uml.ParameterFacade;
import org.andromda.metafacades.uml.PseudostateFacade;
import org.andromda.metafacades.uml.StateVertexFacade;
import org.andromda.metafacades.uml.TransitionFacade;
import org.andromda.metafacades.uml.UseCaseFacade;
import org.andromda.utils.StringUtilsHelper;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.FrontEndForward.
 *
 * @see org.andromda.metafacades.uml.FrontEndForward
 * @author Bob Fields
 */
public class FrontEndForwardLogicImpl
    extends FrontEndForwardLogic
{
    /**
     * @param metaObject
     * @param context
     */
    public FrontEndForwardLogicImpl(
        Object metaObject,
        String context)
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
     * If this forward has a trigger this method returns that trigger's name, otherwise if this forward
     * has a name this method returns that name, otherwise if this forward's target has a name this
     * method returns that name, otherwise simply returns <code>"unknown"</code>
     *
     * @see org.andromda.metafacades.uml14.ModelElementFacadeLogic#handleGetName()
     */
    protected final String handleGetName()
    {
        String forwardName = null;

        // - trigger
        final EventFacade trigger = getTrigger();
        if (trigger != null)
        {
            forwardName = trigger.getName();
        }

        // - name
        if (forwardName == null)
        {
            forwardName = super.handleGetName();
        }

        // - target
        if (forwardName == null)
        {
            forwardName = this.getTarget().getName();
        }

        // - else
        if (forwardName == null)
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
        return StringUtilsHelper.lowerCamelCaseName(this.getName());
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
     * All action states that make up this action, this includes all possible action states traversed
     * after a decision point too.
     */
    private Collection<FrontEndActionState> actionStates = null;

    /**
     * @return new ArrayList(actionStates)
     * @see org.andromda.metafacades.uml.FrontEndAction#getActionStates()
     */
    //@Override
    protected List<FrontEndActionState> handleGetActionStates()
    {
        if (actionStates == null)
        {
            this.initializeCollections();
        }
        return new ArrayList<FrontEndActionState>(actionStates);
    }

    /**
     * Initializes all action states, action forwards, decision transitions and transitions in one shot, so that they
     * can be queried more efficiently later on.
     */
    private void initializeCollections()
    {
        this.actionStates = new LinkedHashSet<FrontEndActionState>();
        this.collectTransitions(
            this,
            new LinkedHashSet());
    }

    /**
     * Recursively collects all action states, action forwards, decision transitions and transitions.
     *
     * @param transition the current transition that is being processed
     * @param processedTransitions the set of transitions already processed
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
        if (target instanceof FrontEndActionState)
        {
            this.actionStates.add((FrontEndActionState)target);
            final FrontEndForward forward = ((FrontEndActionState)target).getForward();
            if (forward != null)
            {
                collectTransitions(
                    forward,
                    processedTransitions);
            }
        }
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndAction#getDecisionTrigger()
     */
    @Override
    protected EventFacade handleGetDecisionTrigger()
    {
        return (this.isEnteringDecisionPoint() ? getTrigger() : null);
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
            new LinkedHashSet<FrontEndForward>());
        return new ArrayList<FrontEndAction>(actions);
    }

    /**
     * Recursively finds all actions for this forward, what this means depends on the context in which
     * this forward is used: if the source is a page action state it will collect all actions going out
     * of this page, if the source is a regular action state it will collect all actions that might traverse
     * this action state, if the source is the initial state it will collect all actions forwarding to this
     * forward's use-case (please not that those actions most likely are defined in other use-cases).
     *
     * @param actions the default set of actions, duplicates will not be recorded
     * @param handledForwards the forwards already processed
     */
    private void findActions(
        final Set<FrontEndAction> actions,
        final Set<FrontEndForward> handledForwards)
    {
        if (!handledForwards.contains(this.THIS()))
        {
            handledForwards.add(this);

            if (this instanceof FrontEndAction) // @todo this is not so nice because FrontEndAction extends FrontEndForward, solution would be to override in FrontEndAction
            {
                actions.add((FrontEndAction)this.THIS());
            }
            else
            {
                final StateVertexFacade vertex = getSource();
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
                        for (final Iterator<TransitionFacade> forwardIterator = incomingForwards.iterator(); forwardIterator.hasNext();)
                        {
                            final FrontEndForward forward = (FrontEndForward)forwardIterator.next();
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
     * @see org.andromda.metafacades.uml14.ModelElementFacadeLogic#handleGetPackageName()
     */
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
        return trigger == null ? Collections.EMPTY_LIST : new ArrayList<ParameterFacade>(trigger.getParameters());
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndAction#getOperationCall()
     */
    @Override
   protected Object handleGetOperationCall()
    {
        FrontEndControllerOperation operation = null;
        final EventFacade triggerEvent = this.getTrigger();
        if (triggerEvent instanceof FrontEndEvent)
        {
            final FrontEndEvent trigger = (FrontEndEvent)triggerEvent;
            operation = trigger.getControllerCall();
        }
        return operation;
    }
}