package org.andromda.andromdapp;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * Represents a &lt;conditions/&gt; element, which groups more than
 * one condition together.
 *
 * @author Chad Brandon
 */
public class Conditions
{
    /**
     * The "and" type.
     */
    static final String TYPE_AND = "and";

    /**
     * The "or" type.
     */
    static final String TYPE_OR = "or";

    /**
     * The type of this conditions instance.
     */
    private String type;

    /**
     * Gets the type of this conditions instance.
     *
     * @return Returns the type.
     */
    public String getType()
    {
        return this.type;
    }

    /**
     * Sets the type of this conditions instance.
     *
     * @param type The type to set.
     */
    public void setType(String type)
    {
        this.type = type;
    }

    /**
     * Stores the conditions.
     */
    private List conditions = new ArrayList();

    /**
     * Adds a condition instance to this conditions.
     *
     * @param condition the condition which must apply to this conditions instance.
     */
    public void addCondition(final Condition condition)
    {
        this.conditions.add(condition);
    }

    /**
     * Gets the condition instances defined in this conditions instance.
     *
     * @return the conditions that are defined within this prompt.
     */
    public List getConditions()
    {
        return this.conditions;
    }

    /**
     * Stores the output paths.
     */
    private Map outputPaths = new LinkedHashMap();

    /**
     * Adds a path to the output paths.
     *
     * @param path the path to the resulting output
     * @param patterns any patterns to which the conditions should apply
     */
    public void addOutputPath(
        final String path,
        final String patterns)
    {
        this.outputPaths.put(
            path,
            AndroMDAppUtils.stringToArray(patterns));
    }

    /**
     * Gets the current output paths for this condition.
     *
     * @return the map of output paths and its patterns (if it has any defined).
     */
    final Map getOutputPaths()
    {
        return this.outputPaths;
    }
}