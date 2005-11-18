package org.andromda.metafacades.emf.uml2;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.PseudostateFacade.
 *
 * @see org.andromda.metafacades.uml.PseudostateFacade
 */
public class PseudostateFacadeLogicImpl
    extends PseudostateFacadeLogic
{
    public PseudostateFacadeLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.PseudostateFacade#isChoice()
     */
    protected boolean handleIsChoice()
    {
        // TODO: put your implementation here.
        return false;
    }

    /**
     * @see org.andromda.metafacades.uml.PseudostateFacade#isDecisionPoint()
     */
    protected boolean handleIsDecisionPoint()
    {
        // TODO: put your implementation here.
        return false;
    }

    /**
     * @see org.andromda.metafacades.uml.PseudostateFacade#isDeepHistory()
     */
    protected boolean handleIsDeepHistory()
    {
        // TODO: put your implementation here.
        return false;
    }

    /**
     * @see org.andromda.metafacades.uml.PseudostateFacade#isFork()
     */
    protected boolean handleIsFork()
    {
        // TODO: put your implementation here.
        return false;
    }

    /**
     * @see org.andromda.metafacades.uml.PseudostateFacade#isInitialState()
     */
    protected boolean handleIsInitialState()
    {
        // TODO: put your implementation here.
        return false;
    }

    /**
     * @see org.andromda.metafacades.uml.PseudostateFacade#isJoin()
     */
    protected boolean handleIsJoin()
    {
        // TODO: put your implementation here.
        return false;
    }

    /**
     * @see org.andromda.metafacades.uml.PseudostateFacade#isJunction()
     */
    protected boolean handleIsJunction()
    {
        // TODO: put your implementation here.
        return false;
    }

    /**
     * @see org.andromda.metafacades.uml.PseudostateFacade#isMergePoint()
     */
    protected boolean handleIsMergePoint()
    {
        // TODO: put your implementation here.
        return false;
    }

    /**
     * @see org.andromda.metafacades.uml.PseudostateFacade#isShallowHistory()
     */
    protected boolean handleIsShallowHistory()
    {
        // TODO: put your implementation here.
        return false;
    }

    /**
     * @see org.andromda.metafacades.uml.PseudostateFacade#isSplit()
     */
    protected boolean handleIsSplit()
    {
        // TODO: put your implementation here.
        return false;
    }

    /**
     * @see org.andromda.metafacades.uml.PseudostateFacade#isCollect()
     */
    protected boolean handleIsCollect()
    {
        // TODO: put your implementation here.
        return false;
    }

    /**
     * @see org.andromda.core.metafacade.MetafacadeBase#getValidationOwner()
     */
    public Object getValidationOwner()
    {
        return this.getStateMachine();
    }
}