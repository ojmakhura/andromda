package org.andromda.metafacades.uml14;

import org.omg.uml.foundation.datatypes.PseudostateKindEnum;
import org.omg.uml.behavioralelements.statemachines.Pseudostate;

/**
 * Metaclass facade implementation.
 * @author Bob Fields
 */
public class PseudostateFacadeLogicImpl
    extends PseudostateFacadeLogic
{
    /**
     * @param metaObject
     * @param context
     */
    public PseudostateFacadeLogicImpl(
        Pseudostate metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.PseudostateFacade#isChoice()
     */
    @Override
    protected boolean handleIsChoice()
    {
        return PseudostateKindEnum.PK_CHOICE.equals(metaObject.getKind());
    }

    /**
     * @see org.andromda.metafacades.uml.PseudostateFacade#isInitialState()
     */
    @Override
    protected boolean handleIsInitialState()
    {
        return PseudostateKindEnum.PK_INITIAL.equals(metaObject.getKind());
    }

    /**
     * @see org.andromda.metafacades.uml.PseudostateFacade#isJoin()
     */
    @Override
    protected boolean handleIsJoin()
    {
        return PseudostateKindEnum.PK_JOIN.equals(metaObject.getKind());
    }

    /**
     * @see org.andromda.metafacades.uml.PseudostateFacade#isFork()
     */
    @Override
    protected boolean handleIsFork()
    {
        return PseudostateKindEnum.PK_FORK.equals(metaObject.getKind());
    }

    /**
     * @see org.andromda.metafacades.uml.PseudostateFacade#isJunction()
     */
    @Override
    protected boolean handleIsJunction()
    {
        return PseudostateKindEnum.PK_JUNCTION.equals(metaObject.getKind());
    }

    /**
     * @see org.andromda.metafacades.uml.PseudostateFacade#isDecisionPoint()
     */
    @Override
    protected boolean handleIsDecisionPoint()
    {
        return (this.isChoice() || this.isJunction()) && metaObject.getOutgoing().size() > 1;
    }

    /**
     * @see org.andromda.metafacades.uml.PseudostateFacade#isMergePoint()
     */
    @Override
    protected boolean handleIsMergePoint()
    {
        return (this.isChoice() || this.isJunction()) && metaObject.getIncoming().size() > 1;
    }

    /**
     * @see org.andromda.metafacades.uml.PseudostateFacade#isDeepHistory()
     */
    @Override
    protected boolean handleIsDeepHistory()
    {
        return PseudostateKindEnum.PK_DEEP_HISTORY.equals(metaObject.getKind());
    }

    /**
     * @see org.andromda.metafacades.uml.PseudostateFacade#isShallowHistory()
     */
    @Override
    protected boolean handleIsShallowHistory()
    {
        return PseudostateKindEnum.PK_SHALLOW_HISTORY.equals(metaObject.getKind());
    }

    /**
     * @see org.andromda.metafacades.uml.PseudostateFacade#isSplit()
     */
    @Override
    protected boolean handleIsSplit()
    {
        return (this.isJoin() || this.isFork()) && metaObject.getOutgoing().size() > 1;
    }

    /**
     * @see org.andromda.metafacades.uml.PseudostateFacade#isCollect()
     */
    @Override
    protected boolean handleIsCollect()
    {
        return (this.isJoin() || this.isFork()) && metaObject.getIncoming().size() > 1;
    }

    /**
     * @see org.andromda.metafacades.uml.PseudostateFacade#getValidationOwner()
     */
    @Override
    public Object handleGetValidationOwner()
    {
        return this.getStateMachine();
    }
}