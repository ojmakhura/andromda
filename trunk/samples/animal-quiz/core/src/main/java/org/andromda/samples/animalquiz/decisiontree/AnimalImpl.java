// license-header java merge-point
/**
 * This is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 */
package org.andromda.samples.animalquiz.decisiontree;

/**
 * @see org.andromda.samples.animalquiz.decisiontree.Animal
 */
public class AnimalImpl
    extends org.andromda.samples.animalquiz.decisiontree.Animal
{
    /** 
     * The serial version UID of this class. Needed for serialization. 
     */
    private static final long serialVersionUID = -99977346143369122L;
    
    /**
     * @see org.andromda.samples.animalquiz.decisiontree.Animal#getPrompt()
     */
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