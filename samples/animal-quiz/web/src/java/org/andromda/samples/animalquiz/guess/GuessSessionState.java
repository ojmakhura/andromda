package org.andromda.samples.animalquiz.guess;

import java.io.Serializable;

import org.andromda.samples.animalquiz.decisiontree.VODecisionItem;

/**
 * Holds the variables that are specific to a certain session of the
 * "guess" workflow.
 * 
 * @since 27.03.2004
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen</a>
 */
public class GuessSessionState implements Serializable {
    private VODecisionItem lastDecisionItem;
    private String lastAnimalName;
    private String lastPrompt;
    private String lastAnswerFromUser;

    /**
     * Returns the last animal that the user specified.
     * @return
     */
    public String getLastAnimalName() {
        return lastAnimalName;
    }

    /**
     * Returns the last answer (yes or no) that the user gave.
     * @return
     */
    public String getLastAnswerFromUser() {
        return lastAnswerFromUser;
    }

    /**
     * Returns the item in the decision tree where the last decision took place.
     * @return
     */
    public VODecisionItem getLastDecisionItem() {
        return lastDecisionItem;
    }

    /**
     * Returns the last differentiatior prompt that the user specified.
     * @return
     */
    public String getLastPrompt() {
        return lastPrompt;
    }

    /**
     * Sets the last animal that the user specified.
     * @param string
     */
    public void setLastAnimalName(String animalName) {
        lastAnimalName = animalName;
    }

    /**
     * Sets the last answer (yes or no) that the user gave.
     * @param string
     */
    public void setLastAnswerFromUser(String answer) {
        lastAnswerFromUser = answer;
    }

    /**
     * Sets the item in the decision tree where the last decision took place.
     * @param item
     */
    public void setLastDecisionItem(VODecisionItem item) {
        lastDecisionItem = item;
    }

    /**
     * Sets the last differentiatior prompt that the user specified.
     * @param string
     */
    public void setLastPrompt(String prompt) {
        lastPrompt = prompt;
    }

}
