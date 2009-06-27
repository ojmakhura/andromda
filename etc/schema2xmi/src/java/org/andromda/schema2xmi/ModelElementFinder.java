package org.andromda.schema2xmi;

import java.util.Collection;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.modelmanagement.Model;
import org.omg.uml.modelmanagement.UmlPackage;


/**
 * Finds model elements by their names.
 *
 * @author Chad Brandon
 */
public class ModelElementFinder
{
    /**
     * Finds the model element having the <code>fullyQualifiedName</code> in
     * the <code>model</code>, returns <code>null</code> if not found.
     *
     * @param model The model to search
     * @param fullyQualifiedName the fully qualified name to find.
     * @return the found model element.
     */
    public static Object find(
        Model model,
        String fullyQualifiedName)
    {
        Object modelElement = null;
        if (model != null)
        {
            String[] names = fullyQualifiedName.split(Schema2XMIGlobals.PACKAGE_SEPERATOR);
            if (names != null && names.length > 0)
            {
                Object element = model;
                for (int ctr = 0; ctr < names.length && element != null; ctr++)
                {
                    String name = names[ctr];
                    if (UmlPackage.class.isAssignableFrom(element.getClass()))
                    {
                        element = getElement(
                                ((UmlPackage)element).getOwnedElement(),
                                name);
                    }
                    modelElement = element;
                }
            }
        }
        return modelElement;
    }

    /**
     * Finds and returns the first model element having the given
     * <code>name</code> in the <code>modelPackage</code>, returns
     * <code>null</code> if not found.
     *
     * @param modelPackage The modelPackage to search
     * @param name the name to find.
     * @return the found model element.
     */
    public static Object find(
        org.omg.uml.UmlPackage modelPackage,
        final String name)
    {
        return CollectionUtils.find(
            modelPackage.getCore().getModelElement().refAllOfType(),
            new Predicate()
            {
                public boolean evaluate(Object object)
                {
                    return StringUtils.trimToEmpty(((ModelElement)object).getName()).equals(name);
                }
            });
    }

    /**
     * Finds and returns the first model element having the given
     * <code>name</code> in the <code>umlPackage</code>, returns
     * <code>null</code> if not found.
     *
     * @param umlPackage The modelPackage to search
     * @param name the name to find.
     * @return the found model element.
     */
    public static Object find(
        org.omg.uml.modelmanagement.UmlPackage umlPackage,
        final String name)
    {
        return CollectionUtils.find(
            umlPackage.getOwnedElement(),
            new Predicate()
            {
                public boolean evaluate(Object object)
                {
                    return StringUtils.trimToEmpty(((ModelElement)object).getName()).equals(name);
                }
            });
    }

    /**
     * Finds the model element having the <code>name</code> contained within
     * the <code>elements</code>, returns null if it can't be found.
     *
     * @param elements the collection of model elements to search
     * @param name the name of the model element.
     * @return the found model element or null if not found.
     */
    private static Object getElement(
        Collection elements,
        final String name)
    {
        return CollectionUtils.find(
            elements,
            new Predicate()
            {
                public boolean evaluate(Object object)
                {
                    return StringUtils.trimToEmpty(((ModelElement)object).getName()).equals(name);
                }
            });
    }
}