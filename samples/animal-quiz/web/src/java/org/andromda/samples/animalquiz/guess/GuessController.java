package org.andromda.samples.animalquiz.guess;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.andromda.samples.animalquiz.decisiontree.DecisionService;
import org.andromda.samples.animalquiz.decisiontree.DecisionServiceHome;
import org.andromda.samples.animalquiz.decisiontree.DecisionServiceUtil;
import org.andromda.samples.animalquiz.decisiontree.VODecisionItem;
import org.apache.struts.action.ActionMapping;

/**
 *
 * This controller class implements all the methods that are called
 * from the activities inside the "Guess" activity graph.
 * 
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen</a>
 */
public final class GuessController implements GuessControllerInterface {
    private final static GuessController INSTANCE = new GuessController();

    private String lastAnswerFromUser = null;

    /**
     * Singleton constructor
     */
    private GuessController() {
    }

    /**
     * Singleton instance accessor
     */
    public static GuessController getInstance() {
        return INSTANCE;
    }

    /**
     * 
     * <p/>
     * This method does not receive any parameters through the form bean.
     */
    public void getFirstQuestion(
        ActionMapping mapping,
        GuessForm form,
        HttpServletRequest request,
        HttpServletResponse reponse)
        throws Exception {
        DecisionServiceHome dsh = DecisionServiceUtil.getHome();
        DecisionService ds = dsh.create();
        VODecisionItem vodi = ds.getFirstQuestion();
        ds.remove();

        form.setQuestion(vodi.getPrompt());

        // Keep the decision item in the session so that
        // the next step can process it.
        HttpSession session = request.getSession();
        session.setAttribute("voLastDecisionItem", vodi);
    }

    /**
     * 
     * <p/>
     * This method does not receive any parameters through the form bean.
     */
    public java.lang.String nextDecisionItemAvailable(
        ActionMapping mapping,
        GuessForm form,
        HttpServletRequest request,
        HttpServletResponse reponse)
        throws Exception {
        HttpSession session = request.getSession();
        VODecisionItem vodi = (VODecisionItem) session.getAttribute("voLastDecisionItem");

        String idNextItem =
            "yes".equals(lastAnswerFromUser) ? vodi.getIdYesItem() : vodi.getIdNoItem();
        if (idNextItem != null) {
            DecisionServiceHome dsh = DecisionServiceUtil.getHome();
            DecisionService ds = dsh.create();
            vodi = ds.getNextQuestion(idNextItem);
            ds.remove();

            form.setQuestion(vodi.getPrompt());

            // Keep the decision item in the session so that
            // the next step can process it.
            session.setAttribute("voLastDecisionItem", vodi);
            return "yes";
        }
        return "no";
    }

    /**
     * 
     * <p/>
     * This method does not receive any parameters through the form bean.
     */
    public void rememberAnimal(
        ActionMapping mapping,
        GuessForm form,
        HttpServletRequest request,
        HttpServletResponse reponse)
        throws Exception {
        HttpSession session = request.getSession();
        session.setAttribute("lastAnimalName", form.getAnimal());
    }

    /**
     * 
     * <p/>
     * This method does not receive any parameters through the form bean.
     */
    public void rememberQuestion(
        ActionMapping mapping,
        GuessForm form,
        HttpServletRequest request,
        HttpServletResponse reponse)
        throws Exception {

        HttpSession session = request.getSession();

        String animalName = (String) session.getAttribute("lastAnimalName");
        VODecisionItem vodi = (VODecisionItem) session.getAttribute("voLastDecisionItem");

        DecisionServiceHome dsh = DecisionServiceUtil.getHome();
        DecisionService ds = dsh.create();

        ds.addNewAnimalWithQuestion(animalName, form.getQuestion(), vodi.getId());

        ds.remove();
    }

    public boolean lastAnswerWasYes(
        ActionMapping mapping,
        GuessForm form,
        HttpServletRequest request,
        HttpServletResponse reponse)
        throws Exception {
        return "yes".equals(lastAnswerFromUser);
    }

    /* (non-Javadoc)
     * @see org.andromda.samples.animalquiz.guess.GuessControllerInterface#rememberPositiveAnswer(org.apache.struts.action.ActionMapping, org.andromda.samples.animalquiz.guess.GuessForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public void rememberPositiveAnswer(
        ActionMapping mapping,
        GuessForm form,
        HttpServletRequest request,
        HttpServletResponse reponse)
        throws Exception {
        lastAnswerFromUser = "yes";
    }

    /* (non-Javadoc)
     * @see org.andromda.samples.animalquiz.guess.GuessControllerInterface#rememberNegativeAnswer(org.apache.struts.action.ActionMapping, org.andromda.samples.animalquiz.guess.GuessForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public void rememberNegativeAnswer(
        ActionMapping mapping,
        GuessForm form,
        HttpServletRequest request,
        HttpServletResponse reponse)
        throws Exception {
        lastAnswerFromUser = "no";
    }
}
