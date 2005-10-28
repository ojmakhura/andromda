package org.andromda.core.configuration;

import java.io.Serializable;

import java.net.URL;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.andromda.core.common.PathMatcher;
import org.andromda.core.common.ResourceUtils;


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
    public URL[] getResources()
    {
        URL[] resources;
        final URL url = ResourceUtils.toURL(this.path);
        if (url != null)
        {
            if (ResourceUtils.isFile(url))
            {
                resources = new URL[] {url};
            }
            else
            {
                final Collection matchedResources = new ArrayList();
                final Collection contents = this.getDirectoryContents(url);
                for (final Iterator iterator = contents.iterator(); iterator.hasNext();)
                {
                    String path = (String)iterator.next();
                    if (this.matchesPattern(path))
                    {
                        path = url.toString().endsWith(FORWARD_SLASH) ? path : FORWARD_SLASH + path;
                        matchedResources.add(ResourceUtils.toURL(url + path));
                    }
                }
                resources = (URL[])matchedResources.toArray(new URL[0]);
            }
        }
        else
        {
            resources = new URL[0];
        }
        return resources;
    }

    /**
     * Gets the contents of the directory, only the first level is retrieved
     * if no patterns are defined, otherwise all levels are retrieved.
     * @param url the URL of the directory.
     * @return a collection of paths.
     */
    private Collection getDirectoryContents(final URL url)
    {
        final boolean patternsDefined = this.patterns != null && this.patterns.length() > 0;
        return ResourceUtils.getDirectoryContents(
            url,
            0,
            patternsDefined);
    }

    /**
     * The forward slash character.
     */
    private static final String FORWARD_SLASH = "/";

    /**
     * The delimiter for seperating location patterns.
     */
    private static final String PATTERN_DELIMITER = ",";

    /**
     * Indicates whether or not the given <code>path</code> matches on
     * one or more of the patterns defined within this class (this automatically)
     * returns true if no patterns are defined.
     *
     * @param path the path to match on.
     * @return true/false
     */
    private boolean matchesPattern(final String path)
    {
        boolean matches = patterns == null;
        if (!matches)
        {
            String[] patterns = this.patterns.split(PATTERN_DELIMITER);
            if (patterns.length > 0)
            {
                final int patternNumber = patterns.length;
                for (int ctr = 0; ctr < patternNumber; ctr++)
                {
                    final String pattern = patterns[ctr];
                    if (PathMatcher.wildcardMatch(
                            path,
                            pattern))
                    {
                        matches = true;
                        break;
                    }
                }
            }
        }
        return matches;
    }
}