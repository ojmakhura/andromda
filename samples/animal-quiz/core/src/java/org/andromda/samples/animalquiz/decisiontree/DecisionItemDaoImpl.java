// license-header java merge-point
/**
 * This is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 */
package org.andromda.samples.animalquiz.decisiontree;

/**
 * @see org.andromda.samples.animalquiz.decisiontree.DecisionItem
 */
public class DecisionItemDaoImpl
    extends org.andromda.samples.animalquiz.decisiontree.DecisionItemDaoBase
{
    /**
     * @see org.andromda.samples.animalquiz.decisiontree.DecisionItemDao#toVODecisionItem(org.andromda.samples.animalquiz.decisiontree.DecisionItem)
     */
    public org.andromda.samples.animalquiz.decisiontree.VODecisionItem toVODecisionItem(final org.andromda.samples.animalquiz.decisiontree.DecisionItem entity)
    {
        DecisionItem yesSuccessor = entity.getYesSuccessor();
        DecisionItem noSuccessor = entity.getNoSuccessor();
        try
        {
            return new VODecisionItem(entity.getId(), entity.getPrompt(), (yesSuccessor == null) ? null : yesSuccessor.getId(),
                    (noSuccessor == null) ? null : noSuccessor.getId());
        }
        catch (Exception ex)
        {
            throw new DecisionServiceException(ex);
        }
    }

}