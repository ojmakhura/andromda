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
        return "Is it " + formatAnimalWithPredicate(getName()) + " ?";
    }

    private String formatAnimalWithPredicate(String name)
    {
        final StringBuffer formattedBuffer = new StringBuffer();

        formattedBuffer.append("a ");
        formattedBuffer.append(name);

        char firstChar = name.charAt(0);
        switch (firstChar)
        {
            case 'a':   // fall-through
            case 'e':   // fall-through
            case 'i':   // fall-through
            case 'o':
                formattedBuffer.insert(1, 'n');
                break;
            default:
        }

        return formattedBuffer.toString();
    }
}
