package org.andromda.maven.plugin.cartridge;

import java.io.File;
import java.io.IOException;

import org.andromda.core.common.ExceptionUtils;
import org.andromda.maven.plugin.AndroMDAMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.archiver.Archiver;
import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.util.FileUtils;

/**
 * Updates the cartridge expected output with the current cartridge output. Invoke it with
 * <code>mvn andromda-cartridge:update</code> when you are inside the cartridge root directory.
 *
 * @goal update
 * @requiresDependencyResolution test
 * @description update AndroMDA Cartridge test archive
 * @author Chad Brandon
 * @author Peter Friese
 * @author Bob Fields
 */
public class CartridgeTestUpdaterMojo
        extends AbstractCartridgeTestMojo
{
    /**
     * @see org.apache.maven.plugin.Mojo#execute()
     */
    public void execute() throws MojoExecutionException, MojoFailureException
    {
        final File expectedOutputArchive = new File(this.expectedOutputArchive);

        try
        {
            this.getLog().info("-----------------------------------------------------------------------------");
            this.getLog().info("  A n d r o M D A   C a r t r i d g e   T e s t   S u i t e   U p d a t e r  ");
            this.getLog().info("-----------------------------------------------------------------------------");

            // - add the cartridge test dependencies (any dependencies of the cartridge test plugin)
            this.addCartridgeTestDependencies();

            // - first run AndroMDA with the test configuration
            final AndroMDAMojo andromdaMojo = new AndroMDAMojo();
            andromdaMojo.setConfigurationUri(this.configurationUri);
            andromdaMojo.setProject(this.project);
            andromdaMojo.setSettings(this.settings);
            andromdaMojo.setPropertyFiles(this.propertyFiles);
            andromdaMojo.execute();

            // - pack the expected output archive
            this.pack(new File(this.actualDirectory), expectedOutputArchive);

        }
        catch (final Throwable throwable)
        {
            if (throwable instanceof MojoExecutionException && !this.testFailureIgnore)
            {
                throw (MojoExecutionException)throwable;
            }
            else if (this.testFailureIgnore)
            {
                this.getLog().error("An error occured while updating cartridge archive '"
                        + this.project.getArtifactId() + "'", ExceptionUtils.getRootCause(throwable));
            }
            else
            {
                throw new MojoExecutionException("An error occured while updating cartridge archive '"
                        + this.project.getArtifactId() + "'", ExceptionUtils.getRootCause(throwable));
            }
        }

    }

    /**
     * Packs the actual directory contents into the expected archive.
     *
     * @param location Location where to put the unpacked files.
     * @param file Archive file to be created.
     */
    protected void pack(final File location,
        final File file) throws MojoExecutionException
    {
        final String archiveExt = FileUtils.getExtension(file.getAbsolutePath()).toLowerCase();
        try
        {
            final Archiver archiver;
            archiver = this.archiverManager.getArchiver(archiveExt);
            archiver.setDestFile(file);
            archiver.addDirectory(location);
            archiver.createArchive();
        }
        catch (Throwable throwable)
        {
            if (this.testFailureIgnore)
            {
                this.getLog().error(this.project.getArtifactId() + "Error packing directory: " + location + "to: " + file, throwable);
            }
            else if (throwable instanceof IOException || throwable instanceof ArchiverException)
            {
                throw new MojoExecutionException("Error packing directory: " + location + "to: " + file, throwable);
            }
        }
    }

}