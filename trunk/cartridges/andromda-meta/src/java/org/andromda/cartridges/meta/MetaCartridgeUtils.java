package org.andromda.cartridges.meta;

import java.text.Collator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.andromda.core.metafacade.MetafacadeConstants;
import org.andromda.metafacades.uml.ConstraintFacade;
import org.andromda.metafacades.uml.ModelElementFacade;


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
    public static Collection sortByFullyQualifiedName(Collection modelElements)
    {
        List sortedElements = null;
        if (modelElements != null)
        {
            sortedElements = new ArrayList(modelElements);
            Collections.sort(
                sortedElements,
                new FullyQualifiedNameComparator());
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

        public int compare(
            final Object objectA,
            final Object objectB)
        {
            ModelElementFacade a = (ModelElementFacade)objectA;
            ModelElementFacade b = (ModelElementFacade)objectB;

            return collator.compare(
                a.getFullyQualifiedName(),
                b.getFullyQualifiedName());
        }
    }

    /**
     * Retrieves the fully qualified constraint name given the constraint (this includes the
     * full name of the context element and the constraint to which it applies).
     * 
     * @param constraint the constraint of which to retrieve the name.
     * @return the fully qualified name.
     */
    public static String getFullyQualifiedConstraintName(final ConstraintFacade constraint)
    {
        final StringBuffer name = new StringBuffer();
        if (constraint != null)
        {
            final ModelElementFacade contextElement = constraint.getContextElement();
            final String contextElementName =
                contextElement != null ? contextElement.getFullyQualifiedName(true) : null;
            if (contextElementName != null && contextElementName.trim().length() > 0)
            {
                name.append(contextElementName.trim());
                name.append(MetafacadeConstants.NAMESPACE_SCOPE_OPERATOR);
            }
            name.append(constraint.getName());
        }
        return name.toString();
    }
}