package org.andromda.core.cartridge.template;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;


/**
 * Represents the &lt;type/&gt; element nested within the &lt;modelElement/&gt; element.
 *
 * @author Chad Brandon
 * @see ModelElement
 */
public class Type
{
    /**
     * The name of this type.
     */
    private String name;

    /**
     * @return Returns the name.
     */
    public String getName()
    {
        return StringUtils.trimToEmpty(name);
    }

    /**
     * @param name The name to set.
     */
    public void setName(final String name)
    {
        this.name = name;
    }

    /**
     * The properties that must be valid for this type.
     */
    private final Map properties = new LinkedHashMap();

    /**
     * @return Returns the properties.
     */
    public Collection getProperties()
    {
        return properties.values();
    }

    /**
     * Adds a property having the given <code>name</code> and <code>value</code>. The <code>value</code> is what the
     * property must be in order to be collected.
     *
     * @param name  the name of the property
     * @param value the value the property must be
     */
    public void addProperty(
        final String name,
        final String value)
    {
        if (value != null && !this.properties.containsKey(name))
        {
            this.properties.put(
                name,
                new Property(name, value));
        }
    }

    /**
     * Stores and provides access to the type's &lt;property/&gt; elements.
     */
    public static final class Property
    {
        private String name;
        private String value;

        Property(
            String name,
            String value)
        {
            this.name = StringUtils.trimToEmpty(name);
            this.value = value;
        }

        /**
         * Gets the value of the <code>name</code> attribute on the <code>property</code> element.
         *
         * @return the name
         */
        public String getName()
        {
            return StringUtils.trimToEmpty(this.name);
        }

        /**
         * Gets the value of the <code>value</code> attribute defined on the <code>property</code> element.
         *
         * @return the value
         */
        public String getValue()
        {
            return StringUtils.trimToEmpty(this.value);
        }
    }
}