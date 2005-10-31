package org.andromda.maven.plugin.modelarchiver;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.andromda.core.common.ResourceUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.maven.archiver.MavenArchiveConfiguration;
import org.apache.maven.archiver.MavenArchiver;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectHelper;
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
     * The model Jar archiver.
     *
     * @parameter expression="${component.org.codehaus.plexus.archiver.Archiver#jar}"
     * @required
     */
    private JarArchiver modelJarArchiver;


    /**
     * The extensions to search for when doing replacement of embedded model HREF references
     * within the archived model file from non-versioned to versioned references.
     *
     * @parameter expression=".xml,.xml.zip,.xmi"
     * @required
     */
    protected String replacementExtensions;

    /**
     * Whether or not the model should have a jar created with it as well.
     *
     * @parameter
     */
    private boolean generateJar;
    
    /**
     * The maven project's helper.
     *
     * @parameter expression="${component.org.apache.maven.project.MavenProjectHelper}"
     * @required
     * @readonly
     */
    private MavenProjectHelper projectHelper;
    
    /**
     * What to include in the jar directory.
     */
    private static final String[] JAR_INCLUDES = new String[]{"**/**"};
    
    /**
     * The pattern of the model file(s) to which to apply the archiving.
     * 
     * @parameter expression=".*(\\.xml|\\.xmi|\\.xml\\.zip)}"
     * @required
     * @readonly
     */
    private final String modelFilePattern;

    /**
     * The maven archiver to use.
     *
     * @parameter
     */
    private MavenArchiveConfiguration archive = new MavenArchiveConfiguration();

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
            final File buildDirectory = this.getBuildDirectory();
            FileUtils.deleteDirectory(buildDirectory);
            buildDirectory.mkdirs();

            final File modelSourceDir = modelSourceDirectory;
            final String[] replacementExtensions =
                this.replacementExtensions != null ? this.replacementExtensions.split(",\\s*") : new String[0];
            if (modelSourceDir.exists())
            {
                getLog().info("Copy xml.zip resources to " + this.getBuildDirectory().getAbsolutePath());
                final File[] modelFiles = modelSourceDir.listFiles();
                for (int ctr = 0; ctr < modelFiles.length; ctr++)
                {
                    final File file = modelFiles[ctr];
                    if (file.isFile() && file.toString().matches(this.modelFilePattern))
                    {
                        this.unpack(
                            file,
                            buildDirectory);
                        final File[] extractedModelFiles = buildDirectory.listFiles();
                        for (int ctr2 = 0; ctr2 < extractedModelFiles.length; ctr2++)
                        {
                            final File extractedFile = extractedModelFiles[ctr2];
                            if (extractedFile.isFile())
                            {
                                final File newFile =
                                    new File(outputDirectory,
                                        finalName + '.' + FileUtils.getExtension(extractedFile.toString()));
                                FileUtils.rename(
                                    extractedFile,
                                    newFile);
                                String contents = ResourceUtils.getContents(newFile.toURL());
                                for (int ctr3 = 0; ctr3 < replacementExtensions.length; ctr3++)
                                {
                                    final String version = escapePattern(this.project.getVersion());
                                    final String extension = escapePattern(replacementExtensions[ctr3]);
                                    final String extensionPattern = "((\\-" + version + ")?)" + extension;
                                    final String newExtension = "\\-" + version + extension;
                                    contents = contents.replaceAll(
                                            extensionPattern,
                                            newExtension);
                                }
                                final FileWriter fileWriter = new FileWriter(newFile);
                                fileWriter.write(contents);
                                fileWriter.flush();
                            }
                        }
                    }
                }
            }
        }
        catch (final Throwable throwable)
        {
            throwable.printStackTrace();
            throw new MojoExecutionException("Error copying model resources", throwable);
        }

        try
        {
            final File xmlZipFile = new File(outputDirectory, finalName + ".xml.zip");
            project.getArtifact().setFile(xmlZipFile);
            final MavenArchiver archiver = new MavenArchiver();
            archiver.setArchiver(jarArchiver);
            archiver.setOutputFile(xmlZipFile);

            archiver.getArchiver().addDirectory(getBuildDirectory());
            archiver.createArchive(
                project,
                archive);

            if (this.generateJar)
            {
                getLog().info("Building model jar " + finalName);

                File modelJar = new File(outputDirectory, finalName + ".jar");

                MavenArchiver clientArchiver = new MavenArchiver();

                clientArchiver.setArchiver(this.modelJarArchiver);

                clientArchiver.setOutputFile(modelJar);

                clientArchiver.getArchiver().addDirectory(
                    new File(outputDirectory),
                    JAR_INCLUDES,
                    null);

                // create archive
                clientArchiver.createArchive(
                    project,
                    archive);

                projectHelper.attachArtifact(
                    project,
                    "ejb-client",
                    "client",
                    modelJar);
            }
        }
        catch (final Throwable throwable)
        {
            throw new MojoExecutionException("Error assembling model", throwable);
        }
    }

    /**
     * Escapes the pattern so that the reserved regular expression
     * characters are used literally.
     * @param pattern the pattern to replace.
     * @return the resulting pattern.
     */
    private static String escapePattern(String pattern)
    {
        pattern = StringUtils.replace(
                pattern,
                ".",
                "\\.");
        pattern = StringUtils.replace(
                pattern,
                "-",
                "\\-");
        return pattern;
    }

    private File buildDirectory;

    /**
     * Gets the current build directory as a file instance.
     *
     * @return the build directory as a file.
     */
    protected File getBuildDirectory()
    {
        if (buildDirectory == null)
        {
            buildDirectory = new File(workDirectory);
        }
        return buildDirectory;
    }

    /**
     * Unpacks the archive file.
     *
     * @param file File to be unpacked.
     * @param location Location where to put the unpacked files.
     */
    protected void unpack(
        final File file,
        final File location)
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
    
    public static void main(String args[])
    {
        System.out.println("my-model.xml.zip".matches(MODEL_FILE_PATTERN));
    }
}