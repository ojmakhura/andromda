package org.andromda.core.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
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
     * Retrieves a resource from the current classpath.
     * 
     * @param resourceName the name of the resource
     * @return the resource url
     */
    public static URL getResource(String resourceName)
    {
        final String methodName = "ResourceUtils.getResource";
        if (logger.isDebugEnabled())
            logger.debug("performing '" + methodName + "' with resourceName '"
                + resourceName + "'");
        ExceptionUtils.checkEmpty(methodName, "resourceName", resourceName);
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL resource = loader.getResource(resourceName);
        return resource;
    }

    /**
     * Loads the resource and returns the contents as a String.
     * 
     * @param resourceName the name of the resource.
     * @return String
     */
    public static String getContents(URL resource)
    {
        final String methodName = "ResourceUtils.getContents";
        try
        {
	        return getContents(resource != null ? new InputStreamReader(
	                    resource.openStream()) : null);
        }
        catch (Throwable th)
        {
            String errMsg = "Error performing " + methodName;
            logger.error(errMsg, th);
            throw new RuntimeException(errMsg, th);
        }
    }
    
    /**
     * Loads the resource and returns the contents as a String.
     * 
     * @param resourceName the name of the resource.
     * @return String
     */
    public static String getContents(Reader resource)
    {
        final String methodName = "ResourceUtils.getContents";
        if (logger.isDebugEnabled())
            logger.debug("performing " + methodName + " with resource '"
                + resource + "'");
        StringBuffer contents = new StringBuffer();
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
        catch (Throwable th)
        {
            String errMsg = "Error performing " + methodName;
            logger.error(errMsg, th);
            throw new RuntimeException(errMsg, th);
        }
        return contents.toString();
    }

    /**
     * If the <code>resource</code> represents a classpath archive (i.e. jar,
     * zip, etc), this method will retrieve all contents from that resource as a
     * List of relative paths (relative to the archive base). Otherwise an empty
     * List will be returned.
     * 
     * @param resource the resource from which to retrieve the contents
     * @return a list of Strings containing the names of every nested resource
     *         found in this resource.
     */
    public static List getClassPathArchiveContents(URL resource)
    {
        List contents = new ArrayList();
        if (isArchive(resource))
        {
            ZipFile archive = getArchive(resource);
            if (archive != null)
            {
                Enumeration entries = archive.entries();
                while (entries.hasMoreElements())
                {
                    ZipEntry entry = (ZipEntry)entries.nextElement();
                    contents.add(entry.getName());
                }
            }
        }
        return contents;
    }

    /**
     * If this <code>resource</code> happens to be a directory, it will load
     * the contents of that directory into the a List and return the list of
     * names relative to the given <code>resource</code> (otherwise it will
     * return an empty List).
     * 
     * @param resource the resource from which to retrieve the contents
     * @param levels the number of levels to go step down if the resource ends
     *        up being a directory (if its an artifact, levels will be ignored).
     * @return a list of Strings containing the names of every nested resource
     *         found in this resource.
     */
    public static List getDirectoryContents(URL resource, int levels)
    {
        List contents = new ArrayList();
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
                CollectionUtils.transform(contents, new Transformer()
                {
                    public Object transform(Object object)
                    {
                        return StringUtils.replace(((File)object).getPath()
                            .replace('\\', '/'), pluginDirectory.getPath()
                            .replace('\\', '/') + '/', "");
                    }
                });
            }
        }
        return contents;
    }

    /**
     * Loads all files find in the <code>directory</code> and adds them to the
     * <code>fileList</code>.
     * 
     * @param directory the directory from which to load all files.
     * @param fileList the List of files to which we'll add the found files.
     */
    private static void loadAllFiles(File directory, List fileList)
    {
        File[] files = directory.listFiles();
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
     * All archive files start with this prefix.
     */
    private static final String ARCHIVE_PREFIX = "jar:";

    /**
     * Returns true/false on whether or not this <code>resource</code>
     * represents an archive or not (i.e. jar, or zip, etc).
     * 
     * @return true if its an archive, false otherwise.
     */
    public static boolean isArchive(URL resource)
    {
        return resource != null
            && resource.toString().startsWith(ARCHIVE_PREFIX);
    }

    /**
     * If this <code>resource</code> is an archive file, it will return the
     * resource as archive.
     * 
     * @return the archive as a ZipFile
     */
    public static ZipFile getArchive(URL resource)
    {
        final String methodName = "ResourceUtils.getArchive";
        try
        {
            ZipFile archive = null;
            if (resource != null)
            {
                String resourceUrl = resource.toString();
                resourceUrl = resourceUrl.replaceFirst(ARCHIVE_PREFIX, "");
                int entryPrefixIndex = resourceUrl.indexOf('!');
                if (entryPrefixIndex != -1)
                {
                    resourceUrl = resourceUrl.substring(0, entryPrefixIndex);
                }
                archive = new ZipFile(new URL(resourceUrl).getFile());
            }
            return archive;
        }
        catch (Throwable th)
        {
            String errMsg = "Error performing " + methodName;
            logger.error(errMsg, th);
            throw new PluginException(errMsg, th);
        }
    }

    /**
     * Loads the file resource and returns the contents as a String.
     * 
     * @param resourceName the name of the resource.
     * @return String
     */
    public static String getContents(String resourceName)
    {
        return getContents(getResource(resourceName));
    }

    /**
     * Takes a className as an argument and returns the URL for the class.
     * 
     * @param className
     * @return java.net.URL
     */
    public static URL getClassResource(String className)
    {
        final String methodName = "ResourceUtils.getClassResource";
        ExceptionUtils.checkEmpty(methodName, "className", className);
        return getResource(getClassNameAsResource(className));
    }

    /**
     * Private helper method.
     * 
     * @param className
     * @return String
     */
    private static String getClassNameAsResource(String className)
    {
        return className.replace('.', '/') + ".class";
    }

    /**
     * <p>
     * Retrieves a resource from an optionally given <code>directory</code> or
     * from the package on the classpath.
     * </p>
     * <p>
     * If the directory is specified and is a valid directory then an attempt at
     * finding the resource by appending the <code>resourceName</code> to the
     * given <code>directory</code> will be made, otherwise an attempt to find
     * the <code>resourceName</code> directly on the classpath will be
     * initiated.
     * </p>
     * 
     * @param resource the name of a resource
     * @param directory the directory location
     * @return the resource url
     */
    public static URL getResource(String resourceName, String directory)
    {
        final String methodName = "ResourceUtils.getResource";
        if (logger.isDebugEnabled())
            logger.debug("performing '" + methodName + "' with resourceName '"
                + resourceName + "' and directory '" + directory + "'");
        ExceptionUtils.checkEmpty(methodName, "resourceName", resourceName);

        if (directory != null)
        {
            File file = new File(directory, resourceName);
            if (file.exists())
            {
                try
                {
                    return file.toURL();
                }
                catch (MalformedURLException ex)
                {
                    logger.warn("'" + file + "' is an invalid resource,"
                        + " attempting to find resource '" + resourceName
                        + "' on classpath");
                }
            }
        }
        return getResource(resourceName);
    }

}