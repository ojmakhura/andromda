package org.andromda.translation.validation;

import org.apache.commons.collections.Bag;
import org.apache.commons.collections.SetUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.bag.HashBag;

import java.util.*;

public final class OCLCollections
{
    public static int count(Collection collection, Object item)
    {
        return CollectionUtils.cardinality(item, collection);
    }

    public static boolean excludes(Collection collection, Object item)
    {
        return !collection.contains(item);
    }

    public static boolean excludesAll(Collection collection, Collection items)
    {
        for (Iterator iterator = items.iterator(); iterator.hasNext();)
        {
            Object object = iterator.next();
            if (!excludes(collection, object)) return false;
        }
        return true;
    }

    public static boolean includes(Collection collection, Object item)
    {
        return collection.contains(item);
    }

    public static boolean includesAll(Collection collection, Collection items)
    {
        return collection.containsAll(items);
    }

    public static boolean isEmpty(Collection collection)
    {
        return collection.isEmpty();
    }

    public static boolean notEmpty(Collection collection)
    {
        return !isEmpty(collection);
    }

    public static int size(Collection collection)
    {
        return collection.size();
    }

    public static double sum(Collection collection)
    {
        double sum = 0;

        for (Iterator iterator = collection.iterator(); iterator.hasNext();)
        {
            Object object = (Object) iterator.next();
            if (object instanceof Number)
                sum += ((Number)object).doubleValue();
            else
                throw new UnsupportedOperationException("In order to calculate the sum of a collection\'s elements " +
                        "all of them must extend java.lang.Number, found: "+object.getClass().getName());
        }

        return sum;
    }

    public static boolean append(List collection, Object item)
    {
        return collection.add(item);
    }

    public static Object prepend(List collection, Object item)
    {
        return collection.set(0, item);
    }

    public static boolean append(Bag collection, Object item)
    {
        return collection.add(item);
    }

    public static Bag asBag(Collection collection)
    {
        return new HashBag(collection);
    }

    public static Set asOrderedSet(Collection collection)
    {
        return SetUtils.orderedSet(new TreeSet(collection));
    }

    public static List asSequence(Collection collection)
    {
        return new LinkedList(collection);
    }

    public static Set asSet(Collection collection)
    {
        return new HashSet(collection);
    }

    public static Object at(List collection, int index)
    {
        return collection.get(index);
    }

    public static boolean excluding(Collection collection, Object item)
    {
        return collection.remove(item);
    }

    public static boolean including(Collection collection, Object item)
    {
        return collection.add(item);
    }

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

    public static int indexOf(List collection, Object item)
    {
        return collection.indexOf(item);
    }

    public static Object insertAt(List collection, int index, Object item)
    {
        return collection.set(index, item);
    }

    public static Collection intersection(Collection first, Collection second)
    {
        return CollectionUtils.intersection(first, second);
    }

    public static Collection union(Collection first, Collection second)
    {
        return CollectionUtils.union(first, second);
    }

    public static Object last(List collection)
    {
        return (collection.isEmpty()) ? null : collection.get(collection.size()-1);
    }

    public static Collection symmetricDifference(Collection first, Collection second)
    {
        return CollectionUtils.disjunction(first, second);
    }

    /**
     * todo: implement
     */
    public static Set subOrderedSet(Set collection)
    {
        throw new UnsupportedOperationException(OCLCollections.class.getName()+".subOrderedSet");
    }

    /**
     * todo: implement
     */
    public static List subSequence(List collection)
    {
        throw new UnsupportedOperationException(OCLCollections.class.getName()+".subSequence");
    }

    /**
     * todo: implement
     */
    public static Collection any(Collection collection)
    {
        throw new UnsupportedOperationException(OCLCollections.class.getName()+".any");
    }

    /**
     * todo: implement
     */
    public static Collection collect(Collection collection)
    {
        throw new UnsupportedOperationException(OCLCollections.class.getName()+".collect");
    }

    /**
     * todo: implement
     */
    public static Collection collectNested(Collection collection)
    {
        throw new UnsupportedOperationException(OCLCollections.class.getName()+".collectNested");
    }

    /**
     * todo: implement
     */
    public static Collection exists(Collection collection)
    {
        throw new UnsupportedOperationException(OCLCollections.class.getName()+".exists");
    }

    /**
     * todo: implement
     */
    public static Collection forAll(Collection collection)
    {
        throw new UnsupportedOperationException(OCLCollections.class.getName()+".forAll");
    }

    /**
     * todo: implement
     */
    public static Collection isUnique(Collection collection)
    {
        throw new UnsupportedOperationException(OCLCollections.class.getName()+".isUnique");
    }

    /**
     * todo: implement
     */
    public static Collection iterate(Collection collection)
    {
        throw new UnsupportedOperationException(OCLCollections.class.getName()+".iterate");
    }

    /**
     * todo: implement
     */
    public static Collection one(Collection collection)
    {
        throw new UnsupportedOperationException(OCLCollections.class.getName()+".one");
    }

    /**
     * todo: implement
     */
    public static Collection reject(Collection collection)
    {
        throw new UnsupportedOperationException(OCLCollections.class.getName()+".reject");
    }

    /**
     * todo: implement
     */
    public static Collection select(Collection collection)
    {
        throw new UnsupportedOperationException(OCLCollections.class.getName()+".select");
    }

    /**
     * todo: implement
     */
    public static Collection sortedBy(Collection collection)
    {
        throw new UnsupportedOperationException(OCLCollections.class.getName()+".sortedBy");
    }

}
