package org.andromda.maven.plugin.modelarchiver;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.maven.plugin.MojoExecutionException;

import java.io.File;


/**
 * Builds archived model uml files.
 *
 * @author Bob Fields
 * @version $Id: $
 * @goal profile.uml
 * @phase package
 * @description builds a versioned uml profile
 */
public class UmlProfileArchiverMojo
        extends BaseArchiveMojo
{
    private static final String ARTIFACT_TYPE = "profile.uml";

    /**
     * The extensions to search for when doing replacement of embedded model HREF references
     * within the archived model file from non-versioned to versioned references.
     *
     * @parameter expression=".profile.uml"
     * @required
     */
    protected String replacementExtensions;

    /**
     * The pattern of the model file(s) that should be versioned.
     *
     * @parameter expression=".*(\\.profile\\.uml)"
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
            getLog().debug(" ======= UmlProfileArchiverMojo settings =======");
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
                getLog().info("Copy " + ARTIFACT_TYPE + " resources to " + buildDirectory.getAbsolutePath());
                final File[] modelFiles = modelSourceDirectory.listFiles();
                for (final File file : modelFiles)
                {
                    if (file.isFile() && file.toString().matches(this.modelFilePattern))
                    {
                        final File newFile =
                                new File(buildDirectory,
                                        this.finalName + '.' + ARTIFACT_TYPE);
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
