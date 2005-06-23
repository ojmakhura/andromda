package org.andromda.translation.ocl.validation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.collections.Bag;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.SetUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.bag.HashBag;
import org.apache.commons.lang.StringUtils;


/**
 * Used to translated OCL collection expressions to their corresponding Java collection expressions.
 */
public final class OCLCollections
{
    /**
     * Counts the number of occurrences of the argument item in the source collection.
     */
    public static int count(
        final Collection collection,
        Object item)
    {
        return collection == null ? 0 : CollectionUtils.cardinality(item, collection);
    }

    /**
     * Return true if the object is not an element of the collection, false otherwise.
     */
    public static boolean excludes(
        final Collection collection,
        final Object item)
    {
        return collection == null || !collection.contains(item);
    }

    /**
     * Returns true if all elements of the parameter collection are not present in the current collection, false
     * otherwise.
     */
    public static boolean excludesAll(
        final Collection collection,
        final Collection items)
    {
        boolean excludesAll = true;
        for (final Iterator iterator = items.iterator(); iterator.hasNext();)
        {
            final Object object = iterator.next();
            if (!excludes(collection, object))
            {
                excludesAll = false;
                break;
            }
        }
        return excludesAll;
    }

    /**
     * Returns true if the object is an element of the collection, false otherwise.
     */
    public static boolean includes(
        final Collection collection,
        final Object item)
    {
        return collection != null && collection.contains(item);
    }

    /**
     * Returns true if all elements of the parameter collection are present in the current collection, false otherwise.
     */
    public static boolean includesAll(
        final Collection collection,
        final Collection items)
    {
        return collection != null && collection.containsAll(items);
    }

    /**
     * Returns true if the collection contains no elements, false otherwise.
     */
    public static boolean isEmpty(final Collection collection)
    {
        return (collection == null) || (collection.isEmpty());
    }

    /**
     * Returns true if the argument is <code>null</code>, false otherwise.
     */
    public static boolean isEmpty(final Object object)
    {
        boolean isEmpty = object == null;
        if (!isEmpty)
        {
            if (object instanceof Collection)
            {
                isEmpty = ((Collection)object).isEmpty();
            }
            else if (object instanceof String)
            {
                isEmpty = isEmpty((String)object);
            }
        }
        return isEmpty;
    }

    /**
     * Returns true if the argument is either <code>null</code> or only contains whitespace characters, false
     * otherwise.
     */
    public static boolean isEmpty(final String string)
    {
        return StringUtils.isBlank(string);
    }

    /**
     * Returns true if the collection contains one or more elements, false otherwise.
     */
    public static boolean notEmpty(final Collection collection)
    {
        return (collection != null) && !isEmpty(collection);
    }

    /**
     * Returns true if the argument is not <code>null</code>, false otherwise.
     */
    public static boolean notEmpty(final Object object)
    {
        boolean notEmpty = object != null;
        if (notEmpty)
        {
            if (object instanceof Collection)
            {
                notEmpty = !((Collection)object).isEmpty();
            }
            else if (object instanceof String)
            {
                notEmpty = notEmpty((String)object);
            }
        }
        return notEmpty;
    }

    /**
     * Returns true if the argument is neither <code>null</code> nor only contains whitespace characters, false
     * otherwise.
     */
    public static boolean notEmpty(final String string)
    {
        return StringUtils.isNotBlank(string);
    }

    /**
     * Checks the instance of the <code>object</code> and makes sure its a Collection, if the object is a collection the
     * size is checked and returned, if its NOT a collection, 0 is returned.
     *
     * @param object the object to check.
     * @return the size of the collection
     */
    public static int size(final Object object)
    {
        int size = 0;
        if (object instanceof Collection)
        {
            size = size((Collection)object);
        }
        return size;
    }

    /**
     * Returns the number of elements in the collection.
     */
    public static int size(final Collection collection)
    {
        int size = 0;
        if (collection != null)
        {
            size = collection.size();
        }
        return size;
    }

