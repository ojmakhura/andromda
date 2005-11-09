package org.andromda.maven.plugin.cartridge;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.lang.StringUtils;
import org.apache.maven.archiver.MavenArchiveConfiguration;
import org.apache.maven.archiver.MavenArchiver;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.archiver.jar.JarArchiver;
import org.codehaus.plexus.util.FileUtils;


/**
 * Provides the archiving of AndroMDA cartridges.
 *
 * @author Chad Brandon
 * @goal andromda-cartridge
 * @phase package
 * @requiresProject
 * @description builds an AndroMDA cartridge.
 */
public class CartridgeArchiverMojo
    extends AbstractMojo
{
    /**
     * Directory that resources are copied to during the build.
     *
     * @parameter expression="${project.build.directory}"
     * @required
     */
    private String workDirectory;

    /**
     * The directory for the generated cartridge.
     *
     * @parameter expression="${project.build.outputDirectory}"
     * @required
     */
    private String outputDirectory;

    /**
     * The name of the cartridge file to generate.
     *
     * @parameter alias="modelName" expression="${project.build.finalName}"
     * @required
     * @readonly
     */
    private String finalName;

    /**
     * The maven project.
     *
     * @parameter expression="${project}"
     * @required
     * @readonly
     * @description "the maven project to use"
     */
    private MavenProject project;

    /**
     * The Jar archiver.
     *
     * @parameter expression="${component.org.codehaus.plexus.archiver.Archiver#jar}"
     * @required
     */
    private JarArchiver jarArchiver;

    /**
     * The maven archiver to use.
     *
     * @parameter
     */
    private MavenArchiveConfiguration archive = new MavenArchiveConfiguration();

    /**
     * The libraries to include in the cartridge.
     *
     * @parameter
     */
    private CartridgeArtifact[] artifacts;

    /**
     *  The patterns indicating what will be included in the cartridge.
     */
    private static final String[] OUTPUT_INCLUDES = new String[] {"**/*"};

    /**
     * @see org.apache.maven.plugin.Mojo#execute()
     */
    public void execute()
        throws MojoExecutionException, MojoFailureException
    {
        getLog().debug(" ======= CartridgeArchiverMojo settings =======");
        getLog().debug("workDirectory[" + this.workDirectory + "]");
        getLog().debug("outputDirectory[" + this.outputDirectory + "]");
        getLog().debug("finalName[" + finalName + "]");
        final File buildDirectory = this.getBuildDirectory();
        final File outputDirectory = new File(this.outputDirectory);
        try
        {
            if (artifacts != null)
            {
                for (int ctr = 0; ctr < artifacts.length; ctr++)
                {
                    final CartridgeArtifact cartridgeArtifact = artifacts[ctr];
                    final Artifact artifact = this.resolveArtifact(cartridgeArtifact);
                    if (artifact != null)
                    {
                        final String path = cartridgeArtifact.getPath();
                        if (path == null || path.trim().length() == 0)
                        {
                            throw new MojoFailureException("Please specify the 'path' for the cartridge artifact [" +
                                cartridgeArtifact.getGroupId() + ":" + cartridgeArtifact.getArtifactId() + ":" +
                                cartridgeArtifact.getType() + "]");
                        }

                        final File destinationDirectory = new File(outputDirectory,
                                cartridgeArtifact.getPath());
                        final File artifactFile = artifact.getFile();
                        final String artifactPath =
                            artifactFile != null
                            ? StringUtils.replace(
                                artifact.getFile().toString().replaceAll(
                                    ".*(\\\\|/)",
                                    ""),
                                '-' + artifact.getVersion(),
                                "") : null;
                        if (artifactPath != null)
                        {
                            FileUtils.copyFile(
                                artifactFile,
                                new File(
                                    destinationDirectory,
                                    artifactPath));
                        }
                    }
                }
            }
            final File cartridgeFile = new File(buildDirectory, this.finalName + ".jar");
            final Artifact artifact = this.project.getArtifact();
            final MavenArchiver archiver = new MavenArchiver();
            archiver.setArchiver(this.jarArchiver);
            archiver.setOutputFile(cartridgeFile);
            archiver.getArchiver().addDirectory(
                outputDirectory,
                OUTPUT_INCLUDES,
                null);
            archiver.createArchive(
                this.project,
                this.archive);
            artifact.setFile(cartridgeFile);
        }
        catch (final Throwable throwable)
        {
            throw new MojoExecutionException("An error occured while packaging this cartridge", throwable);
        }
    }

    /**
     * Resolves the actual Maven artifact for the given cartridge artifact.
     * @param cartridgeArtifact the cartridge artifact.
     * @return
     * @throws MojoFailureException
     */
    public Artifact resolveArtifact(final CartridgeArtifact cartridgeArtifact)
        throws MojoFailureException
    {
        Artifact resolvedArtifact = null;
        if (cartridgeArtifact != null)
        {
            final Collection artifacts = this.project.getArtifacts();
            final String groupId = cartridgeArtifact.getGroupId();
            final String artifactId = cartridgeArtifact.getArtifactId();
            final String type = cartridgeArtifact.getType();
            if (groupId == null || artifactId == null)
            {
                throw new MojoFailureException("Could not resolve cartridge artifact [" + groupId + ":" + artifactId +
                    ":" + type + "]");
            }

            for (final Iterator iterator = artifacts.iterator(); iterator.hasNext();)
            {
                final Artifact artifact = (Artifact)iterator.next();
                if (artifact.getGroupId().equals(groupId) && artifact.getArtifactId().equals(artifactId) &&
                    (type == null || artifact.getType().equals(type)))
                {
                    resolvedArtifact = artifact;
                    break;
                }
            }

            if (resolvedArtifact == null)
            {
                // Artifact has not been found
                throw new MojoFailureException("Artifact[" + groupId + ":" + artifactId + ":" + type + "] " +
                    "is not a dependency of the project.");
            }
            return resolvedArtifact;
        }
        return resolvedArtifact;
    }

    /**
     * The build directory.
     */
    private File buildDirectory;

    /**
     * Gets the current build directory as a file instance.
     *
     * @return the build directory as a file.
     */
    protected File getBuildDirectory()
    {
        if (this.buildDirectory == null)
        {
            this.buildDirectory = new File(workDirectory);
        }
        return this.buildDirectory;
    }
}