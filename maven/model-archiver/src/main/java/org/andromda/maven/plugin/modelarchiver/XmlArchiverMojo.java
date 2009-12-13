package org.andromda.maven.plugin.modelarchiver;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.archiver.manager.ArchiverManager;


/**
 * Builds archived model xml files.
 *
 * @author Bob Fields
 * @goal xml
 * @phase package
 * @requiresProject
 * @description builds a versioned xml
 */
public class XmlArchiverMojo
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
     * The directory for the generated xml.
     *
     * @parameter expression="${project.build.outputDirectory}"
     * @required
     */
    private String outputDirectory;

    /**
     * The name of the xml file to generate.
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
     * The pattern of the model file(s) that should be versioned.
     *
     * @parameter expression=".*(\\.xml)"
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
        getLog().debug(" ======= XmlArchiverMojo settings =======");
        getLog().debug("modelSourceDirectory[" + modelSourceDirectory + ']');
        getLog().debug("workDirectory[" + workDirectory + ']');
        getLog().debug("outputDirectory[" + outputDirectory + ']');
        getLog().debug("finalName[" + finalName + ']');

        try
        {
            // - the directory which to extract the model file
            //final File modelExtractDirectory = new File(this.workDirectory);
            // Copy unaltered files to /target directory
            final File buildDirectory = new File(this.workDirectory);
            if (!buildDirectory.exists())
            {
                buildDirectory.mkdirs();
            }
            else
            {
                // old files in directory are not automatically deleted.                
                MojoUtils.deleteFiles(buildDirectory.getAbsolutePath(), "xml");            	                
                FileUtils.deleteDirectory(new File(buildDirectory.getAbsolutePath(), "models"));
            }
            // - the directory which to extract the model file

            final File modelSourceDir = modelSourceDirectory;
            //final String[] replacementExtensions =
            //    this.replacementExtensions != null ? this.replacementExtensions.split(",\\s*") : new String[0];
            if (modelSourceDir.exists())
            {
                getLog().info("Copy xml resources to " + buildDirectory.getAbsolutePath());
                final File[] modelFiles = modelSourceDir.listFiles();
                for (int ctr = 0; ctr < modelFiles.length; ctr++)
                {
                    final File file = modelFiles[ctr];
                    if (file.isFile() && file.toString().matches(this.modelFilePattern))
                    {
                        FileUtils.copyFileToDirectory(
                            file,
                            buildDirectory);

                        // - copy the model to the extract directory
                        final File[] extractedModelFiles = buildDirectory.listFiles();
                        for (int ctr2 = 0; ctr2 < extractedModelFiles.length; ctr2++)
                        {
                            final File extractedFile = extractedModelFiles[ctr2];
                            final String extractedFilePath = extractedFile.toString();
                            if (extractedFile.isFile() && extractedFilePath.matches(this.modelFilePattern))
                            {
                                final File newFile =
                                    new File(buildDirectory,
                                        this.finalName + '.' + FilenameUtils.getExtension(extractedFile.toString()));
                                boolean renamed = extractedFile.renameTo(newFile);
                                getLog().info("Renamed xml " + extractedFile.getAbsolutePath() + " to " + newFile.getAbsolutePath() + ' ' + renamed);
                                String contents = IOUtils.toString(new FileReader(newFile));
                                final String version = MojoUtils.escapePattern(this.project.getVersion());
                            /*if (replaceExtensions)
                                {
                                    for (int ctr3 = 0; ctr3 < replacementExtensions.length; ctr3++)
                                    {
                                        final String extension = escapePattern(replacementExtensions[ctr3]);
                                        final String extensionPattern = "((\\-" + version + ")?)" + extension;
                                        final String newExtension = "\\-" + version + extension;
                                        contents = contents.replaceAll(
                                                extensionPattern,
                                                newExtension); */
                                        // Fix replacement error for standard UML profiles which follow the _Profile. naming convention.
                                        contents =
                                            contents.replaceAll(
                                                "_Profile\\-" + version,
                                                "_Profile");
                                    /*}
                                }*/
                                final FileWriter fileWriter = new FileWriter(newFile);
                                fileWriter.write(contents);
                                fileWriter.flush();
                            }
                        }
                    }
                }
            }

            final File xmlFile = new File(buildDirectory, this.finalName + ".xml");

            final Artifact artifact = this.project.getArtifact();

            // - set the artifact file back to the correct file (since we've installed modelJar already)
            artifact.setFile(xmlFile);
        }
        catch (final Throwable throwable)
        {
            throw new MojoExecutionException("Error assembling model", throwable);
        }
    }   
}