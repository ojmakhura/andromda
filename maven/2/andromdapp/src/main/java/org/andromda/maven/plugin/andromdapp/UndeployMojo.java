package org.andromda.maven.plugin.andromdapp;

import java.io.File;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.FileUtils;


/**
 * Provides the undeployment of applications from a given directory.
 *
 * @goal undeploy
 * @author Chad Brandon
 */
public class UndeployMojo
    extends AppManagementMojo
{
    /**
     * @see org.apache.maven.plugin.AbstractMojo#execute()
     */
    public void execute()
        throws MojoExecutionException, MojoFailureException
    {
        final File deployDirectory = new File(this.deployLocation);
        if (deployDirectory.exists() && deployDirectory.isDirectory())
        {
            try
            {
                final File deployFile = this.getDeployFile();
                this.getLog().info("Undeploying " + deployFile + " from " + deployDirectory);
                if (deployFile.isDirectory())
                {
                    FileUtils.deleteDirectory(deployFile);
                }
                else
                {
                    deployFile.delete();
                }
            }
            catch (final Throwable throwable)
            {
                throw new MojoExecutionException("An error occurred while attempting to undeploy artifact", throwable);
            }
        }
        else
        {
            this.getLog().warn(
                "Undeploy did not occur because the specified deployLocation '" + deployLocation +
                "' does not exist, or is not a directory");
        }
    }
}