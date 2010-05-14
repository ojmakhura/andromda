package org.andromda.maven.plugin.modelarchiver;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.MojoExecutionException;
import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.archiver.UnArchiver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Builds archived model xml.zip files.
 *
 * @author Chad Brandon
 * @version $Id: $
 * @goal xml.zip
 * @phase package
 * @description builds a versioned xml.zip
 */
public class XmiZipArchiverMojo
        extends BaseArchiveMojo
{
    private static final String ARTIFACT_TYPE = "xml.zip";

    /**
     * The extensions to search for when doing replacement of embedded model HREF references
     * within the archived model file from non-versioned to versioned references.
     *
     * @parameter expression=".xml,.xml.zip,.xmi"
     * @required
     */
    protected String replacementExtensions;

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
     * Whether or not to do replacement of embedded model HREF reference extensions.
     *
     * @parameter expression="true"
     * @required
     */
    protected boolean replaceExtensions;

    /**
     * Whether or not the model should be attached as new artifact.
     *
     * @parameter expression="false"
     */
    private boolean attachArtifact;

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
            getLog().debug("attachArtifact[" + attachArtifact + ']');
        }

        try
        {
            final File buildDirectory = new File(this.workDirectory);
            if (buildDirectory.exists())
            {
                // old files in directory are not automatically deleted.
                deleteFiles(buildDirectory.getAbsolutePath(), ARTIFACT_TYPE);
                FileUtils.deleteDirectory(new File(buildDirectory, "models"));
            }
            // - the directory which to extract the model file
            final File modelExtractDirectory = new File(buildDirectory, "models/xmi");
            modelExtractDirectory.mkdirs();

            if (modelSourceDirectory.exists())
            {
                getLog().info("Copy xml.zip resources to " + modelExtractDirectory.getAbsolutePath());
                final File[] modelFiles = modelSourceDirectory.listFiles();
                for (final File file : modelFiles)
                {
                    if (file.isFile() && file.toString().matches(this.modelArchivePattern))
                    {
                        // - extract model file
                        this.unpack(file, modelExtractDirectory);

                        final File[] extractedModelFiles = modelExtractDirectory.listFiles();
                        for (final File extractedFile : extractedModelFiles)
                        {
                            final String extractedFilePath = extractedFile.toString();
                            if (extractedFile.isFile() && extractedFilePath.matches(this.modelFilePattern))
                            {
                                final File newFile =
                                        new File(modelExtractDirectory,
                                                this.finalName + '.' + FilenameUtils.getExtension(extractedFile.getName()));
                                extractedFile.renameTo(newFile);

                                if (replaceExtensions)
                                {
                                    getLog().info("Replace extensions in " + newFile);
                                    replaceExtensions(this.replacementExtensions, newFile);
                                }

                                final File xmlZipFile = new File(buildDirectory, this.finalName + '.' + ARTIFACT_TYPE);
                                this.writeModelArchive(xmlZipFile, newFile);
                            }
                        }
                    }
                }
            }

            final File xmlZipFile = new File(buildDirectory, this.finalName + "." + ARTIFACT_TYPE);
            if (!this.attachArtifact)
            {
                setArtifactFile(xmlZipFile);
            }
            else
            {
                //Attach artifact
                projectHelper.attachArtifact(project, ARTIFACT_TYPE, xmlZipFile);
            }
        }
        catch (final Throwable throwable)
        {
            throw new MojoExecutionException("Error assembling model", throwable);
        }
    }

    /**
     * Unpacks the archive file.
     *
     * @param file     File to be unpacked.
     * @param location Location where to put the unpacked files.
     * @throws MojoExecutionException if IOException or ArchiverException from unArchiver.extract()
     */
    private void unpack(
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
     * Writes the given given <code>model</code> archive file and includes
     * the file given by the <code>model</code>
     *
     * @param modelArchive the model archive.
     * @param model        the file of model to write.
     * @throws IOException
     */
    private void writeModelArchive(
            final File modelArchive,
            final File model)
            throws IOException
    {
        final ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(modelArchive));
        final ZipEntry zipEntry = new ZipEntry(model.getName());
        zipEntry.setMethod(ZipEntry.DEFLATED);
        zipOutputStream.putNextEntry(zipEntry);
        final FileInputStream inputStream = new FileInputStream(model);

        IOUtils.copy(inputStream, zipOutputStream);

        zipOutputStream.closeEntry();

        IOUtils.closeQuietly(inputStream);
        IOUtils.closeQuietly(zipOutputStream);
    }
}
