package org.andromda.metafacades.uml;

import java.text.Collator;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;


/**
 * A class containing utilities for metafacade manipulation.
 *
 * @author Chad Brandon
 * @author Wouter Zoons
 * @author Bob Fields
 */
public class MetafacadeUtils
{
    /**
     * Checks to see if the element is the specified type and if so casts it to the object and returns it, otherwise it
     * returns null.
     *
     * @param element the element to check.
     * @param type the Class type.
     * @return the element has the given type or null.
     */
    public static Object getElementAsType(
        final Object element,
        final Class type)
    {
        Object elementAsType = null;
        if (element != null && type != null)
        {
            final Class elementClass = element.getClass();
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
    public static void filterByStereotype(
        final Collection modelElements,
        final String stereotype)
    {
        // Should be able to type the Collection as <ModelElementFacade>, but compilation failure results.
        if (StringUtils.isNotEmpty(stereotype))
        {
            CollectionUtils.filter(
                modelElements,
                new Predicate()
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
    public static void filterByType(
        final Collection modelElements,
        final Class type)
    {
        if (type != null)
        {
            CollectionUtils.filter(
                modelElements,
                new Predicate()
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
     * @param type the type of Class.
     */
    public static void filterByNotType(
        final Collection modelElements,
        final Class type)
    {
        if (type != null)
        {
            CollectionUtils.filter(
                modelElements,
                new Predicate()
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
    public static String toRelationName(
        final String roleName,
        final String targetRoleName,
        final String separator)
    {
        if (roleName.compareTo(targetRoleName) <= 0)
        {
            return (roleName + separator + targetRoleName);
        }
        return (targetRoleName + separator + roleName);
    }

    /**
     * Sorts given metafacades by their fully qualified name.
     *
     * @param metafacades the collection of model elements to sort.
     */
    public static void sortByFullyQualifiedName(final List metafacades)
    {
        Collections.sort(
            metafacades,
            new FullyQualifiedNameComparator());
    }

    /**
     * Used to sort operations by <code>fullyQualifiedName</code>.
     */
    private final static class FullyQualifiedNameComparator
        implements Comparator
    {
        private final Collator collator = Collator.getInstance();

        FullyQualifiedNameComparator()
        {
            collator.setStrength(Collator.PRIMARY);
        }

        public int compare(
            final Object objectA,
            final Object objectB)
        {
            final ModelElementFacade a = (ModelElementFacade)objectA;
            final ModelElementFacade b = (ModelElementFacade)objectB;
            return collator.compare(
                a.getFullyQualifiedName() != null ? a.getFullyQualifiedName() : "",
                b.getFullyQualifiedName() != null ? b.getFullyQualifiedName() : "");
        }
    }

    /**
     * Creates a typed argument list with the given <code>arguments</code>.  If the <code>withArgumentNames</code>
     * flag is true, the argument names are included in the list.
     *
     * @param arguments the arguments from which to create the list.
     * @param withArgumentNames whether or not to include the argument names.
     * @param modifier
     * @return arguments.iterator().getGetterSetterTypeName()
     */
    public static String getTypedArgumentList(
        final Collection<ParameterFacade> arguments,
        final boolean withArgumentNames,
        final String modifier)
    {
        final StringBuffer buffer = new StringBuffer();
        boolean commaNeeded = false;
        for (final Iterator<ParameterFacade> iterator = arguments.iterator(); iterator.hasNext();)
        {
            ParameterFacade parameter = (ParameterFacade)iterator.next();
            String type = null;
            ClassifierFacade classifier = parameter.getType();
            if (classifier != null)
            {
                // Takes multiplicity and templating into account
                type = parameter.getGetterSetterTypeName();
            }

            if (commaNeeded)
            {
                buffer.append(", ");
            }
            if (StringUtils.isNotBlank(modifier))
            {
                buffer.append(modifier);
                buffer.append(" ");
            }
            buffer.append(type);
            if (withArgumentNames)
            {
                buffer.append(" ");
                buffer.append(parameter.getName());
            }
            commaNeeded = true;
        }
        return buffer.toString();
    }

    /**
     * Creates a typed argument list with the given <code>arguments</code>.  If the <code>withArgumentNames</code>
     * flag is true, the argument names are included in the list.
     *
     * @param name 
     * @param arguments the arguments from which to create the list.
     * @param withArgumentNames whether or not to include the argument names.
     * @param argumentModifier
     * @return getTypedArgumentList(arguments, withArgumentNames, argumentModifier)
     */
    public static String getSignature(
        final String name,
        Collection<ParameterFacade> arguments,
        final boolean withArgumentNames,
        final String argumentModifier)
    {
        final StringBuffer signature = new StringBuffer(name);
        signature.append("(");
        signature.append(getTypedArgumentList(
                arguments,
                withArgumentNames,
                argumentModifier));
        signature.append(")");
        return signature.toString();
    }

    private static final String at = "@";
    private static final char period = '.';
    private static final char underscore = '_';
    /**
     * Changes andromda standard tag format Strings to EMF standard format Strings
     * (must be a valid Java identifier). Used for backwards compatibility with UML14 conventions.
     * For example, @andromda.whatever becomes andromda_whatever.
     *
     * @param name 
     * @return getTypedArgumentList(arguments, withArgumentNames, argumentModifier)
     */
    public static String getEmfTaggedValue(String name)
    {
        if (name==null)
        {
            return name;
        }
        if (name.startsWith(at))
        {
            name = name.substring(1);
        }
        name = name.replace(period, underscore);
        return name;
    }

    /**
     * Changes EMF standard tag format Strings to AndroMDA standard format Strings.
     * Used for backwards compatibility with UML14 conventions.
     * For example, andromda_whatever becomes @andromda.whatever.
     *
     * @param name 
     * @return getTypedArgumentList(arguments, withArgumentNames, argumentModifier)
     */
    public static String getUml14TaggedValue(String name)
    {
        if (name==null)
        {
            return name;
        }
        if (!name.startsWith(at))
        {
            name = at+name;
        }
        name = name.replace(underscore, period);
        return name;
    }
}