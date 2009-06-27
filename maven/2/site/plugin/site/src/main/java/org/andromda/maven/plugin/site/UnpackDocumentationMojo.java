package org.andromda.maven.plugin.site;

import java.io.File;
import java.net.URL;

import org.andromda.core.common.ResourceUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.maven.project.MavenProject;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

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
     * @parameter expression="${basedir}/src/site/resources/jmi-uml1.4.zip"
     */
    private String jmiApiSourcePath;
    
    /**
     * Path to the JMI 1.4 API destination extraction directory
     * 
     * @parameter expression="${basedir}/../../target/site"
     */
    private String jmiApiOutputDirectory;
    
    /**
     * Path to the UmlDoc car-rental-sample zip source
     * 
     * @parameter expression="${basedir}/src/site/resources/car-rental-umldoc.zip"
     */
    private String umlDocCarRentalSampleSourcePath;
    
    /**
     * Path to the UmlDoc car-rental-sample extraction directory
     * 
     * @parameter expression="${basedir}/../../target/site"
     */
    private String umlDocCarRentalSampleOutputDirectory;
    
    /**
     * The name of the project injected from pom.xml
     * 
     * @parameter default-value="${project.name}"
     */
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
            final File jmiApiSourceFile = new File(this.jmiApiSourcePath);
            if (!jmiApiSourceFile.exists())
            {
                throw new MojoExecutionException("JMI API source location is invalid");
            }
            
            this.unpack(
                    jmiApiSourceFile, 
                    new File(this.jmiApiOutputDirectory));
        }
        catch (final Throwable throwable)
        {
            if (throwable instanceof MojoExecutionException)
            {
                throw (MojoExecutionException)throwable;
            }
            throw new MojoExecutionException("An error occured unpacking JMI 1.4 API '" +
                this.project.getArtifactId() + "'",
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
            final File umlDocCarRentalSampleSourceFile = new File(this.umlDocCarRentalSampleSourcePath);
            if (!umlDocCarRentalSampleSourceFile.exists())
            {
                throw new MojoExecutionException("UmlDoc car-rental-sample source location is invalid");
            }
            
            this.unpack(
                    umlDocCarRentalSampleSourceFile, 
                    new File(this.umlDocCarRentalSampleOutputDirectory));
        }
        catch (final Throwable throwable)
        {
            if (throwable instanceof MojoExecutionException)
            {
                throw (MojoExecutionException)throwable;
            }
            throw new MojoExecutionException("An error occured unpacking UmlDoc for car-rental-sample '" +
                this.project.getArtifactId() + "'",
                ExceptionUtils.getRootCause(throwable));
        }
    }
}
