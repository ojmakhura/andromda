package org.andromda.android.core.internal.project.cartridge;

import java.util.ArrayList;
import java.util.List;

import org.andromda.android.core.project.cartridge.IPrompt;

/**
 * An input prompt.
 * 
 * @author Peter Friese
 * @since 22.05.2006
 */
class Prompt implements IPrompt
{

    /** The id of this prompt. */
    private String id;

    /** The label for this prompt. */
    private String label;

    /** The tooltip for this prompt. */
    private String tooltip;

    /** Holds a list of options for this prompt. */
    private List options = new ArrayList();

    /** The datatype of this prompt. */
    public String type;

    /**
     * {@inheritDoc}
     */
    public String getId()
    {
        return id;
    }

    /**
     * {@inheritDoc}
     */
    public void setId(String id)
    {
        this.id = id;
    }

    /**
     * {@inheritDoc}
     */
    public String getLabel()
    {
        return label;
    }

    /**
     * {@inheritDoc}
     */
    public void setLabel(String label)
    {
        this.label = label;
    }
    
    /**
     * @return the tooltip
     */
    public String getTooltip()
    {
        return tooltip;
    }

    /**
     * {@inheritDoc}
     */
    public void setTooltip(String text)
    {
        this.tooltip = text;
    }

    /**
     * {@inheritDoc}
     */
    public void addOption(String option)
    {
        options.add(option);
    }

    /**
     * {@inheritDoc}
     */
    public List getOptions()
    {
        // TODO have a look at the AndroMDA implementaion of Prompt - pretty neat type conversion in there!
        return options;
    }

    /**
     * {@inheritDoc}
     */
    public void setType(String type)
    {
        this.type = type;
    }

    /**
     * {@inheritDoc}
     */
    public String getType()
    {
        return type;
    }
    
}
