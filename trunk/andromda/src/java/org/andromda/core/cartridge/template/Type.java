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
     * Gets the name of this type (typically the fully qualified class name
     * of the type).
     * 
     * @return Returns the name.
     */
    public String getName()
    {
        return StringUtils.trimToEmpty(name);
    }

    /**
     * Sets the name of this type (this is the fully qualified class name
     * of the type).
     * 
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
     * Gets the properties defined for this type.
     * 
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
     * @param name  the name of the property.
     * @param variable the optional variable name in which the contents of this
     *        property's value should be stored within a template.
     * @param value the option value the property must be in order to be considered <code>valid</code>.
     */
    public void addProperty(
        final String name,
        final String variable,
        final String value)
    {
        if (value != null && !this.properties.containsKey(name))
        {
            this.properties.put(
                name,
                new Property(name, variable, value));
        }
    }

    /**
     * Stores and provides access to the type's &lt;property/&gt; elements.
     */
    public static final class Property
    {
        private String name;
        private String variable;
        private String value;

        Property(
            final String name,
            final String variable,
            final String value)
        {
            this.name = StringUtils.trimToEmpty(name);
            this.variable = StringUtils.trimToEmpty(variable);
            this.value = StringUtils.trimToEmpty(value);
        }

        /**
         * Gets the value of the <code>name</code> attribute on the <code>property</code> element.
         *
         * @return the name
         */
        public String getName()
        {
            return this.name;
        }
        
        /**
         * Gets the variable name under which this property's value (or element if the property 
         * is a collection) should be stored within the template.
         *
         * @return the variable name.
         */
        public String getVariable()
        {
            return this.variable;
        }

        /**
         * Gets the value of the <code>value</code> attribute defined on the <code>property</code> element.
         *
         * @return the value
         */
        public String getValue()
        {
            return this.value;
        }
    }
}