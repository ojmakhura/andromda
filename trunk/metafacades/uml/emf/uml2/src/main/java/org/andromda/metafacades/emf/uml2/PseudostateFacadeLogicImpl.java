package org.andromda.metafacades.emf.uml2;

import org.eclipse.uml2.PseudostateKind;


/**
 * MetafacadeLogic implementation for
 * org.andromda.metafacades.uml.PseudostateFacade.
 *
 * @see org.andromda.metafacades.uml.PseudostateFacade
 */
public class PseudostateFacadeLogicImpl
    extends PseudostateFacadeLogic
{
    public PseudostateFacadeLogicImpl(
        final org.eclipse.uml2.Pseudostate metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.PseudostateFacade#isChoice()
     */
    protected boolean handleIsChoice()
    {
        return this.metaObject.getKind().getValue() == (PseudostateKind.CHOICE);
    }

    /**
     * @see org.andromda.metafacades.uml.PseudostateFacade#isDecisionPoint()
     */
    protected boolean handleIsDecisionPoint()
    {
        return (this.isChoice() || this.isJunction()) && this.metaObject.getOutgoings().size() > 1;
    }

    /**
     * @see org.andromda.metafacades.uml.PseudostateFacade#isMergePoint()
     */
    protected boolean handleIsMergePoint()
    {
        return (this.isChoice() || this.isJunction()) && this.metaObject.getIncomings().size() > 1;
    }

    /**
     * @see org.andromda.metafacades.uml.PseudostateFacade#isDeepHistory()
     */
    protected boolean handleIsDeepHistory()
    {
        return this.metaObject.getKind().getValue() == (PseudostateKind.DEEP_HISTORY);
    }

    /**
     * @see org.andromda.metafacades.uml.PseudostateFacade#isFork()
     */
    protected boolean handleIsFork()
    {
        return this.metaObject.getKind().getValue() == (PseudostateKind.FORK);
    }

    /**
     * @see org.andromda.metafacades.uml.PseudostateFacade#isInitialState()
     */
    protected boolean handleIsInitialState()
    {
        return this.metaObject.getKind().getValue() == (PseudostateKind.INITIAL);
    }

    /**
     * @see org.andromda.metafacades.uml.PseudostateFacade#isJoin()
     */
    protected boolean handleIsJoin()
    {
        return this.metaObject.getKind().getValue() == (PseudostateKind.JOIN);
    }

    /**
     * @see org.andromda.metafacades.uml.PseudostateFacade#isJunction()
     */
    protected boolean handleIsJunction()
    {
        return this.metaObject.getKind().getValue() == (PseudostateKind.JUNCTION);
    }

    /**
     * @see org.andromda.metafacades.uml.PseudostateFacade#isShallowHistory()
     */
    protected boolean handleIsShallowHistory()
    {
        return this.metaObject.getKind().getValue() == (PseudostateKind.SHALLOW_HISTORY);
    }

    /**
     * @see org.andromda.metafacades.uml.PseudostateFacade#isSplit()
     */
    protected boolean handleIsSplit()
    {
        return (this.isJoin() || this.isFork()) && this.metaObject.getOutgoings().size() > 1;
    }

    /**
     * @see org.andromda.metafacades.uml.PseudostateFacade#isCollect()
     */
    protected boolean handleIsCollect()
    {
        return (this.isJoin() || this.isFork()) && this.metaObject.getIncomings().size() > 1;
    }

    public Object getValidationOwner()
    {
        return this.getStateMachine();
    }
}