package org.andromda.maven.plugin.andromdapp;

import java.net.URL;

import org.andromda.andromdapp.AndroMDApp;
import org.andromda.core.common.ResourceUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;


/**
 * AndroMDA application generator Mojo.
 *
 * @author Chad Brandon
 * @goal generate
 * @requiresProject false
 */
public class AndroMDAppMojo
    extends AbstractMojo
{
    /**
     * An AndroMDApp configuration that contains some internal configuration information (like the AndroMDA
     * version, etc).
     */
    private static final String INTERNAL_CONFIGURATION_URI = "META-INF/andromdapp/configuration.xml";
    
    /**
     * The URI to the optional AndroMDApp configuration file.
     *
     * @parameter expression="${configuration.uri}"
     */
    private String configurationUri;
    
    /**
     * @see org.apache.maven.plugin.Mojo#execute()
     */
    public void execute()
        throws MojoExecutionException, MojoFailureException
    {
        try
        {
            AndroMDApp andromdapp = new AndroMDApp();
            final URL configuration = ResourceUtils.getResource(INTERNAL_CONFIGURATION_URI);
            if (configuration == null)
            {
                throw new MojoExecutionException("No configuration could be loaded from --> '" + INTERNAL_CONFIGURATION_URI + "'");
            }
            andromdapp.addConfigurationUri(configuration.toString());
            andromdapp.addConfigurationUri(this.configurationUri);
            andromdapp.run();
        }
        catch (final Throwable throwable)
        {
            if (throwable instanceof MojoExecutionException)
            {
                throw (MojoExecutionException)throwable;
            }
            throw new MojoExecutionException("An error occurred while attempting to generate an application", throwable);
        }
    }

}