    /**
     * Returns the sum of all the element in the collections. Every element must extend java.lang.Number or this method
     * will throw an exception.
     *
     * @param collection a collection containing only classes extending java.lang.Number
     * @return the sum of all the elements in the collection
     */
    public static double sum(final Collection collection)
    {
        double sum = 0;
        if (collection != null && !collection.isEmpty())
        {
            for (final Iterator iterator = collection.iterator(); iterator.hasNext();)
            {
                Object object = iterator.next();
                if (object instanceof Number)
                {
                    sum += ((Number)object).doubleValue();
                }
                else
                {
                    throw new UnsupportedOperationException(
                        "In order to calculate the sum of a collection\'s elements " +
                        "all of them must extend java.lang.Number, found: " + object.getClass().getName());
                }
            }
        }
        return sum;
    }

    /**
     * Appends the item to the list.
     *
     * @return true if the operation was a success
     */
    public static boolean append(
        final List list,
        final Object item)
    {
        return list == null ? false : list.add(item);
    }

    /**
     * Insert the item into the first position of the list.
     *
     * @return the element previously at the first position
     */
    public static Object prepend(
        final List list,
        final Object item)
    {
        return list.set(0, item);
    }

    /**
     * Appends the item to the bag.
     *
     * @return true if the operation was a success
     */
    public static boolean append(
        final Bag collection,
        final Object item)
    {
        return collection == null ? false : collection.add(item);
    }

    /**
     * Returns the argument as a bag.
     */
    public static Bag asBag(final Collection collection)
    {
        return collection == null ? new HashBag() : new HashBag(collection);
    }

    /**
     * Returns the argument as an ordered set.
     */
    public static Set asOrderedSet(final Collection collection)
    {
        return collection == null ? Collections.EMPTY_SET : SetUtils.orderedSet(new TreeSet(collection));
    }

    /**
     * Returns the argument as a list.
     */
    public static List asSequence(final Collection collection)
    {
        return collection == null ? Collections.EMPTY_LIST : new ArrayList(collection);
    }

    /**
     * Returns the argument as a set.
     */
    public static Set asSet(final Collection collection)
    {
        return collection == null ? Collections.EMPTY_SET : new HashSet(collection);
    }

    /**
     * Returns the element at the specified index in the argument list.
     */
    public static Object at(
        final List list,
        final int index)
    {
        return list == null ? null : list.get(index);
    }

    /**
     * Removes all occurrences of the item in the source collection.
     *
     * @return true if one or more elements were removed
     */
    public static boolean excluding(
        final Collection collection,
        final Object item)
    {
        return collection == null ? false : collection.remove(item);
    }

    /**
     * Adds the item to the list
     *
     * @return true if the element was added
     */
    public static boolean including(
        final Collection collection,
        final Object item)
    {
        return collection == null ? false : collection.add(item);
    }

    /**
     * Recursively flattens this collection, this method returns a Collection containing no nested Collection
     * instances.
     */
    public static Collection flatten(final Collection collection)
    {
        final Collection flattenedCollection = new ArrayList();
        for (final Iterator iterator = collection.iterator(); iterator.hasNext();)
        {
            final Object object = iterator.next();
            if (object instanceof Collection)
            {
                flattenedCollection.addAll(flatten((Collection)object));
            }
            else
            {
                flattenedCollection.add(object);
            }
        }

        return flattenedCollection;
    }

    /**
     * Returns the index in this list of the first occurrence of the specified element, or -1 if this list does not
     * contain this element. More formally, returns the lowest index i such that (o == null ? get(i) = =null :
     * o.equals(get(i))), or -1 if there is no such index.
     */
    public static int indexOf(
        final List collection,
        final Object item)
    {
        return collection == null ? -1 : collection.indexOf(item);
    }

    /**
     * Insert the item at the specified index into the collection.
     */
    public static void insertAt(
        final List collection,
        int index,
        Object item)
    {
        collection.add(index, item);
    }

