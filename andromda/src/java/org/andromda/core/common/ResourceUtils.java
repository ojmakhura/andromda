package org.andromda.core.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.Reader;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.lang.StringUtils;


/**
 * Provides utilities for loading resources.
 *
 * @author Chad Brandon
 */
public class ResourceUtils
{
    /**
     * All archive files start with this prefix.
     */
    private static final String ARCHIVE_PREFIX = "jar:";

    /**
     * Retrieves a resource from the current classpath.
     *
     * @param resourceName the name of the resource
     * @return the resource url
     */
    public static URL getResource(final String resourceName)
    {
        ExceptionUtils.checkEmpty(
            "resourceName",
            resourceName);
        final ClassLoader loader = ClassUtils.getClassLoader();
        return loader != null ? loader.getResource(resourceName) : null;
    }

    /**
     * Loads the resource and returns the contents as a String.
     *
     * @param resource the name of the resource.
     * @return String
     */
    public static String getContents(final URL resource)
    {
        try
        {
            return getContents(resource != null ? new InputStreamReader(resource.openStream()) : null);
        }
        catch (final Throwable throwable)
        {
            throw new RuntimeException(throwable);
        }
    }

    /**
     * The line separator.
     */
    private static final char LINE_SEPARATOR = '\n';

    /**
     * Loads the resource and returns the contents as a String.
     *
     * @param resource the name of the resource.
     * @return the contents of the resource as a string.
     */
    public static String getContents(final Reader resource)
    {
        final StringBuffer contents = new StringBuffer();
        try
        {
            if (resource != null)
            {
                BufferedReader resourceInput = new BufferedReader(resource);
                String line;
                while ((line = resourceInput.readLine()) != null)
                {
                    contents.append(line + LINE_SEPARATOR);
                }
                resourceInput.close();
                resourceInput = null;
            }
        }
        catch (final Throwable throwable)
        {
            throw new RuntimeException(throwable);
        }
        return contents.toString();
    }

    /**
     * If the <code>resource</code> represents a classpath archive (i.e. jar, zip, etc), this method will retrieve all
     * contents from that resource as a List of relative paths (relative to the archive base). Otherwise an empty List
     * will be returned.
     *
     * @param resource the resource from which to retrieve the contents
     * @return a list of Strings containing the names of every nested resource found in this resource.
     */
    public static List getClassPathArchiveContents(final URL resource)
    {
        final List contents = new ArrayList();
        if (isArchive(resource))
        {
            final ZipFile archive = getArchive(resource);
            if (archive != null)
            {
                for (final Enumeration entries = archive.entries(); entries.hasMoreElements();)
                {
                    final ZipEntry entry = (ZipEntry)entries.nextElement();
                    contents.add(entry.getName());
                }
            }
        }
        return contents;
    }

    /**
     * If this <code>resource</code> happens to be a directory, it will load the contents of that directory into a
     * List and return the list of names relative to the given <code>resource</code> (otherwise it will return an empty
     * List).
     *
     * @param resource the resource from which to retrieve the contents
     * @param levels the number of levels to step down if the resource ends up being a directory (if its an artifact,
     *               levels will be ignored).
     * @return a list of Strings containing the names of every nested resource found in this resource.
     */
    public static List getDirectoryContents(
        final URL resource,
        final int levels)
    {
        return getDirectoryContents(
            resource,
            levels,
            true);
    }

    /**
     * The character used for substituing whitespace in paths.
     */
    private static final String PATH_WHITESPACE_CHARACTER = "%20";

    /**
     * Replaces any escape characters in the given file path with their
     * counterparts.
     *
     * @param filePath the path of the file to unescape.
     * @return the unescaped path.
     */
    public static String unescapeFilePath(String filePath)
    {
        if (filePath != null && filePath.length() > 0)
        {
            filePath = filePath.replaceAll(
                    PATH_WHITESPACE_CHARACTER,
                    " ");
        }
        return filePath;
    }

