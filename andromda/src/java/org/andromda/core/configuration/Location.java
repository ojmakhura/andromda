package org.andromda.core.configuration;

import java.io.File;
import java.io.FileFilter;
import java.io.Serializable;

import org.andromda.core.common.PathMatcher;


/**
 * Represents a location within a module search or mappings search.
 * @author Chad Brandon
 */
public class Location
    implements Serializable
{
    /**
     * The path of the location.
     */
    private String path;

    /**
     * The patterns (a comma seperated list) to
     * include in the path search
     */
    private String patterns;

    /**
     * Gets the path to this location.
     *
     * @return Returns the path to this location.
     */
    public String getPath()
    {
        return path;
    }

    /**
     * Sets the path to this location.
     *
     * @param path The path to this location.
     */
    public void setPath(String path)
    {
        this.path = path;
    }

    /**
     * Gets the patterns to include in this location.
     *
     * @return Returns the patterns.
     */
    public String getPatterns()
    {
        return patterns;
    }

    /**
     * Sets the patterns to include in this location.
     *
     * @param patterns The patterns to set.
     */
    public void setPatterns(String patterns)
    {
        this.patterns = patterns;
    }

    /**
     * Gets all files that are valid for this location.  It takes into
     * consideration the given patterns.  If the location is an actual
     * file, the an array containing that single file is returned.
     *
     * @return the valid files.
     */
    public File[] getFiles()
    {
        File[] files;
        final File file = new File(this.path);
        if (file.isDirectory())
        {
            files = file.listFiles(new Filter(this.patterns));
        }
        else
        {
            files = new File[] {file};
        }
        return files;
    }

    /**
     * The delimiter for seperating location patterns.
     */
    private static final String PATTERN_DELIMITER = ",";

    /**
     * The file filter used for matching the location patterns.
     */
    private static final class Filter
        implements FileFilter
    {
        private String[] patterns = null;

        Filter(final String patterns)
        {
            if (patterns != null)
            {
                this.patterns = patterns.split(PATTERN_DELIMITER);
            }
        }

        /**
         * @see java.io.FilenameFilter#accept(java.io.File, java.lang.String)
         */
        public boolean accept(final File file)
        {
            boolean accept = this.patterns == null;
            if (this.patterns != null && patterns.length > 0)
            {
                final int patternNumber = patterns.length;
                for (int ctr = 0; ctr < patternNumber; ctr++)
                {
                    final String pattern = patterns[ctr];
                    if (PathMatcher.wildcardMatch(
                            file.toString(),
                            pattern))
                    {
                        accept = true;
                        break;
                    }
                }
            }
            return accept;
        }
    }
}