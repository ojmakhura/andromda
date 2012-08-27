package org.andromda.maven.plugin.bootstrap.install;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Provides the installation of bootstrap artifacts.
 *
 * @author Chad Brandon
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
     * @description whether or not bootstrap artifacts should be installed, by default they are.
     */
    protected boolean installBootstraps = true;

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
     * The qualifier for source artifacts.
     */
    private static final String SOURCES_QUALIFIER = "-sources";

    /**
     * The qualifier for javadoc artifacts.
     */
    private static final String JAVADOC_QUALIFIER = "-javadoc";

    /**
     * The qualifier for test artifacts.
     */
    private static final String TEST_QUALIFIER = "-test";

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

                final String path = this.replaceExtension(artifact, JAR_EXTENSION, null);
                final String localRepositoryDirectory = this.localRepository.getBasedir();
                final File existingFile = new File(localRepositoryDirectory, path);
                final String bootstrapGroupId = this.getBootstrapGroupId(artifact);
                final String bootstrapPath = bootstrapGroupId.replace('.', '/')
                    + '/' + artifact.getArtifactId();
                final File bootstrapPomFile = new File(installDirectory, bootstrapPath + '.' + POM_EXTENSION);
                this.writeMinimalPom(bootstrapPomFile);
                final File bootstrapFile = new File(installDirectory, bootstrapPath + '.' + JAR_EXTENSION);
                this.getLog().info("Installing bootstrap artifact: " + bootstrapFile);
                if (path != null)
                {
                    FileUtils.copyFile(existingFile, bootstrapFile);
                }
                final String sourcePath = this.replaceExtension(artifact, JAR_EXTENSION, SOURCES_QUALIFIER);
                if (sourcePath != null)
                {
                    this.getLog().info("Installing bootstrap artifact: " + sourcePath);
                    FileUtils.copyFile(new File(localRepositoryDirectory, sourcePath), new File(installDirectory, bootstrapPath + SOURCES_QUALIFIER + '.' + JAR_EXTENSION));
                }
                final String javadocPath = this.replaceExtension(artifact, JAR_EXTENSION, JAVADOC_QUALIFIER);
                if (javadocPath != null)
                {
                    this.getLog().info("Installing bootstrap artifact: " + javadocPath);
                    FileUtils.copyFile(new File(localRepositoryDirectory, javadocPath), new File(installDirectory, bootstrapPath + JAVADOC_QUALIFIER + '.' + JAR_EXTENSION));
                }
                final String testSourcePath = this.replaceExtension(artifact, JAR_EXTENSION, TEST_QUALIFIER + SOURCES_QUALIFIER);
                if (testSourcePath != null)
                {
                    this.getLog().info("Installing bootstrap artifact: " + testSourcePath);
                    FileUtils.copyFile(new File(localRepositoryDirectory, testSourcePath), new File(installDirectory, bootstrapPath + TEST_QUALIFIER + SOURCES_QUALIFIER + '.' + JAR_EXTENSION));
                }
                final String testJavadocPath = this.replaceExtension(artifact, JAR_EXTENSION, TEST_QUALIFIER + JAVADOC_QUALIFIER);
                if (testJavadocPath != null)
                {
                    this.getLog().info("Installing bootstrap artifact: " + testJavadocPath);
                    FileUtils.copyFile(new File(localRepositoryDirectory, testJavadocPath), new File(installDirectory, bootstrapPath + TEST_QUALIFIER + JAVADOC_QUALIFIER + '.' + JAR_EXTENSION));
                }
            }
            catch (final Throwable throwable)
            {
                throw new MojoExecutionException("Error creating bootstrap artifact", throwable);
            }
        }
    }

    /**
     * Clears the POM's model of its parent or any dependencies
     * it may have so that we can write a POM that isn't dependent on anything
     * (which we need for bootstrap artifacts).
     *
     * @param bootstrapPomFile the bootstrap POM file to write.
     */
    private void writeMinimalPom(final File bootstrapPomFile)
            throws IOException
    {
        final Model model = this.project.getModel();

        final MavenProject minimalProject = new MavenProject();
        final Model minModel = minimalProject.getModel();

        minModel.setGroupId(getBootstrapGroupId(this.project.getArtifact()));
        minModel.setArtifactId(model.getArtifactId());
        minModel.setVersion(model.getVersion());
        minModel.setName(model.getName());
        minModel.setPackaging("jar");
        minModel.setDescription(model.getDescription());
        minModel.setModelEncoding(model.getModelEncoding());
        minModel.setModelVersion(model.getModelVersion());
        minModel.setUrl(model.getUrl());
        minModel.setScm(model.getScm());
        minModel.setDevelopers(model.getDevelopers());
        minModel.setCiManagement(model.getCiManagement());
        minModel.setIssueManagement(model.getIssueManagement());

        minModel.setPrerequisites(model.getPrerequisites());
        minModel.setOrganization(model.getOrganization());
        minModel.setInceptionYear(model.getInceptionYear());
        minModel.setLicenses(model.getLicenses());

        final List<Dependency> dependencies = new ArrayList<Dependency>(model.getDependencies());
        // filter all of andromda dependencies away
        CollectionUtils.filter(dependencies, new Predicate()
        {
            public boolean evaluate(Object object)
            {
                Dependency dependency = (Dependency)object;
                final String lGroupId = dependency.getGroupId();
                return !lGroupId.startsWith("org.andromda") || lGroupId.startsWith("org.andromda.thirdparty");
            }
        });
        minModel.setDependencies(dependencies);

        final FileWriter fileWriter = new FileWriter(bootstrapPomFile);
        minimalProject.writeModel(fileWriter);
        fileWriter.flush();
    }

    /**
     * Retrieves the project's bootstrap groupId from the given <code>artifact</code>.
     *
     * @param artifact the artifact from which to retrieve the group Id.
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
     * Retrieves the repository artifact file name with extension removed and qualifier added.
     * If the file does not yet exist in the local repository, returns null
     *
     * @param artifact     the artifact from which to retrieve the version information.
     * @param newExtension new extension for the file
     * @param qualifier the -qualifier for the repo artifact such as -sources -javadocs -test-sources -test-javadocs
     * @return the artifact name with no version and extension removed.
     */
    private String replaceExtension(
            final Artifact artifact,
            final String newExtension,
            final String qualifier)
    {
        String path = this.localRepository.pathOf(artifact);
        File artifactFile = new File(this.localRepository.getBasedir(), path);
        if (!artifactFile.exists())
        {
            this.getLog().error("Bootstrap artifact does not exist: " + path);
            return null;
        }
        final String version = artifact.getVersion() != null ? artifact.getVersion().trim() : "";
        int versionIndex = path.lastIndexOf(artifact.getVersion());
        final String extension = path.substring(
                versionIndex + version.length() + 1,
                path.length());
        int extensionIndex = path.lastIndexOf(extension);
        if (StringUtils.isNotBlank(qualifier))
        {
            path = path.substring(0, extensionIndex-1) + qualifier + '.' + extension;
            File qualifiedFile = new File(this.localRepository.getBasedir(), path);
            if (!qualifiedFile.exists())
            {
                this.getLog().warn("Bootstrap qualified artifact does not exist: " + path);
                return null;
            }
        }
        if (!newExtension.equals(extension))
        {
            path = path.substring(0, extensionIndex) + newExtension;
        }
        return path;
    }
}