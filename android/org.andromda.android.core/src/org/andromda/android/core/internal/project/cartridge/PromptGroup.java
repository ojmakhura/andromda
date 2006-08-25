package org.andromda.android.core.internal.project.cartridge;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.andromda.android.core.project.cartridge.IPrompt;
import org.andromda.android.core.project.cartridge.IPromptGroup;

/**
 * A group of prompts.
 *
 * @author Peter Friese
 * @since 22.05.2006
 */
class PromptGroup implements IPromptGroup
{

    /** The name of the prompt group. */
    private String name;

    /** The description of this prompt group. */
    private String description;

    /** The list of prompts contained in this group. */
    private List prompts = new ArrayList();

    /**
     * {@inheritDoc}
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * {@inheritDoc}
     */
    public void setDescription(final String description)
    {
        this.description = description;
    }

    /**
     * {@inheritDoc}
     */
    public String getName()
    {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    public void setName(final String name)
    {
        this.name = name;
    }

    /**
     * {@inheritDoc}
     */
    public Collection getPrompts()
    {
        return prompts;
    }

    /**
     * Adds the given prompt to this prompt group.
     *
     * @param prompt The new prompt.
     */
    public void addPrompt(final IPrompt prompt)
    {
        prompts.add(prompt);
    }

}
