package org.andromda.maven.plugin.andromdapp;

import org.andromda.andromdapp.AndroMDApp;
import org.apache.maven.plugin.MojoExecutionException;


/**
 * Removes the an AndroMDApp generated application structure.
 *
 * @author Chad Brandon
 * @goal clean-structure
 * @requiresProject false
 */
public class AndroMDAppCleanMojo
    extends AbstractAndroMDAppMojo
{
    /**
     * @see org.apache.maven.plugin.Mojo#execute()
     */
    public void execute()
        throws MojoExecutionException
    {
        try
        {
            final AndroMDApp andromdapp = new AndroMDApp();
            final String configuration = this.getConfigurationContents();
            if (configuration != null)
            {
                andromdapp.addConfiguration(this.getConfigurationContents());
            }
            andromdapp.clean();
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