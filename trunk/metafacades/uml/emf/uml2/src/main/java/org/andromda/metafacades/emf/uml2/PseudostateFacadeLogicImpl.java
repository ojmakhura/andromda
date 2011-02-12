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
    private static final long serialVersionUID = 34L;
    /**
     * @param metaObject
     * @param context
     */
    public PseudostateFacadeLogicImpl(
        final org.eclipse.uml2.Pseudostate metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /**
     * @return getKind().getValue() == (PseudostateKind.CHOICE)
     * @see org.andromda.metafacades.uml.PseudostateFacade#isChoice()
     */
    protected boolean handleIsChoice()
    {
        return this.metaObject.getKind().getValue() == (PseudostateKind.CHOICE);
    }

    /**
     * @return metaObject.getOutgoings().size() > 1
     * @see org.andromda.metafacades.uml.PseudostateFacade#isDecisionPoint()
     */
    protected boolean handleIsDecisionPoint()
    {
        return (this.isChoice() || this.isJunction()) && this.metaObject.getOutgoings().size() > 1;
    }

    /**
     * @return metaObject.getIncomings().size() > 1
     * @see org.andromda.metafacades.uml.PseudostateFacade#isMergePoint()
     */
    protected boolean handleIsMergePoint()
    {
        return (this.isChoice() || this.isJunction()) && this.metaObject.getIncomings().size() > 1;
    }

    /**
     * @return metaObject.getKind().getValue() == (PseudostateKind.DEEP_HISTORY)
     * @see org.andromda.metafacades.uml.PseudostateFacade#isDeepHistory()
     */
    protected boolean handleIsDeepHistory()
    {
        return this.metaObject.getKind().getValue() == (PseudostateKind.DEEP_HISTORY);
    }

    /**
     * @return metaObject.getKind().getValue() == (PseudostateKind.FORK)
     * @see org.andromda.metafacades.uml.PseudostateFacade#isFork()
     */
    protected boolean handleIsFork()
    {
        return this.metaObject.getKind().getValue() == (PseudostateKind.FORK);
    }

    /**
     * @return metaObject.getKind().getValue() == (PseudostateKind.INITIAL)
     * @see org.andromda.metafacades.uml.PseudostateFacade#isInitialState()
     */
    protected boolean handleIsInitialState()
    {
        return this.metaObject.getKind().getValue() == (PseudostateKind.INITIAL);
    }

    /**
     * @return metaObject.getKind().getValue() == (PseudostateKind.JOIN)
     * @see org.andromda.metafacades.uml.PseudostateFacade#isJoin()
     */
    protected boolean handleIsJoin()
    {
        return this.metaObject.getKind().getValue() == (PseudostateKind.JOIN);
    }

    /**
     * @return getKind().getValue() == (PseudostateKind.JUNCTION)
     * @see org.andromda.metafacades.uml.PseudostateFacade#isJunction()
     */
    protected boolean handleIsJunction()
    {
        return this.metaObject.getKind().getValue() == (PseudostateKind.JUNCTION);
    }

    /**
     * @return metaObject.getKind().getValue() == (PseudostateKind.SHALLOW_HISTORY)
     * @see org.andromda.metafacades.uml.PseudostateFacade#isShallowHistory()
     */
    protected boolean handleIsShallowHistory()
    {
        return this.metaObject.getKind().getValue() == (PseudostateKind.SHALLOW_HISTORY);
    }

    /**
     * @return getOutgoings().size() > 1
     * @see org.andromda.metafacades.uml.PseudostateFacade#isSplit()
     */
    protected boolean handleIsSplit()
    {
        return (this.isJoin() || this.isFork()) && this.metaObject.getOutgoings().size() > 1;
    }

    /**
     * @return getIncomings().size() > 1
     * @see org.andromda.metafacades.uml.PseudostateFacade#isCollect()
     */
    protected boolean handleIsCollect()
    {
        return (this.isJoin() || this.isFork()) && this.metaObject.getIncomings().size() > 1;
    }

    /**
     * @see org.andromda.metafacades.emf.uml2.StateVertexFacadeLogicImpl#getValidationOwner()
     */
    public Object getValidationOwner()
    {
        return this.getStateMachine();
    }
}