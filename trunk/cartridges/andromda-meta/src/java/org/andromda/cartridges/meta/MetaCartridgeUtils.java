package org.andromda.cartridges.meta;

import org.andromda.metafacades.uml.ModelElementFacade;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Contains utilities for the AndroMDA meta cartridge.
 *
 * @author Chad Brandon
 */
public class MetaCartridgeUtils
{

    /**
     * Sorts model elements by their fully qualified name.
     *
     * @param modelElements the collection of model elements to sort.
     * @return the sorted collection.
     */
    public Collection sortByFullyQualifiedName(Collection modelElements)
    {
        List sortedElements = null;
        if (modelElements != null)
        {
            sortedElements = new ArrayList(modelElements);
            Collections.sort(sortedElements, new FullyQualifiedNameComparator());
        }
        return sortedElements;
    }

    /**
     * Used to sort operations by <code>fullyQualifiedName</code>.
     */
    private final static class FullyQualifiedNameComparator
            implements Comparator
    {
        private final Collator collator = Collator.getInstance();

        private FullyQualifiedNameComparator()
        {
            collator.setStrength(Collator.PRIMARY);
        }

        public int compare(Object objectA, Object objectB)
        {
            ModelElementFacade a = (ModelElementFacade)objectA;
            ModelElementFacade b = (ModelElementFacade)objectB;

            return collator.compare(a.getFullyQualifiedName(), b.getFullyQualifiedName());
        }
    }
}