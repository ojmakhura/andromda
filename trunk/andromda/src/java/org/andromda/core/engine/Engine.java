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
    
    private Engine()
    {
        // do not allow instantiation
    }

    /**
     * Initializes Engine (discovers all plugins, etc).
     */
    public void initialize()
    {
        ModelProcessor.instance().initialize();
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
            final ModelProcessor processor = ModelProcessor.instance();
            processor.addTransformations(configuration.getTransformations());
            final Property[] properties = configuration.getProperties();
            final int propertyNumber = properties.length;
            for (int ctr = 0; ctr < propertyNumber; ctr++)
            {
                final Property property = properties[ctr];
                try
                {
                    PropertyUtils.setProperty(
                        processor,
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
            processor.process(configuration.getModels());
        }
    }

    /**
     * Shuts down Engine.
     */
    public void shutdown()
    {
        ModelProcessor.instance().shutdown();
    }
}