package org.andromda.maven.utils;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Recursively lists empty directories from the given directory. A directory is empty when it and it's subdirectories do
 * not contain any files. CVS subdirectories are ignored.
 */
public class EmptyDirsLister
{
    private static final FileFilter dirFilter = new DirFilter();
    private static final FileFilter noDirFilter = new NoDirFilter();

    public static List list(String rootDir)
    {
        final File root = new File(rootDir);

        Set emptyDirs = new HashSet();
        collectEmptyDirs(root, emptyDirs);
        normalize(emptyDirs);

        final List dirNames = new ArrayList();
        for (Iterator iterator = emptyDirs.iterator(); iterator.hasNext();)
        {
            File file = (File)iterator.next();
            dirNames.add(file.getAbsolutePath());
        }

        Collections.sort(dirNames);

        return dirNames;
    }

    private static void normalize(Set emptyDirs)
    {
        for (Iterator iterator = emptyDirs.iterator(); iterator.hasNext();)
        {
            File file = (File)iterator.next();
            if (emptyDirs.contains(file.getParent()))
            {
                iterator.remove();
            }
        }
    }

    private static void collectEmptyDirs(File file, Set emptyDirs)
    {
        if (file.isDirectory())
        {
            if (isEmptyDir(file))
            {
                emptyDirs.add(file);
            }
            else
            {
                File[] dirs = file.listFiles(dirFilter);
                for (int i = 0; i < dirs.length; i++)
                {
                    File dir = dirs[i];
                    if ("CVS".equals(dir.getName()) == false)
                    {
                        collectEmptyDirs(dir, emptyDirs);
                    }
                }
            }
        }
    }

    private static boolean isEmptyDir(File file)
    {
        boolean emptyDir = true;

        if (file.isDirectory() == false)
        {
            emptyDir = false;
        }
        else if ("CVS".equals(file.getName()) == false)
        {
            File[] files = file.listFiles(noDirFilter);
            if (files.length > 0)
            {
                emptyDir = false;
            }
            else
            {
                File[] dirs = file.listFiles(dirFilter);
                for (int i = 0; i < dirs.length && emptyDir; i++)
                {
                    File dir = dirs[i];
                    emptyDir = isEmptyDir(dir);
                }
            }
        }
        return emptyDir;
    }

    private static class DirFilter
            implements FileFilter
    {
        public boolean accept(File file)
        {
            return file.isDirectory();
        }

    }

    private static class NoDirFilter
            implements FileFilter
    {
        public boolean accept(File file)
        {
            return file.isDirectory() == false;
        }
    }
}