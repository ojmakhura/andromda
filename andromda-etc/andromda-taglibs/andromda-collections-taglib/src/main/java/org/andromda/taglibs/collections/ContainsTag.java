package org.andromda.taglibs.collections;

import java.util.Collection;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 *
 */
public class ContainsTag extends BodyTagSupport
{
    private static final long serialVersionUID = 34L;
    private Object item = null;

    /**
     * @return item
     */
    public Object getItem()
    {
        return this.item;
    }

    /**
     * @param item
     */
    public void setItem(Object item)
    {
        this.item = item;
    }

    private Collection collection = null;

    /**
     * @return collection
     */
    public Collection getCollection()
    {
        return this.collection;
    }

    /**
     * @param collection
     */
    public void setCollection(Collection collection)
    {
        this.collection = collection;
    }

    private Object[] array = null;

    /**
     * @return array
     */
    public Object[] getArray()
    {
        return array;
    }

    /**
     * @param array
     */
    public void setArray(Object[] array)
    {
        this.array = array;
    }

    /**
     * @see javax.servlet.jsp.tagext.BodyTagSupport#doStartTag()
     */
    public int doStartTag() throws javax.servlet.jsp.JspException
    {
        return ( (this.array != null && java.util.Arrays.asList(this.array).contains(this.item))
            || (this.collection != null && this.collection.contains(this.item)) ) ? EVAL_BODY_BUFFERED : SKIP_BODY;
    }
}
