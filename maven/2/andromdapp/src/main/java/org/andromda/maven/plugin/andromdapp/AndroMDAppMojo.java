package org.andromda.maven.plugin.andromdapp;

import org.andromda.andromdapp.AndroMDApp;
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
    public void execute()
        throws MojoExecutionException, MojoFailureException
    {
        try
        {
            AndroMDApp andromdapp = new AndroMDApp();
            andromdapp.run();
        }
        catch (final Throwable throwable)
        {
            throw new MojoExecutionException("An error occurred while attempting to generate an application", throwable);
        }
    }

}