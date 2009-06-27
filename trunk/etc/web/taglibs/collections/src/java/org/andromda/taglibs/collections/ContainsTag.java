package org.andromda.taglibs.collections;

public class ContainsTag extends javax.servlet.jsp.tagext.BodyTagSupport
{
    private Object item = null;

    public Object getItem()
    {
        return this.item;
    }

    public void setItem(Object item)
    {
        this.item = item;
    }

    private java.util.Collection collection = null;

    public java.util.Collection getCollection()
    {
        return this.collection;
    }

    public void setCollection(java.util.Collection collection)
    {
        this.collection = collection;
    }

    private java.lang.Object[] array = null;

    public Object[] getArray()
    {
        return array;
    }

    public void setArray(Object[] array)
    {
        this.array = array;
    }

    public int doStartTag() throws javax.servlet.jsp.JspException
    {
        return ( (this.array != null && java.util.Arrays.asList(this.array).contains(this.item))
            || (this.collection != null && this.collection.contains(this.item)) ) ? EVAL_BODY_BUFFERED : SKIP_BODY;
    }
}
