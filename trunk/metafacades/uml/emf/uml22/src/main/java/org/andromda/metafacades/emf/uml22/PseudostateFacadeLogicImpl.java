package org.andromda.metafacades.emf.uml22;

import org.eclipse.uml2.uml.Pseudostate;
import org.eclipse.uml2.uml.PseudostateKind;

/**
 * MetafacadeLogic implementation for
 * org.andromda.metafacades.uml.PseudostateFacade.
 *
 * @see org.andromda.metafacades.uml.PseudostateFacade
 */
public class PseudostateFacadeLogicImpl
    extends PseudostateFacadeLogic
{
    /**
     * @param metaObject
     * @param context
     */
    public PseudostateFacadeLogicImpl(
        final Pseudostate metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.PseudostateFacade#isChoice()
     */
    @Override
    protected boolean handleIsChoice()
    {
        return this.metaObject.getKind().getValue() == (PseudostateKind.CHOICE);
    }

    /**
     * @see org.andromda.metafacades.uml.PseudostateFacade#isDecisionPoint()
     */
    @Override
    protected boolean handleIsDecisionPoint()
    {
        return (this.isChoice() || this.isJunction()) && this.metaObject.getOutgoings().size() > 1;
    }

    /**
     * @see org.andromda.metafacades.uml.PseudostateFacade#isMergePoint()
     */
    @Override
    protected boolean handleIsMergePoint()
    {
        return (this.isChoice() || this.isJunction()) && this.metaObject.getIncomings().size() > 1;
    }

    /**
     * @see org.andromda.metafacades.uml.PseudostateFacade#isDeepHistory()
     */
    @Override
    protected boolean handleIsDeepHistory()
    {
        return this.metaObject.getKind().getValue() == (PseudostateKind.DEEP_HISTORY);
    }

    /**
     * @see org.andromda.metafacades.uml.PseudostateFacade#isFork()
     */
    @Override
    protected boolean handleIsFork()
    {
        return this.metaObject.getKind().getValue() == (PseudostateKind.FORK);
    }

    /**
     * @see org.andromda.metafacades.uml.PseudostateFacade#isInitialState()
     */
    @Override
    protected boolean handleIsInitialState()
    {
        return this.metaObject.getKind().getValue() == (PseudostateKind.INITIAL);
    }

    /**
     * @see org.andromda.metafacades.uml.PseudostateFacade#isJoin()
     */
    @Override
    protected boolean handleIsJoin()
    {
        return this.metaObject.getKind().getValue() == (PseudostateKind.JOIN);
    }

    /**
     * @see org.andromda.metafacades.uml.PseudostateFacade#isJunction()
     */
    @Override
    protected boolean handleIsJunction()
    {
        return this.metaObject.getKind().getValue() == (PseudostateKind.JUNCTION);
    }

    /**
     * @see org.andromda.metafacades.uml.PseudostateFacade#isShallowHistory()
     */
    @Override
    protected boolean handleIsShallowHistory()
    {
        return this.metaObject.getKind().getValue() == (PseudostateKind.SHALLOW_HISTORY);
    }

    /**
     * @see org.andromda.metafacades.uml.PseudostateFacade#isSplit()
     */
    @Override
    protected boolean handleIsSplit()
    {
        return (this.isJoin() || this.isFork()) && this.metaObject.getOutgoings().size() > 1;
    }

    /**
     * @see org.andromda.metafacades.uml.PseudostateFacade#isCollect()
     */
    @Override
    protected boolean handleIsCollect()
    {
        return (this.isJoin() || this.isFork()) && this.metaObject.getIncomings().size() > 1;
    }

    /**
     * @see org.andromda.metafacades.emf.uml22.StateVertexFacadeLogicImpl#getValidationOwner()
     * @see org.andromda.metafacades.uml.PseudostateFacade#getValidationOwner()
     */
    @Override
    public Object getValidationOwner()
    {
        return this.getStateMachine();
    }
}
