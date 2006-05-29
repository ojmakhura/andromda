package org.andromda.android.core.project.cartridge;

import java.util.List;

/**
 * This interface describes prompts in a project cartridge.
 * 
 * @author Peter Friese
 * @since 22.05.2006
 */
public interface IPrompt
{

    /**
     * @return the id
     */
    public String getId();

    /**
     * @param id the id to set
     */
    public void setId(String id);

    /**
     * @return the label
     */
    public String getLabel();

    /**
     * @param label the label to set
     */
    public void setLabel(String label);

    /**
     * @return the tooltip
     */
    public String getTooltip();

    /**
     * @param text the tooltip to display
     */
    public void setTooltip(String text);

    /**
     * @return a list of valid options for this prompt
     */
    public List getOptions();

    /**
     * Adds an option entry to the list of valid options.
     * 
     * @param option a valid option
     */
    public void addOption(String option);

    /**
     * Set the datatype for this prompt (e.g. boolean, String, Integer ...)
     * 
     * @param type the datatype
     */
    public void setType(String type);
    
    /**
     * @return the datatype for this prompt
     */
    public String getType();

}