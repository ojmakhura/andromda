package org.andromda.beautifier.plugin;

/*
 * Run org.andromda.beautifier as a maven plugin after generating sources. 
 * Beautifier (open source) is available from http://www.andromda.org/activities.html.
 * Version 1.1.7 7/15/2007 is current.
 * Beautifier changes all fully-qualified classname references to import statements.
 * Normally used with AndroMDA, added the the mda/pom.xml file build plugins as a build goal.
 */

import de.plushnikov.doctorjim.ImportProcessor;
import de.plushnikov.doctorjim.javaparser.ParseException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * Runs andromda beautifier
 * @phase process-sources
 * @goal beautify-imports
 * @requiresProject false
 */
public class ImportBeautifierMojo
    extends AbstractMojo
{
    private static final Logger LOG = Logger.getLogger(ImportBeautifierMojo.class);

    /**
     * Location of the directory to be recursively beautified.
     * Defaults to all source directories for parent project (..)
     * @parameter expression="${basedir}"
     * @optional
     */
    private File inputDirectory;

    /**
     * Location of the output directory for beautified source.
     * Defaults to the source directory
     * @parameter expression="${basedir}"
     * @optional
     */
    private File outputDirectory;

    /*
     * Run import beautifier utility.
     * @parameter default-value="true"
     * @optional
    private boolean runBeautifier;
     */

    /*
     * Delegate formatting to Jalopy after beautifying imports.
     * @parameter default-value="false"
     * @optional
    private boolean runJalopy;
     */

    /**
     * Whether or not processing should be skipped (this is if you just want to force Beautifier not to run on your code, i.e. if generating
     * site from already formatted source code).
     * 
     * @parameter expression="${beautifier.run.skip}"
     */
    private boolean skipProcessing = false;

    /**
     * @see org.apache.maven.plugin.Mojo#execute()
     */
    public void execute()
        throws MojoExecutionException
    {
        if (this.skipProcessing)
        {
            getLog().info("process-sources:beautify-imports skipProcessing");
            return;
        }
        getLog().info("process-sources:beautify-imports on " + this.inputDirectory);
        File file = this.inputDirectory;
        if ( !file.exists() )
        {
            throw new MojoExecutionException("Beautifier format input directory does not exist: " + this.inputDirectory);
        }

        if (this.outputDirectory==null)
        {
            this.outputDirectory = this.inputDirectory;
        }
        else
        {
            File outputFile = this.outputDirectory;
            if ( !outputFile.exists() )
            {
                throw new MojoExecutionException("Beautifier format output directory does not exist: " 
                        + this.outputDirectory);
            }
        }
        String directoryString = null;

        try
        {
            directoryString = file.getCanonicalPath();
            String[] extensions = {"java"};
            Collection<File> files = FileUtils.listFiles(file, extensions, true);
            ImportProcessor processor = new ImportProcessor();
            for (File formatFile : files)
            {
                try
                {
                    processor.organizeImports(formatFile.getCanonicalPath());
                }
                catch (ParseException e)
                {
                    // Don't allow a single file error to kill the whole process.
                    LOG.error("Beautifier error on " + formatFile.getCanonicalPath() + ": " + e.getMessage());
                }
            }
        }
        catch ( FileNotFoundException e )
        {
            throw new MojoExecutionException( "FileNotFound creating beautifier output: " + directoryString, e );
        }
        catch ( IOException e )
        {
            throw new MojoExecutionException( "Error creating beautifier output: " + directoryString, e );
        }
    }
}
