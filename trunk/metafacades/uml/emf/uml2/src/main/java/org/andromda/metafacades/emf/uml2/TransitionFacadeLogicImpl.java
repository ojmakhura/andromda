package org.andromda.metafacades.emf.uml2;

import java.util.Collection;
import java.util.Iterator;
import org.andromda.metafacades.uml.ActionStateFacade;
import org.andromda.metafacades.uml.FinalStateFacade;
import org.andromda.metafacades.uml.PseudostateFacade;
import org.andromda.metafacades.uml.StateVertexFacade;
import org.eclipse.uml2.Action;
import org.eclipse.uml2.Activity;


/**
 * MetafacadeLogic implementation for
 * org.andromda.metafacades.uml.TransitionFacade.
 *
 * @see org.andromda.metafacades.uml.TransitionFacade
 */
public class TransitionFacadeLogicImpl
    extends TransitionFacadeLogic
{
    /**
     * @param metaObject
     * @param context
     */
    public TransitionFacadeLogicImpl(
        final org.eclipse.uml2.Transition metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    protected Object handleGetEffect()
    {
        // Effect is mapped to action, not activity
        // We return the first action encountered in the activity
        Action effectAction = null;
        Activity effect = this.metaObject.getEffect();
        if (effect != null)
        {
            Collection nodes = effect.getNodes();
            for (Iterator nodesIt = nodes.iterator(); nodesIt.hasNext() && effectAction == null;)
            {
                Object nextNode = nodesIt.next();
                if (nextNode instanceof Action)
                {
                    effectAction = (Action)nextNode;
                }
            }
        }
        return effectAction;
    }

    protected Object handleGetSource()
    {
        return this.metaObject.getSource();
    }

    protected Object handleGetTarget()
    {
        return this.metaObject.getTarget();
    }

    protected Object handleGetTrigger()
    {
        // We use the effect instead of trigger. It's the same "trick" as
        // using entry instead of deferrable events
        return this.metaObject.getEffect();
    }

    protected Object handleGetGuard()
    {
        return this.metaObject.getGuard();
    }

    protected boolean handleIsTriggerPresent()
    {
        return this.metaObject.getEffect() != null;
    }

    protected boolean handleIsExitingDecisionPoint()
    {
        final StateVertexFacade sourceVertex = this.getSource();
        return sourceVertex instanceof PseudostateFacade && ((PseudostateFacade)sourceVertex).isDecisionPoint();
    }

    protected boolean handleIsEnteringDecisionPoint()
    {
        final StateVertexFacade target = this.getTarget();
        return target instanceof PseudostateFacade && ((PseudostateFacade)target).isDecisionPoint();
    }

    protected boolean handleIsExitingActionState()
    {
        return this.getSource() instanceof ActionStateFacade;
    }

    protected boolean handleIsEnteringActionState()
    {
        return this.getTarget() instanceof ActionStateFacade;
    }

    protected boolean handleIsExitingInitialState()
    {
        StateVertexFacade sourceVertex = this.getSource();
        return sourceVertex instanceof PseudostateFacade && ((PseudostateFacade)sourceVertex).isInitialState();
    }

    protected boolean handleIsEnteringFinalState()
    {
        return this.getTarget() instanceof FinalStateFacade;
    }

    /**
     * @see org.andromda.core.metafacade.MetafacadeBase#getValidationOwner()
     */
    public Object getValidationOwner()
    {
        return this.getTarget().getStateMachine();
    }
}