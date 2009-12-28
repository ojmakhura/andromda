package org.andromda.maven.plugin.bootstrap.install;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.model.Build;
import org.apache.maven.model.Model;
import org.apache.maven.model.Parent;
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
     * @parameter expression="${bootstrap.artifacts}"
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
     * @parameter expression="org.andromda"
     * @required
     * @readonly
     * @description the name of the project groupId.
     */
    protected String projectGroupId;

    /**
     * @parameter expression="org.andromda.bootstrap"
     * @required
     * @readonly
     * @description the name of the project bootstrap groupId.
     */
    protected String projectBootstrapGroupId;

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
                final File installDirectory = new File(this.installDirectory);
                if (!installDirectory.exists() || !installDirectory.isDirectory())
                {
                    throw new MojoExecutionException("'" + installDirectory + "' is not a valid install directory");
                }
                Artifact artifact = this.project.getArtifact();

                final String path = this.replaceExtension(
                        artifact,
                        JAR_EXTENSION);
                final String localRepositoryDirectory = this.localRepository.getBasedir();
                final File existingFile = new File(localRepositoryDirectory, path);
                final String bootstrapGroupId = this.getBootstrapGroupId(artifact);
                final String bootstrapPath = bootstrapGroupId.replace(
                        '.',
                        '/') + '/' + artifact.getArtifactId();
                final File bootstrapFile = new File(installDirectory, bootstrapPath + '.' + JAR_EXTENSION);
                this.getLog().info("Installing bootstrap artifact: " + bootstrapFile);
                FileUtils.copyFile(
                    existingFile,
                    bootstrapFile);
                final File bootstrapPomFile = new File(installDirectory, bootstrapPath + '.' + POM_EXTENSION);
                this.writeMinimalPom(bootstrapPomFile);
            }
            catch (final Throwable throwable)
            {
                throw new MojoExecutionException("Error creating bootstrap artifact", throwable);
            }
        }
    }

    /**
     * Clears the POM's model of its parent or any dependencies
     * it may have so that we can write a POM that isn't dependant on anything
     * (which we need for bootstrap artifacts).
     *
     * @param bootstrapPomFile the bootstrap POM file to write.
     */
    private void writeMinimalPom(final File bootstrapPomFile)
        throws MojoExecutionException, IOException
    {
        if (this.project != null)
        {
            Model model = this.project.getModel();
            if (model == null)
            {
                throw new MojoExecutionException("Model could not be retrieved from current project");
            }

            // - remove the parent
            final Parent parent = model.getParent();
            final List dependencies = new ArrayList(model.getDependencies());
            final String groupId = model.getGroupId();
            final Artifact artifact = this.project.getArtifact();
            final Build build = this.project.getBuild();
            final List developers = new ArrayList(model.getDevelopers());
            final List contributors = new ArrayList(model.getContributors());
            model.setGroupId(this.getBootstrapGroupId(artifact));
            model.setParent(null);
            model.setBuild(null);
            // Maven 2.2.1: Collections.emptyList() causes "setDependencies(List<Dependency>) in the type ModelBase is not applicable for the arguments (List<Object>)"
            model.setDependencies(Collections.EMPTY_LIST);
            model.setDevelopers(Collections.EMPTY_LIST);
            model.setContributors(Collections.EMPTY_LIST);
            final FileWriter fileWriter = new FileWriter(bootstrapPomFile);
            this.project.writeModel(fileWriter);
            fileWriter.flush();

            // - set any removed items back to the way it was since we've written the POM
            model.setParent(parent);
            model.setGroupId(groupId);
            model.setDependencies(dependencies);
            model.setBuild(build);
            model.setDevelopers(developers);
            model.setContributors(contributors);
        }
    }

    /**
     * Retrieves the project's bootstrap groupId from the given <code>artifact</code>.
     *
     * @param artifact the artfact from which to retrieve the group Id.
     * @return the bootstrap groupId.
     */
    private String getBootstrapGroupId(final Artifact artifact)
    {
        return StringUtils.replaceOnce(
            artifact.getGroupId(),
            this.projectGroupId,
            this.projectBootstrapGroupId);
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