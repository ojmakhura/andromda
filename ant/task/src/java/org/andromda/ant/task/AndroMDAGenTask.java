package org.andromda.ant.task;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

import org.andromda.core.AndroMDA;
import org.andromda.core.common.ResourceUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.MatchingTask;


/**
 * <p/> This class wraps the AndroMDA model processor so that AndroMDA can be
 * used as an Ant task. Represents the <code>&lt;andromda&gt;</code> custom
 * task which can be called from an Ant build script.
 * </p>
 *
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen </a>
 * @author <a href="http://www.amowers.com">Anthony Mowers </a>
 * @author Chad Brandon
 * @see org.andromda.core.AndroMDA
 */
public class AndroMDAGenTask
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
     * <p/>
     * Starts the generation of source code from an object model. </p>
     * <p/>
     * This is the main entry point of the application when running Ant. It is called by ant whenever the surrounding
     * task is executed (which could be multiple times). </p>
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
            final AndroMDA andromda = AndroMDA.newInstance();
            if (andromda != null)
            {
                andromda.run(
                    this.replaceProperties(ResourceUtils.getContents(configurationUri)));
                andromda.shutdown();
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
     * Replaces all properties having the style
     * <code>${some.property}</code> with the value
     * of the specified property if there is one.
     *
     * @param fileContents the fileContents to perform replacement on.
     */
    protected String replaceProperties(String string)
    {
        final Map properties = this.getProject().getProperties();
        if (properties != null && !properties.isEmpty())
        {
            for (final Iterator iterator = properties.keySet().iterator(); iterator.hasNext();)
            {
                final String name = (String)iterator.next();
                final String property = "${" + name + '}';
                final String value = (String)properties.get(name);
                string = StringUtils.replace(string, property, value);
            }
        }

        // remove any left over property references
        string = this.removePropertyReferences(string);
        return string;
    }

    /**
     * The property reference pattern.
     */
    private static final String PROPERTY_REFERENCE = "\\$\\{.*\\}";

    /**
     * Removes any ${some.property} type references from the string
     * and returns the modifed string.
     * @param string the string from which to remove the property
     *        references
     *
     * @return the modified string.
     */
    public String removePropertyReferences(String string)
    {
        if (string != null)
        {
            string = string.replaceAll(PROPERTY_REFERENCE, "");
        }
        return string;
    }

    /**
     * Set the context class loader so that any classes using it (the contextContextClassLoader) have access to the
     * correct loader.
     */
    private final static void initializeContextClassLoader()
    {
        Thread.currentThread().setContextClassLoader(AndroMDAGenTask.class.getClassLoader());
    }
}