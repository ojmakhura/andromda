package org.andromda.metafacades.uml14;

import org.andromda.metafacades.uml.ActionStateFacade;
import org.andromda.metafacades.uml.FinalStateFacade;
import org.andromda.metafacades.uml.PseudostateFacade;
import org.andromda.metafacades.uml.StateVertexFacade;

/**
 * Metaclass facade implementation.
 */
public class TransitionFacadeLogicImpl
        extends TransitionFacadeLogic
{
    public TransitionFacadeLogicImpl(org.omg.uml.behavioralelements.statemachines.Transition metaObject,
                                     String context)
    {
        super(metaObject, context);
    }

    protected Object handleGetEffect()
    {
        return metaObject.getEffect();
    }

    protected Object handleGetSource()
    {
        return metaObject.getSource();
    }

    protected Object handleGetTarget()
    {
        return metaObject.getTarget();
    }

    protected Object handleGetTrigger()
    {
        return metaObject.getTrigger();
    }

    protected Object handleGetGuard()
    {
        return metaObject.getGuard();
    }

    protected boolean handleIsTriggerPresent()
    {
        return metaObject.getTrigger() != null;
    }

    protected boolean handleIsExitingDecisionPoint()
    {
        final StateVertexFacade sourceVertex = getSource();
        return sourceVertex instanceof PseudostateFacade && ((PseudostateFacade)sourceVertex).isDecisionPoint();
    }

    protected boolean handleIsEnteringDecisionPoint()
    {
        final StateVertexFacade target = getTarget();
        return target instanceof PseudostateFacade && ((PseudostateFacade)target).isDecisionPoint();
    }

    protected boolean handleIsExitingActionState()
    {
        return getSource() instanceof ActionStateFacade;
    }

    protected boolean handleIsEnteringActionState()
    {
        return getTarget() instanceof ActionStateFacade;
    }

    protected boolean handleIsExitingInitialState()
    {
        StateVertexFacade sourceVertex = getSource();
        return sourceVertex instanceof PseudostateFacade && ((PseudostateFacade)sourceVertex).isInitialState();
    }

    protected boolean handleIsEnteringFinalState()
    {
        return getTarget() instanceof FinalStateFacade;
    }

    public Object getValidationOwner()
    {
        return getTarget().getActivityGraph();
    }

}
