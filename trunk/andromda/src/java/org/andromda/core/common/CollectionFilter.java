package org.andromda.core.common;

/**
 * A simple interface designed to decide what objects to filter out of
 * a collection.
 */
public interface CollectionFilter
{
    /**
     * Returns <code>true</code> if the argument belongs in the collection after the
     * filtering process has terminated, <code>false</code> otherwise.
     *
     * @param object the object to test
     * @return <code>true</code> if the argument belongs in the collection after the
     *    filtering process has terminated, <code>false</code> otherwise.
     */
    public boolean accept(Object object);
}

