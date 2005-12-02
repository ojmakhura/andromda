package org.andromda.maven.plugin.andromdapp;

import java.io.File;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;


/**
 * An abstract Mojo for app server management.
 *
 * @author Chad Brandon
 */
public abstract class AppManagementMojo
    extends AbstractMojo
{
    /**
     * The location (i.e. path) to deploy.
     *
     * @parameter
     * @required
     */
    protected String deployLocation;

    /**
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    protected MavenProject project;
    
    /**
     * Attempts to retrieve the packaging of the current project, and if it can't
     * find it, throws an exception.
     * 
     * @return the packaging.
     * @throws MojoExecutionException if no packaging was found.
     */
    protected String getPackaging() throws MojoExecutionException
    {
        final String packaging = this.project.getPackaging();
        if (packaging == null || packaging.trim().length() == 0)
        {
            throw new MojoExecutionException(
                "This project must have the packaging defined, when attempting to deploy exploded");
        }
        return packaging;
    }
    
    /**
     * Retrieves the file that will be or is deployed.
     * 
     * @return the deploy file.
     * @throws MojoExecutionException
     */
    protected File getDeployFile() throws MojoExecutionException
    {
        return new File(this.deployLocation, this.project.getBuild().getFinalName() + '.' + this.getPackaging());
    }
}