package org.andromda.metafacades.uml14;

import org.omg.uml.foundation.datatypes.PseudostateKindEnum;

/**
 * Metaclass facade implementation.
 */
public class PseudostateFacadeLogicImpl
        extends PseudostateFacadeLogic
{
    public PseudostateFacadeLogicImpl(org.omg.uml.behavioralelements.statemachines.Pseudostate metaObject,
                                      String context)
    {
        super(metaObject, context);
    }

    protected boolean handleIsChoice()
    {
        return PseudostateKindEnum.PK_CHOICE.equals(metaObject.getKind());
    }

    protected boolean handleIsInitialState()
    {
        return PseudostateKindEnum.PK_INITIAL.equals(metaObject.getKind());
    }

    protected boolean handleIsJoin()
    {
        return PseudostateKindEnum.PK_JOIN.equals(metaObject.getKind());
    }

    protected boolean handleIsFork()
    {
        return PseudostateKindEnum.PK_FORK.equals(metaObject.getKind());
    }

    protected boolean handleIsJunction()
    {
        return PseudostateKindEnum.PK_JUNCTION.equals(metaObject.getKind());
    }

    protected boolean handleIsDecisionPoint()
    {
        return (isChoice() || isJunction()) && metaObject.getOutgoing().size()>1;
    }

    protected boolean handleIsMergePoint()
    {
        return (isChoice() || isJunction()) && metaObject.getIncoming().size()>1;
    }

    protected boolean handleIsDeepHistory()
    {
        return PseudostateKindEnum.PK_DEEP_HISTORY.equals(metaObject.getKind());
    }

    protected boolean handleIsShallowHistory()
    {
        return PseudostateKindEnum.PK_SHALLOW_HISTORY.equals(metaObject.getKind());
    }

    protected boolean handleIsSplit()
    {
        return (isJoin() || isFork()) && metaObject.getIncoming().size()==1 && metaObject.getOutgoing().size()>1;
    }

    protected boolean handleIsCollect()
    {
        return (isJoin() || isFork()) && metaObject.getOutgoing().size()==1 && metaObject.getIncoming().size()>1;
    }

    public Object getValidationOwner()
    {
        return getActivityGraph();
    }
}
