package org.andromda.metafacades.emf.uml2;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.TransitionFacade.
 *
 * @see org.andromda.metafacades.uml.TransitionFacade
 */
public class TransitionFacadeLogicImpl
    extends TransitionFacadeLogic
{
    public TransitionFacadeLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.TransitionFacade#isTriggerPresent()
     */
    protected boolean handleIsTriggerPresent()
    {
        // TODO: put your implementation here.
        return false;
    }

    /**
     * @see org.andromda.metafacades.uml.TransitionFacade#isExitingDecisionPoint()
     */
    protected boolean handleIsExitingDecisionPoint()
    {
        // TODO: put your implementation here.
        return false;
    }

    /**
     * @see org.andromda.metafacades.uml.TransitionFacade#isEnteringDecisionPoint()
     */
    protected boolean handleIsEnteringDecisionPoint()
    {
        // TODO: put your implementation here.
        return false;
    }

    /**
     * @see org.andromda.metafacades.uml.TransitionFacade#isExitingActionState()
     */
    protected boolean handleIsExitingActionState()
    {
        // TODO: put your implementation here.
        return false;
    }

    /**
     * @see org.andromda.metafacades.uml.TransitionFacade#isEnteringActionState()
     */
    protected boolean handleIsEnteringActionState()
    {
        // TODO: put your implementation here.
        return false;
    }

    /**
     * @see org.andromda.metafacades.uml.TransitionFacade#isExitingInitialState()
     */
    protected boolean handleIsExitingInitialState()
    {
        // TODO: put your implementation here.
        return false;
    }

    /**
     * @see org.andromda.metafacades.uml.TransitionFacade#isEnteringFinalState()
     */
    protected boolean handleIsEnteringFinalState()
    {
        // TODO: put your implementation here.
        return false;
    }

    /**
     * @see org.andromda.metafacades.uml.TransitionFacade#getEffect()
     */
    protected java.lang.Object handleGetEffect()
    {
        // TODO: add your implementation here!
        return null;
    }

    /**
     * @see org.andromda.metafacades.uml.TransitionFacade#getTrigger()
     */
    protected java.lang.Object handleGetTrigger()
    {
        // TODO: add your implementation here!
        return null;
    }

    /**
     * @see org.andromda.metafacades.uml.TransitionFacade#getTarget()
     */
    protected java.lang.Object handleGetTarget()
    {
        // TODO: add your implementation here!
        return null;
    }

    /**
     * @see org.andromda.metafacades.uml.TransitionFacade#getSource()
     */
    protected java.lang.Object handleGetSource()
    {
        // TODO: add your implementation here!
        return null;
    }

    /**
     * @see org.andromda.metafacades.uml.TransitionFacade#getGuard()
     */
    protected java.lang.Object handleGetGuard()
    {
        // TODO: add your implementation here!
        return null;
    }

    /**
     * @see org.andromda.core.metafacade.MetafacadeBase#getValidationOwner()
     */
    public Object getValidationOwner()
    {
        return getTarget().getStateMachine();
    }
}