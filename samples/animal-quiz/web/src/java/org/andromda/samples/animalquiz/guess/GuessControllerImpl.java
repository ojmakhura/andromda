package org.andromda.samples.animalquiz.guess;

import org.andromda.samples.animalquiz.decisiontree.client.DecisionService;
import org.andromda.samples.animalquiz.decisiontree.client.DecisionServiceServiceLocator;
import org.andromda.samples.animalquiz.decisiontree.client.VODecisionItem;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.rpc.ServiceException;

/**
 * This controller class implements all the methods that are called
 * from the activities inside the "Guess" activity graph.
 *
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen</a>
 * @author Chad Brandon
 * @author Wouter Zoons
 */
final class GuessControllerImpl extends GuessController
{
    /**
     * Fetches the first question from the business tier and
     * returns the prompt string in the form.
     */
    public void getFirstQuestion(ActionMapping mapping, GuessControllerGetFirstQuestion form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        DecisionService decisionService = this.getService();
        VODecisionItem vodi = decisionService.getFirstQuestion();

        form.setQuestion(vodi.getPrompt());

        // Keep the decision item in the session so that
        // the next step can process it.
        getGuessSessionState(request).setLastDecisionItem(vodi);
    }

    /**
     * Checks whether a next decision item is available in the
     * decision tree.
     *
     * @return String "yes" or "no".
     */
    public String nextDecisionItemAvailable(ActionMapping mapping, GuessControllerNextDecisionItemAvailable form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        GuessSessionState sessionState = getGuessSessionState(request);
        VODecisionItem vodi = sessionState.getLastDecisionItem();

        String idNextItem =
                "yes".equals(sessionState.getLastAnswerFromUser()) ? vodi.getIdYesItem() : vodi.getIdNoItem();

        if (idNextItem != null)
        {
            DecisionService decisionService = this.getService();
            vodi = decisionService.getNextQuestion(idNextItem);

            form.setQuestion(vodi.getPrompt());

            // Keep the decision item in the session so that
            // the next step can process it.
            sessionState.setLastDecisionItem(vodi);
            return "yes";
        }
        return "no";
    }

    /**
     * Stores the name of the animal that the user has given. It is stored
     * inside the session state - no call to the business tier.
     */
    public void rememberAnimal(ActionMapping mapping, GuessControllerRememberAnimal form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        getGuessSessionState(request).setLastAnimalName(form.getThisIsTheAnimalAnimal());
    }

    /**
     * Takes the differentiator question that the user has given and creates
     * a new animal in the business tier. If the user answers "yes" to that question
     * during the next run of the game, that animal is presented as a guess.
     */
    public void rememberQuestion(ActionMapping mapping, GuessControllerRememberQuestion form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        GuessSessionState sessionState = getGuessSessionState(request);

        DecisionService decisionService = this.getService();

        decisionService.addNewAnimalWithQuestion(sessionState.getLastAnimalName(),
                form.getThisIsTheQuestionQuestion(),
                sessionState.getLastDecisionItem().getId());
    }

    private DecisionService getService() throws ServiceException
    {
        DecisionServiceServiceLocator locator =
                new DecisionServiceServiceLocator();
        return locator.getDecisionService();
    }

    /**
     * Checks if the last answer from the user was "yes".
     */
    public boolean lastAnswerWasYes(ActionMapping mapping, GuessControllerLastAnswerWasYes form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        return "yes".equals(getGuessSessionState(request).getLastAnswerFromUser());
    }


    /**
     * Stores the fact that the last answer from the user was positive.
     */
    public void rememberPositiveAnswer(ActionMapping mapping, GuessControllerRememberPositiveAnswer form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        getGuessSessionState(request).setLastAnswerFromUser("yes");
    }

    /**
     * Stores the fact that the last answer from the user was negative.
     */
    public void rememberNegativeAnswer(ActionMapping mapping, GuessControllerRememberNegativeAnswer form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        getGuessSessionState(request).setLastAnswerFromUser("no");
    }
}
