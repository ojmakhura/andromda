package org.andromda.maven.plugin.andromdapp;

import java.net.URL;
import java.net.URLClassLoader;

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
        if (this.skipProcessing)
        {
            getLog().info("andromdapp:clean skipProcessing");
            return;
        }
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
        catch (final NoClassDefFoundError ncdfe)
        {
        	ClassLoader cl = this.getClass().getClassLoader();
        	// Its a RealmClassLoader but its a private class and so cant reference grrr !!!
        	// but it extends URLClassLoader
        	System.err.println( "classloader:" + cl );
        	if ( cl instanceof URLClassLoader ) {
        		URLClassLoader ucl = (URLClassLoader) cl;
        		URL [] urls = ucl.getURLs();
        		for (int i = 0; i < urls.length; i++) {
					System.err.println( "cp:" + urls[i] );
				}
        	}
        	System.out.println( "Exception:" + ncdfe );
        	ncdfe.printStackTrace(System.err);
        	Throwable th = ncdfe.getCause();
        	while ( th != null ) {
        		System.err.println( "================= cause =================");
        		th.printStackTrace(System.err);
        		th = th.getCause();
        	}
        }
        catch (final Throwable throwable)
        {
            if (throwable instanceof MojoExecutionException)
            {
                throw (MojoExecutionException)throwable;
            }
            throw new MojoExecutionException("An error occurred during application cleanup operation", throwable);
        }
    }
}