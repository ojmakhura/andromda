package org.andromda.android.core.project.cartridge;

import java.util.List;
import java.util.Map;

import org.andromda.android.core.internal.project.cartridge.Precondition;

/**
 * This interface describes prompts in a project cartridge.
 *
 * @author Peter Friese
 * @since 22.05.2006
 */
public interface IPrompt
{

    /** "and" type for conditions. */
    String TYPE_AND = "and";

    /** "or" type for conditions. */
    String TYPE_OR = "or";

    /**
     * @return the id
     */
    String getId();

    /**
     * @param id the id to set
     */
    void setId(String id);

    /**
     * @return the label
     */
    String getLabel();

    /**
     * @param label the label to set
     */
    void setLabel(String label);

    /**
     * @return the tooltip
     */
    String getTooltip();

    /**
     * @param text the tooltip to display
     */
    void setTooltip(String text);

    /**
     * @return a list of valid options for this prompt
     */
    List getOptions();

    /**
     * Adds an option entry to the list of valid options.
     *
     * @param option a valid option
     */
    void addOption(String option);

    /**
     * Set the datatype for this prompt (e.g. boolean, String, Integer ...)
     *
     * @param type the datatype
     */
    void setType(String type);

    /**
     * @return the datatype for this prompt
     */
    String getType();

    /**
     * @return a list of preconditions for this prompt.
     */
    List getPreconditions();

    /**
     * Adds a precondition to the list of precondition.
     *
     * @param precondition a valid precondition
     */
    void addPrecondition(Precondition precondition);

    /**
     * @return The type of the condition.
     */
    String getConditionsType();

    /**
     * Sets the type of the condition.
     *
     * @param conditionsType The type of the condition.
     */
    void setConditionsType(String conditionsType);

    /**
     * Check whether the prompt is enabled or disabled according to its preconditions.
     *
     * @param projectProperties The map of project proerties.
     * @return <code>true</code> if all preconditions are met, <code>false</code> otherwise.
     */
    boolean isPromptEnabled(final Map projectProperties);

    /**
     * Whether or not the response should be set to a boolean value of <code>true</code>.
     *
     * @param setAsTrue The setResponseAsTrue value
     */
    void setSetAsTrue(boolean setAsTrue);

    /**
     * Whether or not the response should be set to a boolean value of <code>true</code>.
     *
     * @return Returns the setResponseAsTrue.
     */
    boolean isSetAsTrue();

}
