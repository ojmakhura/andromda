/**
 * This class is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 */
package org.andromda.samples.animalquiz.decisiontree;

public abstract class DecisionItemImpl
        extends DecisionItem
{
    public abstract java.lang.String getPrompt();

    public org.andromda.samples.animalquiz.decisiontree.VODecisionItem getVO()
    {
        DecisionItem yesSuccessor = getYesSuccessor();
        DecisionItem noSuccessor = getNoSuccessor();
        return new VODecisionItem(getId(), getPrompt(), (yesSuccessor == null) ? null : yesSuccessor.getId(),
                (noSuccessor == null) ? null : noSuccessor.getId());
    }
}
