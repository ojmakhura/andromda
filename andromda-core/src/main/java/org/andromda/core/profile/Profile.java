package org.andromda.core.profile;

import java.io.Serializable;
import java.net.URL;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import org.andromda.core.common.ComponentContainer;
import org.andromda.core.common.XmlObjectFactory;
import org.andromda.core.configuration.NamespaceProperties;
import org.andromda.core.configuration.Namespaces;
import org.andromda.core.configuration.Property;
import org.andromda.core.namespace.BaseNamespaceComponent;
import org.apache.commons.lang.StringUtils;


/**
 * Represents an AndroMDA profile applied to a model.
 * Profiles allow us to extend aspects of a model.
 *
 * @author Chad Brandon
 * @author Bob Fields
 */
public class Profile
    extends BaseNamespaceComponent
    implements Serializable
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
    private final Map<String, String> elements = new LinkedHashMap<String, String>();

    /**
     * Adds a new element to this namespace registry.
     * @param name 
     * @param value 
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
    public String get(String name)
    {
        name = StringUtils.trim(name);
        // - attempt to get the profile value from the profile defined
        //   by the profile mappings uri first
        String value = this.elements.get(name);

        // - if we can't get any profile value from an the override profile
        //   mapping, then we resort to the ones defined in the namespace
        if (StringUtils.isBlank(value))
        {
            Map<String, String> namespaceElements = this.getNamespaceElements(this.getNamespace());
            if (namespaceElements != null)
            {
                value = namespaceElements.get(name);
            }
            if (value == null)
            {
                namespaceElements = this.getNamespaceElements(Namespaces.DEFAULT);
                if (namespaceElements != null)
                {
                    value = namespaceElements.get(name);
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
        final Collection<Profile> profiles = ComponentContainer.instance().findComponentsOfType(Profile.class);
        for (final Profile profile : profiles)
        {
            String namespace = profile.getNamespace();
            if (Namespaces.instance().isShared(namespace))
            {
                profile.setNamespace(Namespaces.DEFAULT);
            }
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
    private final Map<String, Map<String, String>> allElements = new LinkedHashMap<String, Map<String, String>>();

    /**
     * Adds the elements to the internal elements map.
     * @param profile Profile
     */
    private void addElements(final Profile profile)
    {
        final Collection<String> elements = profile != null ? profile.elements.keySet() : null;
        if (elements != null && profile != null)
        {
            final String namespace = profile.getNamespace();
            final Map<String, String> namespaceElements = this.getNamespaceElements(namespace);
            for (final String name : elements)
            {
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
        final Map<String, String> namespaceElements = this.getNamespaceElements(namespace);
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
    private Map<String, String> getNamespaceElements(final String namespace)
    {
        Map<String, String> namespaceElements = this.allElements.get(namespace);
        if (namespaceElements == null)
        {
            namespaceElements = new LinkedHashMap<String, String>();
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
        Profile.instance = null;
    }
}