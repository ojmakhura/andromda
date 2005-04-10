package org.andromda.metafacades.uml;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;

/**
 * Contains utilities that are common to the UML metafacades.
 *
 * @author Chad Brandon
 */
public class UMLMetafacadeUtils
{
    /**
     * Returns true or false depending on whether or not this Classifier or any of its specializations is of the given
     * type having the specified <code>typeName</code>
     *
     * @param typeName the name of the type (i.e. datatype::Collection)
     * @return true/false
     */
    public static boolean isType(ClassifierFacade classifier, String typeName)
    {
        boolean isType = false;
        if (classifier != null && typeName != null)
        {
            final String type = StringUtils.trimToEmpty(typeName);
            String name = StringUtils.trimToEmpty(classifier.getFullyQualifiedName(true));
            isType = name.equals(type);
            // if this isn't a type defined by typeName, see if we can find any
            // types that inherit from the type.
            if (!isType)
            {
                isType = CollectionUtils.find(classifier.getAllGeneralizations(), new Predicate()
                {
                    public boolean evaluate(Object object)
                    {
                        String name = StringUtils.trimToEmpty(((ModelElementFacade) object).getFullyQualifiedName(true));
                        return name.equals(type);
                    }
                }) != null;
            }
        }
        return isType;
    }
}
