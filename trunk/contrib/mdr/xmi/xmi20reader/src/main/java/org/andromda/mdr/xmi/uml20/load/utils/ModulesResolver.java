package org.andromda.mdr.xmi.uml20.load.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import java.net.URI;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ModulesResolver
{
    private Collection mSystemIDS = new HashSet();
    private static final char STANDARD_SEPARATOR = '#';
    private static final char MD_SEPARATOR = '|';
    private Collection mModulesDirectories;
    private Collection mLoaded;

    public ModulesResolver()
    {
        mLoaded = new HashSet();
    }

    public void register(String systemId)
    {
        mSystemIDS.add(systemId);
    }

    public boolean resolve()
    {
        return false;
    }

    public Collection getExternalFilesToLoad()
    {
        ArrayList list = new ArrayList(mSystemIDS);
        list.removeAll(mLoaded);
        return list;
    }

    public static String getIDFromHREF(String href)
    {
        String s1 = getIDfromHREF(href, MD_SEPARATOR);
        if (s1 == null)
        {
            s1 = getIDfromHREF(href, STANDARD_SEPARATOR);
        }
        return s1;
    }

    private static String getIDfromHREF(String href, char c)
    {
        int indexOf = href.indexOf(c);
        if (indexOf >= 0)
        {
            return href.substring(indexOf + 1);
        }
        return null;
    }

    public static String getFileNameFromHREF(String ref)
    {
        String s1 = getFileNameFromHREF(ref, MD_SEPARATOR);
        if (s1 == null)
        {
            s1 = getFileNameFromHREF(ref, STANDARD_SEPARATOR);
        }
        return s1;
    }

    private static String getFileNameFromHREF(String ref, char separator)
    {
        int index = ref.indexOf(separator);
        if (index > 0)
        {
            return ref.substring(0, index);
        }
        return null;
    }

    private File findProperFile(String properResourceName)
    {
        if (mModulesDirectories != null)
        {
            for (Iterator iter = mModulesDirectories.iterator(); iter.hasNext();)
            {
                String mDir = (String)iter.next();
                File file = new File(mDir, properResourceName);
                if (file.exists())
                {
                    return file;
                }
            }
        }
        return null;
    }

    public URI getURI(String properResourceName)
    {
        File file = findProperFile(properResourceName);
        if (file != null)
        {
            return file.toURI();
        }
        return null;
    }

    public InputStream getStream(String properResourceName)
    {
        File file = findProperFile(properResourceName);
        if (file != null)
        {
            try
            {
                return ModulesResolver.createInputStreamFromFile(file.getAbsolutePath());
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void setModulesDirectories(String modulesDirectories)
    {
        mModulesDirectories = new ArrayList();
        StringTokenizer tokenizer = new StringTokenizer(
            modulesDirectories,
            File.pathSeparator,
            false);
        while (tokenizer.hasMoreTokens())
        {
            mModulesDirectories.add(tokenizer.nextToken());
        }
    }

    public static InputStream createInputStreamFromFile(String fileName) throws IOException
    {
        String toLowerCase = fileName.toLowerCase();
        if (toLowerCase.endsWith(".xml.zip"))
        {
            return createInputStreamFromZipFile(fileName);
        }

        if (toLowerCase.endsWith(".xml"))
        {
            return createInputStreamFromXmlFile(fileName);
        }

        throw new IOException("Can not create stream for : " + fileName);
    }

    /**
     * Creates input stream from given zip file.
     */
    private static InputStream createInputStreamFromZipFile(String fName) throws IOException
    {
        ZipFile zipFile = new ZipFile(fName);
        ZipEntry entry = null;
        Enumeration entries = zipFile.entries();
        while (entries.hasMoreElements())
        {
            entry = (ZipEntry)entries.nextElement();
        }
        return new BufferedInputStream(zipFile.getInputStream(entry));
    }

    /**
     * Creates input stream from given xml file.
     * 
     * @param fName the name of the xml file.
     * @return stream for reading file
     */
    private static InputStream createInputStreamFromXmlFile(String fName) throws IOException
    {
        return new BufferedInputStream(new FileInputStream(fName));
    }

    public void addLoaded(String externalFile)
    {
        mLoaded.add(externalFile);
    }
}