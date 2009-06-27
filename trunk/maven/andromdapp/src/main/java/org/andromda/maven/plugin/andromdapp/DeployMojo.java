package org.andromda.maven.plugin.andromdapp;

import java.io.File;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.maven.model.Build;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.DirectoryScanner;


/**
 * Provides the deployment of applications to a given directory.
 *
 * @goal deploy
 * @phase package
 * @author Chad Brandon
 */
public class DeployMojo
    extends AppManagementMojo
{
    /**
     * Indicates whether or not this plugin should perform the deploy.
     *
     * @parameter expression="${deploy}"
     */
    private String deploy;

    /**
     * The string indicating whether or not the deploy should be exploded or not.
     */
    private static final String EXPLODED = "exploded";

    /**
     * Any additional files to include in the deploy liked datasource files etc
     * (the files must reside in the project build directory).
     * By default nothing besides the file artifact is deployed.
     *
     * @parameter
     */
    private String[] includes = new String[0];

    /**
     * Any files to exclude in the deploy.
     *
     * @parameter
     */
    private String[] excludes = new String[0];

    /**
     * @see org.apache.maven.plugin.AbstractMojo#execute()
     */
    public void execute()
        throws MojoExecutionException, MojoFailureException
    {
        File artifactFile = this.project.getArtifact().getFile();

        // - if we're deploying within a phase then deploy has to be set, otherwise
        //   its not needed (we know we're not deploying in a phase when the artifactFile is null).
        if (this.deploy != null && this.deploy.equals(Boolean.TRUE.toString()) || artifactFile == null)
        {
            final Build build = this.project.getBuild();
            if (EXPLODED.equalsIgnoreCase(this.deploy))
            {
                artifactFile = new File(
                        build.getDirectory(),
                        build.getFinalName());
            }
            else if (artifactFile == null)
            {
                artifactFile = new File(
                        build.getDirectory(),
                        build.getFinalName() + '.' + this.getPackaging());
            }
            if (artifactFile.exists())
            {
                final File deployFile = this.getDeployFile();
                final File deployDirectory = new File(this.deployLocation);
                if (deployDirectory.exists() && deployDirectory.isDirectory())
                {
                    try
                    {
                        if (EXPLODED.equalsIgnoreCase(this.deploy))
                        {
                            this.getLog().info("Deploying exploded " + artifactFile + " to " + deployFile);
                            FileUtils.copyDirectory(
                                artifactFile,
                                deployFile);
                        }
                        else
                        {
                            // - if the deploy file is a directory, then attempt to delete it first before we
                            //   attempting deploying
                            if (deployFile.exists() && deployFile.isDirectory())
                            {
                                this.getLog().info("Removing exploded artifact: " + deployFile);
                                FileUtils.deleteDirectory(deployFile);
                            }
                            final List deployFiles = this.getAdditionalFiles();
                            deployFiles.add(0, artifactFile);
                            for (final Iterator iterator = deployFiles.iterator(); iterator.hasNext();)
                            {
                                final File file = (File)iterator.next();
                                this.getLog().info("Deploying file " + file + " to " + deployDirectory);
                                FileUtils.copyFileToDirectory(
                                    file,
                                    deployDirectory);
                            }
                        }
                    }
                    catch (final Throwable throwable)
                    {
                        throw new MojoExecutionException("An error occurred while attempting to deploy artifact",
                            throwable);
                    }
                }
                else
                {
                    this.getLog().error(
                        "Deploy did not occur because the specified deployLocation '" + deployLocation +
                        "' does not exist, or is not a directory");
                }
            }
            else
            {
                this.getLog().warn("Deploy did not occur because file '" + artifactFile + "' does not exist");
            }
        }
    }

    /**
     * Retrieves any additional files to include in the deploy.
     *
     * @return all poms found.
     * @throws MojoExecutionException
     */
    private List getAdditionalFiles()
    {
        final DirectoryScanner scanner = new DirectoryScanner();
        scanner.setBasedir(this.project.getBuild().getDirectory());
        scanner.setIncludes(this.includes);
        scanner.setExcludes(this.excludes);
        scanner.scan();

        final List files = new ArrayList();
        for (int ctr = 0; ctr < scanner.getIncludedFiles().length; ctr++)
        {
            final File file = new File(
                    this.project.getBuild().getDirectory(),
                    scanner.getIncludedFiles()[ctr]);
            if (file.exists())
            {
                files.add(file);
            }
        }

        return files;
    }
}