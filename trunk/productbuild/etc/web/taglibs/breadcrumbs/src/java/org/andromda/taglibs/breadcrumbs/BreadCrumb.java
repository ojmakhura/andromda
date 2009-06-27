package org.andromda.taglibs.breadcrumbs;

import java.io.Serializable;

public class BreadCrumb implements Serializable
{
    private String url = null;
    private String value = null;

    public BreadCrumb(String url, String value)
    {
        setUrl(url);
        setValue(value);
    }

    public String getUrl()
    {
        return url;
    }

    public String getValue()
    {
        return value;
    }

    public void setUrl(String url)
    {
        if (url == null)
            throw new IllegalArgumentException("Error constructing breadcrumb: Breadcrumb URL cannot be null");
        this.url = url;
    }

    public void setValue(String value)
    {
        if (value == null)
            throw new IllegalArgumentException("Error constructing breadcrumb: Breadcrumb value cannot be null");
        this.value = value;
    }

    public boolean hasSimilarProperties(BreadCrumb breadCrumb)
    {
        return (breadCrumb == null)
                ? false
                : url.equals(breadCrumb.getUrl()) || value.equals(breadCrumb.getValue());
    }
}
