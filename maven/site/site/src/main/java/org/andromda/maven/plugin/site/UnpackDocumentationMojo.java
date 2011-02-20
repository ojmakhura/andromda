package org.andromda.maven.plugin.site;

import java.io.File;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

/**
 * Goal that unpacks the required AndroMDA site zip packages to suitable locations preparing
 * for deployment.
 * 
 * @phase site
 * @goal unpack-documentation
 * @description Goal to unpack required site files prior to site deployment
 * @author vancek
 */
public class UnpackDocumentationMojo
    extends AbstractSiteMojo
{
    /**
     * Path to the JMI 1.4 API zip source
     * 
     * @parameter expression="${basedir}/src/site/resources/resources/jmi-uml1.4.zip"
     */
    private File jmiApiSourcePath;
    
    /**
     * Path to the JMI 1.4 API destination extraction directory
     * 
     * @parameter expression="${basedir}/../../target/site"
     */
    private File jmiApiOutputDirectory;
    
    /**
     * Path to the UmlDoc car-rental-sample zip source
     * 
     * @parameter expression="${basedir}/src/site/resources/resources/car-rental-umldoc.zip"
     */
    private File umlDocCarRentalSampleSourcePath;
    
    /**
     * Path to the UmlDoc car-rental-sample extraction directory
     * 
     * @parameter expression="${basedir}/../../target/site"
     */
    private File umlDocCarRentalSampleOutputDirectory;
    
    /**
     * The name of the project injected from pom.xml. Not used.
     * 
     * @parameter default-value="${project.name}"
     */
    @SuppressWarnings("unused")
    private String projectName;
    
    /**
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    protected MavenProject project;
    
    /**
     * @see org.apache.maven.plugin.Mojo#execute()
     */
    public void execute()
        throws MojoExecutionException, MojoFailureException
    {
        this.getLog().info("-----------------------------------------------------------------------------");
        this.getLog().info("  A n d r o M D A   S i t e   D o c   U n p a c k");
        this.getLog().info("-----------------------------------------------------------------------------");

        this.unpackUmlDocCarRentalSample();
        this.unpackJmiApi();
        
        this.getLog().info("SITE DOCUMENTATION UNPACK SUCCESSFUL");
    }
    
    /**
     * Unpack the JMI 1.4 API
     * 
     * @throws MojoExecutionException
     * @throws MojoFailureException
     */
    private void unpackJmiApi()
        throws MojoExecutionException, MojoFailureException
    {
        try
        {
            if (!this.jmiApiSourcePath.exists())
            {
                throw new MojoExecutionException("JMI API source location is invalid");
            }
            
            this.unpack(
                    this.jmiApiSourcePath,
                    this.jmiApiOutputDirectory);
        }
        catch (final Throwable throwable)
        {
            if (throwable instanceof MojoExecutionException)
            {
                throw (MojoExecutionException)throwable;
            }
            throw new MojoExecutionException("An error occured unpacking JMI 1.4 API '" +
                this.project.getArtifactId() + '\'',
                ExceptionUtils.getRootCause(throwable));
        }
    }
    
    /**
     * Unpack the UmlDoc for the car-rental-sample
     * 
     * @throws MojoExecutionException
     * @throws MojoFailureException
     */
    private void unpackUmlDocCarRentalSample()
        throws MojoExecutionException, MojoFailureException
    {
        try
        {
            if (!this.umlDocCarRentalSampleSourcePath.exists())
            {
                throw new MojoExecutionException("UmlDoc car-rental-sample source location is invalid");
            }
            
            this.unpack(
                    this.umlDocCarRentalSampleSourcePath,
                    this.umlDocCarRentalSampleOutputDirectory);
        }
        catch (final Throwable throwable)
        {
            if (throwable instanceof MojoExecutionException)
            {
                throw (MojoExecutionException)throwable;
            }
            throw new MojoExecutionException("An error occured unpacking UmlDoc for car-rental-sample '" +
                this.project.getArtifactId() + '\'',
                ExceptionUtils.getRootCause(throwable));
        }
    }
}
