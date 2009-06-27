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
 */
public abstract class FilteredCollection
    extends ArrayList
    implements Predicate
{
    /**
     * Constructor for the FilterCollection object
     *
     * @param collection
     */
    public FilteredCollection(Collection collection)
    {
        this.addAll(collection);
        CollectionUtils.filter(
            this,
            this);
    }

    /**
     * @see org.apache.commons.collections.Predicate#evaluate(java.lang.Object)
     */
    public abstract boolean evaluate(Object object);
}