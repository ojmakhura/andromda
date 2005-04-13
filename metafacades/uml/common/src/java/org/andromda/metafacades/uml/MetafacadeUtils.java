package org.andromda.metafacades.uml;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * A class containing utlities for metafacade manipulation.
 *
 * @author Chad Brandon
 * @author Wouter Zoons
 */
public class MetafacadeUtils
{

    /**
     * Checks to see if the element is the specified type and if so casts it to the object and returns it, otherwise it
     * returns null.
     *
     * @param element the element to check.
     * @param type    the Class type.
     * @return java.lang.Object
     */
    public static Object getElementAsType(Object element, Class type)
    {
        Object elementAsType = null;
        if (element != null && type != null)
        {
            Class elementClass = element.getClass();
            if (type.isAssignableFrom(elementClass))
            {
                elementAsType = element;
            }
        }
        return elementAsType;
    }

    /**
     * Filters out the model elements from the <code>modelElements</code> collection that don't have the specified
     * <code>stereotype</code>
     *
     * @param modelElements the model elements to filter.
     * @param stereotype    the stereotype that a model element must have in order to stay remain within the
     *                      <code>modelElements</code> collection.
     */
    public static void filterByStereotype(Collection modelElements, final String stereotype)
    {
        if (StringUtils.isNotEmpty(stereotype))
        {
            CollectionUtils.filter(modelElements, new Predicate()
            {
                public boolean evaluate(Object object)
                {
                    return ((ModelElementFacade)object).hasStereotype(stereotype);
                }
            });
        }
    }

    /**
     * Filters out the model elements from the <code>modelElements</code> collection that are not of (or do not inherit
     * from) the specified type <code>type</code>
     *
     * @param modelElements the model elements to filter.
     * @param type          the type of Class.
     */
    public static void filterByType(Collection modelElements, final Class type)
    {
        if (type != null)
        {
            CollectionUtils.filter(modelElements, new Predicate()
            {
                public boolean evaluate(Object object)
                {
                    return type.isAssignableFrom(object.getClass());
                }
            });
        }
    }

    /**
     * Filters out the model elements from the <code>modelElements</code> collection that are of (or inherit from) the
     * specified type <code>type</code>
     *
     * @param modelElements the model elements to filter.
     * @param type          the type of Class.
     */
    public static void filterByNotType(Collection modelElements, final Class type)
    {
        if (type != null)
        {
            CollectionUtils.filter(modelElements, new Predicate()
            {
                public boolean evaluate(Object object)
                {
                    return !type.isAssignableFrom(object.getClass());
                }
            });
        }
    }

    /**
     * <p/>Returns a consistent name for a relation, independent from the end of the relation one is looking at. </p>
     * <p/>In order to guarantee consistency with relation names, they must appear the same whichever angle (ie entity)
     * that you come from. For example, if you are at Customer end of a relationship to an Address then your relation
     * may appear with the name Customer-Address. But if you are in the Address entity looking at the Customer then you
     * will get an error because the relation will be called Address-Customer. A simple way to guarantee that both ends
     * of the relationship have the same name is merely to use alphabetical ordering. </p>
     *
     * @param roleName       name of role in relation
     * @param targetRoleName name of target role in relation
     * @param separator      character used to separate words
     * @return uniform mapping name (in alphabetical order)
     */
    public static String toRelationName(String roleName, String targetRoleName, String separator)
    {
        if (roleName.compareTo(targetRoleName) <= 0)
        {
            return (roleName + separator + targetRoleName);
        }
        return (targetRoleName + separator + roleName);
    }

    private final static Map uniqueNames = new HashMap();

    /**
     * Registers the argument name and updates it if necessary so it is unique amoung all the registered names so far.
     *
     * @param name the name to register
     * @return the argument, possible transformed in case it was already registered
     */
    public static String createUniqueName(String name)
    {
        if (StringUtils.isBlank(name))
            return name;

        String uniqueName = null;
        if (uniqueNames.containsKey(name))
        {
            int collisionCount = ((Integer)uniqueNames.get(name)).intValue() + 1;
            String suffix = String.valueOf(collisionCount);

            uniqueNames.put(name, new Integer(collisionCount));

            uniqueName = name.substring(0, name.length() - suffix.length()) + suffix;
        }
        else
        {
            uniqueName = name;
        }
        uniqueNames.put(uniqueName, new Integer(0));
        return uniqueName;
    }
}