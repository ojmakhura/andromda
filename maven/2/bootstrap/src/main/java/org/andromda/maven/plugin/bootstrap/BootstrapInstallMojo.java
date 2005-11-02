package org.andromda.maven.plugin.bootstrap;

import java.io.File;

import org.apache.commons.lang.StringUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.FileUtils;


/**
 * Provides the installation of bootstrap artifacts.
 *
 * @author Chad Brandon
 *
 * @goal install
 * @phase install
 */
public class BootstrapInstallMojo
    extends AbstractMojo
{
    /**
     * The maven project.
     *
     * @parameter expression="${project}"
     * @required
     * @readonly
     * @description "the maven project to use"
     */
    protected MavenProject project;

    /**
     * @parameter expression="${localRepository}"
     * @required
     * @readonly
     */
    protected ArtifactRepository localRepository;

    /**
     * @parameter expression="${installBootstraps}"
     * @description whether or not bootstrap artifacts should be installed, by default they are not.
     */
    protected boolean installBootstraps;

    /**
     * @parameter
     * @required
     * @description the directory to which the bootstrap artifact should be installed.
     */
    protected String installDirectory;

    /**
     * @parameter expression="bootstrap"
     * @required
     * @readonly
     * @description the name root directory of the bootstrap artifacts.
     */
    protected String bootstrapDirectoryName;

    /**
     * The extension for "JAR" files.
     */
    private static final String JAR_EXTENSION = "jar";

    /**
     * The extension for "POM" files.
     */
    private static final String POM_EXTENSION = "pom";

    /**
     * @see org.apache.maven.plugin.Mojo#execute()
     */
    public void execute()
        throws MojoExecutionException, MojoFailureException
    {
        if (this.installBootstraps)
        {
            try
            {
                Artifact artifact = this.project.getArtifact();

                final String path = this.replaceExtension(
                        artifact,
                        JAR_EXTENSION);
                final String localRepositoryDirectory = this.localRepository.getBasedir();
                final File existingFile = new File(localRepositoryDirectory, path);
                final String bootstrapPath =
                    this.bootstrapDirectoryName + '/' +
                    StringUtils.trimToEmpty(artifact.getGroupId()).replace(
                        '.',
                        '/') + '/' + artifact.getArtifactId();
                final File bootstrapFile = new File(this.installDirectory, bootstrapPath + '.' + JAR_EXTENSION);
                this.getLog().info("Installing bootstrap artifact: " + bootstrapFile);
                FileUtils.copyFile(
                    existingFile,
                    bootstrapFile);
                final File existingPomFile =
                    new File(localRepositoryDirectory,
                        this.replaceExtension(
                            artifact,
                            POM_EXTENSION));
                final File bootstrapPomFile = new File(this.installDirectory, bootstrapPath + '.' + POM_EXTENSION);
                this.getLog().info("Installing bootstrap artifact jar: " + bootstrapPomFile);
                FileUtils.copyFile(
                    existingPomFile,
                    bootstrapPomFile);
            }
            catch (final Throwable throwable)
            {
                throw new MojoExecutionException("Error creating bootstrap artifact", throwable);
            }
        }
    }

    /**
     * Retrieves the extension from the given path.
     * @param artifact the artifact from which to retrieve the version information.
     * @param path the path of the file
     * @return the extension.
     */
    private String replaceExtension(
        final Artifact artifact,
        final String newExtension)
    {
        String path = this.localRepository.pathOf(artifact);
        final String version = artifact.getVersion() != null ? artifact.getVersion().trim() : "";
        int versionIndex = path.lastIndexOf(artifact.getVersion());
        final String extension = path.substring(
                versionIndex + version.length() + 1,
                path.length());
        if (!newExtension.equals(extension))
        {
            int extensionIndex = path.lastIndexOf(extension);
            path = path.substring(
                    0,
                    extensionIndex) + newExtension;
        }
        return path;
    }
}