    /**
     * Returns the collection of elements common in both argument collections.
     */
    public static Collection intersection(
        final Collection first,
        final Collection second)
    {
        return CollectionUtils.intersection(first, second);
    }

    /**
     * Returns the union of both collections into a single collection.
     */
    public static Collection union(
        final Collection first,
        final Collection second)
    {
        return CollectionUtils.union(first, second);
    }

    /**
     * Returns the last element in the collection.
     *
     * @param object the collection or single instance which will be converted to a collection.
     * @return the last object of the collection or the object itself if the object is not a collection instance (or
     *         null if the object is null or an empty collection).
     */
    public static Object last(final Object object)
    {
        Object last = null;
        final List list = objectToList(object);
        if (!list.isEmpty())
        {
            last = list.get(list.size() - 1);
        }
        return last;
    }

    /**
     * Returns the first element in the collection.
     *
     * @param object the collection or single instance which will be converted to a collection.
     * @return the first object of the collection or the object itself if the object is not a collection instance (or
     *         null if the object is null or an empty collection).
     */
    public static Object first(final Object object)
    {
        Object first = null;
        final List list = objectToList(object);
        if (!list.isEmpty())
        {
            first = list.get(0);
        }
        return first;
    }

    /**
     * Returns those element that are contained in only one of both collections.
     */
    public static Collection symmetricDifference(
        final Collection first,
        final Collection second)
    {
        return CollectionUtils.disjunction(first, second);
    }

    /**
     * @todo: implement
     */
    public static Set subOrderedSet(final Set collection)
    {
        throw new UnsupportedOperationException(OCLCollections.class.getName() + ".subOrderedSet");
    }

    /**
     * @todo: implement
     */
    public static List subSequence(final List collection)
    {
        throw new UnsupportedOperationException(OCLCollections.class.getName() + ".subSequence");
    }

    /**
     * Returns a random element from the collection for which the argument expression evaluates true.
     */
    public static Object any(
        final Collection collection,
        final Predicate predicate)
    {
        final List selectedElements = new ArrayList(select(collection, predicate));
        final Random random = new Random(System.currentTimeMillis());
        return selectedElements.isEmpty() ? null : selectedElements.get(random.nextInt(selectedElements.size()));
    }

    /**
     * Returns the collection of Objects that results from executing the transformer on each individual element in the
     * source collection.
     */
    public static Collection collect(
        final Collection collection,
        final Transformer transformer)
    {
        return CollectionUtils.collect(collection, transformer);
    }

    /**
     * @todo: implement
     */
    public static Collection collectNested(final Collection collection)
    {
        throw new UnsupportedOperationException(OCLCollections.class.getName() + ".collectNested");
    }

    /**
     * Returns true if a predicate is true for at least one element of a collection. <p/>A null collection or predicate
     * returns false.
     */
    public static boolean exists(
        final Collection collection,
        final Predicate predicate)
    {
        return CollectionUtils.exists(collection, predicate);
    }

    /**
     * <p/>
     * Executes every <code>predicate</code> for the given collectoin, if one evaluates to <code>false</code> this
     * operation returns <code>false</code>, otherwise <code>true</code> is returned. </p> If the input collection or
     * closure is null <code>false</code> is returned.
     *
     * @return true if every evaluated predicate returns true, false otherwise.
     */
    public static boolean forAll(
        final Collection collection,
        final Predicate predicate)
    {
        boolean valid = collection != null;
        if (valid)
        {
            for (final Iterator iterator = collection.iterator(); iterator.hasNext();)
            {
                final Object object = iterator.next();
                valid = predicate.evaluate(object);
                if (!valid)
                {
                    break;
                }
            }
        }
        return valid;
    }

    /**
     * <p/>
     * Executes every <code>predicate</code> for the given collection, if one evaluates to <code>false</code> this
     * operation returns <code>false</code>, otherwise <code>true</code> is returned. </p> If the input collection or
     * closure is null <code>false</code> is returned.
     *
     * @return true if every evaluated predicate returns true, false otherwise.
     */
    public static boolean forAll(
        final Object collection,
        final Predicate predicate)
    {
        boolean valid = false;
        if (collection instanceof Collection)
        {
            valid = forAll((Collection)collection, predicate);
        }
        return valid;
    }

