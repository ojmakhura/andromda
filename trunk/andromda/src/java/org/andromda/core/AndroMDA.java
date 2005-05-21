package org.andromda.core;

import org.andromda.core.common.AndroMDALogger;
import org.andromda.core.common.PropertyUtils;
import org.andromda.core.configuration.Configuration;
import org.andromda.core.configuration.Property;

import java.io.InputStream;

import java.net.URL;


/**
 * The main entry point to the framework, handles the configuration of AndroMDA and
 * loading/processing of models by plugins. Basically a wrapper around the {@link ModelProcessor}
 * that takes a configuration file in order to configuration AndroMDA.
 *
 * @see ModelProcessor 
 * @author Chad Brandon
 */
public class AndroMDA
{
    /**
     * The configuration instance that configures AndroMDA.
     */
    private Configuration configuration;

    /**
     * Gets an instance of AndroMDA.
     *
     * @param configurationUri the URI to the configuration file
     *        that configures AndroMDA.
     *
     * @return the new instance of AndroMDA.
     */
    public static final AndroMDA getInstance(final URL configurationUri)
    {
        final AndroMDA andromda = new AndroMDA();
        andromda.configuration = Configuration.getInstance(configurationUri);
        return andromda;
    }

    /**
     * Gets an instance of AndroMDA.
     *
     * @param configurationStream the InputStream that contains the configuration
     *        contents for configuring AndroMDA.
     *
     * @return the new instance of AndroMDA.
     */
    public static final AndroMDA getInstance(final InputStream configurationStream)
    {
        final AndroMDA andromda = new AndroMDA();
        andromda.configuration = Configuration.getInstance(configurationStream);
        return andromda;
    }

    /**
     * Gets an instance of AndroMDA.
     *
     * @param configuration the String that contains the configuration
     *        contents for configuring AndroMDA.
     *
     * @return the new instance of AndroMDA.
     */
    public static final AndroMDA getInstance(final String configuration)
    {
        final AndroMDA andromda = new AndroMDA();
        andromda.configuration = Configuration.getInstance(configuration);
        return andromda;
    }
    
    /**
     * Gets an instance of AndroMDA.
     *
     * @param configuration the Configuration instance that configures
     *        AndroMDA.
     *
     * @return the new instance of AndroMDA.
     */
    public static final AndroMDA getInstance(final Configuration configuration)
    {
        final AndroMDA andromda = new AndroMDA();
        andromda.configuration = configuration;
        return andromda;
    }

    /**
     * Runs AndroMDA's with the given configuration.
     */
    public void run()
    {
        if (this.configuration != null)
        {
            this.configuration.initialize();
            final ModelProcessor processor = ModelProcessor.instance();
            processor.addTransformations(this.configuration.getTransformations());
            final Property[] properties = this.configuration.getProperties();
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
            processor.process(this.configuration.getModels());
        }
    }
    
    /**
     * Shuts down AndroMDA.
     */
    public void shutdown()
    {
        ModelProcessor.instance().shutdown();
    }
}