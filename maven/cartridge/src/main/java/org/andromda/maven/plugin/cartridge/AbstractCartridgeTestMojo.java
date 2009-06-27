package org.andromda.maven.plugin.cartridge;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Plugin;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.settings.Settings;
import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.archiver.UnArchiver;
import org.codehaus.plexus.archiver.manager.ArchiverManager;
import org.codehaus.plexus.util.FileUtils;


/**
 * Abstract base mojo for cartridge tests. Must be subclassed.
 *
 * @author Chad Brandon
 * @author Peter Friese
 * @author Bob Fields
 */
public abstract class AbstractCartridgeTestMojo
    extends AbstractMojo
{
    /**
     * Base directory to which the cartridge test report is written
     *
     * @parameter expression="${project.build.directory}/cartridge-test/reports"
     */
    protected String reportDirectory;

    /**
     * Specifies the directory that contains the "actual" output (meaning the output
     * that was currently generated)
     * @parameter expression="${project.build.directory}/cartridge-test/actual"
     * @required
     */
    protected String actualDirectory;

    /**
     * Specifies the directory that contains the "expected" output.
     * @parameter expression="${project.build.directory}/cartridge-test/expected"
     * @required
     */
    protected String expectedDirectory;

    /**
     * The location of the archive storing the expected output.
     * @parameter expression="${basedir}/src/test/expected/cartridge-output.zip"
     * @required
     */
    protected String expectedOutputArchive;

    /**
     * This is the URI to the AndroMDA configuration file.
     *
     * @parameter expression="file:${basedir}/conf/test/andromda.xml"
     * @required
     */
    protected String configurationUri;

    /**
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    protected MavenProject project;

    /**
     * @parameter expression="${project.build.filters}"
     */
    protected List propertyFiles;

    /**
     * The current user system settings for use in Maven. (allows us to pass the user
     * settings to the AndroMDA configuration).
     *
     * @parameter expression="${settings}"
     * @required
     * @readonly
     */
    protected Settings settings;

    /**
     * Defines the extensions of binary files, binary files are checked for presence
     * and equality, however they aren't compared as strings, like every other file.
     *
     * @parameter expression="jpg,jpeg,gif,png,jar,zip"
     */
    protected String binaryOutputSuffixes;

    /**
     * To look up Archiver/UnArchiver implementations
     *
     * @parameter expression="${component.org.codehaus.plexus.archiver.manager.ArchiverManager}"
     * @required
     */
    protected ArchiverManager archiverManager;

    /**
     * The registered plugin implementations.
     *
     * @parameter expression="${project.build.plugins}"
     * @required
     * @readonly
     */
    protected List plugins;

    /**
     * @parameter expression="${component.org.apache.maven.artifact.factory.ArtifactFactory}"
     * @required
     * @readonly
     */
    protected ArtifactFactory factory;

    /**
     * @parameter expression="${localRepository}"
     * @required
     * @readonly
     */
    protected ArtifactRepository localRepository;

    /**
     * Set this to 'true' to bypass cartridge tests entirely. Its use is NOT RECOMMENDED, but quite convenient on occasion.
     *
     * @parameter expression="${maven.test.skip}"
     */
    protected boolean skip;

    /**
     *  Set this to 'true' to skip running tests, but still compile them. Its use is NOT RECOMMENDED, but quite convenient on occasion. 
     *
     * @parameter expression="${skipTests}"
     */
    protected boolean skipTests;

    /**
     * Set this to true to ignore a failure during testing. Its use is NOT RECOMMENDED, but quite convenient on occasion.
     *
     * @parameter expression="${maven.test.failure.ignore}" default-value="false"
     */
    protected boolean testFailureIgnore;

    /**
     * Adds any dependencies for the cartridge plugin
     * to the current dependencies of the project.
     */
    protected void addCartridgeTestDependencies()
    {
        if (this.plugins != null && !this.plugins.isEmpty())
        {
            for (final Iterator iterator = this.plugins.iterator(); iterator.hasNext();)
            {
                final Plugin plugin = (Plugin)iterator.next();
                if (Constants.ARTIFACT_ID.equals(plugin.getArtifactId()))
                {
                    final List dependencies = plugin.getDependencies();
                    if (dependencies != null)
                    {
                        for (final Iterator dependencyIterator = plugin.getDependencies().iterator();
                            dependencyIterator.hasNext();)
                        {
                            final Dependency dependency = (Dependency)dependencyIterator.next();
                            dependency.setScope(Artifact.SCOPE_RUNTIME);
                            this.addDependency(dependency);
                        }
                    }
                }
            }
        }

        // - get all test dependencies, change the scope and add them to them to the dependencies of this
        //   project as runtime scope so that the AndroMDA plugin can see them.
        for (final Iterator iterator = this.project.getTestDependencies().iterator(); iterator.hasNext();)
        {
            final Dependency dependency = (Dependency)iterator.next();
            dependency.setScope(Artifact.SCOPE_RUNTIME);
            this.project.getDependencies().add(dependency);
        }
        for (final Iterator iterator = this.project.getTestArtifacts().iterator(); iterator.hasNext();)
        {
            final Artifact artifact = (Artifact)iterator.next();
            artifact.setScope(Artifact.SCOPE_RUNTIME);
            this.project.getArtifacts().add(artifact);
        }
    }

    /**
     * Adds a dependency to the current project's dependencies.
     *
     * @param dependency
     */
    protected void addDependency(final Dependency dependency)
    {
        if (dependency != null)
        {
            final Artifact artifact =
                this.factory.createArtifact(
                    dependency.getGroupId(),
                    dependency.getArtifactId(),
                    dependency.getVersion(),
                    dependency.getScope(),
                    dependency.getType());
            final File file = new File(
                    this.localRepository.getBasedir(),
                    this.localRepository.pathOf(artifact));
            artifact.setFile(file);
            this.project.getDependencies().add(dependency);
            this.project.getArtifacts().add(artifact);
        }
    }

    /**
     * Unpacks the expected archive file to the expected directory
     *
     * @param file File to be unpacked.
     * @param location Location where to put the unpacked files.
     */
    protected void unpack(
        final File file,
        final File location)
        throws MojoExecutionException
    {
        final String archiveExt = FileUtils.getExtension(file.getAbsolutePath()).toLowerCase();
        try
        {
            final UnArchiver unArchiver;
            unArchiver = this.archiverManager.getUnArchiver(archiveExt);
            unArchiver.setSourceFile(file);
            location.mkdirs();
            unArchiver.setDestDirectory(location);
            unArchiver.extract();
        }
        catch (Throwable throwable)
        {
            if (this.testFailureIgnore)
            {
                this.getLog().error(this.project.getArtifactId() + "Error unpacking file " + file + "to " + location, throwable);
            }
            else if (throwable instanceof IOException || throwable instanceof ArchiverException)
            {
                throw new MojoExecutionException("Error unpacking file: " + file + "to: " + location, throwable);
            }
        }
    }
}