package org.andromda.core.common;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;


/**
 * Provides utilities for loading resources.
 *
 * @author Chad Brandon
 * @author Bob Fields
 */
public class ResourceUtils
{
    private static final Logger logger = Logger.getLogger(ResourceUtils.class);
    
    /**
     * All archive files start with this prefix.
     */
    private static final String ARCHIVE_PREFIX = "jar:";
    
    /**
     * The prefix for URL file resources.
     */
    private static final String FILE_PREFIX = "file:";
    
    /**
     * The prefix to use for searching the classpath for a resource.
     */
    private static final String CLASSPATH_PREFIX = "classpath:";

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
                for (String line = resourceInput.readLine(); line != null; line = resourceInput.readLine())
                {
                    contents.append(line).append(LINE_SEPARATOR);
                }
                resourceInput.close();
            }
        }
        catch (final Throwable throwable)
        {
            throw new RuntimeException(throwable);
        }
        return contents.toString().trim();
    }

    /**
     * If the <code>resource</code> represents a classpath archive (i.e. jar, zip, etc), this method will retrieve all
     * contents from that resource as a List of relative paths (relative to the archive base). Otherwise an empty List
     * will be returned.
     *
     * @param resource the resource from which to retrieve the contents
     * @return a list of Strings containing the names of every nested resource found in this resource.
     */
    public static List<String> getClassPathArchiveContents(final URL resource)
    {
        final List<String> contents = new ArrayList<String>();
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
    public static List<String> getDirectoryContents(
        final URL resource,
        final int levels)
    {
        return getDirectoryContents(
            resource,
            levels,
            true);
    }

    /**
     * The character used for substituting whitespace in paths.
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
        if (StringUtils.isNotBlank(filePath))
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
    public static List<String> getDirectoryContents(
        final URL resource,
        final int levels,
        boolean includeSubdirectories)
    {
        final List<String> contents = new ArrayList<String>();
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
                for (final ListIterator<String> iterator = contents.listIterator(); iterator.hasNext();)
                {
                    iterator.set(
                        StringUtils.replace(
                            new File(iterator.next()).getPath().replace(
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
        final List<String> fileList,
        boolean includeSubdirectories)
    {
        if (directory != null)
        {
            final File[] files = directory.listFiles();
            if (files != null)
            {
                for (File file : files)
                {
                    if (!file.isDirectory())
                    {
                        fileList.add(file.toString());
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
     * @param resource 
     * @return true if its an archive, false otherwise.
     */
    public static boolean isArchive(final URL resource)
    {
        return resource != null && resource.toString().startsWith(ARCHIVE_PREFIX);
    }
    
    private static final String URL_DECODE_ENCODING = "UTF-8";

    /**
     * If this <code>resource</code> is an archive file, it will return the resource as an archive.
     *
     * @param resource 
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
                resourceUrl = URLDecoder.decode(new URL(resourceUrl).getFile(), URL_DECODE_ENCODING); 
                archive = new ZipFile(resourceUrl);
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
     * @param resourceName the name of a resource
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
                    return file.toURI().toURL();
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
    public static long getLastModifiedTime(final URL resource)
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

                // - we need to set the urlConnection to null and explicitly
                //   call garbage collection, otherwise the JVM won't let go
                //   of the URL resource
//                uriConnection = null;
//                System.gc();
                uriConnection.getInputStream().close();
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
     * @param resourceName the name of a resource
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
     * to a URL instance. If the argument cannot be resolved as a resource
     * on the file system this method will attempt to locate it on the
     * classpath.
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

            try
            {
                if (path.startsWith(CLASSPATH_PREFIX))
                {
                    url = ResourceUtils.resolveClasspathResource(path);
                }
                else
                {
                    final File file = new File(path);
                    url = file.exists() ? file.toURI().toURL() : new URL(path);
                }
            }
            catch (MalformedURLException exception)
            {
                // ignore means no protocol was specified
            }
        }
        return url;
    }

    /**
     * Resolves a URL to a classpath resource, this method will treat occurrences of the exclamation mark
     * similar to what {@link URL} does with the <code>jar:file</code> protocol.
     * <p/>
     * Example: <code>my/path/to/some.zip!/file.xml</code> represents a resource <code>file.xml</code>
     * that is located in a ZIP file on the classpath called <code>my/path/to/some.zip</code>
     * <p/>
     * It is possible to have nested ZIP files, example:
     * <code>my/path/to/first.zip!/subdir/second.zip!/file.xml</code>.
     * <p/>
     * <i>Please note that the extension of the ZIP file can be anything,
     * but in the case the extension is <code>.jar</code> the JVM will automatically unpack resources
     * one level deep and put them all on the classpath</i>
     *
     * @param path the name of the resource to resolve to a URL, potentially nested in ZIP archives
     * @return a URL pointing the resource resolved from the argument path
     *      or <code>null</code> if the argument is <code>null</code> or impossible to resolve
     */
    public static URL resolveClasspathResource(String path)
    {
        URL urlResource = null;
        if (path.startsWith(CLASSPATH_PREFIX))
        {        
            path = path.substring(CLASSPATH_PREFIX.length(), path.length());
            
            // - the value of the following constant is -1 of no nested resources were specified,
            //   otherwise it points to the location of the first occurrence
            final int nestedPathOffset = path.indexOf("!/");
    
            // - take the part of the path that is not nested (may be all of it)
            final String resourcePath = nestedPathOffset == -1 ? path : path.substring(0, nestedPathOffset);
            final String nestingPath = nestedPathOffset == -1 ? "" : path.substring(nestedPathOffset);
    
            // - use the new path to load a URL from the classpath using the context class loader for this thread
            urlResource = Thread.currentThread().getContextClassLoader().getResource(resourcePath);
    
            // - at this point the URL might be null in case the resource was not found
            if (urlResource == null)
            {
                if (logger.isDebugEnabled())
                {
                    logger.debug("Resource could not be located on the classpath: " + resourcePath);
                }
            }
            else
            {
                try
                {
                    // - extract the filename from the entire resource path
                    final int fileNameOffset = resourcePath.lastIndexOf('/');
                    final String resourceFileName =
                        fileNameOffset == -1 ? resourcePath : resourcePath.substring(fileNameOffset + 1);
    
                    if (logger.isDebugEnabled())
                    {
                        logger.debug("Creating temporary copy on the file system of the classpath resource");
                    }
                    final File fileSystemResource = File.createTempFile(resourceFileName, null);
                    if (logger.isDebugEnabled())
                    {
                        logger.debug("Temporary file will be deleted on VM exit: " + fileSystemResource.getAbsolutePath());
                    }
                    fileSystemResource.deleteOnExit();
                    if (logger.isDebugEnabled())
                    {
                        logger.debug("Copying classpath resource contents into temporary file");
                    }
                    writeUrlToFile(urlResource, fileSystemResource.toString(), null);
    
                    // - count the times the actual resource to resolve has been nested
                    final int nestingCount = StringUtils.countMatches(path, "!/");
                    // - this buffer is used to construct the URL spec to that specific resource
                    final StringBuffer buffer = new StringBuffer();
                    for (int ctr = 0; ctr < nestingCount; ctr++)
                    {
                        buffer.append(ARCHIVE_PREFIX);
                    }
                    buffer.append(FILE_PREFIX).append(fileSystemResource.getAbsolutePath()).append(nestingPath);
                    if (logger.isDebugEnabled())
                    {
                        logger.debug("Constructing URL to " +
                            (nestingCount > 0 ? "nested" : "") + " resource in temporary file");
                    }
    
                    urlResource = new URL(buffer.toString());
                }
                catch (final IOException exception)
                {
                    logger.warn("Unable to resolve classpath resource", exception);
                    // - impossible to properly resolve the path into a URL
                    urlResource = null;
                }
            }
        }
        return urlResource;
    }
    
    /**
     * Writes the URL contents to a file specified by the fileLocation argument.
     *
     * @param url the URL to read
     * @param fileLocation the location which to write.
     * @param encoding the optional encoding
     * @throws IOException if error writing file
     */
    public static void writeUrlToFile(
        final URL url,
        final String fileLocation,
        final String encoding)
        throws IOException
    {
        ExceptionUtils.checkNull(
            "url",
            url);
        ExceptionUtils.checkEmpty(
            "fileLocation",
            fileLocation);
        final File file = new File(fileLocation);
        final File parent = file.getParentFile();
        if (parent != null)
        {
            parent.mkdirs();
        }
        OutputStream stream = new BufferedOutputStream(new FileOutputStream(file));
        if (StringUtils.isNotBlank(encoding))
        {
            BufferedReader inputReader = new BufferedReader(new InputStreamReader(
                        url.openStream(),
                        encoding));
            for (int ctr = inputReader.read(); ctr != -1; ctr = inputReader.read())
            {
                stream.write(ctr);
            }
            inputReader.close();
        }
        else
        {
            InputStream inputStream = new BufferedInputStream(url.openStream());
            for (int ctr = inputStream.read(); ctr != -1; ctr = inputStream.read())
            {
                stream.write(ctr);
            }
            inputStream.close();
        }
        stream.flush();
        stream.close();
    }


    /**
     * Indicates whether or not the given <code>url</code> is a file.
     *
     * @param url the URL to check.
     * @return true/false
     */
    public static boolean isFile(final URL url)
    {
        return url != null && new File(url.getFile()).isFile();
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
     * @param absolute whether or not the returned content paths should be absolute (if
     *        false paths will be relative to URL).
     * @param patterns 
     * @return a collection of paths.
     */
    public static List<String> getDirectoryContents(
        final URL url,
        boolean absolute,
        final String[] patterns)
    {
        List<String> contents = ResourceUtils.getDirectoryContents(
                url,
                0,
                true);

        // - first see if its a directory
        if (!contents.isEmpty())
        {
            for (final ListIterator<String> iterator = contents.listIterator(); iterator.hasNext();)
            {
                String path = iterator.next();
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
            for (final ListIterator<String> iterator = contents.listIterator(); iterator.hasNext();)
            {
                final String relativePath = iterator.next();
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
     * @param patterns 
     * @return true/false
     */
    public static boolean matchesAtLeastOnePattern(
        final String path,
        final String[] patterns)
    {
        boolean matches = (patterns == null || patterns.length == 0);
        if (!matches)
        {
            if (patterns!=null && patterns.length > 0)
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
        final List<String> files = new ArrayList<String>();
        ResourceUtils.loadFiles(
            directory,
            files,
            true);
        boolean changed = files.isEmpty();
        for (String fileName : files)
        {
            final File file = new File(fileName);
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
    private static final String FORWARD_SLASH_NORMALIZATION_PATTERN = FORWARD_SLASH + '+';

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

    /**
     * Takes a path and replaces the oldException with the newExtension.
     *
     * @param path the path to rename.
     * @param oldExtension the extension to rename from.
     * @param newExtension the extension to rename to.
     * @return the path with the new extension.
     */
    public static String renameExtension(
        final String path,
        final String oldExtension,
        final String newExtension)
    {
        ExceptionUtils.checkEmpty(
            "path",
            path);
        ExceptionUtils.checkNull(
            "oldExtension",
            oldExtension);
        ExceptionUtils.checkNull(
            "newExtension",
            newExtension);
        String newPath = path;
        final int oldExtensionIndex = path.lastIndexOf(oldExtension);
        if (oldExtensionIndex != -1)
        {
            newPath = path.substring(
                    0,
                    oldExtensionIndex) + newExtension;
        }
        return newPath;
    }
}