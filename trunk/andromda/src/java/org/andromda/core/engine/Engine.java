package org.andromda.core.engine;

import org.andromda.core.configuration.Configuration;


/**
 * The <em>engine</em> of AndroMDA. Handles the configuration of AndroMDA and
 * loading/processing of models by plugins. Basically a wrapper around the {@link ModelProcessor}
 * that takes a configuration file in order to configure AndroMDA.
 *
 * @see ModelProcessor
 * @author Chad Brandon
 */
public class Engine
{
    /**
     * Create a new Engine instance.
     *
     * @param configurationUri the URI to the configuration file
     *        that configures Engine.
     *
     * @return the new instance of Engine.
     */
    public static final Engine newInstance()
    {
        return new Engine();
    }

    /**
     * The model processor for this engine.
     */
    private ModelProcessor modelProcessor;

    private Engine()
    {
        // do not allow instantiation
        this.modelProcessor = ModelProcessor.newInstance();
    }

    /**
     * Initializes Engine (discovers all plugins, etc) with the
     * given configuration.  This configuration is overridden (if changed)
     * when calling {@link #run(Configuration)}.
     */
    public void initialize(final Configuration configuration)
    {
        this.modelProcessor.initialize(configuration);
    }

    /**
     * Checks to see if any of the models in the given configuration
     * should be loaded (based on whether or not they've been modified),
     * and if so, performs the load.  This way the
     * models are loaded for the next run of the model processor.
     */
    public void loadModelsIfNecessary(final Configuration configuration)
    {
        if (configuration != null)
        {
            this.modelProcessor.loadIfNecessary(configuration.getRepositories());
        }
    }

    /**
     * Runs Engine with the given configuration.
     *
     * @param configuration the String that contains the configuration
     *        contents for configuring Engine.
     *
     * @return the new instance of Engine.
     */
    public void run(final Configuration configuration)
    {
        if (configuration != null)
        {
            this.modelProcessor.process(configuration);
        }
    }

    /**
     * Shuts down this instance.
     */
    public void shutdown()
    {
        this.modelProcessor.shutdown();
    }
}