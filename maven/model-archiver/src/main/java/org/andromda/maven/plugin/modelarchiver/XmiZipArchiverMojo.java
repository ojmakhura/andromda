package org.andromda.maven.plugin.modelarchiver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.maven.archiver.MavenArchiveConfiguration;
import org.apache.maven.archiver.MavenArchiver;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectHelper;
import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.archiver.UnArchiver;
import org.codehaus.plexus.archiver.jar.JarArchiver;
import org.codehaus.plexus.archiver.manager.ArchiverManager;

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
     * Whether or not to do replacement of embedded model HREF reference extensions.
     *
     * @parameter expression="true"
     * @required
     */
    protected boolean replaceExtensions;

    /**
     * The maven project's helper.
     *
     * @parameter expression="${component.org.apache.maven.project.MavenProjectHelper}"
     * @required
     * @readonly
     */
    private MavenProjectHelper projectHelper;

    /**
     * Whether or not the model should have a jar created with it as well.
     *
     * @parameter
     */
    private boolean generateJar;
    private static final String[] JAR_INCLUDES = new String[] {"**/*"};
    private static final String[] JAR_EXCLUDES = new String[] {"**/package.html"};
    private final Collection<String> modelArchiveExcludes =
        new ArrayList<String>(Arrays.asList("*.xml.zip, **/*.java", "*reports*/**", "tests*/**"));

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

    /**
     * @see org.apache.maven.plugin.Mojo#execute()
     */
    public void execute()
        throws MojoExecutionException
    {
        if(getLog().isDebugEnabled())
        {
            getLog().debug(" ======= XmlZipArchiverMojo settings =======");
            getLog().debug("modelSourceDirectory[" + modelSourceDirectory + ']');
            getLog().debug("workDirectory[" + workDirectory + ']');
            getLog().debug("outputDirectory[" + outputDirectory + ']');
            getLog().debug("finalName[" + finalName + ']');
            getLog().debug("replaceExtensions[" + replaceExtensions + ']');
            getLog().debug("version[" + this.project.getVersion() + ']');
            getLog().debug("extension[" + this.replacementExtensions + ']');
            getLog().debug("extensionPattern[" + "((\\-" + this.project.getVersion() + ")?)" + this.replacementExtensions + ']');
            getLog().debug("newExtension[" + "\\-" + this.project.getVersion() + this.replacementExtensions + ']');
        }

        try
        {
            final File buildDirectory = new File(this.workDirectory);
            if (!buildDirectory.exists())
            {
                buildDirectory.mkdirs();
            }
            else
            {
                // old files in directory are not automatically deleted.
            	MojoUtils.deleteFiles(buildDirectory.getAbsolutePath(), "xml.zip");            	                
                FileUtils.deleteDirectory(new File(buildDirectory.getAbsolutePath(), "models"));
            }
            // - the directory which to extract the model file
            final File modelExtractDirectory = new File(this.workDirectory, "models/xmi");
            modelExtractDirectory.mkdirs();

            final File modelSourceDir = modelSourceDirectory;
            final String[] replacementExtensions =
                this.replacementExtensions != null ? this.replacementExtensions.split(",\\s*") : new String[0];
            if (modelSourceDir.exists())
            {
                getLog().info("Copy xml.zip resources to " + buildDirectory.getAbsolutePath());
                final File[] modelFiles = modelSourceDir.listFiles();
                for (final File file : modelFiles)
                {
                    if (file.isFile() && file.toString().matches(this.modelArchivePattern))
                    {
                        // - extract model file
                        this.unpack(
                                file,
                                modelExtractDirectory);
                        final File[] extractedModelFiles = modelExtractDirectory.listFiles();
                        for (final File extractedFile : extractedModelFiles)
                        {
                            final String extractedFilePath = extractedFile.toString();
                            if (extractedFile.isFile() && extractedFilePath.matches(this.modelFilePattern))
                            {
                                final File newFile =
                                        new File(modelExtractDirectory,
                                                this.finalName + '.' + FilenameUtils.getExtension(extractedFile.toString()));

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
                                if (replaceExtensions)
                                {
                                    final String version = MojoUtils.escapePattern(this.project.getVersion());
                                    for (String replacementExtension : replacementExtensions)
                                    {
                                        // add ' to extension, to match only href elements (example: href='abcdefg.xml')
                                        // and not abc.xml.efg substrings
                                        final String extension = MojoUtils.escapePattern(replacementExtension);
                                        final String extensionPattern = "((\\-" + version + ")?)" + extension+"(['\"|])";
                                        final String newExtension = "\\-" + version + extension+"$3";
                                        if(getLog().isDebugEnabled())
                                        {
                                            getLog().debug("replacing " + extensionPattern + " with " + newExtension + " in " + extractedFile.getName() + " from " + file.getAbsolutePath());
                                        }
                                        contents =
                                                contents.replaceAll(
                                                        extensionPattern,
                                                        newExtension);
                                    }
                                    // Put original versions back for standard _Profile references
                                    contents =
                                            contents.replaceAll(
                                                    "_Profile\\-" + version,
                                                    "_Profile");
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

                final Artifact jarArtifact =
                    artifactFactory.createArtifact(
                        project.getGroupId(),
                        project.getArtifactId(),
                        project.getVersion(),
                        null,
                        ATTACHED_ARTIFACT_TYPE);

                File modelJar = new File(workDirectory, finalName + '.' + ATTACHED_ARTIFACT_TYPE);

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
                projectHelper.attachArtifact(
                    project,
                    ATTACHED_ARTIFACT_TYPE,
                    null,
                    modelJar);
            }
        }
        catch (final Throwable throwable)
        {
            throw new MojoExecutionException("Error assembling model", throwable);
        }
    }

    /**
     * The type of the attached artifact.
     */
    private static final String ATTACHED_ARTIFACT_TYPE = "jar";

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
     * Unpacks the archive file.
     *
     * @param file File to be unpacked.
     * @param location Location where to put the unpacked files.
     * @throws MojoExecutionException if IOException or ArchiverException from unArchiver.extract()
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
        final ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(modelArchive));
        final ZipEntry zipEntry = new ZipEntry(name);
        zipEntry.setMethod(ZipEntry.DEFLATED);
        zipOutputStream.putNextEntry(zipEntry);
        final FileInputStream inputStream = new FileInputStream(path);
        final byte[] buffer = new byte[1024];
        int n = 0;
        while ((n =
                inputStream.read(
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