    /**
     * Returns <code>true</code> if the result of executing the <code>transformer</code> has a unique value for each
     * element in the source collection.
     */
    public static boolean isUnique(
        final Collection collection,
        final Transformer transformer)
    {
        boolean unique = true;
        final Set collected = new HashSet();
        for (Iterator iterator = collection.iterator(); iterator.hasNext() && unique;)
        {
            final Object result = transformer.transform(iterator.next());
            if (collected.contains(result))
            {
                unique = false;
            }
            else
            {
                collected.add(result);
            }
        }
        return unique;
    }

    /**
     * Returns <code>true</code> if the result of executing the <code>transformer</code> has a unique value for each
     * element in the source collection.
     */
    public static boolean isUnique(
        final Object collection,
        final Transformer transformer)
    {
        boolean unique = collection != null;
        if (unique && Collection.class.isAssignableFrom(collection.getClass()))
        {
            unique = isUnique((Collection)collection, transformer);
        }
        return unique;
    }

    /**
     * @todo: implement
     */
    public static Collection iterate(final Collection collection)
    {
        throw new UnsupportedOperationException(OCLCollections.class.getName() + ".iterate");
    }

    /**
     * Returns <code>true</true> when the argument expression evaluates true for one and only one element in the
     * collection. Returns <code>false</code> otherwise.
     */
    public static boolean one(
        final Collection collection,
        final Predicate predicate)
    {
        boolean found = false;

        if (collection != null)
        {
            for (final Iterator iterator = collection.iterator(); iterator.hasNext();)
            {
                if (predicate.evaluate(iterator.next()))
                {
                    if (found)
                    {
                        found = false;
                        break;
                    }
                    found = true;
                }
            }
        }
        return found;
    }

    /**
     * <p/>
     * Returns <code>true</true> if <code>collection</code> is actually a Collection instance and if the
     * <code>predicate</code> expression evaluates true for one and only one element in the collection. Returns
     * <code>false</code> otherwise. </p>
     */
    public static boolean one(
        final Object collection,
        final Predicate predicate)
    {
        return collection != null && Collection.class.isAssignableFrom(collection.getClass()) &&
        one((Collection)collection, predicate);
    }

    /**
     * Returns a subcollection of the source collection containing all elements for which the expression evaluates
     * <code>false</code>.
     */
    public static Collection reject(
        final Collection collection,
        final Predicate predicate)
    {
        return CollectionUtils.selectRejected(collection, predicate);
    }

    /**
     * Returns a subcollection of the source collection containing all elements for which the expression evaluates
     * <code>true</code>.
     */
    public static Collection select(
        final Collection collection,
        final Predicate predicate)
    {
        return CollectionUtils.select(collection, predicate);
    }

    /**
     * Returns a subcollection of the source collection containing all elements for which the expression evaluates
     * <code>true</code>.
     */
    public static Collection select(
        final Object collection,
        final Predicate predicate)
    {
        return CollectionUtils.select((Collection)collection, predicate);
    }

    /**
     * @todo: implement
     */
    public static Collection sortedBy(final Collection collection)
    {
        throw new UnsupportedOperationException(OCLCollections.class.getName() + ".sortedBy");
    }

    /**
     * Converts the given object to a java.util.List implementation. If the object is not a collection type, then the
     * object is placed within a collection as the only element.
     *
     * @param object the object to convert.
     * @return the new List.
     */
    private static List objectToList(Object object)
    {
        List list = null;
        if (object instanceof Collection)
        {
            final Collection collection = (Collection)object;
            if (!(object instanceof List))
            {
                object = new ArrayList(collection);
            }
            list = (List)object;
        }
        else
        {
            list = new ArrayList();
            if (object != null)
            {
                list.add(object);
            }
        }
        return list;
    }
}