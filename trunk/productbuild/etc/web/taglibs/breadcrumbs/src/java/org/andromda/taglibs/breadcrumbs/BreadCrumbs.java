package org.andromda.taglibs.breadcrumbs;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;

public class BreadCrumbs extends LinkedList implements Serializable
{
    public final static String SESSION_KEY = "org.andromda.bpm4struts.breadcrumbs";
    public final static int DEFAULT_SIZE = 6;

    private int maxSize = -1;

    public BreadCrumbs()
    {
        this(DEFAULT_SIZE);
    }

    public BreadCrumbs(int maxSize)
    {
        this.maxSize = maxSize;
    }

    public int getMaxSize()
    {
        return maxSize;
    }

    public void setMaxSize(int maxSize)
    {
        this.maxSize = maxSize;
    }

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

    public void add(int index, Object element)
    {
        super.add(index, element);
        trimToSize();
    }

    public boolean add(Object element)
    {
        boolean added = super.add(element);
        trimToSize();
        return added;
    }

    public void addFirst(Object element)
    {
        super.addFirst(element);
        trimToSize();
    }

    public void addLast(Object element)
    {
        super.addLast(element);
        trimToSize();
    }

    public boolean addAll(int index, Collection collection)
    {
        boolean added = super.addAll(index, collection);
        trimToSize();
        return added;
    }

    public boolean addAll(Collection collection)
    {
        boolean added = super.addAll(collection);
        trimToSize();
        return added;
    }
}
