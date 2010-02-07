package org.andromda.maven.plugin.modelarchiver;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.archiver.manager.ArchiverManager;


/**
 * Builds archived model uml files.
 *
 * @author Bob Fields
 * @goal uml
 * @phase package
 * @requiresProject
 * @description builds a versioned uml
 */
public class UmlArchiverMojo
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
     * The directory for the generated uml.
     *
     * @parameter expression="${project.build.outputDirectory}"
     * @required
     */
    private String outputDirectory;

    /**
     * The name of the uml file to generate.
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
     * To look up Archiver/UnArchiver implementations
     *
     * @parameter expression="${component.org.codehaus.plexus.archiver.manager.ArchiverManager}"
     * @required
     */
    protected ArchiverManager archiverManager;

    /**
     * The extensions to search for when doing replacement of embedded model HREF references
     * within the archived model file from non-versioned to versioned references.
     *
     * @parameter expression=".uml"
     * @required
     */
    protected String replacementExtensions;

    /**
     * Whether or not to do replacement of embedded model HREF reference extensions.
     *
     * @parameter expression=false
     * @required
     */
    protected boolean replaceExtensions;

    /**
     * The pattern of the model file(s) that should be versioned.
     *
     * @parameter expression=".*(\\.uml)"
     * @required
     * @readonly
     */
    private String modelFilePattern;

    /**
     * @parameter expression="${localRepository}"
     * @required
     * @readonly
     */
    protected ArtifactRepository localRepository;

    public void execute()
        throws MojoExecutionException
    {
        if(getLog().isDebugEnabled())
        {
            getLog().debug(" ======= UmlArchiverMojo settings =======");
            getLog().debug("modelSourceDirectory[" + modelSourceDirectory + ']');
            getLog().debug("workDirectory[" + workDirectory + ']');
            getLog().debug("outputDirectory[" + outputDirectory + ']');
            getLog().debug("finalName[" + finalName + ']');
            getLog().debug("replaceExtensions[" + replaceExtensions + ']');
        }

        try
        {
            // - the directory which to extract the model file
            //final File modelExtractDirectory = new File(this.workDirectory, "models/xmi");
            //modelExtractDirectory.mkdirs();
            final File buildDirectory = new File(this.workDirectory);
            if (!buildDirectory.exists())
            {
                buildDirectory.mkdirs();
            }
            else
            {
                // old files in directory are not automatically deleted.
                MojoUtils.deleteFiles(buildDirectory.getAbsolutePath(), "uml");
                FileUtils.deleteDirectory(new File(buildDirectory.getAbsolutePath(), "models"));
            }

            String packaging = this.project.getPackaging();
            // Use default naming convention of .library.uml and .profile.uml
            // Can't change goal or packaging type dynamically in archiver plugin.
            /*if (this.finalName.indexOf("datatype")>7 && !this.finalName.endsWith(".library"))
            {
                packaging = "library." + packaging;
            }
            else if (this.finalName.startsWith("andromda-") && !this.finalName.endsWith(".profile"))
            {
                packaging = "profile." + packaging;
            }*/
            final File modelSourceDir = modelSourceDirectory;
            final String[] replacementExtensions =
                this.replacementExtensions != null ? this.replacementExtensions.split(",\\s*") : new String[0];
            if (modelSourceDir.exists())
            {
                getLog().debug("Copy uml resources to " + buildDirectory.getAbsolutePath());
                final File[] modelFiles = modelSourceDir.listFiles();
                for (File file : modelFiles)
                {
                    if (file.isFile() && file.toString().matches(this.modelFilePattern))
                    {
                        final String version = MojoUtils.escapePattern(this.project.getVersion());
                        File buildFile = new File(buildDirectory, this.finalName + '.' + packaging);
                        FileUtils.copyFile(file, buildFile);
                        getLog().debug("File " + file + " copied to " + buildFile.getAbsolutePath());
                        final String extractedFilePath = buildFile.toString();
                        if (buildFile.isFile() && extractedFilePath.matches(this.modelFilePattern))
                        {
                            /*if (!buildFile.getName().endsWith(packaging))
                            {
                                //String archiveExtension = FileUtils.getExtension(extractedFilePath);
                                final File newFile =
                                    new File(buildDirectory,
                                        this.finalName + '.' + packaging);
                                getLog().info("File " + extractedFilePath + " renamed to " + newFile.getAbsolutePath());
                                buildFile.renameTo(newFile);
                                buildFile = newFile;
                            }*/
                            String contents = IOUtils.toString(new FileReader(buildFile));
                            if (replaceExtensions)
                            {
                                for (String replacementExtension : replacementExtensions)
                                {
                                    final String extension = MojoUtils.escapePattern(replacementExtension);
                                    final String extensionPattern = "((\\-" + version + ")?)" + extension;
                                    final String newExtension = "\\-" + version + extension;
                                    contents = contents.replaceAll(
                                            extensionPattern,
                                            newExtension);
                                    // Fix replacement error for standard UML profiles which follow the _Profile. naming convention.
                                    contents =
                                            contents.replaceAll(
                                                    "_Profile\\-" + version,
                                                    "_Profile");
                                }
                            }
                            final FileWriter fileWriter = new FileWriter(buildFile);
                            fileWriter.write(contents);
                            fileWriter.flush();
                            getLog().debug("File written " + buildFile.getAbsolutePath());
                        }
                    }
                }
            }

            final File umlFile = new File(buildDirectory, this.finalName + '.' + this.project.getPackaging());

            final Artifact artifact = this.project.getArtifact();

            // - set the artifact file back to the correct file (since we've installed modelJar already)
            artifact.setFile(umlFile);
            getLog().debug("File artifact set " + umlFile.getAbsolutePath() + " packaging " + packaging);
        }
        catch (final Throwable throwable)
        {
            throw new MojoExecutionException("Error assembling model", throwable);
        }
    }  
}