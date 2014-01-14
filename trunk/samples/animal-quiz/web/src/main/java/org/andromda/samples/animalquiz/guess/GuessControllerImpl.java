// license-header java merge-point
package org.andromda.samples.animalquiz.guess;

import org.andromda.samples.animalquiz.decisiontree.VODecisionItem;

/**
 * @see org.andromda.samples.animalquiz.guess.GuessController
 */
public class GuessControllerImpl extends GuessController
{

	private static final long serialVersionUID = -98798789781L;

	/**
	 * @see org.andromda.samples.animalquiz.guess.GuessController#getFirstQuestion(org.andromda.samples.animalquiz.guess.GetFirstQuestionForm)
	 */
	public void getFirstQuestion(GetFirstQuestionForm form) throws Throwable {
		try {
			final VODecisionItem item = this.getDecisionService()
					.getFirstQuestion();
			form.setQuestion(item.getPrompt());

			// Keep the decision item in the session so that
			// the next step can process it.
			this.getGuessSessionState().setLastDecisionItem(item);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		}
	}
	 
	 /**
	 * @see org.andromda.samples.animalquiz.guess.GuessController#nextDecisionItemAvailable(org.andromda.samples.animalquiz.guess.NextDecisionItemAvailableForm)
	 */
	public String nextDecisionItemAvailable(NextDecisionItemAvailableForm form)
			throws Throwable {

		final GuessSessionState sessionState = this.getGuessSessionState();
		VODecisionItem item = sessionState.getLastDecisionItem();

		final Long idNextItem = "yes".equals(sessionState
				.getLastAnswerFromUser()) ? item.getIdYesItem() : item
				.getIdNoItem();

		if (idNextItem != null) {
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
	 * @see org.andromda.samples.animalquiz.guess.GuessController#rememberAnimal(org.andromda.samples.animalquiz.guess.RememberAnimalForm)
	 */
	public void rememberAnimal(RememberAnimalForm form) throws Throwable{
		 this.getGuessSessionState().setLastAnimalName(form.getAnimal());
	 }
	 

	 /**
	 * @see org.andromda.samples.animalquiz.guess.GuessController#rememberQuestion(org.andromda.samples.animalquiz.guess.RememberQuestionForm)
	 */
	public void rememberQuestion(RememberQuestionForm form) throws Throwable {
		final GuessSessionState sessionState = this.getGuessSessionState();
		this.getDecisionService().addNewAnimalWithQuestion(
				sessionState.getLastAnimalName(), form.getQuestion(),
				sessionState.getLastDecisionItem().getId());
	}
	 
	 /**
	  * @see org.andromda.samples.animalquiz.guess.GuessController#lastAnswerWasYes()
	  */
	public boolean lastAnswerWasYes() throws Throwable {
		return "yes"
				.equals(this.getGuessSessionState().getLastAnswerFromUser());
	}
	 

	 /**
	 * @see org.andromda.samples.animalquiz.guess.GuessController#rememberPositiveAnswer()
	 */
	public void rememberPositiveAnswer() throws Throwable {
		this.getGuessSessionState().setLastAnswerFromUser("yes");
	}
	 

	/**
	 * @see org.andromda.samples.animalquiz.guess.GuessController#rememberNegativeAnswer()
	 */
	public void rememberNegativeAnswer() throws Throwable {
		this.getGuessSessionState().setLastAnswerFromUser("no");
	}
	 
	/**
	 * @see org.andromda.samples.animalquiz.guess.GuessController#initializeSession()
	 */
	public void initializeSession() throws Throwable {
		setGuessSessionState(new GuessSessionState());
	}
}