package org.andromda.core.metafacade;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import org.andromda.core.common.ClassUtils;
import org.andromda.core.profile.Profile;
import org.apache.commons.lang.StringUtils;


/**
 * A meta facade mapping class. This class is a child of {@link MetafacadeMappings}
 * (that is: instances of this class below to an instance of {@link MetafacadeMappings}).
 *
 * @author Chad Brandon
 * @author Bob Fields
 */
public class MetafacadeMapping
{
    /**
     * The meta facade for which this mapping applies.
     */
    private Class metafacadeClass;

    /**
     * Gets the metafacadeClass for this mapping.
     *
     * @return Returns the metafacadeClass.
     */
    public Class getMetafacadeClass()
    {
        return metafacadeClass;
    }

    /**
     * Sets the metafacadeClassName for this mapping.
     *
     * @param metafacadeClassName The name of the metafacade class to set.
     */
    public void setMetafacadeClassName(final String metafacadeClassName)
    {
        try
        {
            this.metafacadeClass = ClassUtils.loadClass(StringUtils.trimToEmpty(metafacadeClassName));
        }
        catch (final Throwable throwable)
        {
            throw new MetafacadeMappingsException(throwable);
        }
    }

    /**
     * The name of the mapping class for which this mapping applies. The {@link #context},{@link #stereotypes}and this
     * name make up the identifying key for this mapping.
     */
    private String mappingClassName;

    /**
     * Gets the name of the metaobject class used for this mapping.
     *
     * @return Returns the mappingClassName.
     */
    protected String getMappingClassName()
    {
        // - if we have a mappingClassName defined, we use it
        if (StringUtils.isBlank(this.mappingClassName))
        {
            // - attempt to get the inherited mapping since it doesn't exist on this class
            this.mappingClassName = MetafacadeUtils.getInheritedMappingClassName(this);
        }
        return this.mappingClassName;
    }

    /**
     * Indicates whether or not the mapping class has been present.
     *
     * @return whether or not the mapping class is present in this mapping.
     */
    final boolean isMappingClassNamePresent()
    {
        return StringUtils.isNotBlank(this.mappingClassName);
    }

    /**
     * The name of the metaobject class to use for this mapping.
     *
     * @param mappingClassName The mappingClassName to set.
     */
    public void setMappingClassName(final String mappingClassName)
    {
        this.mappingClassName = StringUtils.trimToEmpty(mappingClassName);
    }

    /**
     * Whether or not this mapping represents a <code>contextRoot</code>.
     */
    private boolean contextRoot;

    /**
     * <p/>
     * Gets whether or not this mapping represents a <code>contextRoot</code>, by default a mapping is <strong>NOT
     * </strong> a contextRoot. You'll want to specify this as true when other metafacades need to be created within the
     * context of this metafacade. </p>
     *
     * @return Returns the contextRoot.
     */
    public boolean isContextRoot()
    {
        return this.contextRoot;
    }

    /**
     * Sets the name of the <code>contextRoot</code> for this mapping.
     *
     * @param contextRoot The contextRoot to set.
     * @see #isContextRoot()
     */
    public void setContextRoot(final boolean contextRoot)
    {
        this.contextRoot = contextRoot;
    }

    /**
     * The stereotypes to which this mapping applies (all stereotypes must be present for this mapping to apply).
     */
    private final List<String> stereotypes = new ArrayList<String>();

    /**
     * Adds a <code>stereotype</code> to the stereotypes.
     *
     * @param stereotype
     */
    public void addStereotype(final String stereotype)
    {
        this.stereotypes.add(stereotype);
    }

    /**
     * Gets the stereotypes which apply to this mapping.
     *
     * @return the names of the stereotypes
     */
    final List<String> getStereotypes()
    {
        for (final ListIterator<String> iterator = this.stereotypes.listIterator(); iterator.hasNext();)
        {
            iterator.set(Profile.instance().get(iterator.next()));
        }
        return this.stereotypes;
    }

    /**
     * Indicates whether or not this mapping has any stereotypes defined.
     *
     * @return true/false
     */
    final boolean hasStereotypes()
    {
        return !this.stereotypes.isEmpty();
    }

