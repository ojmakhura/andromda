package org.andromda.core.profile;


/**
 * Stores information about a namespace component.
 *
 * @author Chad Brandon
 */
public class Element
{
    /**
     * The name of the component
     */
    private String name;

    /**
     * Gets the name of the component.
     *
     * @return the component name.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Sets the name of the component.
     *
     * @param name the component's name.
     */
    public void setName(final String name)
    {
        this.name = name;
    }

    /**
     * The value of the element.
     */
    private String value;

    /**
     * Gets the value of this element.
     *
     * @return the value of this element.
     */
    public String getValue()
    {
        return value;
    }

    /**
     * Sets the value of this element.
     *
     * @param value that value of this element.
     */
    public void setValue(final String value)
    {
        this.value = value;
    }
}