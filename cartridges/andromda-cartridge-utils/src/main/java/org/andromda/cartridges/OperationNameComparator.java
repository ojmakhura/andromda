package org.andromda.cartridges;

import java.text.Collator;
import java.util.Comparator;

import org.andromda.metafacades.uml.ModelElementFacade;

public class OperationNameComparator implements Comparator
{
    private final Collator collator = Collator.getInstance();

    /**
     *
     */
    public OperationNameComparator()
    {
        collator.setStrength(Collator.PRIMARY);
    }

    /**
     * @see java.util.Comparator#compare(Object, Object)
     */
    public int compare(
        Object objectA,
        Object objectB)
    {
        ModelElementFacade a = (ModelElementFacade)objectA;
        ModelElementFacade b = (ModelElementFacade)objectB;

        return collator.compare(
            a.getName(),
            b.getName());
    }
}
