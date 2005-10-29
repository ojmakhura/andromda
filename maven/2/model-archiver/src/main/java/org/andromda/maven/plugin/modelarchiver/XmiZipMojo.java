package org.andromda.maven.plugin.modelarchiver;

import java.io.File;
import java.io.IOException;

import org.apache.maven.archiver.MavenArchiveConfiguration;
import org.apache.maven.archiver.MavenArchiver;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.archiver.UnArchiver;
import org.codehaus.plexus.archiver.jar.JarArchiver;
import org.codehaus.plexus.archiver.manager.ArchiverManager;
import org.codehaus.plexus.archiver.manager.NoSuchArchiverException;
import org.codehaus.plexus.util.FileUtils;


/**
 * Builds archived model xml.zip files.
 *
 * @author Chad Brandon
 * @goal xml.zip
 * @phase package
 * @description builds a versioned xml.zip
 */
public class XmiZipMojo
    extends AbstractMojo
{
    /**
     * Single directory that contains the model
     *
     * @parameter expression="${basedir}/src/main/uml"
     * @required
     */
    private File modelSourceDirectory;

    /**
     * Directory that resources are copied to during the build.
     *
     * @parameter expression="${project.build.directory}"
     * @required
     */
    private String workDirectory;

    /**
     * The directory for the generated xml.zip.
     *
     * @parameter expression="${project.build.directory}"
     * @required
     */
    private String outputDirectory;

    /**
     * The name of the xml.zip file to generate.
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
     * To look up Archiver/UnArchiver implementations
     *
     * @parameter expression="${component.org.codehaus.plexus.archiver.manager.ArchiverManager}"
     * @required
     */
    protected ArchiverManager archiverManager;

    /**
     * The maven archiver to use.
     *
     * @parameter
     */
    private MavenArchiveConfiguration archive = new MavenArchiveConfiguration();
    private File buildDir;

    public void execute()
        throws MojoExecutionException
    {
        getLog().debug(" ======= XmlZipMojo settings =======");
        getLog().debug("modelSourceDirectory[" + modelSourceDirectory + "]");
        getLog().debug("workDirectory[" + workDirectory + "]");
        getLog().debug("outputDirectory[" + outputDirectory + "]");
        getLog().debug("finalName[" + finalName + "]");

        // - extract model file
        try
        {
            // - delete and remake the directory each time
            final File buildDirectory = this.getBuildDir();
            FileUtils.deleteDirectory(buildDirectory);
            buildDirectory.mkdirs();

            final File modelSourceDir = modelSourceDirectory;
            if (modelSourceDir.exists())
            {
                getLog().info("Copy xml.zip resources to " + getBuildDir().getAbsolutePath());
                final File[] modelFiles = modelSourceDir.listFiles();
                for (int ctr = 0; ctr < modelFiles.length; ctr++)
                {
                    final File file = modelFiles[ctr];
                    if (file.isFile())
                    {
                        this.unpack(
                            file,
                            buildDirectory);
                        final File[] extractedModelFiles = buildDirectory.listFiles();
                        for (int ctr2 = 0; ctr2 < modelFiles.length; ctr2++)
                        {
                            final File extractedFile = extractedModelFiles[ctr];
                            if (extractedFile.isFile())
                            {
                                final File newFile =
                                    new File(outputDirectory,
                                        finalName + '.' + FileUtils.getExtension(extractedFile.toString()));
                                FileUtils.rename(
                                    extractedFile,
                                    newFile);
                            }
                        }
                    }
                }
            }
        }
        catch (Throwable throwable)
        {
            throw new MojoExecutionException("Error copying model resources", throwable);
        }

        try
        {
            File xmlZipFile = new File(outputDirectory, finalName + ".xml.zip");
            project.getArtifact().setFile(xmlZipFile);
            MavenArchiver archiver = new MavenArchiver();
            archiver.setArchiver(jarArchiver);
            archiver.setOutputFile(xmlZipFile);

            archiver.getArchiver().addDirectory(getBuildDir());
            archiver.createArchive(
                project,
                archive);
        }
        catch (Exception e)
        {
            throw new MojoExecutionException("Error assembling model", e);
        }
    }

    protected File getBuildDir()
    {
        if (buildDir == null)
        {
            buildDir = new File(workDirectory);
        }
        return buildDir;
    }

    /**
     * Unpacks the archive file.
     *
     * @param file File to be unpacked.
     * @param location Location where to put the unpacked files.
     */
    protected void unpack(
        File file,
        File location)
        throws MojoExecutionException, NoSuchArchiverException
    {
        String archiveExt = FileUtils.getExtension(file.getAbsolutePath()).toLowerCase();

        try
        {
            UnArchiver unArchiver;
            unArchiver = this.archiverManager.getUnArchiver(archiveExt);
            unArchiver.setSourceFile(file);
            unArchiver.setDestDirectory(location);
            unArchiver.extract();
        }
        catch (IOException e)
        {
            throw new MojoExecutionException("Error unpacking file: " + file + "to: " + location, e);
        }
        catch (ArchiverException e)
        {
            throw new MojoExecutionException("Error unpacking file: " + file + "to: " + location, e);
        }
    }
}