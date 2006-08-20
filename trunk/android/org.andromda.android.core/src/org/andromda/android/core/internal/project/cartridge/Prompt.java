package org.andromda.android.core.internal.project.cartridge;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.andromda.android.core.AndroidCore;
import org.andromda.android.core.project.cartridge.IPrecondition;
import org.andromda.android.core.project.cartridge.IPrompt;

/**
 * An input prompt.
 *
 * @author Peter Friese
 * @since 22.05.2006
 */
class Prompt
        implements IPrompt
{

    /** The id of this prompt. */
    private String id;

    /** The label for this prompt. */
    private String label;

    /** The tooltip for this prompt. */
    private String tooltip;

    /** Holds a list of options for this prompt. */
    private List options = new ArrayList();

    /** Holds a list of precondition for this prompt. */
    private List preconditions = new ArrayList();

    /** The datatype of this prompt. */
    private String type;

    /** The type of the preconditions for this prompt. */
    private String conditionsType;

    /** Whether or not the response should be set to a boolean value of <code>true</code>. */
    private boolean setAsTrue;

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
    public void setId(final String id)
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
    public void setLabel(final String label)
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
    public void setTooltip(final String text)
    {
        this.tooltip = text;
    }

    /**
     * {@inheritDoc}
     */
    public void addOption(final String option)
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
    public void setType(final String type)
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

    /**
     * {@inheritDoc}
     */
    public void addPrecondition(final Precondition precondition)
    {
        preconditions.add(precondition);
    }

    /**
     * {@inheritDoc}
     */
    public List getPreconditions()
    {
        return preconditions;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isPromptEnabled(final Map projectProperties)
    {
        // check preconditions
        List relevantPreconditions = this.getPreconditions();
        boolean validPreconditions = true;
        for (Iterator iter = preconditions.iterator(); iter.hasNext();)
        {
            IPrecondition precondition = (IPrecondition)iter.next();
            validPreconditions = precondition.evaluate(projectProperties);

            // - if we're 'anding' the conditions, we break at the first false
            if (TYPE_AND.equals(conditionsType))
            {
                if (!validPreconditions)
                {
                    break;
                }
            }
            else
            {
                // otherwise we break at the first true condition
                if (validPreconditions)
                {
                    break;
                }
            }
        }
        return validPreconditions;
    }

    /**
     * {@inheritDoc}
     */
    public String getConditionsType()
    {
        return conditionsType;
    }

    /**
     * {@inheritDoc}
     */
    public void setConditionsType(final String conditionsType)
    {
        this.conditionsType = conditionsType;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isSetAsTrue()
    {
        return setAsTrue;
    }

    /**
     * {@inheritDoc}
     */
    public void setSetAsTrue(final boolean setAsTrue)
    {
        this.setAsTrue = setAsTrue;
    }

    /**
     * {@inheritDoc}
     */
    public Class getTypeClass()
    {
        if (type != null)
        {
            try
            {
                return Class.forName(type);
            }
            catch (ClassNotFoundException e)
            {
                AndroidCore.log(e);
            }
        }
        return null;
    }

}
