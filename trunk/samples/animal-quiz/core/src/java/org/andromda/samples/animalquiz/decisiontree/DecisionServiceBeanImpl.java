package org.andromda.samples.animalquiz.decisiontree;

import net.sf.hibernate.HibernateException;

import javax.ejb.EJBException;
import java.util.Collection;

public class DecisionServiceBeanImpl
        extends DecisionServiceBean
        implements javax.ejb.SessionBean
{

    protected org.andromda.samples.animalquiz.decisiontree.VODecisionItem handleGetFirstQuestion(
            net.sf.hibernate.Session sess) throws DecisionException
    {
        try
        {
            Collection dItems = DecisionItemFactory.findRoot(sess);
            DecisionItem dItem;
            if (dItems.isEmpty())
            {
                dItem = AnimalFactory.create("elephant", true);
                sess.save(dItem);
                sess.flush();
            }
            else
            {
                dItem = (DecisionItem)dItems.iterator().next();
            }
            return dItem.getVO();
        }
        catch (HibernateException e)
        {
            throw new EJBException(e);
        }
    }

    protected org.andromda.samples.animalquiz.decisiontree.VODecisionItem handleGetNextQuestion(
            net.sf.hibernate.Session sess, java.lang.String itemId) throws DecisionException
    {
        try
        {
            DecisionItem di = DecisionItemFactory.findByPrimaryKey(sess, itemId);
            return di.getVO();
        }
        catch (HibernateException e)
        {
            throw new EJBException(e);
        }
    }

    protected void handleAddNewAnimalWithQuestion(net.sf.hibernate.Session sess, java.lang.String animalName,
                                                  java.lang.String promptForYes, java.lang.String idOfLastNoDecision)
            throws DecisionException
    {

        try
        {
            Animal newAnimal = AnimalFactory.create(animalName, false);
            Question newQuestion = QuestionFactory.create(promptForYes, false);
            newQuestion.setYesSuccessor(newAnimal);

            DecisionItem lastNoDecision = DecisionItemFactory.findByPrimaryKey(sess, idOfLastNoDecision);
            lastNoDecision.setNoSuccessor(newQuestion);

            sess.save(newAnimal);
            sess.save(newQuestion);
            sess.save(lastNoDecision);
        }
        catch (HibernateException e)
        {
            throw new EJBException(e);
        }
    }
}
