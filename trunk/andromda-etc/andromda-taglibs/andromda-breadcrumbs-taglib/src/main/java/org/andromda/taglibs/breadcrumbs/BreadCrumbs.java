package org.andromda.taglibs.breadcrumbs;

import java.util.Collection;
import java.util.LinkedList;

/**
 *
 */
public class BreadCrumbs extends LinkedList
{
    /** org.andromda.bpm4struts.breadcrumbs */
    public final static String SESSION_KEY = "org.andromda.bpm4struts.breadcrumbs";
    /** 6 */
    public final static int DEFAULT_SIZE = 6;

    private int maxSize = -1;

    /**
     * 
     */
    public BreadCrumbs()
    {
        this(DEFAULT_SIZE);
    }

    /**
     * @param maxSize
     */
    public BreadCrumbs(int maxSize)
    {
        this.maxSize = maxSize;
    }

    /**
     * @return maxSize
     */
    public int getMaxSize()
    {
        return maxSize;
    }

    /**
     * @param maxSize
     */
    public void setMaxSize(int maxSize)
    {
        this.maxSize = maxSize;
    }

    /**
     * @param collection
     */
    public BreadCrumbs(Collection collection)
    {
        super(collection);
        maxSize = collection.size();
    }

    private boolean isOverflowed()
    {
        return size() > maxSize;
    }

    private void trimToSize()
    {
        if (isOverflowed() && !isEmpty())
        {
            int difference = size() - maxSize;
            this.removeRange(0, difference);
        }
    }

    /**
     * @see LinkedList#add(int, java.lang.Object)
     */
    public void add(int index, Object element)
    {
        super.add(index, element);
        trimToSize();
    }

    /**
     * @see LinkedList#add(java.lang.Object)
     */
    public boolean add(Object element)
    {
        boolean added = super.add(element);
        trimToSize();
        return added;
    }

    /**
     * @see LinkedList#addFirst(java.lang.Object)
     */
    public void addFirst(Object element)
    {
        super.addFirst(element);
        trimToSize();
    }

    /**
     * @see LinkedList#addLast(java.lang.Object)
     */
    public void addLast(Object element)
    {
        super.addLast(element);
        trimToSize();
    }

    /**
     * @see LinkedList#addAll(int, java.util.Collection)
     */
    public boolean addAll(int index, Collection collection)
    {
        boolean added = super.addAll(index, collection);
        trimToSize();
        return added;
    }

    /**
     * @see LinkedList#addAll(java.util.Collection)
     */
    public boolean addAll(Collection collection)
    {
        boolean added = super.addAll(collection);
        trimToSize();
        return added;
    }
}
