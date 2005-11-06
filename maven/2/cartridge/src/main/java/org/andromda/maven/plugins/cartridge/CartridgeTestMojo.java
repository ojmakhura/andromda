package org.andromda.maven.plugins.cartridge;

import java.io.File;
import java.io.IOException;

import java.util.Iterator;
import java.util.List;

import junit.textui.TestRunner;

import org.andromda.cartridges.testsuite.CartridgeTest;
import org.andromda.core.common.ExceptionUtils;
import org.andromda.maven.plugin.AndroMDAMojo;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Plugin;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.settings.Settings;
import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.archiver.UnArchiver;
import org.codehaus.plexus.archiver.manager.ArchiverManager;
import org.codehaus.plexus.util.FileUtils;


/**
 * Provides the ability to compare cartridge output with existing output.
 *
 * @phase test
 * @goal test
 * @requiresDependencyResolution test
 * @description runs AndroMDA Cartridge tests
 * @author Chad Brandon
 */
public class CartridgeTestMojo
    extends AbstractMojo
{
    /**
     * Base directory to which all reports are written
     *
     * @parameter expression="${project.build.directory}/cartridge-reports"
     */
    private String reportsDirectory;

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
    private String configurationUri;

    /**
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    private MavenProject project;

    /**
     * @parameter expression="${project.build.filters}"
     */
    private List propertyFiles;

    /**
     * The current user system settings for use in Maven. (allows us to pass the user
     * settings to the AndroMDA configuration).
     *
     * @parameter expression="${settings}"
     * @required
     * @readonly
     */
    private Settings settings;

    /**
     * Defines the extensions of binary files, binary files are checked for presence
     * and equality, however they aren't compared as strings, like every other file.
     *
     * @parameter expression="jpg,jpeg,gif,png,jar,zip"
     */
    private String binaryOutputSuffixes;

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
     * @readonlya
     */
    protected List plugins;

    /**
     * @parameter expression="${component.org.apache.maven.artifact.factory.ArtifactFactory}"
     * @required
     * @readonly
     */
    private ArtifactFactory factory;

    /**
     * @parameter expression="${localRepository}"
     * @required
     * @readonly
     */
    protected ArtifactRepository localRepository;

    /**
     * @see org.apache.maven.plugin.Mojo#execute()
     */
    public void execute()
        throws MojoExecutionException, MojoFailureException
    {
        final File expectedOutputArchive = new File(this.expectedOutputArchive);
        if (!expectedOutputArchive.exists() || !expectedOutputArchive.isFile())
        {
            throw new MojoExecutionException("The path specifying the expectedOutputArchive '" +
                this.expectedOutputArchive + "' must be a file");
        }

        try
        {
            // - add the cartridge test dependencies (any dependencies of the cartridge test plugin)
            this.addCartridgeTestDependencies();

            // - first run AndroMDA with the test configuration
            final AndroMDAMojo andromdaMojo = new AndroMDAMojo();
            andromdaMojo.setConfigurationUri(this.configurationUri);
            andromdaMojo.setProject(this.project);
            andromdaMojo.setSettings(this.settings);
            andromdaMojo.setPropertyFiles(this.propertyFiles);
            andromdaMojo.execute();

            // - unpack the expected output archive
            this.unpack(
                expectedOutputArchive,
                new File(this.expectedDirectory));

            // TODO: - these should really be changed to set properties on an instance of a CartridgeTest
            // instead of just setting system properties
            System.setProperty(
                CartridgeTest.ACTUAL_DIRECTORY,
                this.actualDirectory);
            System.setProperty(
                CartridgeTest.EXPECTED_DIRECTORY,
                this.expectedDirectory);
            System.setProperty(
                CartridgeTest.BINARY_SUFFIXES,
                this.binaryOutputSuffixes);
            TestRunner.run(CartridgeTest.suite());
        }
        catch (final Throwable throwable)
        {
            throw new MojoExecutionException("An error occured while testing cartridge '" +
                this.project.getArtifactId() + "'",
                ExceptionUtils.getRootCause(throwable));
        }
    }

    private void addCartridgeTestDependencies()
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
                        this.addDependencyPath((Dependency)dependencyIterator.next());
                    }
                }
            }
        }
    }

    private void addDependencyPath(final Dependency dependency)
    {
        if (dependency != null)
        {
            final Artifact artifact =
                this.factory.createArtifact(
                    dependency.getGroupId(),
                    dependency.getArtifactId(),
                    dependency.getVersion(),
                    Artifact.SCOPE_RUNTIME,
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
            if (throwable instanceof IOException || throwable instanceof ArchiverException)
            {
                throw new MojoExecutionException("Error unpacking file: " + file + "to: " + location, throwable);
            }
        }
    }
}