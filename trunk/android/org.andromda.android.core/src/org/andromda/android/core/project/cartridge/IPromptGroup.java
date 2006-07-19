package org.andromda.android.core.project.cartridge;

import java.util.Collection;

/**
 * This interface describes prompt groups in a project cartridge.
 *
 * @author Peter Friese
 * @since 22.05.2006
 */
public interface IPromptGroup
{

    /**
     * @return the description
     */
    String getDescription();

    /**
     * @param description the description to set
     */
    void setDescription(String description);

    /**
     * @return the name
     */
    String getName();

    /**
     * @param name the name to set
     */
    void setName(String name);

    /**
     * @return the prompts
     */
    Collection getPrompts();

}