    /**
     * If this <code>resource</code> happens to be a directory, it will load the contents of that directory into a
     * List and return the list of names relative to the given <code>resource</code> (otherwise it will return an empty
     * List).
     *
     * @param resource the resource from which to retrieve the contents
     * @param levels the number of levels to step down if the resource ends up being a directory (if its an artifact,
     *               levels will be ignored).
     * @param includeSubdirectories whether or not to include subdirectories in the contents.
     * @return a list of Strings containing the names of every nested resource found in this resource.
     */
    public static List getDirectoryContents(
        final URL resource,
        final int levels,
        boolean includeSubdirectories)
    {
        final List contents = new ArrayList();
        if (resource != null)
        {
            // - create the file and make sure we remove any path white space characters
            final File fileResource = new File(unescapeFilePath(resource.getFile()));
            if (fileResource.isDirectory())
            {
                File rootDirectory = fileResource;
                for (int ctr = 0; ctr < levels; ctr++)
                {
                    rootDirectory = rootDirectory.getParentFile();
                }
                final File pluginDirectory = rootDirectory;
                loadFiles(
                    pluginDirectory,
                    contents,
                    includeSubdirectories);

                // - remove the root path from each file
                for (final ListIterator iterator = contents.listIterator(); iterator.hasNext();)
                {
                    iterator.set(
                        StringUtils.replace(
                            ((File)iterator.next()).getPath().replace(
                                '\\',
                                '/'),
                            pluginDirectory.getPath().replace(
                                '\\',
                                '/') + '/',
                            ""));
                }
            }
        }
        return contents;
    }

    /**
     * Loads all files find in the <code>directory</code> and adds them to the <code>fileList</code>.
     *
     * @param directory the directory from which to load all files.
     * @param fileList  the List of files to which we'll add the found files.
     * @param includeSubdirectories whether or not to include sub directories when loading the files.
     */
    private static void loadFiles(
        final File directory,
        final List fileList,
        boolean includeSubdirectories)
    {
        if (directory != null)
        {
            final File[] files = directory.listFiles();
            if (files != null)
            {
                for (int ctr = 0; ctr < files.length; ctr++)
                {
                    File file = files[ctr];
                    if (!file.isDirectory())
                    {
                        fileList.add(file);
                    }
                    else if (includeSubdirectories)
                    {
                        loadFiles(
                            file,
                            fileList,
                            includeSubdirectories);
                    }
                }
            }
        }
    }

    /**
     * Returns true/false on whether or not this <code>resource</code> represents an archive or not (i.e. jar, or zip,
     * etc).
     *
     * @return true if its an archive, false otherwise.
     */
    public static boolean isArchive(final URL resource)
    {
        return resource != null && resource.toString().startsWith(ARCHIVE_PREFIX);
    }

    /**
     * If this <code>resource</code> is an archive file, it will return the resource as an archive.
     *
     * @return the archive as a ZipFile
     */
    public static ZipFile getArchive(final URL resource)
    {
        try
        {
            ZipFile archive = null;
            if (resource != null)
            {
                String resourceUrl = resource.toString();
                resourceUrl = resourceUrl.replaceFirst(
                        ARCHIVE_PREFIX,
                        "");
                final int entryPrefixIndex = resourceUrl.indexOf('!');
                if (entryPrefixIndex != -1)
                {
                    resourceUrl = resourceUrl.substring(
                            0,
                            entryPrefixIndex);
                }
                archive = new ZipFile(new URL(resourceUrl).getFile());
            }
            return archive;
        }
        catch (final Throwable throwable)
        {
            throw new RuntimeException(throwable);
        }
    }

    /**
     * Loads the file resource and returns the contents as a String.
     *
     * @param resourceName the name of the resource.
     * @return String
     */
    public static String getContents(final String resourceName)
    {
        return getContents(getResource(resourceName));
    }

    /**
     * Takes a className as an argument and returns the URL for the class.
     *
     * @param className
     * @return java.net.URL
     */
    public static URL getClassResource(final String className)
    {
        ExceptionUtils.checkEmpty(
            "className",
            className);
        return getResource(getClassNameAsResource(className));
    }

    /**
     * Gets the class name as a resource.
     *
     * @param className the name of the class.
     * @return the class name as a resource path.
     */
    private static String getClassNameAsResource(final String className)
    {
        return className.replace(
            '.',
            '/') + ".class";
    }

