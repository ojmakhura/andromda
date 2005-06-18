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
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;


/**
 * Provides utilities for loading resources.
 *
 * @author Chad Brandon
 */
public class ResourceUtils
{
    private static final Logger logger = Logger.getLogger(ResourceUtils.class);

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
        final String methodName = "ResourceUtils.getResource";
        if (logger.isDebugEnabled())
        {
            logger.debug("performing '" + methodName + "' with resourceName '" + resourceName + "'");
        }
        ExceptionUtils.checkEmpty(methodName, "resourceName", resourceName);
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
        catch (Throwable throwable)
        {
            throw new RuntimeException(throwable);
        }
    }

    /**
     * Loads the resource and returns the contents as a String.
     *
     * @param resource the name of the resource.
     * @return the contents of the resource as a string.
     */
    public static String getContents(final Reader resource)
    {
        final String methodName = "ResourceUtils.getContents";
        if (logger.isDebugEnabled())
        {
            logger.debug("performing " + methodName + " with resource '" + resource + "'");
        }
        final StringBuffer contents = new StringBuffer();
        try
        {
            if (resource != null)
            {
                BufferedReader in = new BufferedReader(resource);
                String line;
                while ((line = in.readLine()) != null)
                {
                    contents.append(line + "\n");
                }
                in.close();
                in = null;
            }
        }
        catch (final Throwable throwable)
        {
            final String errMsg = "Error performing " + methodName;
            logger.error(errMsg, throwable);
            throw new RuntimeException(errMsg, throwable);
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
     * If this <code>resource</code> happens to be a directory, it will load the contents of that directory into the a
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
        final List contents = new ArrayList();
        if (resource != null)
        {
            final File fileResource = new File(resource.getFile());
            if (fileResource.isDirectory())
            {
                // we go two levels since descriptors reside in META-INF
                // and we want the parent of the META-INF directory
                File rootDirectory = fileResource;
                for (int ctr = 0; ctr < levels; ctr++)
                {
                    rootDirectory = rootDirectory.getParentFile();
                }
                final File pluginDirectory = rootDirectory;
                loadAllFiles(pluginDirectory, contents);

                // remove the root path from each file
                CollectionUtils.transform(
                    contents,
                    new Transformer()
                    {
                        public Object transform(Object object)
                        {
                            return StringUtils.replace(
                                ((File)object).getPath().replace('\\', '/'),
                                pluginDirectory.getPath().replace('\\', '/') + '/',
                                "");
                        }
                    });
            }
        }
        return contents;
    }

    /**
     * Loads all files find in the <code>directory</code> and adds them to the <code>fileList</code>.
     *
     * @param directory the directory from which to load all files.
     * @param fileList  the List of files to which we'll add the found files.
     */
    private static void loadAllFiles(
        final File directory,
        final List fileList)
    {
        final File[] files = directory.listFiles();
        for (int ctr = 0; ctr < files.length; ctr++)
        {
            File file = files[ctr];
            if (!file.isDirectory())
            {
                fileList.add(file);
            }
            else
            {
                loadAllFiles(file, fileList);
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
                resourceUrl = resourceUrl.replaceFirst(ARCHIVE_PREFIX, "");
                final int entryPrefixIndex = resourceUrl.indexOf('!');
                if (entryPrefixIndex != -1)
                {
                    resourceUrl = resourceUrl.substring(0, entryPrefixIndex);
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
        final String methodName = "ResourceUtils.getClassResource";
        ExceptionUtils.checkEmpty(methodName, "className", className);
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
        return className.replace('.', '/') + ".class";
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
        final String methodName = "ResourceUtils.getResource";
        if (logger.isDebugEnabled())
        {
            logger.debug(
                "performing '" + methodName + "' with resourceName '" + resourceName + "' and directory '" + directory +
                "'");
        }
        ExceptionUtils.checkEmpty(methodName, "resourceName", resourceName);

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
                    logger.warn(
                        "'" + file + "' is an invalid resource," + " attempting to find resource '" + resourceName +
                        "' on classpath");
                }
            }
        }
        return getResource(resourceName);
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

                // we need to set the urlConnection to null and explicity
                // call garbage collection, otherwise the JVM won't let go
                // of the URL resource
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
        return getResource(resourceName, directoryLocation);
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
}