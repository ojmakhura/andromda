package org.andromda.taglibs.breadcrumbs;

import java.io.Serializable;

/**
 *
 */
public class BreadCrumb implements Serializable
{
    private static final long serialVersionUID = 34L;
    private String url = null;
    private String value = null;

    /**
     * @param urlIn
     * @param valueIn
     */
    public BreadCrumb(String urlIn, String valueIn)
    {
        this.url = urlIn;
        this.value = valueIn;
    }

    /**
     * @return url
     */
    public String getUrl()
    {
        return url;
    }

    /**
     * @return value
     */
    public String getValue()
    {
        return value;
    }

    /**
     * @param url
     */
    public void setUrl(String url)
    {
        if (url == null)
            throw new IllegalArgumentException("Error constructing breadcrumb: Breadcrumb URL cannot be null");
        this.url = url;
    }

    /**
     * @param value
     */
    public void setValue(String value)
    {
        if (value == null)
            throw new IllegalArgumentException("Error constructing breadcrumb: Breadcrumb value cannot be null");
        this.value = value;
    }

    /**
     * @param breadCrumb
     * @return true/false
     */
    public boolean hasSimilarProperties(BreadCrumb breadCrumb)
    {
        return (breadCrumb == null)
                ? false
                : url.equals(breadCrumb.getUrl()) || value.equals(breadCrumb.getValue());
    }
}
