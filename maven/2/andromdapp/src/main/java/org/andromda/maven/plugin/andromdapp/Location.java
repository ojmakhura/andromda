package org.andromda.maven.plugin.andromdapp;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.plexus.util.DirectoryScanner;

/**
 * Represents a location.
 * 
 * @author Chad Brandon
 */
public class Location
{
    /**
     * The path of the location.
     */
    private String rootPath;
    
    /**
     * Retrieves the root path.
     * 
     * @return the root path.
     */
    public String getRootPath()
    {
        return this.rootPath;
    }
    
    /**
     * Defines what to include from the path of the location.
     */
    private String[] includes = new String[] {"**/*.java"};
    
    /**
     * Defines what to exclude from the path of the location.
     */
    private String[] excludes = new String[0];
    
    /**
     * Gets all paths from this location.
     * 
     * @return the paths.
     */
    public List getPaths()
    {
        final List paths = new ArrayList();
        if (this.rootPath != null && new File(this.rootPath).exists())
        {
            final DirectoryScanner scanner = new DirectoryScanner();
            scanner.setBasedir(this.rootPath);
            scanner.setIncludes(this.includes);
            scanner.setExcludes(this.excludes);
            scanner.scan();
    
            for (int ctr = 0; ctr < scanner.getIncludedFiles().length; ctr++)
            {
                paths.add(scanner.getIncludedFiles()[ctr]);
            }
        }
        return paths;
    }
}
