package org.andromda.maven.plugin.distribution;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.plugin.MojoExecutionException;
import org.codehaus.plexus.util.DirectoryScanner;


/**
 * Represents a location which represents a location that can have
 * values included/excluded.
 *
 * @author Chad Brandon
 */
public class Location
{
    /**
     * The patterns to exclude.
     */
    private String[] excludes;

    /**
     * @param excludes The excludes to set.
     */
    public void setExcludes(String[] excludes)
    {
        this.excludes = excludes;
    }

    /**
     * The patterns to include.
     */
    private String[] includes;

    /**
     * @param includes The includes to set.
     */
    public void setIncludes(String[] includes)
    {
        this.includes = includes;
    }

    /**
     * The path to which output should be generated.
     */
    private String outputPath;

    /**
     * Sets the output path.
     * 
     * @param outputPath The outputPath to set.
     */
    public void setOutputPath(String outputPath)
    {
        this.outputPath = outputPath;
    }
    
    /**
     * Retrieves the path to which output of this location should be written.
     * 
     * @return the output path.
     */
    public String getOuputPath()
    {
        return this.outputPath;
    }

    /**
     * The path to the location.
     */
    private String path;

    /**
     * Sets the path of the location.
     * 
     * @param path The path to set.
     */
    public void setPath(String path)
    {
        this.path = path;
    }
    
    /**
     * Retrieves the complete file given the <code>relativePath</code>.
     * 
     * @return the location's path.
     */
    public File getFile(final String relativePath)
    {
        File file;
        final File filePath = new File(this.path);
        if (filePath.isFile())
        {
            file = filePath;
        }
        else
        {
            file = new File(this.path, relativePath);
        }
        return file;
    }

    /**
     * Retrieves all the paths found in this location.
     *
     * @return all poms found.
     * @throws MojoExecutionException
     */
    public List getPaths()
    {
        final DirectoryScanner scanner = new DirectoryScanner();
        final File filePath = new File(this.path);
        final List paths = new ArrayList();
        if (filePath.isDirectory())
        {
            scanner.setBasedir(this.path);
            scanner.setIncludes(this.includes);    
            scanner.setExcludes(this.excludes);
            scanner.scan();
    
            final String[] files = scanner.getIncludedFiles();
            for (int ctr = 0; ctr < files.length; ctr++)
            {
                paths.add(files[ctr]);
            }
        }
        else if (filePath.isFile())
        {
            paths.add(this.path);
        }
        return paths;
    }
}