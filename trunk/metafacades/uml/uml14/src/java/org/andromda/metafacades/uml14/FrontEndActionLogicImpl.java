package org.andromda.metafacades.uml14;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.andromda.metafacades.uml.EventFacade;
import org.andromda.metafacades.uml.FrontEndActionState;
import org.andromda.metafacades.uml.FrontEndActivityGraph;
import org.andromda.metafacades.uml.FrontEndController;
import org.andromda.metafacades.uml.FrontEndEvent;
import org.andromda.metafacades.uml.FrontEndFinalState;
import org.andromda.metafacades.uml.FrontEndForward;
import org.andromda.metafacades.uml.FrontEndView;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.PseudostateFacade;
import org.andromda.metafacades.uml.StateVertexFacade;
import org.andromda.metafacades.uml.TransitionFacade;
import org.andromda.metafacades.uml.UMLProfile;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.FrontEndAction.
 *
 * @see org.andromda.metafacades.uml.FrontEndAction
 */
public class FrontEndActionLogicImpl
    extends FrontEndActionLogic
{
    public FrontEndActionLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndAction#getInput()
     */
    protected java.lang.Object handleGetInput()
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
            if (source.hasStereotype(UMLProfile.STEREOTYPE_FRONT_END_VIEW))
            {
                input = source;
            }
        }
        return input;
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndAction#getParameters()
     */
    protected java.util.List handleGetParameters()
    {
        final EventFacade trigger = this.getTrigger();
        return trigger == null ? Collections.EMPTY_LIST : new ArrayList(trigger.getParameters());
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndAction#getDeferredOperations()
     */
    protected java.util.List handleGetDeferredOperations()
    {
        final Collection deferredOperations = new LinkedHashSet();

        final FrontEndController controller = this.getController();
        if (controller != null)
        {
            final List actionStates = this.getActionStates();
            for (int i = 0; i < actionStates.size(); i++)
            {
                final FrontEndActionState actionState = (FrontEndActionState)actionStates.get(i);
                deferredOperations.addAll(actionState.getControllerCalls());
            }

            final List transitions = this.getDecisionTransitions();
            for (int i = 0; i < transitions.size(); i++)
            {
                final FrontEndForward forward = (FrontEndForward)transitions.get(i);
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
     * @see org.andromda.metafacades.uml.FrontEndAction#getDecisionTransitions()
     */
    protected List handleGetDecisionTransitions()
    {
        if (decisionTransitions == null)
        {
            initializeCollections();
        }
        return new ArrayList(decisionTransitions);
    }
    
    /**
     * All action states that make up this action, this includes all possible action states traversed
     * after a decision point too.
     */
    private Collection actionStates = null;

    /**
     * All transitions leading into either a page or final state that originated from a call to this action.
     */
    private Map actionForwards = null;

    /**
     * All transitions leading into a decision point that originated from a call to this action.
     */
    private Collection decisionTransitions = null;

    /**
     * All transitions that can be traversed when calling this action.
     */
    private Collection transitions = null;
    
    /**
     * Initializes all action states, action forwards, decision transitions and transitions in one shot, so that they
     * can be queried more effiencently later on.
     */
    private void initializeCollections()
    {
        actionStates = new HashSet();
        transitions = new HashSet();
        collectTransitions(this, transitions);
    }
    
    /**
     * Recursively collects all action states, action forwards, decision transitions and transitions.
     *
     * @param transition the current transition that is being processed
     * @param processedTransitions the set of transitions already processed
     */
    private void collectTransitions(TransitionFacade transition, Collection processedTransitions)
    {
        if (processedTransitions.contains(transition))
        {
            return;
        }
        processedTransitions.add(transition);

        final StateVertexFacade target = transition.getTarget();
        if ((target instanceof FrontEndView) || (target instanceof FrontEndFinalState))
        {
            if (!actionForwards.containsKey(transition.getTarget()))
            {
                actionForwards.put(transition.getTarget(), transition);
            }
        }
        else if ((target instanceof PseudostateFacade) && ((PseudostateFacade)target).isDecisionPoint())
        {
            decisionTransitions.add(transition);
            final Collection outcomes = target.getOutgoing();
            for (final Iterator iterator = outcomes.iterator(); iterator.hasNext();)
            {
                final TransitionFacade outcome = (TransitionFacade)iterator.next();
                collectTransitions(outcome, processedTransitions);
            }
        }
        else if (target instanceof FrontEndActionState)
        {
            actionStates.add(target);
            final FrontEndForward forward = ((FrontEndActionState)target).getForward();
            if (forward != null)
            {
                collectTransitions(forward, processedTransitions);
            }
        }
        else    // all the rest is ignored but outgoing transitions are further processed
        {
            final Collection outcomes = target.getOutgoing();
            for (final Iterator iterator = outcomes.iterator(); iterator.hasNext();)
            {
                final TransitionFacade outcome = (TransitionFacade)iterator.next();
                collectTransitions(outcome, processedTransitions);
            }
        }
    }
    
    /**
     * @see org.andromda.metafacades.uml.FrontEndAction#getActionStates()
     */
    protected List handleGetActionStates()
    {
        if (this.actionStates == null) 
        {
            this.initializeCollections();
        }
        return new ArrayList(this.actionStates);
    }
    
    /**
     * @see org.andromda.metafacades.uml.FrontEndAction#getTransitions()
     */
    protected List handleGetTransitions()
    {
        if (this.transitions == null)
        {
            this.initializeCollections();
        }
        return new ArrayList(this.transitions);
    }
    
    /**
     * @see org.andromda.metafacades.uml.FrontEndAction#getActionForwards()
     */
    protected List handleGetActionForwards()
    {
        if (this.actionForwards == null) 
        {
            this.initializeCollections();
        }
        return new ArrayList(this.actionForwards.values());
    }
    
    /**
     * @see org.andromda.metafacades.uml.FrontEndAction#getController()
     */
    protected Object handleGetController()
    {
        final FrontEndActivityGraph graph = this.getFrontEndActivityGraph();
        return graph == null ? null : graph.getController();
    }
}