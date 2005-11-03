package org.andromda.maven.plugin.modelarchiver;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.maven.archiver.MavenArchiveConfiguration;
import org.apache.maven.archiver.MavenArchiver;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.archiver.UnArchiver;
import org.codehaus.plexus.archiver.jar.JarArchiver;
import org.codehaus.plexus.archiver.manager.ArchiverManager;
import org.codehaus.plexus.util.FileUtils;


/**
 * Builds archived model xml.zip files.
 *
 * @author Chad Brandon
 * @goal xml.zip
 * @phase package
 * @requiresDependencyResolution runtime
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
     * @parameter expression="${project.build.outputDirectory}"
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
    private static final String[] JAR_INCLUDES = new String[] {"**/*"};
    private static final String[] JAR_EXCLUDES = new String[] {"**/package.html"};
    private static final String[] MODEL_INCLUDES = new String[] {"*.xml", "*.xmi"};
    private final Collection modelArchiveExcludes =
        new ArrayList(Arrays.asList(new String[] {"*.xml.zip, **/*.java", "*reports*/**", "tests*/**"}));

    /**
     * Gets the current excludes as an array.
     *
     * @return the exclude patterns.
     */
    private String[] getModelArchiveExcludes()
    {
        return (String[])this.modelArchiveExcludes.toArray(new String[0]);
    }

    /**
     * The pattern of the archived model files that should be extracted
     * before being re-created as versioned model archives.
     *
     * @parameter expression=".*(\\.xml\\.zip)"
     * @required
     * @readonly
     */
    private String modelArchivePattern;

    /**
     * The pattern of the non-archived model file(s) that should be archived.
     *
     * @parameter expression=".*(\\.xml|\\.xmi)"
     * @required
     * @readonly
     */
    private String modelFilePattern;

    /**
     * The maven archiver to use.
     *
     * @parameter
     */
    private MavenArchiveConfiguration archive = new MavenArchiveConfiguration();

    /**
     * @parameter expression="${localRepository}"
     * @required
     * @readonly
     */
    protected ArtifactRepository localRepository;

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
            final File buildDirectory = this.getBuildDirectory();
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
                    if (file.isFile() && file.toString().matches(this.modelArchivePattern))
                    {
                        this.unpack(
                            file,
                            buildDirectory);
                        final File[] extractedModelFiles = buildDirectory.listFiles();
                        for (int ctr2 = 0; ctr2 < extractedModelFiles.length; ctr2++)
                        {
                            final File extractedFile = extractedModelFiles[ctr2];
                            final String extractedFilePath = extractedFile.toString();
                            if (extractedFile.isFile() && extractedFilePath.matches(this.modelFilePattern))
                            {
                                final File newFile =
                                    new File(buildDirectory,
                                        this.finalName + '.' + FileUtils.getExtension(extractedFile.toString()));

                                // - exclude the file that we extracted
                                if (!newFile.equals(extractedFile))
                                {
                                    final String shortPath = extractedFilePath.replaceAll(
                                            ".*\\\\|/",
                                            "");
                                    this.modelArchiveExcludes.add(shortPath);
                                }
                                extractedFile.renameTo(newFile);
                                String contents = IOUtils.toString(new FileReader(newFile));
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
            throw new MojoExecutionException("Error copying model resources", throwable);
        }

        try
        {
            final File xmlZipFile = new File(buildDirectory, this.finalName + ".xml.zip");
            final Artifact artifact = this.project.getArtifact();
            final MavenArchiver archiver = new MavenArchiver();
            archiver.setArchiver(this.jarArchiver);
            archiver.setOutputFile(xmlZipFile);
            archiver.getArchiver().addDirectory(
                this.getBuildDirectory(),
                MODEL_INCLUDES,
                this.getModelArchiveExcludes());
            archiver.createArchive(
                project,
                archive);

            if (this.generateJar)
            {
                getLog().info("Building model jar " + finalName);

                File modelJar = new File(buildDirectory, finalName + ".jar");

                final MavenArchiver modelJarArchiver = new MavenArchiver();

                modelJarArchiver.setArchiver(this.modelJarArchiver);
                modelJarArchiver.setOutputFile(modelJar);
                modelJarArchiver.getArchiver().addDirectory(
                    new File(this.outputDirectory),
                    JAR_INCLUDES,
                    JAR_EXCLUDES);

                // - create archive
                modelJarArchiver.createArchive(
                    project,
                    archive);

                // - set the artifact file as the modelJar so that we can install the model jar
                artifact.setFile(modelJar);
                this.installModelJar(
                    artifact,
                    modelJar);
            }

            // - set the artifact file back to the correct file (since we've installed modelJar already)
            artifact.setFile(xmlZipFile);
        }
        catch (final Throwable throwable)
        {
            throw new MojoExecutionException("Error assembling model", throwable);
        }
    }

    /**
     * Installs the model jar for this xml.zip artifact into the local repository.
     * @param artifact the artifact to install.
     * @param source the source of the artifact.
     * @throws IOException
     */
    private void installModelJar(
        final Artifact artifact,
        final File source)
        throws IOException
    {
        // - change the extension to the correct 'jar' extension
        final String localPath = this.localRepository.pathOf(artifact).replaceAll(
                "\\.xml\\.zip",
                "\\.jar");
        final File destination = new File(
                this.localRepository.getBasedir(),
                localPath);
        this.getLog().info("Installing " + source.getPath() + " to " + destination);

        FileUtils.copyFile(
            source,
            destination);
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
        if (this.buildDirectory == null)
        {
            this.buildDirectory = new File(workDirectory);
        }
        return this.buildDirectory;
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
        throws MojoExecutionException
    {
        final String archiveExt = FileUtils.getExtension(file.getAbsolutePath()).toLowerCase();
        try
        {
            final UnArchiver unArchiver;
            unArchiver = this.archiverManager.getUnArchiver(archiveExt);
            unArchiver.setSourceFile(file);
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