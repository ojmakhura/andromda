// license-header java merge-point
/**
 * This is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 */
package org.andromda.samples.animalquiz.decisiontree;

/**
 * @see org.andromda.samples.animalquiz.decisiontree.DecisionService
 */
public class DecisionServiceImpl
    extends DecisionServiceBase
{
    /**
     * @see org.andromda.samples.animalquiz.decisiontree.DecisionService#getFirstQuestion()
     */
    protected VODecisionItem handleGetFirstQuestion()
        throws Exception
    {
        VODecisionItem item = (VODecisionItem)this.getDecisionItemDao().findRoot(DecisionItemDao.TRANSFORM_VODECISIONITEM);
        if (item == null)
        {
        	DecisionItem animal = (DecisionItem) this.getAnimalDao().create("elephant", true);
        	item = (VODecisionItem)this.getDecisionItemDao().toVODecisionItem(animal);
        }
        return item;
    }

    /**
     * @see org.andromda.samples.animalquiz.decisiontree.DecisionService#getNextQuestion(Long)
     */
    protected VODecisionItem handleGetNextQuestion(Long itemId)
        throws Exception
    {
        return (VODecisionItem)this.getDecisionItemDao().load(DecisionItemDao.TRANSFORM_VODECISIONITEM, itemId);
    }

    /**
     * @see org.andromda.samples.animalquiz.decisiontree.DecisionService#addNewAnimalWithQuestion(String, String, Long)
     */
    protected void handleAddNewAnimalWithQuestion(String animalName, String promptForYes, Long idOfLastNoDecision)
        throws Exception
    {
        Animal newAnimal = this.getAnimalDao().create(animalName, false);
        Question newQuestion = this.getQuestionDao().create(promptForYes, false);
        newQuestion.setYesSuccessor(newAnimal);

        DecisionItem lastNoDecision = this.getDecisionItemDao().load(idOfLastNoDecision);
        lastNoDecision.setNoSuccessor(newQuestion);
        this.getAnimalDao().create(newAnimal);
        this.getQuestionDao().create(newQuestion);
        this.getDecisionItemDao().update(lastNoDecision);
    }
}