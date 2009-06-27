package org.andromda.core.namespace;


/**
 * Represents a property definition.
 *
 * @author Chad Brandon
 */
public class PropertyDefinition
{
    private String name;

    /**
     * Gets the name of this property definition.
     *
     * @return Returns the name.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Sets the name of this property definition.
     *
     * @param name The name to set.
     */
    public void setName(final String name)
    {
        this.name = name;
    }

    /**
     * Stores the default value.
     */
    private String defaultValue;

    /**
     * Gets the default for this property definition.
     *
     * @return Returns the defaultValue.
     */
    public String getDefaultValue()
    {
        return this.defaultValue;
    }

    /**
     * Sets the default for the property definition.
     *
     * @param defaultValue The defaultValue to set.
     */
    public void setDefaultValue(final String defaultValue)
    {
        this.defaultValue = defaultValue;
    }
    
    /**
     * The flag indicating whether or not this property is required.
     */
    private boolean required = true;
    
    /**
     * Sets this property is required, by default
     * this flag is <code>true</code>.
     * 
     * @param required true/false
     */
    public void setRequired(final boolean required)
    {
        this.required = required;
    }
    
    /**
     * Indicates of this property is required, by default
     * this flag is <code>true</code>.
     * 
     * @return true/false
     */
    public boolean isRequired()
    {
        return this.required;
    }
}