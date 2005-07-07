package org.andromda.metafacades.uml14;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import org.andromda.core.common.StringUtilsHelper;
import org.andromda.metafacades.uml.EventFacade;
import org.andromda.metafacades.uml.FrontEndActionState;
import org.andromda.metafacades.uml.FrontEndActivityGraph;
import org.andromda.metafacades.uml.FrontEndForward;
import org.andromda.metafacades.uml.FrontEndView;
import org.andromda.metafacades.uml.StateVertexFacade;
import org.andromda.metafacades.uml.TransitionFacade;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.FrontEndForward.
 *
 * @see org.andromda.metafacades.uml.FrontEndForward
 */
public class FrontEndForwardLogicImpl
    extends FrontEndForwardLogic
{
    public FrontEndForwardLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndForward#isContainedInFrontEndUseCase()
     */
    protected boolean handleIsContainedInFrontEndUseCase()
    {
        return this.getFrontEndActivityGraph() != null;
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndForward#isContainedInFrontEndUseCase()
     */
    protected Object handleGetFrontEndActivityGraph()
    {
        final Object graph = this.getSource().getStateMachine();
        return graph instanceof FrontEndActivityGraph ? graph : null;
    }

    /**
     * If this forward has a trigger this method returns that trigger's name, otherwise if this forward
     * has a name this method returns that name, otherwise if this forward's target has a name this
     * method returns that name, otherwise simply returns <code>"unknown"</code>
     */
    private final String resolveName()
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
            forwardName = getName();
        }

        // - target
        if (forwardName == null)
        {
            forwardName = getTarget().getName();
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
    protected String handleGetActionMethodName()
    {
        return StringUtilsHelper.lowerCamelCaseName(resolveName());
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndForward#isEnteringView()
     */
    protected boolean handleIsEnteringView()
    {
        return this.getTarget() instanceof FrontEndView;
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndForward#isExitingView()
     */
    protected boolean handleIsExitingView()
    {
        return this.getSource() instanceof FrontEndView;
    }
    
    /**
     * All action states that make up this action, this includes all possible action states traversed
     * after a decision point too.
     */
    private Collection actionStates = null;

    /**
     * @see org.andromda.metafacades.uml.FrontEndAction#getActionStates()
     */
    protected java.util.List handleGetActionStates()
    {
        if (actionStates == null)
        {
            this.initializeCollections();
        }
        return new ArrayList(actionStates);
    }

    /**
     * Initializes all action states, action forwards, decision transitions and transitions in one shot, so that they
     * can be queried more effiencently later on.
     */
    private void initializeCollections()
    {
        this.actionStates = new HashSet();
        this.transitions = new HashSet();
        this.collectTransitions(this, transitions);
    }
    
    /**
     * All transitions that can be traversed when calling this action.
     */
    private Collection transitions = null;

    /**
     * Recursively collects all action states, action forwards, decision transitions and transitions.
     *
     * @param transition the current transition that is being processed
     * @param processedTransitions the set of transitions already processed
     */
    private void collectTransitions(
        TransitionFacade transition,
        Collection processedTransitions)
    {
        if (processedTransitions.contains(transition))
        {
            return;
        }
        processedTransitions.add(transition);

        final StateVertexFacade target = transition.getTarget();
        if (target instanceof FrontEndActionState)
        {
            this.actionStates.add(target);
            final FrontEndForward forward = ((FrontEndActionState)target).getForward();
            if (forward != null)
            {
                collectTransitions(forward, processedTransitions);
            }
        }
    }
}