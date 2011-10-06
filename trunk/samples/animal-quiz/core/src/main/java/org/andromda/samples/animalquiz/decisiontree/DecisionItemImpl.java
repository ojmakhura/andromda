// license-header java merge-point
/**
 * This is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 */
package org.andromda.samples.animalquiz.decisiontree;

/**
 * @see DecisionItem
 */
public abstract class DecisionItemImpl
    extends DecisionItem
{
    /**
     * The serial version UID of this class. Needed for serialization.
     */
    private static final long serialVersionUID = -2291269241763377348L;

    /**
     * @see DecisionItem#getPrompt()
     */
    public abstract String getPrompt()
        throws DecisionException;
}