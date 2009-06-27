package org.andromda.maven.plugin.site;

import java.io.File;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.archiver.UnArchiver;
import org.codehaus.plexus.archiver.manager.ArchiverManager;
import org.codehaus.plexus.archiver.manager.NoSuchArchiverException;
import org.codehaus.plexus.util.FileUtils;

/**
 * Abstract parent class used by site mojos to perform helper functions like copy
 * and unpack.
 * 
 * @author vancek
 *
 */
public abstract class AbstractSiteMojo
    extends AbstractMojo
{
    /**
     * To look up Archiver/UnArchiver implementations
     * 
     * @parameter expression="${component.org.codehaus.plexus.archiver.manager.ArchiverManager}"
     * @required
     * @readonly
     */
    protected ArchiverManager archiverManager;
    
    /**
     * Does the actual copy of the file or directory
     * 
     * @param sourceFile represents the file to copy.
     * @param destFile file name of destination file.
     * 
     * @throws MojoExecutionException with a message if an error occurs.
     */
    public void copyFile(File sourceFile, File destFile)
        throws MojoExecutionException
    {
        try
        {
            this.getLog().info("Copying " + sourceFile.getAbsolutePath() + " to " + destFile.getAbsolutePath());
            if (sourceFile.isDirectory())
            {
                FileUtils.copyDirectoryStructure(
                        sourceFile,
                        destFile);
            }
            else
            {
                FileUtils.copyFile(
                        sourceFile, 
                        destFile);
            }
        }
        catch (Exception e)
        {
            throw new MojoExecutionException("Error copying file from " + sourceFile + " to " + destFile, e);
        }
    }
    
    /**
     * Unpacks the archive file
     * 
     * @param file File to be unpacked.
     * @param location Location where to put the unpacked files.
     */
    protected void unpack(File file, File location)
        throws MojoExecutionException
    {
        final String archiveExt = FileUtils.getExtension(file.getAbsolutePath()).toLowerCase();
        try
        {
            location.mkdirs();
            UnArchiver unArchiver;
            unArchiver = archiverManager.getUnArchiver(archiveExt);
            unArchiver.setSourceFile(file);
            unArchiver.setDestDirectory(location);
            unArchiver.extract();
        }
        catch (NoSuchArchiverException e)
        {
            throw new MojoExecutionException("Unknown archiver type", e);
        }
        catch (ArchiverException e)
        {
            e.printStackTrace();
            throw new MojoExecutionException("Error unpacking file: " + 
                    file + " to: " + location + "\r\n" + e.toString(), e);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new MojoExecutionException("Error unpacking file: " + 
                    file.getAbsolutePath() + " to: " + location + "\r\n" + e.toString(), e);
        }
    }
}
