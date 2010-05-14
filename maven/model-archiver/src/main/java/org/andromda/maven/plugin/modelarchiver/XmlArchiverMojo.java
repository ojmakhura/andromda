package org.andromda.maven.plugin.modelarchiver;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.maven.plugin.MojoExecutionException;

import java.io.File;


/**
 * Builds archived model xml files.
 *
 * @author Bob Fields
 * @version $Id: $
 * @goal xml
 * @phase package
 * @description builds a versioned xml
 */
public class XmlArchiverMojo
        extends BaseArchiveMojo
{
    private static final String ARTIFACT_TYPE = "xml";

    /**
     * The pattern of the model file(s) that should be versioned.
     *
     * @parameter expression=".*(\\.xml)"
     * @required
     * @readonly
     */
    private String modelFilePattern;

    /**
     * <p>execute</p>
     *
     * @throws org.apache.maven.plugin.MojoExecutionException
     *          if any.
     * @see org.apache.maven.plugin.Mojo#execute()
     */
    public void execute()
            throws MojoExecutionException
    {
        if (getLog().isDebugEnabled())
        {
            getLog().debug(" ======= XmlArchiverMojo settings =======");
            getLog().debug("modelSourceDirectory[" + modelSourceDirectory + ']');
            getLog().debug("workDirectory[" + workDirectory + ']');
            getLog().debug("outputDirectory[" + outputDirectory + ']');
            getLog().debug("finalName[" + finalName + ']');
        }

        try
        {
            final File buildDirectory = new File(this.workDirectory);
            if (buildDirectory.exists())
            {   // old files in directory are not automatically deleted.
                deleteFiles(buildDirectory.getAbsolutePath(), ARTIFACT_TYPE);
            }
            else
            {
                buildDirectory.mkdirs();
            }

            //final String[] replacementExtensions =
            //    this.replacementExtensions != null ? this.replacementExtensions.split(",\\s*") : new String[0];
            if (modelSourceDirectory.exists())
            {
                final File[] modelFiles = modelSourceDirectory.listFiles();
                for (final File file : modelFiles)
                {
                    if (file.isFile() && file.toString().matches(this.modelFilePattern))
                    {
                        final File newFile =
                                new File(buildDirectory,
                                        this.finalName + '.' + FilenameUtils.getExtension(file.getName()));
                        getLog().info("File " + file + " copied  to " + newFile);
                        FileUtils.copyFile(file, newFile);

                        String contents = FileUtils.readFileToString(newFile);
                        final String version = escapePattern(this.project.getVersion());
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
                        FileUtils.writeStringToFile(newFile, contents);

                        setArtifactFile(newFile);
                    }
                }
            }
        }
        catch (final Throwable throwable)
        {
            throw new MojoExecutionException("Error assembling model", throwable);
        }
    }
}
