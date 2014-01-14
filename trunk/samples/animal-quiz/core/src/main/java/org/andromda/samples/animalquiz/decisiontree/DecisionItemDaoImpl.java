// license-header java merge-point
/**
 * This is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 */
package org.andromda.samples.animalquiz.decisiontree;

/**
 * @see DecisionItem
 */
public class DecisionItemDaoImpl
    extends DecisionItemDaoBase
{
    /**
	 * @see org.andromda.samples.animalquiz.decisiontree.DecisionItemDaoBase#findRoot(int)
	 */
	@Override
    public Object findRoot(final int transform)
        throws DecisionException
    {
        return this.findRoot(transform, "from DecisionItemImpl as decisionItem where decisionItem.rootItem = true");
    }
	
    /**
     * @see org.andromda.samples.animalquiz.decisiontree.DecisionItemDao#toVODecisionItem(DecisionItem)
     */
    @Override
    public VODecisionItem toVODecisionItem(final DecisionItem entity)
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

    /**
     * @see org.andromda.samples.animalquiz.decisiontree.DecisionItemDao#vODecisionItemToEntity(VODecisionItem)
     */
    public DecisionItem vODecisionItemToEntity(VODecisionItem vODecisionItem)
    {
        // TODO Auto-generated method stub
        return null;
    }
}