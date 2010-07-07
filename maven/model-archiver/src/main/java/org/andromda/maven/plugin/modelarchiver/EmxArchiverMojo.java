package org.andromda.maven.plugin.modelarchiver;

import java.io.File;
import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * Builds archived model emx files.
 *
 * @author Chad Brandon
 * @version $Id: $
 * @goal emx
 * @phase package
 * @description builds a versioned emx
 */
public class EmxArchiverMojo
        extends BaseArchiveMojo
{
    private static final String ARTIFACT_TYPE = "emx";

    /**
     * The extensions to search for when doing replacement of embedded model HREF references
     * within the archived model file from non-versioned to versioned references.
     *
     * @parameter expression=".emx"
     * @required
     */
    protected String replacementExtensions;

    /**
     * The pattern of the model file(s) that should be versioned.
     *
     * @parameter expression=".*(\\.emx)"
     * @required
     * @readonly
     */
    protected String modelFilePattern;

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
            getLog().debug(" ======= EmxArchiverMojo settings =======");
            getLog().debug("modelSourceDirectory[" + modelSourceDirectory + ']');
            getLog().debug("workDirectory[" + workDirectory + ']');
            getLog().debug("outputDirectory[" + outputDirectory + ']');
            getLog().debug("finalName[" + finalName + ']');
            getLog().debug("replaceExtensions[" + replaceExtensions + ']');
        }

        try
        {
            final File buildDirectory = this.workDirectory;
            if (buildDirectory.exists())
            {   // old files in directory are not automatically deleted.
                deleteFiles(buildDirectory.getAbsolutePath(), ARTIFACT_TYPE);
            }
            else
            {
                buildDirectory.mkdirs();
            }

            if (modelSourceDirectory.exists())
            {
                final File[] modelFiles = modelSourceDirectory.listFiles();
                for (final File file : modelFiles)
                {
                    if (file.isFile() && file.toString().matches(this.modelFilePattern))
                    {
                        final File newFile =
                                new File(buildDirectory,
                                        this.finalName + '.' + ARTIFACT_TYPE);
                        getLog().info("File " + file + " copied  to " + newFile);
                        FileUtils.copyFile(file, newFile);

                        if (replaceExtensions)
                        {
                            getLog().info("Replace extensions in " + newFile);
                            replaceExtensions(this.replacementExtensions, newFile);
                        }

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
