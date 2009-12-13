package org.andromda.ant.task;

import java.io.FileNotFoundException;

import java.net.URL;

import org.andromda.core.AndroMDAServer;
import org.andromda.core.configuration.Configuration;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.MatchingTask;


/**
 * <p>
 * This class wraps the AndroMDA model processor so that AndroMDA Server can be
 * used as an Ant task. Represents the <code>&lt;andromda&gt;</code> custom
 * task which can be called from an Ant build script.
 * </p>
 *
 * @author Lofi Dewanto
 */
public class AndroMDAServerStopTask
    extends MatchingTask
{
    /**
     * Initialize the context class loader.
     */
    static
    {
        initializeContextClassLoader();
    }

    /**
     * Stores the configuration URI.
     */
    private URL configurationUri;

    /**
     * Sets the URI to the configuration file.
     *
     * @param configurationUri
     */
    public void setConfigurationUri(final URL configurationUri)
    {
        this.configurationUri = configurationUri;
    }

    /**
     * <p>
     * Stops the AndroMDA server.
     * </p>
     * <p>
     * This is the main entry point of the application when running Ant. It is
     * called by ant whenever the surrounding task is executed (which could be
     * multiple times).
     * </p>
     *
     * @throws BuildException if something goes wrong
     */
    public void execute()
        throws BuildException
    {
        // initialize before the execute as well in case we
        // want to execute more than once
        initializeContextClassLoader();
        try
        {
            if (this.configurationUri == null)
            {
                throw new BuildException("Configuration is not a valid URI --> '" + this.configurationUri + '\'');
            }

            // Create the configuration file
            final Configuration configuration = Configuration.getInstance(this.configurationUri);

            final AndroMDAServer andromdaServer = AndroMDAServer.newInstance();
            if (andromdaServer != null)
            {
                andromdaServer.stop(configuration);
            }
        }
        catch (Throwable throwable)
        {
            final Throwable cause = ExceptionUtils.getCause(throwable);
            if (cause != null)
            {
                throwable = cause;
            }
            if (throwable instanceof FileNotFoundException)
            {
                throw new BuildException("No configuration could be loaded from --> '" + configurationUri + '\'');
            }
            throw new BuildException(throwable);
        }
        finally
        {
            // Set the context class loader back ot its system class loaders
            // so that any processes running after won't be trying to use
            // the ContextClassLoader for this class.
            Thread.currentThread().setContextClassLoader(ClassLoader.getSystemClassLoader());
        }
    }

    /**
     * Set the context class loader so that any classes using it (the
     * contextContextClassLoader) have access to the correct loader.
     */
    private final static void initializeContextClassLoader()
    {
        Thread.currentThread().setContextClassLoader(AndroMDAServerStopTask.class.getClassLoader());
    }
}