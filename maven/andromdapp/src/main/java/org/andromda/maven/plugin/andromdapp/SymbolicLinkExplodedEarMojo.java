package org.andromda.maven.plugin.andromdapp;

import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.model.Model;
import org.apache.maven.model.Parent;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.DirectoryScanner;


/**
 * Basically post processes a previously built ear and replaces any war
 * artifacts with symbolic links and then symbolic links the ear to the deploy
 * directory so that we don't have to redeploy an ear in order to make jsp changes.
 *
 * @author Chad Brandon
 * @goal link
 * @phase package
 */
public class SymbolicLinkExplodedEarMojo
    extends AbstractMojo
{
    /**
     * The maven project.
     *
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    protected MavenProject project;

    /**
     * The location in which to link the exploded ear.
     *
     * @parameter expression="${env.JBOSS_HOME}/server/default/deploy"
     */
    private String deployLocation;

    /**
     * Artifact factory, needed to download source jars for inclusion in
     * classpath.
     *
     * @component role="org.apache.maven.artifact.factory.ArtifactFactory"
     * @required
     * @readonly
     */
    private ArtifactFactory artifactFactory;

    /**
     * Command to create a symbolic link on the Windows platform
     */
    private static final String LN_WINDOWS = "junction.exe";

    /**
     * Command to create a symbolic link on the UNIX platform
     */
    private static final String LN_UNIX = "ln -s";

    /**
     * <code>true</code> if the base OS is Windows.
     */
    private static  boolean isWindows = false;

    private static final String WINDOWS = "Windows";

    static
    {
        final String osName = System.getProperty("os.name");
        if (osName.contains(WINDOWS))
        {
            isWindows = true;
        }
    }

    /**
     * @see org.apache.maven.plugin.Mojo#execute()
     */
    public void execute()
        throws MojoExecutionException, MojoFailureException
    {
        try
        {
            final File earDirectory =
                new File(this.project.getBuild().getDirectory() + '/' + project.getBuild().getFinalName());
            final Map artifacts = new LinkedHashMap();
            for (final Iterator iterator = this.getExplodedModuleArtifacts().iterator(); iterator.hasNext();)
            {
                final Artifact artifact = (Artifact)iterator.next();
                artifacts.put(
                    artifact.getFile().toString().replaceAll(
                        ".*(\\\\|//)",
                        ""),
                    artifact);
            }

            if (earDirectory.exists() && earDirectory.isDirectory())
            {
                String linkCommand;
                if (isWindows)
                {
                    linkCommand = LN_WINDOWS;
                    final File executableFile =
                        new File(System.getProperty("java.io.tmpdir") + '/' + System.getProperty("user.name"),
                            LN_WINDOWS);
                    if (!executableFile.exists())
                    {
                        final URL resource =
                            Thread.currentThread().getContextClassLoader().getResource(LN_WINDOWS);
                        FileUtils.copyURLToFile(
                            resource,
                            executableFile);
                    }
                    linkCommand = executableFile.toString();
                }
                else
                {
                    linkCommand = LN_UNIX;
                }
                final File[] files = earDirectory.listFiles();
                final File explodedEarDirectory = new File(earDirectory + "-exploded");
                explodedEarDirectory.mkdirs();
                for (int ctr = 0; ctr < files.length; ctr++)
                {
                    final File file = files[ctr];
                    final String fileName = file.toString().replaceAll(
                            ".*(\\\\|//)",
                            "");
                    final Artifact artifact = (Artifact)artifacts.get(fileName);
                    if (artifact == null)
                    {
                        if (file.isFile())
                        {
                            FileUtils.copyFileToDirectory(
                                file,
                                explodedEarDirectory);
                        }
                        else
                        {
                            final File targetDirectory = new File(explodedEarDirectory,
                                    file.getName());
                            targetDirectory.mkdir();
                            FileUtils.copyDirectory(
                                file,
                                targetDirectory);
                        }
                    }
                    else
                    {
                        final File targetFile = new File(explodedEarDirectory, fileName);
                        final File explodedArtifactDirectory =
                            new File(artifact.getFile().toString().replaceAll(
                                    "\\." + artifact.getType(),
                                    ""));

                        final String command;
                        if (!isWindows)
                        {
                            command = linkCommand + ' ' + explodedArtifactDirectory + ' ' +
                                targetFile.getAbsolutePath();
                        }
                        else
                        {
                            command = linkCommand + ' ' + targetFile.getAbsolutePath() + ' ' +
                                explodedArtifactDirectory;
                        }
                        if (this.getLog().isDebugEnabled())
                        {
                            this.getLog().debug("executing command: " + command);
                        }
                        // - remove the archive artifact if its already present
                        if (targetFile.exists() && targetFile.isFile())
                        {
                            targetFile.delete();
                        }
                        this.getLog().info("linking " + explodedArtifactDirectory + " to " + targetFile);
                        Runtime.getRuntime().exec(command);
                    }
                }

                final File targetFile =
                    new File(this.deployLocation,
                        this.project.getBuild().getFinalName() + '.' + this.project.getPackaging());
                final String command;
                if (!isWindows)
                {
                    command = linkCommand + ' ' + explodedEarDirectory + ' ' + targetFile.getAbsolutePath();
                }
                else
                {
                    command = linkCommand + ' ' + targetFile.getAbsolutePath() + ' ' + explodedEarDirectory;
                }
                if (this.getLog().isDebugEnabled())
                {
                    this.getLog().debug("executing command: " + command);
                }
                final File applicationContextXml = new File(explodedEarDirectory, "META-INF/application.xml");
                FileUtils.touch(applicationContextXml);
                this.getLog().info("linking " + explodedEarDirectory + " to " + targetFile);

                // - remove the ear file if it exists
                if (targetFile.isFile())
                {
                    targetFile.delete();
                }
                Runtime.getRuntime().exec(command);
            }
        }
        catch (Exception exception)
        {
            throw new MojoExecutionException("A failure occured while trying to link the ear", exception);
        }
    }

    /**
     * Stores the root project.
     */
    private MavenProject rootProject;

    /**
     * Retrieves the root project (i.e. the root parent project)
     * for this project.
     *
     * @return the root project.
     * @throws MojoExecutionException
     */
    private MavenProject getRootProject()
        throws MojoExecutionException
    {
        if (this.rootProject == null)
        {
            MavenProject root = null;
            for (root = this.project.getParent(); root.getParent() != null; root = root.getParent())
            {
            }
            if (root == null)
            {
                throw new MojoExecutionException("No parent could be retrieved for project --> " +
                    this.project.getId() + "', you must specify a parent project");
            }
            this.rootProject = root;
        }
        return this.rootProject;
    }

    private static final String[] INCLUDE_ALL_POMS = new String[]{"*/**/pom.xml"};

    /**
     * Retrieves all the POMs for the given project.
     *
     * @return all poms found.
     * @throws MojoExecutionException
     */
    private List getPoms()
        throws MojoExecutionException
    {
        final DirectoryScanner scanner = new DirectoryScanner();
        scanner.setBasedir(this.getRootProject().getBasedir());
        scanner.setIncludes(INCLUDE_ALL_POMS);
        scanner.scan();
        final List poms = new ArrayList();
        for (int ctr = 0; ctr < scanner.getIncludedFiles().length; ctr++)
        {
            final File file = new File(
                    this.getRootProject().getBasedir(),
                    scanner.getIncludedFiles()[ctr]);
            if (file.exists())
            {
                poms.add(file);
            }
        }
        return poms;
    }

    /**
     * Constructs an artifact from the given <code>pom</code> file.
     *
     * @return all module artifacts
     * @throws Exception
     */
    private List getExplodedModuleArtifacts()
        throws Exception
    {
        final List artifacts = new ArrayList();
        final MavenXpp3Reader reader = new MavenXpp3Reader();

        for (final Iterator iterator = this.getPoms().iterator(); iterator.hasNext();)
        {
            final File pom = (File)iterator.next();
            final Model model = reader.read(new FileReader(pom));
            String groupId = model.getGroupId();
            for (Parent parent = model.getParent(); groupId == null && model.getParent() != null;
                parent = model.getParent())
            {
                groupId = parent.getGroupId();
            }
            String version = model.getVersion();
            for (Parent parent = model.getParent(); version == null && model.getParent() != null;
                parent = model.getParent())
            {
                version = parent.getVersion();
            }
            final Artifact artifact =
                this.artifactFactory.createArtifact(
                    groupId,
                    model.getArtifactId(),
                    version,
                    null,
                    model.getPackaging());
            final File pomParent = pom.getParentFile();
            final String finalName = model.getArtifactId() + '-' + version;
            final File explodedDirectory = new File(pomParent, "target/" + finalName);
            final File artifactFile = new File(explodedDirectory + "." + model.getPackaging());
            if (explodedDirectory.isDirectory() && artifactFile.exists() &&
                !finalName.equals(this.project.getBuild().getFinalName()))
            {
                artifacts.add(artifact);
                artifact.setFile(artifactFile);
            }
        }
        return artifacts;
    }
}