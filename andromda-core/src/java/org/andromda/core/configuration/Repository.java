package org.andromda.core.configuration;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collection;


/**
 * Stores the repository information for each model that AndroMDA will process.
 *
 * @author Chad Brandon
 */
public class Repository
    implements Serializable
{
    /**
     * Stores the unique name of this repository.
     */
    private String name;

    /**
     * Sets the unique (among other repositories)
     * name.
     *
     * @param name the unique name of this repository.
     */
    public void setName(final String name)
    {
        this.name = name;
    }

    /**
     * Gets the unique name of this repository.
     *
     * @return the repository name.
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * The models loaded by this repository.
     */
    private final Collection<Model> models = new ArrayList<Model>();

    /**
     * Adds a model that this repository will load.
     *
     * @param model the model to load.
     */
    public void addModel(final Model model)
    {
        model.setRepository(this);
        this.models.add(model);
    }

    /**
     * Gets the model instances belonging to this repository.
     *
     * @return the of model instances.
     */
    public Model[] getModels()
    {
        return this.models.toArray(new Model[this.models.size()]);
    }
}