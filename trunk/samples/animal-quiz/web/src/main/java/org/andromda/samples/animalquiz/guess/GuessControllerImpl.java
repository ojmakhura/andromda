// license-header java merge-point
package org.andromda.samples.animalquiz.guess;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.andromda.samples.animalquiz.decisiontree.VODecisionItem;
import org.apache.struts.action.ActionMapping;

/**
 * @see org.andromda.samples.animalquiz.guess.GuessController
 */
public class GuessControllerImpl extends GuessController
{
    /**
     * @see org.andromda.samples.animalquiz.guess.GuessController#getFirstQuestion(org.apache.struts.action.ActionMapping, GetFirstQuestionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public final void getFirstQuestion(ActionMapping mapping, GetFirstQuestionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        try
        {
            final VODecisionItem item = this.getDecisionService().getFirstQuestion();
            form.setQuestion(item.getPrompt());

            // Keep the decision item in the session so that
            // the next step can process it.
            this.getGuessSessionState(request).setLastDecisionItem(item);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    /**
     * @see org.andromda.samples.animalquiz.guess.GuessController#nextDecisionItemAvailable(org.apache.struts.action.ActionMapping, NextDecisionItemAvailableForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public final String nextDecisionItemAvailable(ActionMapping mapping, NextDecisionItemAvailableForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        final GuessSessionState sessionState = this.getGuessSessionState(request);
        VODecisionItem item = sessionState.getLastDecisionItem();

        final Long idNextItem = "yes".equals(sessionState.getLastAnswerFromUser()) ?
                item.getIdYesItem() : item.getIdNoItem();

        if (idNextItem != null)
        {
            item = this.getDecisionService().getNextQuestion(idNextItem);

            form.setQuestion(item.getPrompt());

            // Keep the decision item in the session so that
            // the next step can process it.
            sessionState.setLastDecisionItem(item);
            return "yes";
        }
        return "no";
    }

    /**
     * @see org.andromda.samples.animalquiz.guess.GuessController#rememberAnimal(org.apache.struts.action.ActionMapping, RememberAnimalForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public final void rememberAnimal(ActionMapping mapping, RememberAnimalForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        this.getGuessSessionState(request).setLastAnimalName(form.getAnimal());
    }

    /**
     * @see org.andromda.samples.animalquiz.guess.GuessController#rememberQuestion(org.apache.struts.action.ActionMapping, RememberQuestionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public final void rememberQuestion(ActionMapping mapping, RememberQuestionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        final GuessSessionState sessionState = this.getGuessSessionState(request);
        this.getDecisionService().addNewAnimalWithQuestion(sessionState.getLastAnimalName(), form.getQuestion(),
                sessionState.getLastDecisionItem().getId());
    }

    /**
     * @see org.andromda.samples.animalquiz.guess.GuessController#lastAnswerWasYes(org.apache.struts.action.ActionMapping, LastAnswerWasYesForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public final boolean lastAnswerWasYes(ActionMapping mapping, LastAnswerWasYesForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        return "yes".equals(this.getGuessSessionState(request).getLastAnswerFromUser());
    }

    /**
     * @see org.andromda.samples.animalquiz.guess.GuessController#rememberPositiveAnswer(org.apache.struts.action.ActionMapping, RememberPositiveAnswerForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public final void rememberPositiveAnswer(ActionMapping mapping, RememberPositiveAnswerForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        this.getGuessSessionState(request).setLastAnswerFromUser("yes");
    }

    /**
     * @see org.andromda.samples.animalquiz.guess.GuessController#rememberNegativeAnswer(org.apache.struts.action.ActionMapping, RememberNegativeAnswerForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public final void rememberNegativeAnswer(ActionMapping mapping, RememberNegativeAnswerForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        this.getGuessSessionState(request).setLastAnswerFromUser("no");
    }

    /**
     * @see org.andromda.samples.animalquiz.guess.GuessController#initializeSession(org.apache.struts.action.ActionMapping, InitializeSessionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public final void initializeSession(ActionMapping mapping, InitializeSessionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        setGuessSessionState(request, new GuessSessionState());
    }
}