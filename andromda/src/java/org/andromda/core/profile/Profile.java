package org.andromda.core.profile;

import java.util.HashMap;
import java.util.Map;

import org.andromda.core.namespace.BaseNamespaceComponent;


/**
 * Represents an AndroMDA profile applied to model.
 * Profiles allow us to extend aspects of a model.
 *
 * @author Chad Brandon
 */
public class Profile
    extends BaseNamespaceComponent
{
    /**
     * Stores the elements for the profile (by name).
     */
    private final Map elements = new HashMap();

    /**
     * Adds a new element to this namespace registry.
     *
     * @param element the element to add to this namespace registry.
     */
    public void addElement(final Element element)
    {
        if (element != null)
        {
            this.elements.put(
                element.getName(),
                element);
        }
    }

    /**
     * Gets the profile value (if one is available)
     * for the given name, otherwise returns null.
     *
     * @param name the profile name to retrieve.
     * @return the value.
     */
    public String getValue(final String name)
    {
        return this.elements.containsKey(name) ? ((Element)this.elements.get(name)).getValue() : null;
    }
}