    /**
     * Used to hold references to language mapping classes.
     */
    private final Collection<String> propertyReferences = new LinkedHashSet<String>();

    /**
     * Adds a mapping property reference. These are used to populate metafacade impl classes with mapping files, etc.
     * The property reference applies to the given mapping.
     *
     * @param reference the name of the reference.
     * @see MetafacadeMappings#addPropertyReference(String)
     */
    public void addPropertyReference(final String reference)
    {
        this.propertyReferences.add(reference);
    }

    /**
     * Returns all mapping references for this MetafacadeMapping instance.
     * @return this.propertyReferences
     */
    public Collection<String> getPropertyReferences()
    {
        return this.propertyReferences;
    }

    /**
     * Used to hold the properties that should apply to the mapping element.
     */
    private PropertyGroup mappingProperties;

    /**
     * Adds a mapping property. This are used to narrow the metafacade to which the mapping can apply. The properties
     * must exist and must evaluate to the specified value if given for the mapping to match.
     *
     * @param name the name of the reference.
     * @param value the default value of the property reference.
     */
    public void addMappingProperty(
        final String name,
        final String value)
    {
        if (value != null)
        {
            if (this.mappingProperties == null)
            {
                this.mappingProperties = new PropertyGroup();

                // we add the mapping properties to the mappingPropertyGroups
                // collection only once
                this.mappingPropertyGroups.add(this.mappingProperties);
            }
            this.mappingProperties.addProperty(new Property(
                    name,
                    value));
        }
    }

    /**
     * Stores a collection of all property groups added through {@link #addPropertyReferences(java.util.Collection)}. These are
     * property groups added from other mappings that return true when executing {@link #match(MetafacadeMapping)}.
     */
    private final Collection<PropertyGroup> mappingPropertyGroups = new ArrayList<PropertyGroup>();

    /**
     * Adds the <code>propertyGroup</code> to the existing mapping property groups within this mapping.
     *
     * @param propertyGroup a property group for this mapping
     */
    final void addMappingPropertyGroup(final PropertyGroup propertyGroup)
    {
        this.mappingPropertyGroups.add(propertyGroup);
    }

    /**
     * Returns all mapping property groups for this MetafacadeMapping instance.
     * @return this.mappingPropertyGroups
     */
    final Collection<PropertyGroup> getMappingPropertyGroups()
    {
        return this.mappingPropertyGroups;
    }

    /**
     * Gets the mapping properties associated this this mapping directly (contained within a {@link
     * PropertyGroup}instance).
     *
     * @return the mapping property group.
     */
    final PropertyGroup getMappingProperties()
    {
        return this.mappingProperties;
    }

    /**
     * Indicates whether or not this mapping contains any mapping properties.
     *
     * @return true/false
     */
    final boolean hasMappingProperties()
    {
        return this.mappingProperties != null && !this.mappingProperties.getProperties().isEmpty();
    }

    /**
     * Adds all <code>propertyReferences</code> to the property references contained in this MetafacadeMapping
     * instance.
     *
     * @param propertyReferences the property references to add.
     */
    public void addPropertyReferences(final Collection<String> propertyReferences)
    {
        if (propertyReferences != null)
        {
            this.propertyReferences.addAll(propertyReferences);
        }
    }

    /**
     * The context to which this mapping applies.
     */
    private String context = "";

    /**
     * Sets the context to which this mapping applies.
     *
     * @param context The metafacade context name to set.
     */
    public void setContext(final String context)
    {
        this.context = StringUtils.trimToEmpty(context);
    }

    /**
     * Gets the context to which this mapping applies.
     *
     * @return the name of the context
     */
    final String getContext()
    {
        return this.context;
    }

    /**
     * Indicates whether or not this mapping has a context.
     *
     * @return true/false
     */
    final boolean hasContext()
    {
        return StringUtils.isNotEmpty(this.context);
    }

    /**
     * The "parent" metafacade mappings;
     */
    private MetafacadeMappings mappings;

