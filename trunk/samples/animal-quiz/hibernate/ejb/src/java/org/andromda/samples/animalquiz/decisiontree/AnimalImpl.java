/**
 * This class is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 */
package org.andromda.samples.animalquiz.decisiontree;

/**
 * @hibernate.subclass
 *    discriminator-value="AnimalImpl"
 */
public class AnimalImpl
    extends Animal
{
    // concrete business methods that were declared
    // abstract in class Animal ...

    public java.lang.String getPrompt()
    {
        return "Is it a(n) " + getName() + "?";
    }

}
