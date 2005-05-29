package org.andromda.core.engine;

import org.andromda.core.common.AndroMDALogger;
import org.andromda.core.common.PropertyUtils;
import org.andromda.core.configuration.Configuration;
import org.andromda.core.configuration.Property;


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
     * Initializes Engine (discovers all plugins, etc).
     */
    public void initialize()
    {
        this.modelProcessor.initialize();
        AndroMDALogger.info("- core initialization complete -");
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
            this.modelProcessor.loadIfNecessary(configuration.getModels());
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
            configuration.initialize();
            this.modelProcessor.addTransformations(configuration.getTransformations());
            final Property[] properties = configuration.getProperties();
            final int propertyNumber = properties.length;
            for (int ctr = 0; ctr < propertyNumber; ctr++)
            {
                final Property property = properties[ctr];
                try
                {
                    PropertyUtils.setProperty(
                        this.modelProcessor,
                        property.getName(),
                        property.getValue());
                }
                catch (final Throwable throwable)
                {
                    AndroMDALogger.warn(
                        "Could not set model processor property '" + property.getName() + "' with a value of '" +
                        property.getValue() + "'");
                }
            }
            this.modelProcessor.process(configuration.getModels());
        }
    }

    /**
     * Shuts down Engine.
     */
    public void shutdown()
    {
        this.modelProcessor.shutdown();
    }
}