    /**
     * Sets the metafacade mappings instance to which this particular mapping belongs. (i.e. the parent) Note, that this
     * is populated during the call to {@link MetafacadeMappings#addMapping(MetafacadeMapping)}.
     *
     * @param mappings the MetacadeMappings instance to which this mapping belongs.
     */
    final void setMetafacadeMappings(final MetafacadeMappings mappings)
    {
        this.mappings = mappings;
    }

    /**
     * Gets the "parent" MetafacadeMappings instance to which this mapping belongs.
     *
     * @return the parent metafacade mappings instance.
     */
    final MetafacadeMappings getMetafacadeMappings()
    {
        return this.mappings;
    }

    /**
     * Indicates whether or not the <code>mapping</code> matches this mapping. It matches on the following: <ul>
     * <li>metafacadeClass</li> <li>mappingClassName</li> <li>stereotypes</li> </ul>
     * @param mapping 
     * @return match this.getMappingClassName().equals(mapping.getMappingClassName())
     */
    final boolean match(final MetafacadeMapping mapping)
    {
        boolean match =
            mapping != null && this.getMetafacadeClass().equals(mapping.getMetafacadeClass()) &&
            this.getStereotypes().equals(mapping.getStereotypes()) && this.getContext().equals(mapping.getContext());

        // - if they match and the mappingClassNames are both non-null, verify they match
        if (match && this.mappingClassName != null && mapping != null && mapping.mappingClassName != null)
        {
            match = this.getMappingClassName().equals(mapping.getMappingClassName());
        }
        return match;
    }

    /**
     * @see Object#toString()
     */
    public String toString()
    {
        return super.toString() + '[' + this.getMetafacadeClass() + "], mappingClassName[" + this.mappingClassName +
        "], properties[" + this.getMappingProperties() + "], stereotypes" + this.stereotypes + ", context[" +
        this.context + "], propertiesReferences" + this.getPropertyReferences();
    }

    /**
     * Represents a group of properties. Properties within a group are evaluated within an 'AND' expression.
     * PropertyGroups are evaluated together as an 'OR' expressions (i.e. you 'OR' property groups together, and 'AND'
     * properties together).
     *
     * @see MetafacadeMappings#addMapping(MetafacadeMapping)
     */
    static final class PropertyGroup
    {
        private final Map<String, Property> properties = new LinkedHashMap<String, Property>();

        /**
         * Adds a property to the internal collection of properties.
         *
         * @param property the property to add to this group.
         */
        final void addProperty(final Property property)
        {
            final String name = property.getName();
            if (!this.properties.containsKey(name))
            {
                this.properties.put(
                    name,
                    property);
            }
        }

        /**
         * Gets the currently internal collection of properties.
         *
         * @return the properties collection.
         */
        final Collection<Property> getProperties()
        {
            return this.properties.values();
        }

        /**
         * @see Object#toString()
         */
        public String toString()
        {
            final StringBuffer toString = new StringBuffer();
            char seperator = ':';
            for (final Iterator<Property> iterator = this.getProperties().iterator(); iterator.hasNext();)
            {
                final Property property = iterator.next();
                toString.append(property.getName());
                if (StringUtils.isNotEmpty(property.getValue()))
                {
                    toString.append(seperator);
                    toString.append(property.getValue());
                }
                if (iterator.hasNext())
                {
                    toString.append(seperator);
                }
            }
            return toString.toString();
        }
    }

    /**
     * Stores and provides access to the mapping element's nested &lt;property/&gt;.
     */
    static final class Property
    {
        private String name;
        private String value;

        Property(
            final String name,
            final String value)
        {
            this.name = StringUtils.trimToEmpty(name);
            this.value = value;
        }

        /**
         * Gets the value of the <code>name</code> attribute on the <code>property</code> element.
         *
         * @return the name
         */
        final String getName()
        {
            return StringUtils.trimToEmpty(this.name);
        }

        /**
         * Gets the value of the <code>value</code> attribute defined on the <code>property</code> element.
         *
         * @return the value
         */
        final String getValue()
        {
            return StringUtils.trimToEmpty(this.value);
        }
    }
}