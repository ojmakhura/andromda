package org.andromda.maven.plugin.cartridge;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.io.FilenameUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.settings.Settings;
import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.archiver.UnArchiver;
import org.codehaus.plexus.archiver.manager.ArchiverManager;

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
    protected File reportDirectory;

    /**
     * Specifies the directory that contains the "actual" output (meaning the output
     * that was currently generated)
     * @parameter expression="${project.build.directory}/cartridge-test/actual"
     * @required
     */
    protected File actualDirectory;

    /**
     * Specifies the directory that contains the "expected" output.
     * @parameter expression="${project.build.directory}/cartridge-test/expected"
     * @required
     */
    protected File expectedDirectory;

    /**
     * The location of the archive storing the expected output.
     * @parameter expression="${basedir}/src/test/expected/cartridge-output.zip"
     * @required
     */
    protected File expectedOutputArchive;

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
    protected List<String> propertyFiles;

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
     * @component role="org.codehaus.plexus.archiver.manager.ArchiverManager"
     * @required
     */
    protected ArchiverManager archiverManager;

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
     *  Set this to 'true' to skip code generation if code has not changed since the latest model date.
     *
     * @parameter expression="${lastModifiedCheck}"
     */
    protected boolean lastModifiedCheck;

    /**
     * Adds any dependencies for the cartridge plugin
     * to the current dependencies of the project.
     */
    protected void changeScopeForTestDependencies()
    {
        // - get all test dependencies, change the scope and add them to them to the dependencies of this
        //   project as runtime scope so that the AndroMDA plugin can see them.
        for (final Dependency dependency : (Iterable<Dependency>) this.project.getTestDependencies())
        {
            //process only test dependencies
            if (dependency.getScope().equals(Artifact.SCOPE_TEST))
            {
                dependency.setScope(Artifact.SCOPE_RUNTIME);
                project.getDependencies().add(dependency);
            }
        }

        final Set artifacts = new HashSet<Artifact>(this.project.getArtifacts());
        for (final Artifact artifact : (Iterable<Artifact>) this.project.getTestArtifacts())
        {
            //process only test dependencies
            if (artifact.getScope().equals(Artifact.SCOPE_TEST))
            {
                artifact.setScope(Artifact.SCOPE_RUNTIME);
                artifacts.add(artifact);
            }
        }
        project.setArtifacts(artifacts);
    }

    /**
     * Unpacks the expected archive file to the expected directory
     *
     * @param file File to be unpacked.
     * @param location Location where to put the unpacked files.
     * @throws MojoExecutionException Error unpacking file
     */
    protected void unpack(
        final File file,
        final File location)
        throws MojoExecutionException
    {
        final String archiveExt = FilenameUtils.getExtension(file.getAbsolutePath()).toLowerCase();
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
                this.getLog().error(this.project.getArtifactId() + ": Error unpacking file: " + file + " to " + location, throwable);
            }
            else if (throwable instanceof IOException || throwable instanceof ArchiverException)
            {
                throw new MojoExecutionException("Error unpacking file: " + file + " to: " + location, throwable);
            }
        }
    }
}