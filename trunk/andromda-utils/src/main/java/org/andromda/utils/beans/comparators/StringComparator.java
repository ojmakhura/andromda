package org.andromda.utils.beans.comparators;

import java.io.Serializable;
import java.text.Collator;
import java.util.Comparator;

/**
 * Used to sort String values
 *
 * @author Chad Brandon
 */
class StringComparator
    implements Comparator,
        Serializable
{
    private static final long serialVersionUID = 34L;

    private Collator collator = null;

    /**
     * Used to sort String values, both objects are assumed
     * to be String instances.
     * @param objectA 
     * @param objectB 
     * @return compare result
     */
    public int compare(
        final Object objectA,
        final Object objectB)
    {
        this.initializeCollator();
        return collator.compare(
            objectA,
            objectB);
    }

    /**
     * Initializes the Collator
     */
    private void initializeCollator()
    {
        if (this.collator == null)
        {
            this.collator = Collator.getInstance();

            //set to ignore case
            collator.setStrength(Collator.PRIMARY);
        }
    }
}