package org.andromda.andromdapp;

import java.util.LinkedHashMap;
import java.util.Map;


/**
 * Represents a prompt "condition".  That is, a prompt
 * value will be set if the condition is satisfied.
 *
 * @author Chad Brandon
 */
public class Condition
{
    /**
     * The id of the prompt to which this condition applies.
     */
    private String id;

    /**
     * Gets the id of the prompt to which this condition applies.
     *
     * @return Returns the id.
     */
    public String getId()
    {
        return id;
    }

    /**
     * Sets the id of the prompt to which this condition applies.
     *
     * @param id The id to set.
     */
    public void setId(String id)
    {
        this.id = id;
    }

    /**
     * Stores the properties to set if the condition is true.
     */
    private final Map properties = new LinkedHashMap();

    /**
     * Sets the value of the property in the template context
     * with the given <code>id</code> to have the given <code>value</code>
     * if this condition is true.
     *
     * @param id the identifier of the prompt.
     * @param value the value to give the prompt.
     * @param type the fully qualified type name.
     */
    public void setProperty(
        final String id,
        final String value,
        final String type)
    {
        this.properties.put(
            id,
            AndroMDAppUtils.convert(
                value,
                type));
    }

    /**
     * Gets all properties to set for this condition.
     *
     * @return the prompt values.
     */
    public Map getProperties()
    {
        return this.properties;
    }

    /**
     * The value of which the condition must be equal.
     */
    private String equal;

    /**
     * Gets the value of which the condition must be equal.
     *
     * @return Returns the equal.
     */
    public String getEqual()
    {
        return equal;
    }

    /**
     * Sets the value of which the condition must be equal.
     *
     * @param equal The equal to set.
     */
    public void setEqual(final String equal)
    {
        this.equal = equal;
    }

    /**
     * The value of which the condition must not be equal.
     */
    private String notEqual;

    /**
     * Gets the value of which the condition must <strong>not</strong> be equal.
     *
     * @return Returns the notEqual.
     */
    public String getNotEqual()
    {
        return notEqual;
    }

    /**
     * Sets the value of which the condition must <strong>not</strong> be equal.
     *
     * @param notEqual The notEqual to set.
     */
    public void setNotEqual(final String notEqual)
    {
        this.notEqual = notEqual;
    }

    /**
     * The value of which the condition must be present.
     */
    private Boolean present;
    
    /**
     * Sets whether or not the condition must be present.
     *
     * @param present The present to set.
     */
    public void setPresent(final boolean present)
    {
        this.present = Boolean.valueOf(present);
    }

    /**
     * Evalutes whether or not the value is valid according to this condition.
     *
     * @param value the value to evaluate.
     * @return true/false
     */
    public boolean evaluate(Object value)
    {
        boolean valid = true;
        if (this.present != null)
        {
            // - if the condition must be present, very that it is
            if (this.present.booleanValue())
            {
                valid = value != null;
            }
            else if (!this.present.booleanValue())
            {
                // - otherwise verify that the condition is not present (if it shouldn't be)
                valid = value == null;
            }
        }
        if (valid)
        {
            final String equal = this.getEqual();
            final String notEqual = this.getNotEqual();
            final boolean equalConditionPresent = equal != null;
            final boolean notEqualConditionPresent = notEqual != null;
            value = String.valueOf(value);
            if (equalConditionPresent || notEqualConditionPresent)
            {
                if (equalConditionPresent)
                {
                    valid = equal.equals(value);
                }
                else if (notEqualConditionPresent)
                {
                    valid = !notEqual.equals(value);
                }
            }
        }
        return valid;
    }
}