package org.andromda.maven.plugin.modelarchiver;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.maven.archiver.MavenArchiveConfiguration;
import org.apache.maven.archiver.MavenArchiver;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.factory.ArtifactFactory;
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
 * @requiresProject
 * @description builds a versioned xml.zip
 */
public class XmiZipArchiverMojo
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
     * Artifact factory, needed to download source jars for inclusion in
     * classpath.
     *
     * @component role="org.apache.maven.artifact.factory.ArtifactFactory"
     * @required
     * @readonly
     */
    private ArtifactFactory artifactFactory;

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
    private final Collection modelArchiveExcludes =
        new ArrayList(Arrays.asList(new String[] {"*.xml.zip, **/*.java", "*reports*/**", "tests*/**"}));

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
        getLog().debug(" ======= XmlZipArchiverMojo settings =======");
        getLog().debug("modelSourceDirectory[" + modelSourceDirectory + "]");
        getLog().debug("workDirectory[" + workDirectory + "]");
        getLog().debug("outputDirectory[" + outputDirectory + "]");
        getLog().debug("finalName[" + finalName + "]");

        try
        {
            // - the directory which to extract the model file
            final File modelExtractDirectory = new File(this.workDirectory, "models/xmi");
            modelExtractDirectory.mkdirs();
            final File buildDirectory = new File(this.workDirectory);

            final File modelSourceDir = modelSourceDirectory;
            final String[] replacementExtensions =
                this.replacementExtensions != null ? this.replacementExtensions.split(",\\s*") : new String[0];
            if (modelSourceDir.exists())
            {
                getLog().info("Copy xml.zip resources to " + buildDirectory.getAbsolutePath());
                final File[] modelFiles = modelSourceDir.listFiles();
                for (int ctr = 0; ctr < modelFiles.length; ctr++)
                {
                    final File file = modelFiles[ctr];
                    if (file.isFile() && file.toString().matches(this.modelArchivePattern))
                    {
                        // - extract model file
                        this.unpack(
                            file,
                            modelExtractDirectory);
                        final File[] extractedModelFiles = modelExtractDirectory.listFiles();
                        for (int ctr2 = 0; ctr2 < extractedModelFiles.length; ctr2++)
                        {
                            final File extractedFile = extractedModelFiles[ctr2];
                            final String extractedFilePath = extractedFile.toString();
                            if (extractedFile.isFile() && extractedFilePath.matches(this.modelFilePattern))
                            {
                                final File newFile =
                                    new File(modelExtractDirectory,
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
                                final File xmlZipFile = new File(buildDirectory, this.finalName + ".xml.zip");
                                this.writeModelArchive(
                                    xmlZipFile,
                                    newFile.toString());
                            }
                        }
                    }
                }
            }

            final File xmlZipFile = new File(buildDirectory, this.finalName + ".xml.zip");
            final Artifact artifact = this.project.getArtifact();
            artifact.setFile(xmlZipFile);
            if (this.generateJar)
            {
                final File workDirectory = new File(this.workDirectory);
                this.getLog().info("Building model jar " + finalName);
                
                final Artifact jarArtifact = artifactFactory.createArtifact( project.getGroupId(),
                    project.getArtifactId(),
                    project.getVersion(), null, "jar");

                File modelJar = new File(workDirectory, finalName + ".jar");

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
                jarArtifact.setFile(modelJar);
                this.installModelJar(
                    jarArtifact,
                    modelJar);
                project.addAttachedArtifact(jarArtifact);
            }

        }
        catch (final Throwable throwable)
        {
            throw new MojoExecutionException("Error assembling model", throwable);
        }
    }

    /**
     * Installs the model jar for this xml.zip artifact into the local repository.
     * 
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

    /**
     * The regular expression pattern used to remove the beginning of the path from the file (and leave the name).
     */
    private static final String PATH_REMOVE_PATTERN = ".*(\\\\|/)";

    /**
     * Writes the given given <code>model</code> archive file and includes
     * the file given by the <code>path</code>
     * 
     * @param modelArchive the model archive.
     * @param path the path of the model to write.
     * @throws IOException
     */
    private void writeModelArchive(
        final File modelArchive,
        final String path)
        throws IOException
    {
        // - retrieve the name of the file given by the path.
        final String name = path.replaceAll(
                PATH_REMOVE_PATTERN,
                "");
        final ZipOutputStream zipOutputStream = new ZipOutputStream(new java.io.FileOutputStream(modelArchive));
        final ZipEntry zipEntry = new ZipEntry(name);
        zipEntry.setMethod(ZipEntry.DEFLATED);
        zipOutputStream.putNextEntry(zipEntry);
        final java.io.FileInputStream inputStream = new java.io.FileInputStream(path);
        final byte[] buffer = new byte[1024];
        int n = 0;
        while ((n = inputStream.read(
                    buffer,
                    0,
                    buffer.length)) > 0)
        {
            zipOutputStream.write(
                buffer,
                0,
                n);
        }
        inputStream.close();
        zipOutputStream.closeEntry();
        zipOutputStream.close();
    }
}