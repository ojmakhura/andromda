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
     * Stores the prompt values to set if the condition is true.
     */
    private final Map promptValues = new LinkedHashMap();
    
    /**
     * Adds the prompt id and the value to give the prompt.
     * 
     * @param id the identifier of the prompt.
     * @param value the value to give the prompt.
     */
    public void addPromptValue(final String id, final String value)
    {
        this.promptValues.put(id, value);
    }
    
    /**
     * Gets all prompt values for this condition.
     * 
     * @return the prompt values.
     */
    public Map getPromptValues()
    {
        return this.promptValues;
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
}