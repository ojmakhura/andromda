package org.andromda.translation.validation;

import org.apache.commons.collections.bag.HashBag;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Bag;
import org.apache.commons.collections.SetUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.Closure;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.LinkedList;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Random;

public final class OCLCollections
{
    /**
     * Counts the number of occurrences of the argument item in the source collection.
     */
    public static int count(Collection collection, Object item)
    {
        return CollectionUtils.cardinality(item, collection);
    }

    /**
     * Return true if the object is not an element of the collection, false otherwise.
     */
    public static boolean excludes(Collection collection, Object item)
    {
        return !collection.contains(item);
    }

    /**
     * Returns true if all elements of the parameter collection are not present in the current
     * collection, false otherwise.
     */
    public static boolean excludesAll(Collection collection, Collection items)
    {
        for (Iterator iterator = items.iterator(); iterator.hasNext();)
        {
            Object object = iterator.next();
            if (!excludes(collection, object)) return false;
        }
        return true;
    }

    /**
     * Returns true if the object is an element of the collection, false otherwise.
     */
    public static boolean includes(Collection collection, Object item)
    {
        return collection.contains(item);
    }

    /**
     * Returns true if all elements of the parameter collection are
     * present in the current collection, false otherwise.
     */
    public static boolean includesAll(Collection collection, Collection items)
    {
        return collection.containsAll(items);
    }

    /**
     * Returns true if the collection contains no elements, false otherwise.
     */
    public static boolean isEmpty(Collection collection)
    {
        return (collection == null) || (collection.isEmpty());
    }

    /**
     * Returns true if the argument is <code>null</code>, false otherwise.
     */
    public static boolean isEmpty(Object object)
    {
        return object == null;
    }

    /**
     * Returns true if the argument is either <code>null</code> or only contains whitespace characters, false otherwise.
     */
    public static boolean isEmpty(String string)
    {
        return (string == null) || (string.trim().length() == 0);
    }

    /**
     * Returns true if the collection contains one or more elements, false otherwise.
     */
    public static boolean notEmpty(Collection collection)
    {
        return (collection != null) && (!isEmpty(collection));
    }

    /**
     * Returns true if the argument is not <code>null</code>, false otherwise.
     */
    public static boolean notEmpty(Object object)
    {
        return object != null;
    }

    /**
     * Returns true if the argument is neither <code>null</code>
     * nor only contains whitespace characters, false otherwise.
     */
    public static boolean notEmpty(String string)
    {
        return (string != null) && (string.trim().length() > 0);
    }

    /**
     * Returns the number of elements in the collection.
     */
    public static int size(Collection collection)
    {
        return collection.size();
    }

    /**
     * Returns the sum of all the element in the collections. Every element must extend java.lang.Number or
     * this method will throw an exception.
     *
     * @param collection a collection containing only classes extending java.lang.Number
     * @return the sum of all the elements in the collection
     */
    public static double sum(Collection collection)
    {
        double sum = 0;

        for (Iterator iterator = collection.iterator(); iterator.hasNext();)
        {
            Object object = iterator.next();
            if (object instanceof Number)
                sum += ((Number)object).doubleValue();
            else
                throw new UnsupportedOperationException("In order to calculate the sum of a collection\'s elements " +
                        "all of them must extend java.lang.Number, found: "+object.getClass().getName());
        }

        return sum;
    }

    /**
     * Appends the item to the list.
     *
     * @return true if the operation was a success
     */
    public static boolean append(List list, Object item)
    {
        return list.add(item);
    }

    /**
     * Insert the item into the first position of the list.
     *
     * @return the element previously at the first position
     */
    public static Object prepend(List list, Object item)
    {
        return list.set(0, item);
    }

    /**
     * Appends the item to the bag.
     *
     * @return true if the operation was a success
     */
    public static boolean append(Bag collection, Object item)
    {
        return collection.add(item);
    }

    /**
     * Returns the argument as a bag.
     */
    public static Bag asBag(Collection collection)
    {
        return new HashBag(collection);
    }

    /**
     * Returns the argument as an ordered set.
     */
    public static Set asOrderedSet(Collection collection)
    {
        return SetUtils.orderedSet(new TreeSet(collection));
    }

    /**
     * Returns the argument as a list.
     */
    public static List asSequence(Collection collection)
    {
        return new LinkedList(collection);
    }

    /**
     * Returns the argument as a set.
     */
    public static Set asSet(Collection collection)
    {
        return new HashSet(collection);
    }

    /**
     * Returns the element at the specified index in the argument list.
     */
    public static Object at(List list, int index)
    {
        return list.get(index);
    }

    /**
     * Removes all occurrences of the item in the source collection.
     *
     * @return true if one or more elements were removed
     */
    public static boolean excluding(Collection collection, Object item)
    {
        return collection.remove(item);
    }

    /**
     * Adds the item to the list
     *
     * @return true if the element was added
     */
    public static boolean including(Collection collection, Object item)
    {
        return collection.add(item);
    }

