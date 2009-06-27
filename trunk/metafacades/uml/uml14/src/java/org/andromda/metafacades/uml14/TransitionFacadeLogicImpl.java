package org.andromda.metafacades.uml14;

import org.andromda.metafacades.uml.ActionStateFacade;
import org.andromda.metafacades.uml.FinalStateFacade;
import org.andromda.metafacades.uml.PseudostateFacade;
import org.andromda.metafacades.uml.StateVertexFacade;
import org.omg.uml.behavioralelements.commonbehavior.Action;
import org.omg.uml.behavioralelements.statemachines.Event;
import org.omg.uml.behavioralelements.statemachines.Guard;
import org.omg.uml.behavioralelements.statemachines.StateVertex;
import org.omg.uml.behavioralelements.statemachines.Transition;

/**
 * Metaclass facade implementation.
 * @author Bob Fields
 */
public class TransitionFacadeLogicImpl
        extends TransitionFacadeLogic
{
    /**
     * @param metaObject
     * @param context
     */
    public TransitionFacadeLogicImpl(Transition metaObject,
                                     String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml14.TransitionFacadeLogic#handleGetEffect()
     */
    protected Action handleGetEffect()
    {
        return metaObject.getEffect();
    }

    /**
     * @see org.andromda.metafacades.uml14.TransitionFacadeLogic#handleGetSource()
     */
    protected StateVertex handleGetSource()
    {
        return metaObject.getSource();
    }

    /**
     * @see org.andromda.metafacades.uml14.TransitionFacadeLogic#handleGetTarget()
     */
    protected StateVertex handleGetTarget()
    {
        return metaObject.getTarget();
    }

    /**
     * @see org.andromda.metafacades.uml14.TransitionFacadeLogic#handleGetTrigger()
     */
    protected Event handleGetTrigger()
    {
        return metaObject.getTrigger();
    }

    /**
     * @see org.andromda.metafacades.uml14.TransitionFacadeLogic#handleGetGuard()
     */
    protected Guard handleGetGuard()
    {
        return metaObject.getGuard();
    }

    /**
     * @see org.andromda.metafacades.uml14.TransitionFacadeLogic#handleIsTriggerPresent()
     */
    protected boolean handleIsTriggerPresent()
    {
        return metaObject.getTrigger() != null;
    }

    /**
     * @see org.andromda.metafacades.uml14.TransitionFacadeLogic#handleIsExitingDecisionPoint()
     */
    protected boolean handleIsExitingDecisionPoint()
    {
        final StateVertexFacade sourceVertex = getSource();
        return sourceVertex instanceof PseudostateFacade && ((PseudostateFacade)sourceVertex).isDecisionPoint();
    }

    /**
     * @see org.andromda.metafacades.uml14.TransitionFacadeLogic#handleIsEnteringDecisionPoint()
     */
    protected boolean handleIsEnteringDecisionPoint()
    {
        final StateVertexFacade target = getTarget();
        return target instanceof PseudostateFacade && ((PseudostateFacade)target).isDecisionPoint();
    }

    /**
     * @see org.andromda.metafacades.uml14.TransitionFacadeLogic#handleIsExitingActionState()
     */
    protected boolean handleIsExitingActionState()
    {
        return getSource() instanceof ActionStateFacade;
    }

    /**
     * @see org.andromda.metafacades.uml14.TransitionFacadeLogic#handleIsEnteringActionState()
     */
    protected boolean handleIsEnteringActionState()
    {
        return getTarget() instanceof ActionStateFacade;
    }

    /**
     * @see org.andromda.metafacades.uml14.TransitionFacadeLogic#handleIsExitingInitialState()
     */
    protected boolean handleIsExitingInitialState()
    {
        StateVertexFacade sourceVertex = getSource();
        return sourceVertex instanceof PseudostateFacade && ((PseudostateFacade)sourceVertex).isInitialState();
    }

    /**
     * @see org.andromda.metafacades.uml14.TransitionFacadeLogic#handleIsEnteringFinalState()
     */
    protected boolean handleIsEnteringFinalState()
    {
        return getTarget() instanceof FinalStateFacade;
    }

    /**
     * @return getTarget().getStateMachine()
     */
    public Object handleGetValidationOwner()
    {
        return getTarget().getStateMachine();
    }

}
