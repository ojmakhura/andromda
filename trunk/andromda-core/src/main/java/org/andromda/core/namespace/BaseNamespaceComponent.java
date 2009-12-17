package org.andromda.core.namespace;

import java.net.URL;


/**
 * Represents the base plugin of AndroMDA. All Plugin instances inherit from this class.
 *
 * @author Chad Brandon
 */
public abstract class BaseNamespaceComponent
    implements NamespaceComponent
{
    /**
     * The namespace in which this component resides.
     */
    private String namespace;

    /**
     * @see org.andromda.core.namespace.NamespaceComponent#setNamespace(java.lang.String)
     */
    public void setNamespace(final String namespace)
    {
        this.namespace = namespace;
    }

    /**
     * @see org.andromda.core.namespace.NamespaceComponent#getNamespace()
     */
    public String getNamespace()
    {
        return this.namespace;
    }

    /**
     * The URL to the resource that configured this instance.
     */
    private URL resource;

    /**
     * @see org.andromda.core.namespace.NamespaceComponent#getResource()
     */
    public URL getResource()
    {
        return this.resource;
    }

    /**
     * @see org.andromda.core.namespace.NamespaceComponent#setResource(java.net.URL)
     */
    public void setResource(final URL resource)
    {
        this.resource = resource;
    }
}