    /**
     * <p/>
     * Retrieves a resource from an optionally given <code>directory</code> or from the package on the classpath. </p>
     * <p/>
     * If the directory is specified and is a valid directory then an attempt at finding the resource by appending the
     * <code>resourceName</code> to the given <code>directory</code> will be made, otherwise an attempt to find the
     * <code>resourceName</code> directly on the classpath will be initiated. </p>
     *
     * @param resource  the name of a resource
     * @param directory the directory location
     * @return the resource url
     */
    public static URL getResource(
        final String resourceName,
        final String directory)
    {
        ExceptionUtils.checkEmpty(
            "resourceName",
            resourceName);

        if (directory != null)
        {
            final File file = new File(directory, resourceName);
            if (file.exists())
            {
                try
                {
                    return file.toURL();
                }
                catch (final MalformedURLException exception)
                {
                    // - ignore, we just try to find the resource on the classpath
                }
            }
        }
        return getResource(resourceName);
    }

    /**
     * Makes the directory for the given location if it doesn't exist.
     *
     * @param location the location to make the directory.
     */
    public static void makeDirectories(final String location)
    {
        final File file = new File(location);
        final File parent = file.getParentFile();
        if (parent != null)
        {
            parent.mkdirs();
        }
    }

    /**
     * Gets the time as a <code>long</code> when this <code>resource</code> was last modified.
     * If it can not be determined <code>0</code> is returned.
     *
     * @param resource the resource from which to retrieve
     *        the last modified time.
     * @return the last modified time or 0 if it couldn't be retrieved.
     */
    public static final long getLastModifiedTime(final URL resource)
    {
        long lastModified;
        try
        {
            final File file = new File(resource.getFile());
            if (file.exists())
            {
                lastModified = file.lastModified();
            }
            else
            {
                URLConnection uriConnection = resource.openConnection();
                lastModified = uriConnection.getLastModified();

                // - we need to set the urlConnection to null and explicity
                //   call garbage collection, otherwise the JVM won't let go
                //   of the URL resource
                uriConnection = null;
                System.gc();
            }
        }
        catch (final Exception exception)
        {
            lastModified = 0;
        }
        return lastModified;
    }

    /**
     * <p>
     * Retrieves a resource from an optionally given <code>directory</code> or from the package on the classpath.
     * </p>
     * If the directory is specified and is a valid directory then an attempt at finding the resource by appending the
     * <code>resourceName</code> to the given <code>directory</code> will be made, otherwise an attempt to find the
     * <code>resourceName</code> directly on the classpath will be initiated. </p>
     *
     * @param resource  the name of a resource
     * @param directory the directory location
     * @return the resource url
     */
    public static URL getResource(
        final String resourceName,
        final URL directory)
    {
        String directoryLocation = null;
        if (directory != null)
        {
            directoryLocation = directory.getFile();
        }
        return getResource(
            resourceName,
            directoryLocation);
    }

    /**
     * Attempts to construct the given <code>path</code>
     * to a URL instance.
     *
     * @param path the path from which to construct the URL.
     * @return the constructed URL or null if one couldn't be constructed.
     */
    public static URL toURL(String path)
    {
        URL url = null;
        if (path != null)
        {
            path = ResourceUtils.normalizePath(path);
            final File file = new File(path);
            if (file.exists())
            {
                try
                {
                    url = file.toURL();
                }
                catch (MalformedURLException exception)
                {
                    // ignore
                }
            }
            else
            {
                try
                {
                    url = new URL(path);
                }
                catch (MalformedURLException exception)
                {
                    // ignore
                }
            }
        }
        return url;
    }

    /**
     * Indicates whether or not the given <code>url</code> is a file.
     *
     * @param url the URL to check.
     * @return true/false
     */
    public static boolean isFile(final URL url)
    {
        return url != null ? new File(url.getFile()).isFile() : false;
    }

    /**
     * Recursively deletes a directory and its contents.
     *
     * @param directory the directory to delete.
     */
    public static void deleteDirectory(final File directory)
    {
        if (directory != null && directory.exists() && directory.isDirectory())
        {
            final File[] files = directory.listFiles();
            if (files != null && files.length > 0)
            {
                final int mergedTemplatesCount = files.length;
                for (int ctr = 0; ctr < mergedTemplatesCount; ctr++)
                {
                    final File file = files[ctr];
                    if (file.isDirectory())
                    {
                        deleteDirectory(file);
                        file.delete();
                    }
                    else
                    {
                        file.delete();
                    }
                }
            }
        }
    }

