package org.andromda.metafacades.uml14;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

/**
 * Filters a collection of objects so that the collection
 * contains only those objects that pass the 'accept' test.
 *
 * <p>It is useful for filtering the results of a query.</p>
 *
 *@author    Anthony Mowers
 */
public abstract class FilteredCollection extends Vector
{
    /**
     *  Constructor for the FilterCollection object
     *
     *@param  c  Description of the Parameter
     */
    public FilteredCollection(Collection c)
    {
        for (Iterator i = c.iterator(); i.hasNext();)
        {
            Object object = i.next();
            if (accept(object))
            {
                add(object);
            }
        }
    }

    /**
     * Evaluates if an element should be added to a
     * filtered collection.
     *
     *@param  object  the element
     *@return         boolean true, if element should be added
     */
    protected abstract boolean accept(Object object);
}