    /**
     * Recursively flattens this collection, this method returns a Collection containing no nested Collection
     * instances.
     */
    public static Collection flatten(Collection collection)
    {
        Collection flattenedCollection = new LinkedList();

        for (Iterator iterator = collection.iterator(); iterator.hasNext();)
        {
            Object object = iterator.next();
            if (object instanceof Collection)
                flattenedCollection.addAll(flatten((Collection)object));
            else
                flattenedCollection.add(object);
        }

        return flattenedCollection;
    }

    /**
     * Returns the index in this list of the first occurrence of the specified element, or -1
     * if this list does not contain this element. More formally, returns the lowest index i
     * such that (o==null ? get(i)==null : o.equals(get(i))), or -1 if there is no such index.
     */
    public static int indexOf(List collection, Object item)
    {
        return collection.indexOf(item);
    }

    /**
     * Insert the item at the specified index into the collection.
     */
    public static void insertAt(List collection, int index, Object item)
    {
        collection.add(index, item);
    }

    /**
     * Returns the collection of elements common in both argument collections.
     */
    public static Collection intersection(Collection first, Collection second)
    {
        return CollectionUtils.intersection(first, second);
    }

    /**
     * Returns the union of both collections into a single collection.
     */
    public static Collection union(Collection first, Collection second)
    {
        return CollectionUtils.union(first, second);
    }

    /**
     * Returns the last element in the collection.
     */
    public static Object last(List collection)
    {
        return (collection.isEmpty()) ? null : collection.get(collection.size()-1);
    }

    /**
     * Returns those element that are contained in only one of both collections.
     */ 
    public static Collection symmetricDifference(Collection first, Collection second)
    {
        return CollectionUtils.disjunction(first, second);
    }

    /**
     * @todo: implement
     */
    public static Set subOrderedSet(Set collection)
    {
        throw new UnsupportedOperationException(OCLCollections.class.getName()+".subOrderedSet");
    }

    /**
     * @todo: implement
     */
    public static List subSequence(List collection)
    {
        throw new UnsupportedOperationException(OCLCollections.class.getName()+".subSequence");
    }

    /**
     * Returns a random element from the collection for which the argument expression evaluates true.
     */
    public static Object any(Collection collection, Predicate predicate)
    {
        final List selectedElements = new ArrayList(select(collection, predicate));
        Random random = new Random(System.currentTimeMillis());
        return selectedElements.isEmpty()
            ? null
            : selectedElements.get(random.nextInt(selectedElements.size()));
    }

    /**
     * Returns the collection of Objects that results from executing the transformer on each individual
     * element in the source collection.
     */
    public static Collection collect(Collection collection, Transformer transformer)
    {
        return CollectionUtils.collect(collection, transformer);
    }

    /**
     * @todo: implement
     */
    public static Collection collectNested(Collection collection)
    {
        throw new UnsupportedOperationException(OCLCollections.class.getName()+".collectNested");
    }

    /**
     * Returns true if a predicate is true for at least one element of a collection.
     * <p>
     * A null collection or predicate returns false.
     */
    public static boolean exists(Collection collection, Predicate predicate)
    {
        return CollectionUtils.exists(collection, predicate);
    }

    /**
     * Executes the given closure on each element in the collection.
     * <p>
     * If the input collection or closure is null, there is no change made.
     */
    public static void forAll(Collection collection, Closure closure)
    {
        CollectionUtils.forAllDo(collection, closure);
    }

    /**
     * Returns <code>true</code> if the expression has a unique value for each
     * element in the source collection.
     * @todo: implement
     */
    public static Collection isUnique(Collection collection, OCLExpression expression)
    {
        throw new UnsupportedOperationException(OCLCollections.class.getName()+".isUnique");
    }

    /**
     * @todo: implement
     */
    public static Collection iterate(Collection collection)
    {
        throw new UnsupportedOperationException(OCLCollections.class.getName()+".iterate");
    }

    /**
     * Returns <code>true</true> when the argument expression evaluates true for one and only
     * one element in the collection. Returns <code>false</code> otherwise.
     */
    public static boolean one(Collection collection, Predicate predicate)
    {
        boolean found = false;
        for (Iterator iterator = collection.iterator(); iterator.hasNext();)
        {
            if (predicate.evaluate(iterator.next()))
            {
                if (found) return false; found = true;
            }
        }
        return found;
    }

    /**
     * Returns a subcollection of the source collection containing all elements for which
     * the expression evaluates <code>false</code>.
     */
    public static Collection reject(Collection collection, Predicate predicate)
    {
        return CollectionUtils.selectRejected(collection, predicate);
    }

    /**
     * Returns a subcollection of the source collection containing all elements for which
     * the expression evaluates <code>true</code>.
     */
    public static Collection select(Collection collection, Predicate predicate)
    {
        return CollectionUtils.select(collection, predicate);
    }

    /**
     * @todo: implement
     */
    public static Collection sortedBy(Collection collection)
    {
        throw new UnsupportedOperationException(OCLCollections.class.getName()+".sortedBy");
    }
}
