package org.andromda.maven.plugin.site;

import java.io.File;
import java.io.FilenameFilter;
import java.net.URL;

import org.andromda.core.common.ResourceUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.maven.project.MavenProject;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * Goal that copies the required AndroMDA site files to suitable locations preparing
 * for deployment.
 * 
 * @phase site
 * @goal copy-documentation
 * @description Goal to copy required site files prior to site deployment
 * @author vancek
 */
public class CopyDocumentationMojo
    extends AbstractSiteMojo
{
    /**
     * Path to the mapping source directory containing the mappings
     * 
     * @parameter expression="${basedir}/../../etc/mappings"
     */
    private String mappingsSourceDirectory;
    
    /**
     * Path to the mapping destination directory
     * 
     * @parameter expression="${basedir}/../../target/site/mappings"
     */
    private String mappingsOutputDirectory;
    
    /**
     * Path to the car-rental-system model
     * 
     * @parameter expression="${basedir}/../../samples/car-rental-system/mda/src/main/uml/CarRentalSystem.xml.zip"
     */
    private String carRentalSystemSourcePath;
    
    /**
     * Path to the destination directory to copy the car-rental-system model
     * 
     * @parameter expression="${basedir}/../../target/site"
     */
    private String carRentalSystemOutputDirectory;
    
    /**
     * Path to the animal-quiz model
     * 
     * @parameter expression="${basedir}/../../samples/animal-quiz/mda/src/main/uml/AnimalQuiz.xml.zip"
     */
    private String animalQuizSourcePath;
    
    /**
     * Path to the destination directory to copy the animal-quiz model
     * 
     * @parameter expression="${basedir}/../../target/site"
     */
    private String animalQuizOutputDirectory;
    
    /**
     * The directory containing the documentation site reporting artifacts
     * 
     * @parameter expression="${basedir}/target/site"
     */
    private String documentationSourceDirectory;
    
    /**
     * The documentation output directory used to copy the generated site reporting artifacts
     * 
     * @parameter expression="${basedir}/../../target/site"
     */
    private String documentationOutputDirectory;
    
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
        this.getLog().info("  A n d r o M D A   S i t e   D o c   C o p y");
        this.getLog().info("-----------------------------------------------------------------------------");

        this.copyAnimalQuizModel();
        this.copyCarRentalSystemModel();
        this.copyMappings();
        this.copyDocumentationReportArtifacts();
        
        this.getLog().info("SITE DOCUMENTATION COPY SUCCESSFUL");
    }
    
    /**
     * Copy the animal-quiz model for site reference
     * 
     * @throws MojoExecutionException
     * @throws MojoFailureException
     */
    private void copyAnimalQuizModel()
        throws MojoExecutionException, MojoFailureException
    {
        try
        {
            final File animalQuizSourceFile = new File(this.animalQuizSourcePath);
            if (!animalQuizSourceFile.exists())
            {
                throw new MojoExecutionException("The animal-quiz model location is invalid");
            }
            
            this.copyFile(
                    animalQuizSourceFile, 
                    new File(
                            this.animalQuizOutputDirectory, 
                            animalQuizSourceFile.getName()));
        }
        catch (final Throwable throwable)
        {
            if (throwable instanceof MojoExecutionException)
            {
                throw (MojoExecutionException)throwable;
            }
            throw new MojoExecutionException("An error occured copying the animal-quiz model '" +
                this.project.getArtifactId() + "'",
                ExceptionUtils.getRootCause(throwable));
        }
    }
    
    /**
     * Copy the car-rental-system model for site reference
     * 
     * @throws MojoExecutionException
     * @throws MojoFailureException
     */
    private void copyCarRentalSystemModel()
        throws MojoExecutionException, MojoFailureException
    {
        try
        {
            final File carRentalSystemSourceFile = new File(this.carRentalSystemSourcePath);
            if (!carRentalSystemSourceFile.exists())
            {
                throw new MojoExecutionException("The car-rental-system model location is invalid");
            }
            
            this.copyFile(
                    carRentalSystemSourceFile, 
                    new File(
                            this.carRentalSystemOutputDirectory,
                            carRentalSystemSourceFile.getName()));
        }
        catch (final Throwable throwable)
        {
            if (throwable instanceof MojoExecutionException)
            {
                throw (MojoExecutionException)throwable;
            }
            throw new MojoExecutionException("An error occured copying the car-rental-system model '" +
                this.project.getArtifactId() + "'",
                ExceptionUtils.getRootCause(throwable));
        }
    }
    
    /**
     * Copy the mapping files to site documentation location
     * 
     * @throws MojoExecutionException
     * @throws MojoFailureException
     */
    private void copyMappings()
        throws MojoExecutionException, MojoFailureException
    {
        try
        {
            final File mappingSourceDir = new File(this.mappingsSourceDirectory);
            if (!mappingSourceDir.exists())
            {
                throw new MojoExecutionException("Mapping source location is invalid");
            }
            
            final File[] files = mappingSourceDir.listFiles();
            for (int i = 0; i < files.length; i++)
            {
                // Ignore SVN dir/files
                if (!files[i].getName().equalsIgnoreCase(".svn"))
                {
                    this.copyFile(
                            files[i], 
                            new File(this.mappingsOutputDirectory, files[i].getName()));
                }
            }
        }
        catch (final Throwable throwable)
        {
            if (throwable instanceof MojoExecutionException)
            {
                throw (MojoExecutionException)throwable;
            }
            throw new MojoExecutionException("An error occured copying mappings '" +
                this.project.getArtifactId() + "'",
                ExceptionUtils.getRootCause(throwable));
        }
    }
    
    /**
     * Copy the documentation reporting artifacts
     * 
     * @throws MojoExecutionException
     * @throws MojoFailureException
     */
    private void copyDocumentationReportArtifacts()
        throws MojoExecutionException, MojoFailureException
    {
        try
        {
            final File documentationSourceDir = new File(this.documentationSourceDirectory);
            if (!documentationSourceDir.exists())
            {
                throw new MojoExecutionException("Documentation source location is invalid");
            }
            
            /**
             * Retrieve a directory listing with a filename filter
             */
            FilenameFilter filter = new FilenameFilter()
            {
                final String[] filteredReports = 
                {
                        ".svn",
                        "integration.html",
                        "dependencies.html",
                        "dependency-convergence.html",
                        "issue-tracking.html",
                        "mailing-lists.html",
                        "license.html",
                        "project-summary.html",
                        "team-list.html",
                        "source-repository.html"
                };

                public boolean accept(File dir, String name)
                {
                    boolean accept = true;
                    for (int i = 0; i < filteredReports.length; i++)
                    {
                        if (name.equalsIgnoreCase(filteredReports[i]))
                        {
                            accept =  false;
                        }
                    }
                    return accept;
                }
            };
            final File[] files = documentationSourceDir.listFiles(filter);
            for (int i = 0; i < files.length; i++)
            {
                this.copyFile(
                        files[i], 
                        new File(
                                this.documentationOutputDirectory, 
                                files[i].getName()));
            }
        }
        catch (final Throwable throwable)
        {
            if (throwable instanceof MojoExecutionException)
            {
                throw (MojoExecutionException)throwable;
            }
            throw new MojoExecutionException("An error occured copying documentation/reporting artifacts '" +
                this.project.getArtifactId() + "'",
                ExceptionUtils.getRootCause(throwable));
        }
    }
}