    /**
     * The forward slash character.
     */
    private static final String FORWARD_SLASH = "/";

    /**
     * Gets the contents of this directory and any of its sub directories based on the given <code>patterns</code>.
     * And returns absolute or relative paths depending on the value of <code>absolute</code>.
     *
     * @param url the URL of the directory.
     * @param absolute whether or not the returned content paths should be absoluate (if
     *        false paths will be relative to URL).
     * @return a collection of paths.
     */
    public static List getDirectoryContents(
        final URL url,
        boolean absolute,
        final String[] patterns)
    {
        List contents = ResourceUtils.getDirectoryContents(
                url,
                0,
                true);

        // - first see if its a directory
        if (!contents.isEmpty())
        {
            for (final ListIterator iterator = contents.listIterator(); iterator.hasNext();)
            {
                String path = (String)iterator.next();
                if (!matchesAtLeastOnePattern(
                        path,
                        patterns))
                {
                    iterator.remove();
                }
                else if (absolute)
                {
                    path = url.toString().endsWith(FORWARD_SLASH) ? path : FORWARD_SLASH + path;
                    final URL resource = ResourceUtils.toURL(url + path);
                    if (resource != null)
                    {
                        iterator.set(resource.toString());
                    }
                }
            }
        }
        else // - otherwise handle archives (i.e. jars, etc).
        {
            final String urlAsString = url.toString();
            final String delimiter = "!/";
            final String archivePath = urlAsString.replaceAll(
                    delimiter + ".*",
                    delimiter);
            contents = ResourceUtils.getClassPathArchiveContents(url);
            for (final ListIterator iterator = contents.listIterator(); iterator.hasNext();)
            {
                final String relativePath = (String)iterator.next();
                final String fullPath = archivePath + relativePath;
                if (!fullPath.startsWith(urlAsString) || fullPath.equals(urlAsString + FORWARD_SLASH))
                {
                    iterator.remove();
                }
                else if (!matchesAtLeastOnePattern(
                        relativePath,
                        patterns))
                {
                    iterator.remove();
                }
                else if (absolute)
                {
                    iterator.set(fullPath);
                }
            }
        }
        return contents;
    }

    /**
     * Indicates whether or not the given <code>path</code> matches on
     * one or more of the patterns defined within this class
     * returns true if no patterns are defined.
     *
     * @param path the path to match on.
     * @return true/false
     */
    public static boolean matchesAtLeastOnePattern(
        final String path,
        final String[] patterns)
    {
        boolean matches = patterns == null || patterns.length == 0;
        if (!matches)
        {
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

    /**
     * Indicates whether or not the contents of the given <code>directory</code>
     * and any of its sub directories have been modified after the given <code>time</code>.
     *
     * @param directory the directory to check
     * @param time the time to check against
     * @return true/false
     */
    public static boolean modifiedAfter(
        long time,
        final File directory)
    {
        final List files = new ArrayList();
        ResourceUtils.loadFiles(
            directory,
            files,
            true);
        boolean changed = files.isEmpty();
        for (final Iterator iterator = files.iterator(); iterator.hasNext();)
        {
            final File file = (File)iterator.next();
            changed = file.lastModified() < time;
            if (changed)
            {
                break;
            }
        }
        return changed;
    }

    /**
     * The pattern used for normalizing paths paths with more than one back slash.
     */
    private static final String BACK_SLASH_NORMALIZATION_PATTERN = "\\\\+";

    /**
     * The pattern used for normalizing paths with more than one forward slash.
     */
    private static final String FORWARD_SLASH_NORMALIZATION_PATTERN = FORWARD_SLASH + "+";

    /**
     * Removes any extra path separators and converts all from back slashes
     * to forward slashes.
     *
     * @param path the path to normalize.
     * @return the normalizd path
     */
    public static String normalizePath(final String path)
    {
        return path != null
        ? path.replaceAll(
            BACK_SLASH_NORMALIZATION_PATTERN,
            FORWARD_SLASH).replaceAll(
            FORWARD_SLASH_NORMALIZATION_PATTERN,
            FORWARD_SLASH) : null;
    }
}