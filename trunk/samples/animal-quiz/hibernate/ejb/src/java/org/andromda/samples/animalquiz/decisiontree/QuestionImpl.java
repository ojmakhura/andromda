/**
 * This class is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 */
package org.andromda.samples.animalquiz.decisiontree;

/**
 * @hibernate.subclass
 *    discriminator-value="QuestionImpl"
 */
public class QuestionImpl
    extends Question
{
    // concrete business methods that were declared
    // abstract in class Question ...

    public java.lang.String getPrompt()
    {
        return getPromptString();
    }

}
