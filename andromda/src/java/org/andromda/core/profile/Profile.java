package org.andromda.core.profile;

import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.andromda.core.common.ComponentContainer;
import org.andromda.core.common.XmlObjectFactory;
import org.andromda.core.configuration.NamespaceProperties;
import org.andromda.core.configuration.Namespaces;
import org.andromda.core.configuration.Property;
import org.andromda.core.namespace.BaseNamespaceComponent;


/**
 * Represents an AndroMDA profile applied to a model.
 * Profiles allow us to extend aspects of a model.
 *
 * @author Chad Brandon
 */
public class Profile
    extends BaseNamespaceComponent
{
    /**
     * The shared instance of this class.
     */
    private static Profile instance;

    /**
     * Gets the shared instance of this class.
     *
     * @return the shared instance.
     */
    public static Profile instance()
    {
        if (instance == null)
        {
            instance = new Profile();
        }
        return instance;
    }

    /**
     * Stores the elements for the profile (by name).
     */
    private final Map elements = new HashMap();

    /**
     * Adds a new element to this namespace registry.
     */
    public void addElement(
        final String name,
        final String value)
    {
        this.elements.put(
            name,
            value);
    }

    /**
     * Gets the profile value (if one is available)
     * for the given name, otherwise returns name.
     *
     * @param name the profile name to retrieve.
     * @return the value.
     */
    public String get(final String name)
    {
        // - attempt to get the profile value from the profile defined
        //   by the profile mappings uri first
        String value = (String)this.elements.get(name);

        // - if we can't get any profile value from an the override profile
        //   mapping, then we resort to the ones defined in the namespace
        if (value == null || value.trim().length() == 0)
        {
            Map namespaceElements = this.getNamespaceElements(this.getNamespace());
            if (namespaceElements != null)
            {
                value = (String)namespaceElements.get(name);
            }
            if (value == null)
            {
                namespaceElements = this.getNamespaceElements(Namespaces.DEFAULT);
                if (namespaceElements != null)
                {
                    value = (String)namespaceElements.get(name);
                }
            }
        }
        return value != null ? value : name;
    }

    /**
     * Initializes this profile instance.
     */
    public void initialize()
    {
        final Collection profiles = ComponentContainer.instance().findComponentsOfType(Profile.class);
        for (final Iterator iterator = profiles.iterator(); iterator.hasNext();)
        {
            final Profile profile = (Profile)iterator.next();
            this.addElements(profile);
        }
    }

    /**
     * Refreshes the profile instance.
     */
    public void refresh()
    {
        // - clear out the instance's elements
        this.elements.clear();
        try
        {
            final Property mappingsUri =
                Namespaces.instance().getProperty(
                    Namespaces.DEFAULT,
                    NamespaceProperties.PROFILE_MAPPINGS_URI,
                    false);
            final String mappingsUriValue = mappingsUri != null ? mappingsUri.getValue() : null;
            if (mappingsUriValue != null)
            {
                final XmlObjectFactory factory = XmlObjectFactory.getInstance(Profile.class);
                final Profile profile = (Profile)factory.getObject(new URL(mappingsUriValue.trim()));
                this.elements.putAll(profile.elements);
            }
        }
        catch (final Throwable throwable)
        {
            throw new ProfileException(throwable);
        }
    }

    /**
     * Stores all elements.
     */
    private final Map allElements = new HashMap();

    /**
     * Adds the elements to the interal elements map.
     */
    private void addElements(final Profile profile)
    {
        final Collection elements = profile != null ? profile.elements.keySet() : null;
        if (elements != null)
        {
            final String namespace = profile.getNamespace();
            final Map namespaceElements = this.getNamespaceElements(namespace);
            for (final Iterator iterator = elements.iterator(); iterator.hasNext();)
            {
                final String name = (String)iterator.next();
                namespaceElements.put(
                    name,
                    profile.elements.get(name));
            }
        }
    }

    /**
     * Adds a namespace element for the given namespace with the given name and
     * value.
     * @param namespace the namespace for which to add the namespace element.
     * @param name the element name.
     * @param value the element value.
     */
    public void addElement(
        final String namespace,
        final String name,
        final String value)
    {
        final Map namespaceElements = this.getNamespaceElements(namespace);
        namespaceElements.put(
            name,
            value);
    }

    /**
     * Retrieves the namespace elements map for the given namespace.
     * If one doesn't exist, then a new one is created.
     * @param namespace the namespace for which to retrieve the namespace elements
     * @return the namespace element map
     */
    private Map getNamespaceElements(final String namespace)
    {
        Map namespaceElements = (Map)this.allElements.get(namespace);
        if (namespaceElements == null)
        {
            namespaceElements = new HashMap();
            this.allElements.put(
                namespace,
                namespaceElements);
        }
        return namespaceElements;
    }

    /**
     * Shuts down the shared instance and releases any used resources.
     */
    public void shutdown()
    {
        instance = null;
    }
}