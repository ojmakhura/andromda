package org.andromda.maven.plugin.initialize;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.FileUtils;


/**
 * Provides any initialization required by the AndroMDA build.
 *
 * @author Chad Brandon
 * @goal initialize
 * @phase validate
 */
public class InitializeMojo
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
     * @parameter
     * @required
     * @description the directory where any local (those not kept in a remote repository) artifacts are kept.
     */
    protected String localArtifactDirectory;

    /**
     * @parameter expression="${bootstrap.artifacts}"
     * @description Whether or not bootstrap artifacts should be installed, by default they are not.  If this is
     *              set to true this means the andromda bootstrap-install plugin will be updating the bootstraps, therefore
     *              this plugin will not deploy any of the current bootstraps (since that would create an inconsistent state).
     */
    protected boolean installBootstraps;

    /**
     * @parameter expression="org/andromda/bootstrap"
     * @required
     * @readonly
     * @description the name root directory of the bootstrap artifacts.
     */
    protected String bootstrapDirectoryName;

    /**
     * Set this to 'true' to bypass cartridge tests entirely. Its use is NOT RECOMMENDED, but quite convenient on occasion.
     *
     * @parameter expression="${maven.test.skip}" default-value="false"
     */
    protected boolean skip;

    /**
     *  Set this to 'true' to skip running tests, but still compile them. Its use is NOT RECOMMENDED, but quite convenient on occasion. 
     *
     * @parameter expression="${skipTests}" default-value="false"
     */
    protected boolean skipTests;

    /**
     * Set this to true to ignore a failure during testing. Its use is NOT RECOMMENDED, but quite convenient on occasion.
     *
     * @parameter expression="${maven.test.failure.ignore}" default-value="false"
     */
    protected boolean testFailureIgnore;

    /**
     * @see org.apache.maven.plugin.Mojo#execute()
     */
    public void execute()
        throws MojoExecutionException, MojoFailureException
    {
        if (!this.installBootstraps)
        {
            try
            {
                final File bootstrapDirectory = new File(this.localArtifactDirectory);
                final Collection contents = this.getDirectoryContents(bootstrapDirectory);
                for (final Iterator iterator = contents.iterator(); iterator.hasNext();)
                {
                    String path = (String)iterator.next();
                    final String extension = FileUtils.getExtension(path);
                    String fileName = this.getFileName(path);
                    final String version = this.project.getVersion();
                    final String versionedFileName =
                        StringUtils.replace(
                            fileName,
                            '.' + extension,
                            '-' + version + '.' + extension);
                    final String artifactId = this.getArtifactId(path);
                    final String newPath =
                        StringUtils.replace(
                            path,
                            fileName,
                            artifactId + '/' + version + '/' + versionedFileName);
                    final File bootstrapFile = new File(this.localArtifactDirectory, path);
                    final File repositoryFile = new File(
                            this.localRepository.getBasedir(),
                            newPath);

                    if (bootstrapFile.lastModified() > repositoryFile.lastModified())
                    {
                        this.getLog().info("Installing bootstrap artifact " + bootstrapFile + " to " + repositoryFile);
                        FileUtils.copyFile(
                            bootstrapFile,
                            repositoryFile);
                    }
                }
            }
            catch (final Throwable throwable)
            {
                throw new MojoExecutionException("Error installing bootstrap artifact(s)", throwable);
            }
        }
    }

    /**
     * Retrieves the artifact id from the given <code>path</code>.
     * @param path the path from which to retrieve the artifactId.
     * @return the artifactId.
     */
    private String getArtifactId(final String path)
    {
        return this.getFileName(path).replaceAll(
            "\\..*",
            "");
    }

    /**
     * Retrieves the file name from the given <code>path</code>.
     * @param path the path from which to retrieve the file name
     * @return the path.
     */
    private String getFileName(final String path)
    {
        return path.replaceAll(
            ".*(\\\\|/)",
            "");
    }

    /**
     * Retrieves the contents of this directory as a list of relative
     * paths.
     * @param the directory from which to retrieve the contents.
     * @return the contents of the directory as a list of relative paths.
     */
    private List getDirectoryContents(final File directory)
    {
        final List files = new ArrayList();
        this.loadFiles(
            directory,
            files);
        for (final ListIterator iterator = files.listIterator(); iterator.hasNext();)
        {
            final File file = (File)iterator.next();
            final String filePath = file.toString().replace(
                    '\\',
                    '/');
            final String directoryPath = directory.toString().replace(
                    '\\',
                    '/');
            final String relativePath = StringUtils.replace(
                    filePath,
                    directoryPath + '/',
                    "");
            if (!FilenameUtils.wildcardMatch(
                    relativePath,
                    this.bootstrapDirectoryName + "**/*"))
            {
                iterator.remove();
            }
            else
            {
                iterator.set(relativePath);
            }
        }
        return files;
    }

    /**
     * Loads all files find in the <code>directory</code> and adds them to the <code>fileList</code>.
     *
     * @param directory the directory from which to load all files.
     * @param fileList  the List of files to which we'll add the found files.
     * @param includeSubdirectories whether or not to include sub directories when loading the files.
     */
    private void loadFiles(
        final File directory,
        final List fileList)
    {
        if (directory != null)
        {
            final File[] files = directory.listFiles();
            if (files != null)
            {
                for (int ctr = 0; ctr < files.length; ctr++)
                {
                    File file = files[ctr];
                    if (!file.isDirectory())
                    {
                        fileList.add(file);
                    }// Ignore SVN dir/files
                    else if (!".svn".equalsIgnoreCase(file.getName()))
                    {
                        loadFiles(
                            file,
                            fileList);
                    }
                }
            }
        }
    }
}