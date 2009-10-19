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
    extends org.andromda.samples.animalquiz.decisiontree.DecisionServiceBase
{

    /**
     * @see org.andromda.samples.animalquiz.decisiontree.DecisionService#getFirstQuestion()
     */
    protected org.andromda.samples.animalquiz.decisiontree.VODecisionItem handleGetFirstQuestion()
        throws java.lang.Exception
    {
        VODecisionItem item = (VODecisionItem)this.getDecisionItemDao().findRoot(DecisionItemDao.TRANSFORM_VODECISIONITEM);
        if (item == null)
        {
            item = (VODecisionItem)this.getAnimalDao().create(DecisionItemDao.TRANSFORM_VODECISIONITEM, "elephant", true);
        }
        return item;
    }

    /**
     * @see org.andromda.samples.animalquiz.decisiontree.DecisionService#getNextQuestion(java.lang.Long)
     */
    protected org.andromda.samples.animalquiz.decisiontree.VODecisionItem handleGetNextQuestion(java.lang.Long itemId)
        throws java.lang.Exception
    {
        return (VODecisionItem)this.getDecisionItemDao().load(DecisionItemDao.TRANSFORM_VODECISIONITEM, itemId);
    }

    /**
     * @see org.andromda.samples.animalquiz.decisiontree.DecisionService#addNewAnimalWithQuestion(java.lang.String, java.lang.String, java.lang.Long)
     */
    protected void handleAddNewAnimalWithQuestion(java.lang.String animalName, java.lang.String promptForYes, java.lang.Long idOfLastNoDecision)
        throws java.lang.Exception
    {
        Animal newAnimal = (Animal)this.getAnimalDao().create(animalName, false);
        Question newQuestion = (Question)this.getQuestionDao().create(promptForYes, false);
        newQuestion.setYesSuccessor(newAnimal);

        DecisionItem lastNoDecision = this.getDecisionItemDao().load(idOfLastNoDecision);
        lastNoDecision.setNoSuccessor(newQuestion);
        this.getAnimalDao().create(newAnimal);
        this.getQuestionDao().create(newQuestion);
        this.getDecisionItemDao().update(lastNoDecision);
    }

}