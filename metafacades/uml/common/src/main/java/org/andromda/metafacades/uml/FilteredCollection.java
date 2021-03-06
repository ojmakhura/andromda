package org.andromda.metafacades.uml;

import java.util.ArrayList;
import java.util.Collection;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

/**
 * Filters a collection of objects so that the collection contains only those
 * objects that pass the <code>evaluate</code> test. <p/> It is useful for
 * filtering the results of a query.
 * </p>
 *
 * @author Anthony Mowers
 * @author Chad Brandon
 * @param <T> Type within the Collection
 */
public abstract class FilteredCollection<T>
    extends ArrayList<T>
    implements Predicate
{
    /**
     * Constructor for the FilterCollection object
     *
     * @param collection
     */
    public FilteredCollection(Collection<T> collection)
    {
        this.addAll(collection);
        CollectionUtils.filter(
            this,
            this);
    }

    /**
     * @see org.apache.commons.collections.Predicate#evaluate(Object)
     */
    public abstract boolean evaluate(Object object);
}