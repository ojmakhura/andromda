package org.andromda.core.configuration;

import java.io.Serializable;


/**
 * Used to specify which packages should or should not be processed within the model. This is useful if you need to
 * reference stereotyped model elements from other packages but you don't want to generate elements from them.
 *
 * @author Chad Brandon
 * @see org.andromda.core.configuration.ModelPackages
 */
public class ModelPackage
    implements Serializable
{
    private String name;

    /**
     * Gets the name of this ModelPackage.
     *
     * @return Returns the name.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Sets the name of this ModelPackage.
     *
     * @param name the name of the model package.
     */
    public void setName(final String name)
    {
        this.name = name;
    }

    /**
     * The flag indicating whether or not this model package
     * should be processed.
     */
    private boolean process = true;

    /**
     * Whether or not this ModelPackage should be processed.
     *
     * @return Returns the shouldProcess.
     */
    public boolean isProcess()
    {
        return this.process;
    }

    /**
     * Sets whether or not this ModelPackage should be processed.
     *
     * @param process The shouldProcess to set.
     */
    public void setProcess(final boolean process)
    {
        this.process = process;
    }
}