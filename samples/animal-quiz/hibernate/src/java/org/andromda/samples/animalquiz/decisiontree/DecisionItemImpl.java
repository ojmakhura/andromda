/**
 * This class is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 */
package org.andromda.samples.animalquiz.decisiontree;

/**
 * @hibernate.subclass
 *    discriminator-value="DecisionItemImpl"
 */
public abstract class DecisionItemImpl extends DecisionItem {
    // concrete business methods that were declared
    // abstract in class DecisionItem ...

    public abstract java.lang.String getPrompt();

    public org.andromda.samples.animalquiz.decisiontree.VODecisionItem getVO() {
        DecisionItem yesSuccessor = getYesSuccessor();
        DecisionItem noSuccessor = getNoSuccessor();
        return new VODecisionItem(
            getId(),
            getPrompt(),
            (yesSuccessor == null) ? null : yesSuccessor.getId(),
            (noSuccessor == null) ? null : noSuccessor.getId());
    }

}
