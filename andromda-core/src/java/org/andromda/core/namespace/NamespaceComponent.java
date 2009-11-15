package org.andromda.core.namespace;

import java.net.URL;


/**
 * This represents a component that can be discovered/registered within a namespace.
 *
 * @author Chad Brandon
 */
public interface NamespaceComponent
{
    /**
     * Sets the namespace to which this component belongs.
     *
     * @param namespace the name of the namespace to which this component belongs.
     */
    public void setNamespace(String namespace);

    /**
     * Gets the namespace to which this component belongs.
     *
     * @return the name of the namespace.
     */
    public String getNamespace();

    /**
     * The entire path to the resource this namespace component instance
     * is configured from.
     *
     * @return URL the path to the resource from which this namespace
     *         component was configured.
     */
    public URL getResource();

    /**
     * Sets the path of the resource from which this namespace component is configured.
     *
     * @param resource the resource URL.
     */
    public void setResource(URL resource);
}